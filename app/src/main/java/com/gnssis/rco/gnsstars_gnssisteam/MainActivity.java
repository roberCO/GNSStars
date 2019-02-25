package com.gnssis.rco.gnsstars_gnssisteam;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnssis.rco.gnsstars_gnssisteam.database.Database;
import com.gnssis.rco.gnsstars_gnssisteam.entity.Message;
import com.gnssis.rco.gnsstars_gnssisteam.provider.LocationListenerTest;
import com.gnssis.rco.gnsstars_gnssisteam.provider.RawMeasurementsProvider;


public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener, MapFragment.OnFragmentInteractionListener {

    private final int ACCESS_REQUEST = 10;

    private ViewPager mViewPager;
    private DataViewerAdapter mViewPagerAdapter;
    private RawMeasurementsProvider rawMeasurementsProvider;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        initializeGNSStars();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListenerTest();

        rawMeasurementsProvider = new RawMeasurementsProvider(this, locationManager, locationListener);
        rawMeasurementsProvider.addGnssStatusListener();
        rawMeasurementsProvider.addGnssMeasurementsListener();

        Database database = new Database();
        database.saveMessage(new Message("desc", "123", "321", "earth", "planet", "http://", "title"));

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar_custom);
        setSupportActionBar(toolbar);

    }

    /**
     * Initialize the main view and set up the ViewPager with the sections adapter
     */
    private void initializeGNSStars() {
        setContentView(R.layout.activity_main);

        mViewPager = findViewById(R.id.container);
        mViewPagerAdapter = new DataViewerAdapter(getSupportFragmentManager());
        mViewPagerAdapter.initialize();
        mViewPager.setAdapter(mViewPagerAdapter);
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
}