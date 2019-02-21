package com.gnssis.rco.gnsstars_gnssisteam;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GnssMeasurement;
import android.location.GnssMeasurementsEvent;
import android.location.GnssNavigationMessage;
import android.location.GnssStatus;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.OnNmeaMessageListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnssis.rco.gnsstars_gnssisteam.database.Database;
import com.gnssis.rco.gnsstars_gnssisteam.entity.Message;

import java.util.Collection;


public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener, MapFragment.OnFragmentInteractionListener {

    private final int ACCESS_REQUEST = 10;
    private ViewPager mViewPager;
    private DataViewerAdapter mViewPagerAdapter;

    private LocationManager locationManager;
    LocationListener locationListener;
    private GnssStatus.Callback gnssStatusListener;
    private GnssMeasurementsEvent.Callback gnssMeasurementsListener;
    private OnNmeaMessageListener onNmeaMessageListener;
    private GnssNavigationMessage.Callback gnssNavMessageListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeGNSStars();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListenerTest();

        // Register the listener with the Location Manager to receive location updates
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Do not have all permission, let's ask them");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_REQUEST);

        } else {
            System.out.println("Already have the permissions. Lets request location manager");
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

        addGnssStatusListener();
        addGnssMeasurementsListener();

        Database database = new Database();
        database.saveMessage(new Message("desc", "123", "321", "earth", "planet", "http://", "title"));


        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar_custom);
        setSupportActionBar(toolbar);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        System.out.println("on request permission callback");
        if (requestCode == ACCESS_REQUEST) {
            System.out.println("inside requestCode. Now will check if permissions were granted");
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    private void initializeGNSStars() {
        //Initialize main view
        setContentView(R.layout.activity_main);

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPagerAdapter = new DataViewerAdapter(getSupportFragmentManager());
        mViewPagerAdapter.initialize();
        mViewPager.setAdapter(mViewPagerAdapter);
    }

    private void addGnssStatusListener() {
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

    public void displayGnssStatus(GnssStatus status) {
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

    private void addGnssMeasurementsListener() {
        System.out.println("addGnssMeasurementsListener");
        gnssMeasurementsListener = new GnssMeasurementsEvent.Callback() {
            @Override
            public void onGnssMeasurementsReceived(GnssMeasurementsEvent eventArgs) {
                super.onGnssMeasurementsReceived(eventArgs);
                System.out.println("onGnssMeasurementsReceived");
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

    public void displayGnssInfo(GnssMeasurementsEvent event) {
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

    public void addNavigationMessageListener() {
        Log.d("Marina-NavigationMsg", "starting");
        onNmeaMessageListener = new OnNmeaMessageListener() {
            @Override
            public void onNmeaMessage(String s, long l) {
                Log.d("Marina-Nmea", "onNmeaMessage");
                //displayNmea(s);
            }
        };
        try {
            locationManager.addNmeaListener(onNmeaMessageListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }


    public void addGnssNavigationMsg() {
        gnssNavMessageListener = new GnssNavigationMessage.Callback() {
            @Override
            public void onGnssNavigationMessageReceived(GnssNavigationMessage obj) {
                super.onGnssNavigationMessageReceived(obj);
                displayNavMessage(obj);
            }

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
                Log.d("GnssMeasureEvt", "on Status Changed: " + statusMessage);
            }
        };
        try {
            locationManager.registerGnssNavigationMessageCallback(gnssNavMessageListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void displayNavMessage(GnssNavigationMessage obj) {
        String gnssString =
                obj.getData() + " | " +
                        obj.getMessageId() + " | " +
                        obj.getStatus() + " | " +
                        obj.getSubmessageId() + " | " +
                        obj.getSvid() + " | " +
                        obj.getType();
        Log.d("Marina-GnssMeasureEvt", "onGnssNavigationMessageReceived: " + gnssString);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Fragment newInstance(int sectionNumber) {

            Fragment fragment = null;
            switch (sectionNumber) {
                case 1:
                    fragment = new MainFragment();
                    break;

                case 2:
                    fragment = new MapFragment();
                    break;

            }
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            //TextView textView = (TextView) rootView.findViewById(R.id.txt_display);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }
}