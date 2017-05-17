package es.bahiasoftware.bstrack.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import es.bahiasoftware.bstrack.R;
import es.bahiasoftware.bstrack.db.DbHelper;
import es.bahiasoftware.bstrack.fragments.FragmentSettings;
import es.bahiasoftware.bstrack.iot.ConnectionRequest;
import es.bahiasoftware.bstrack.iot.DisconnectionRequest;
import es.bahiasoftware.bstrack.iot.IoTEvent;
import es.bahiasoftware.bstrack.iot.IoTInfo;
import es.bahiasoftware.bstrack.iot.IoTListener;
import es.bahiasoftware.bstrack.iot.IoTManager;
import es.bahiasoftware.bstrack.iot.callback.ConnectCallback;

public class MainActivity extends Activity implements IoTListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //http://stackoverflow.com/a/38945375/5130239
        try {
            FirebaseApp.getInstance();
        } catch (IllegalStateException ex) {
            FirebaseApp.initializeApp(this, FirebaseOptions.fromResource(this));
        }

        IoTManager.getInstance().connect(new ConnectionRequest(this), null);
        IoTManager.getInstance().register(this);
        openFragment(new FragmentSettings());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IoTManager.getInstance().disconnect(new DisconnectionRequest(this), null);
    }

    public void openFragment(Fragment newFragment) {
        android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        int backStackCount = getFragmentManager().getBackStackEntryCount();
        if (backStackCount == 1) {
            finish();
        } else {
            getFragmentManager().popBackStackImmediate();
        }
    }

    /**
     * @return false if sms permission is granted, true if we're requested sms permission to the user.
     */
    public boolean requestSmsPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return false;
        if ((ActivityCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECEIVE_SMS}, 190);
            return true;
        }
        return false;
    }

    @Override
    public void notify(IoTEvent event) {
        DbHelper db = new DbHelper(this.getBaseContext());
        db.save(event);
    }
}