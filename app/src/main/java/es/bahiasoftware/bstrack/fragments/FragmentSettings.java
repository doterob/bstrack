package es.bahiasoftware.bstrack.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import es.bahiasoftware.bstrack.R;
import es.bahiasoftware.bstrack.activities.MainActivity;
import es.bahiasoftware.bstrack.iot.ConnectionRequest;
import es.bahiasoftware.bstrack.iot.DisconnectionRequest;
import es.bahiasoftware.bstrack.iot.IoTEvent;
import es.bahiasoftware.bstrack.iot.IoTInfo;
import es.bahiasoftware.bstrack.iot.IoTListener;
import es.bahiasoftware.bstrack.iot.IoTManager;
import es.bahiasoftware.bstrack.iot.IotEventType;
import es.bahiasoftware.bstrack.iot.callback.ConnectCallback;
import es.bahiasoftware.bstrack.iot.callback.DisconnectCallback;

public class FragmentSettings extends Fragment implements IoTListener {

    final IoTManager manager;

    TextView status;
    TextView phone;
    TextView user;
    TextView token;

    public FragmentSettings(){
        manager = IoTManager.getInstance();
        manager.register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        status = (TextView) view.findViewById(R.id.fragment_settings_status_txt);
        phone = (TextView) view.findViewById(R.id.fragment_settings_phone_txt);
        user = (TextView) view.findViewById(R.id.fragment_settings_user_txt);
        token = (TextView) view.findViewById(R.id.fragment_settings_token_txt);

        ((Button)view.findViewById(R.id.fragment_status_connect_btn)).setOnClickListener(connect());
        ((Button)view.findViewById(R.id.fragment_settings_disconnect_btn)).setOnClickListener(disconnect());
        ((Button)view.findViewById(R.id.fragment_settings_view_events_btn)).setOnClickListener(viewEvents());
        ((Button)view.findViewById(R.id.fragment_settings_info_btn)).setOnClickListener(info());

        status.setText(manager.isConnected() ? "Conectado" : "Desconectado");
        setInfo(manager.getAccountInfo());
    }

    private void setInfo(IoTInfo info){
        user.setText(info != null ? info.getUserId() : "");
        token.setText(info != null ? info.getToken() : "");
        phone.setText(info != null ? info.getPhone() : "");
    }

    public View.OnClickListener connect(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(manager.isConnected()){
                    Toast.makeText(getActivity(),
                            "Ya conectado", Toast.LENGTH_SHORT).show();
                } else {
                    ConnectionRequest request = new ConnectionRequest(getActivity(), phone.getText().toString(), FirebaseInstanceId.getInstance().getToken(), true);
                    manager.connect(request, new ConnectCallback() {
                        @Override
                        public void onSuccess(IoTInfo info) {
                            Toast.makeText(getActivity(),
                                    "Conectado", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure() {
                            Toast.makeText(getActivity(),
                                    "Conexión fallida", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        };
    }

    public View.OnClickListener disconnect() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DisconnectionRequest request = new DisconnectionRequest(getActivity());
                manager.disconnect(request, new DisconnectCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getActivity(),
                                "Desconectado", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure() {

                    }
                });
            }
        };
    }

    public View.OnClickListener viewEvents() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).openFragment(new FragmentViewEvents());
            }
        };
    }

    public View.OnClickListener info() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).openFragment(new FragmentUserInfo());
            }
        };
    }

    @Override
    public void notify(IoTEvent event) {
        if(IotEventType.CONNECTED.equals(event.getType())){
            status.setText("Conectado");
        }
        if(IotEventType.DISCONNECTED.equals(event.getType())){
            status.setText("Desconectado");
        }
        setInfo((IoTInfo) event.getData());
    }
}
