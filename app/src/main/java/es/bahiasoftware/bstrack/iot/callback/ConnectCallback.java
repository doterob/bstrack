package es.bahiasoftware.bstrack.iot.callback;

import android.util.Log;

import com.neura.resources.authentication.AuthenticateData;
import com.neura.standalonesdk.util.SDKUtils;

import es.bahiasoftware.bstrack.iot.IoTInfo;

public interface ConnectCallback {
    void onSuccess(IoTInfo info);
    void onFailure();
}
