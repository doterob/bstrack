package es.bahiasoftware.bstrack.iot.callback;

import es.bahiasoftware.bstrack.iot.IoTInfo;

public interface DisconnectCallback {
    void onSuccess();
    void onFailure();
}
