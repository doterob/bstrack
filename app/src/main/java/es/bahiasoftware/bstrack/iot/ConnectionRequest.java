package es.bahiasoftware.bstrack.iot;

import android.app.Activity;
import android.content.Context;
import android.hardware.camera2.params.StreamConfigurationMap;

/**
 * Created by David on 16/05/2017.
 */

public class ConnectionRequest {

    private final Activity activity;
    private final String phone;
    private final String fcmToken;
    private final boolean auth;

    public ConnectionRequest(Activity activity, String phone, String fcmToken, boolean auth){
        this.activity = activity;
        this.phone = phone;
        this.fcmToken = fcmToken;
        this.auth = auth;
    }

    public ConnectionRequest(Activity activity){
        this(activity, null, null, false);
    }

    public Activity getActivity() {
        return activity;
    }

    public String getPhone() {
        return phone;
    }

    public String getFcmToken(){
        return fcmToken;
    }

    public boolean isAuth() { return auth; }
}
