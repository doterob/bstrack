package es.bahiasoftware.bstrack.iot.location;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by David on 16/05/2017.
 */

public class LocationController implements LocationListener {

    private Location location;

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}
