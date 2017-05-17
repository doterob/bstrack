package es.bahiasoftware.bstrack.iot;

import android.app.Activity;

/**
 * Created by David on 16/05/2017.
 */

public class DisconnectionRequest {

    private final Activity activity;

    public DisconnectionRequest(Activity activity){
        this.activity = activity;
    }

    public Activity getActivity(){
        return activity;
    }
}
