package es.bahiasoftware.bstrack.iot;

import com.google.firebase.messaging.RemoteMessage;

import es.bahiasoftware.bstrack.iot.callback.ConnectCallback;

public interface IoTController {

    void connect(ConnectionRequest request, final ConnectCallback callback);

    IoTInfo getInfo();

    void disconnect(DisconnectionRequest request);

    boolean isConnected();

    boolean isIoTEvent(RemoteMessage message);

    void processEvent(RemoteMessage message);
}
