package com.gnssis.rco.gnsstars_gnssisteam.provider;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class LocationListenerTest implements LocationListener {

    @Override
    public void onLocationChanged(Location location) {
        /*System.out.println("on Location Changed");
        System.out.println(location.getLatitude() + "");
        System.out.println(location.getLongitude() + "");
        System.out.println(location.getProvider());
        System.out.println(location.getTime() + "");
        System.out.println(location.getAccuracy() + "");*/
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        System.out.println("on Status Changed");
        System.out.println("provider: " + provider);
        System.out.println("extras: " + extras.toString());

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
