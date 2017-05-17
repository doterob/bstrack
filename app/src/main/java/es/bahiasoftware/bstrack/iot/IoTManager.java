package es.bahiasoftware.bstrack.iot;

import com.google.firebase.messaging.RemoteMessage;
import com.neura.standalonesdk.service.NeuraApiClient;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Observable;
import java.util.Set;

import es.bahiasoftware.bstrack.db.DbHelper;
import es.bahiasoftware.bstrack.iot.callback.ConnectCallback;
import es.bahiasoftware.bstrack.iot.callback.DisconnectCallback;
import es.bahiasoftware.bstrack.iot.neura.NeuraController;

public class IoTManager implements IoTNotificator {

    private boolean connected;
    private Set<IoTListener> listeners;
    private IoTController controller;

    private static IoTManager instance;
    public synchronized static IoTManager getInstance(){
        if(instance == null){
            instance = new IoTManager();
        }
        return instance;
    }

    private IoTManager(){
        controller = new NeuraController(this);
        listeners = new LinkedHashSet<>();
    }

    public void connect(ConnectionRequest request, ConnectCallback callback){
        if(controller != null){
            controller.connect(request,callback);
        } else {
            callback.onFailure();
        }
    }

    public void disconnect(DisconnectionRequest request, DisconnectCallback callback){
        if(controller != null){
            controller.disconnect(request);
        }

        if(callback != null) callback.onSuccess();
    }

    public boolean isConnected(){
        return controller.isConnected();
    }

    public boolean isIoTEvent(RemoteMessage message){
        return controller != null ? controller.isIoTEvent(message) : false;
    }

    public void process(RemoteMessage message){
        if(controller != null){
            controller.processEvent(message);
        }
    }

    public synchronized boolean register(IoTListener listener){
        return listeners.add(listener);
    }

    public void notify(IoTEvent event){
        for(IoTListener listener : listeners){
            listener.notify(event);
        }
    }

    public IoTInfo getAccountInfo(){
        return controller.getInfo();
    }

    public NeuraApiClient getNeuraClient(){
        return ((NeuraController)controller).getClient();
    }
}
