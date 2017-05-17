package es.bahiasoftware.bstrack.iot.neura;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.RemoteMessage;
import com.neura.resources.authentication.AuthenticateCallback;
import com.neura.resources.authentication.AuthenticateData;
import com.neura.sdk.object.AuthenticationRequest;
import com.neura.sdk.service.SubscriptionRequestCallbacks;
import com.neura.sdk.util.NeuraUtil;
import com.neura.standalonesdk.events.NeuraEvent;
import com.neura.standalonesdk.events.NeuraPushCommandFactory;
import com.neura.standalonesdk.service.NeuraApiClient;
import com.neura.standalonesdk.util.Builder;
import com.neura.standalonesdk.util.SDKUtils;

import java.sql.Timestamp;

import es.bahiasoftware.bstrack.R;
import es.bahiasoftware.bstrack.iot.ConnectionRequest;
import es.bahiasoftware.bstrack.iot.DisconnectionRequest;
import es.bahiasoftware.bstrack.iot.IoTController;
import es.bahiasoftware.bstrack.iot.IoTEvent;
import es.bahiasoftware.bstrack.iot.IoTInfo;
import es.bahiasoftware.bstrack.iot.IoTNotificator;
import es.bahiasoftware.bstrack.iot.IotEventType;
import es.bahiasoftware.bstrack.iot.callback.ConnectCallback;

public class NeuraController implements IoTController {

    public static final String PREFS_NAME = "NeuraControllerPreferences";
    public static final String USER_ID = "NeuraControllerUserId";

    private NeuraApiClient client;
    private IoTNotificator listener;
    private String userId;

    public NeuraController(IoTNotificator listener){
        this.listener = listener;

    }

    @Override
    public void connect(final ConnectionRequest request, final ConnectCallback callback) {

        final Context context = request.getActivity().getBaseContext();

        client = new Builder(context).build();
        client.setAppUid(context.getResources().getString(R.string.app_uid));
        client.setAppSecret(context.getResources().getString(R.string.app_secret));
        client.connect();

        final SharedPreferences settings = request.getActivity().getSharedPreferences(PREFS_NAME, 0);
        userId = settings.getString(USER_ID, null);

        if(callback == null) return;

        if(request.isAuth() && !client.isLoggedIn()) {
            AuthenticationRequest neuraRequest = new AuthenticationRequest(null, AuthenticationRequest.AuthenticationType.LightAuthenticationUi);
            neuraRequest.setPhone(request.getPhone());
            client.authenticate(neuraRequest, new AuthenticateCallback() {
                @Override
                public void onSuccess(AuthenticateData data) {
                    Log.i(getClass().getSimpleName(), "Successfully authenticate with neura.");
                    client.registerFirebaseToken(request.getActivity(),
                            FirebaseInstanceId.getInstance().getToken());

                    userId = data.getNeuraUserId();
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(USER_ID, userId);
                    editor.apply();
                    IoTInfo info = new IoTInfo(settings.getString(USER_ID, null), client.getUserAccessToken(), request.getPhone());
                    callback.onSuccess(info);
                    listener.notify(new IoTEvent(IotEventType.CONNECTED, new Timestamp(System.currentTimeMillis()), info));
                    Log.i(getClass().getSimpleName(), "User:" + info.getUserId() + " - token:" + info.getToken());
                }

                @Override
                public void onFailure(int errorCode) {
                    Log.e(getClass().getSimpleName(), "Failed to authenticate with neura. Reason : "
                            + SDKUtils.errorCodeToString(errorCode));
                    callback.onFailure();
                }
            });
        } else {
            IoTInfo info = new IoTInfo(userId, client.getUserAccessToken(), request.getPhone());
            callback.onSuccess(info);
            listener.notify(new IoTEvent(IotEventType.CONNECTED, new Timestamp(System.currentTimeMillis()), info));
        }
    }

    @Override
    public IoTInfo getInfo(){
        return new IoTInfo(userId, client.getUserAccessToken(), null);
    }

    public NeuraApiClient getClient(){
        return client;
    }

    private void subscribe(final Activity activity){
        for(final NeuraEventType event : NeuraEventType.values()){
            client.subscribeToEvent(event.name(), event.getIotTypeName(), new SubscriptionRequestCallbacks() {
                @Override
                public void onSuccess(String s, Bundle bundle, String s1) {

                }

                @Override
                public void onFailure(String s, Bundle bundle, int i) {
                    if (activity != null) {
                        Log.e(getClass().getSimpleName(), "Error: Failed to subscribe to event " + s + ". Error code: " +
                                NeuraUtil.errorCodeToString(i));
                        Toast.makeText(activity,
                                "Error: No se ha subscripto al evento " + s + ". Código: " +
                                        NeuraUtil.errorCodeToString(i), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    @Override
    public void disconnect(DisconnectionRequest request){

        client.forgetMe(request.getActivity(), false, new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                return false;
            }
        });
        client.disconnect();
        client = null;
        listener.notify(new IoTEvent(IotEventType.DISCONNECTED, new Timestamp(System.currentTimeMillis()), null));
    }

    @Override
    public boolean isConnected(){
        return client != null && client.isConnected();
    }

    @Override
    public boolean isIoTEvent(RemoteMessage message) {
        return NeuraPushCommandFactory.getInstance().isNeuraEvent(message.getData());
    }

    @Override
    public void processEvent(RemoteMessage message) {

        if(isIoTEvent(message)){
            NeuraEvent event = NeuraPushCommandFactory.getInstance().getEvent(message.getData());
            IotEventType eventType = NeuraEventType.getByName(event.getEventName());
            if(eventType != null && listener != null){
                listener.notify(new IoTEvent(eventType, new Timestamp(event.getEventTimestamp()), event.getMetadata().toString()));
            }

        } else {
            Log.e(getClass().getSimpleName(), "Evento " + message.getData().get("pushType") + " no compatible");
        }
    }
}
