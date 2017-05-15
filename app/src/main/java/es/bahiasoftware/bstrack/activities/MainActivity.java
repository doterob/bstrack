package es.bahiasoftware.bstrack.activities;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.iid.FirebaseInstanceId;

import es.bahiasoftware.bstrack.NeuraManager;
import es.bahiasoftware.bstrack.R;
import es.bahiasoftware.bstrack.fragments.BaseFragment;
import es.bahiasoftware.bstrack.fragments.FragmentMain;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //http://stackoverflow.com/a/38945375/5130239
        try {
            FirebaseApp.getInstance();
            Log.i(getClass().getSimpleName(), "Iniciado FCM con token -> " + FirebaseInstanceId.getInstance().getToken());
        } catch (IllegalStateException ex) {
            Log.e(getClass().getSimpleName(), "Error en FCM", ex);
            FirebaseApp.initializeApp(this, FirebaseOptions.fromResource(this));
        }

        NeuraManager.getInstance().initNeuraConnection(getApplicationContext());
        Log.i(getClass().getSimpleName(), "Neura token -> " + NeuraManager.getInstance().getClient().getUserAccessToken());

        openFragment(new FragmentMain());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NeuraManager.getInstance().getClient().disconnect();
    }

    public void openFragment(BaseFragment newFragment) {
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        //Since we're only calling permissions request on sms, which is only called from FragmentMain,
        //so we can assume that the current fragment is FragmentMain.
        //It doesn't matter if the user approves or rejects the sms request, we'll show authentication
        //screen regardless.
        ((FragmentMain) getFragmentManager().findFragmentById
                (R.id.fragment_container)).authenticateWithNeura();

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}