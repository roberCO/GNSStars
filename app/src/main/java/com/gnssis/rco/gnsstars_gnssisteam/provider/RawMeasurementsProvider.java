package com.gnssis.rco.gnsstars_gnssisteam.provider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.GnssMeasurement;
import android.location.GnssMeasurementsEvent;
import android.location.GnssStatus;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.gnssis.rco.gnsstars_gnssisteam.MainActivity;

import java.util.Collection;

public class RawMeasurementsProvider {

    private final int ACCESS_REQUEST = 10;

    private LocationManager locationManager;
    private GnssStatus.Callback gnssStatusListener;
    private GnssMeasurementsEvent.Callback gnssMeasurementsListener;

    public RawMeasurementsProvider(MainActivity mainActivity, LocationManager locationManager, LocationListener locationListener) {
        this.locationManager = locationManager;
        this.registerLocationManager(mainActivity, locationManager, locationListener);
    }

    public void addGnssStatusListener() {
        System.out.println("GnssStatus starting");
        gnssStatusListener = new GnssStatus.Callback() {
            @Override
            public void onSatelliteStatusChanged(GnssStatus status) {
                super.onSatelliteStatusChanged(status);
                System.out.println("GnssStatus on Satellite Status Changed");
                displayGnssStatus(status);
            }

            @Override
            public void onFirstFix(int ttffMillis) {
                super.onFirstFix(ttffMillis);
                System.out.println("GnssStatus on First Fix");
            }

            @Override
            public void onStarted() {
                super.onStarted();
                System.out.println("GnssStatus on Started");
            }

            @Override
            public void onStopped() {
                super.onStopped();
                System.out.println("GnssStatus on Stopped");
            }
        };
        try {
            locationManager.registerGnssStatusCallback(gnssStatusListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void addGnssMeasurementsListener() {
        System.out.println("addGnssMeasurementsListener");
        gnssMeasurementsListener = new GnssMeasurementsEvent.Callback() {
            @Override
            public void onGnssMeasurementsReceived(GnssMeasurementsEvent eventArgs) {
                super.onGnssMeasurementsReceived(eventArgs);
                System.out.println("onGnssMeasurementsReceived");
                System.out.println("GNSSClock.getBiasNanos() " + eventArgs.getClock().getBiasNanos());
                displayGnssInfo(eventArgs);
            }

            @Override
            public void onStatusChanged(int status) {
                final String statusMessage;
                switch (status) {
                    case STATUS_LOCATION_DISABLED:
                        statusMessage = "STATUS_LOCATION_DISABLED";
                        break;
                    case STATUS_NOT_SUPPORTED:
                        statusMessage = "STATUS_NOT_SUPPORTED";
                        break;
                    case STATUS_READY:
                        statusMessage = "STATUS_READY";
                        break;
                    default:
                        statusMessage = "gnss_status_unknown";
                }
                System.out.println("GnssMeasureEvt on Status Changed: " + statusMessage);
            }
        };

        Boolean res;
        try {
            res = locationManager.registerGnssMeasurementsCallback(gnssMeasurementsListener);
            System.out.println("registerGnssMeasurementsCallback = " + res);

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void displayGnssStatus(GnssStatus status) {
        String data = "";
        System.out.println("GnssStatus satellites count: " + status.getSatelliteCount());
        for (int i = 0; i < status.getSatelliteCount(); i++) {
            data = data + "\n" +
                    status.getSvid(i) + " | " +
                    status.getAzimuthDegrees(i) + " | " +
                    status.getCn0DbHz(i) + " | " +
                    status.getConstellationType(i);
            //SV id | Azimuth Degrees | Signal | Constellation type
            System.out.println("GnssStatus data: " + data);
        }
    }

    private void displayGnssInfo(GnssMeasurementsEvent event) {
        Collection<GnssMeasurement> gnssList = event.getMeasurements();
        String satData = "";
        for (GnssMeasurement sat : gnssList) {
            System.out.println("displayGnssInfo: ");
            satData = satData + "\n" +
                    sat.getAccumulatedDeltaRangeMeters() + " | " +
                    sat.getCn0DbHz() + " | " +
                    sat.getConstellationType() + " | " +
                    sat.getSnrInDb() + " | " +
                    sat.getState() + " | " +
                    sat.getSvid();
            System.out.println("sat: " + sat);
        }
    }

    /**
     * @param mainActivity
     * @param locationManager
     * @param locationListener
     * Register the listener with the Location Manager to receive location updates
     */
    private void registerLocationManager(MainActivity mainActivity, LocationManager locationManager, LocationListener locationListener) {
        if (ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Do not have all permission, let's ask them");
            ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_REQUEST);

        } else {
            System.out.println("Already have the permissions. Lets request location manager");
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }
}
