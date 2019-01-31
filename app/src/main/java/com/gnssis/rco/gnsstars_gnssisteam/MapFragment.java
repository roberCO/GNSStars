package com.gnssis.rco.gnsstars_gnssisteam;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Message message;
    private Double test_latitude;

    private OnFragmentInteractionListener mListener;
    private MapboxMap mapboxMap;
    private MapView mMapView;
    private PermissionsManager permissionsManager;

    public static MapFragment create() {
        return new MapFragment();
    }

    public void retrievePoints(int id){
        // FIREBASE SAMPLE
        // Get a reference to points
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("points/" + id);

        // Attach a listener to read the data at our points reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                message = dataSnapshot.getValue(Message.class);
                System.out.println("Retrieved point from Firebase: " + message);
                System.out.println("Retrieved latitude from Firebase: " + message.getLatitude());
                System.out.println("Retrieved longitude from Firebase: " + message.getLongitude());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    // Create the instance
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context ctx = getActivity().getApplicationContext();
        Mapbox.getInstance(ctx, "pk.eyJ1Ijoic2dvbmdvcmEiLCJhIjoiY2pyNjkxemhiMGR4MzQ4bXRkZHdhc2g4ayJ9.74xaSv0UpPG3bsZU0Z865w");
    }

    // Create the map
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = v.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState); // If this line is omitted there is a native crash in the MapBox library

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                // retrievePoints(1);
                // System.out.println("Test latitude from Firebase: " + test_latitude);
                // Retiro
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(40.415791, -3.683848))
                        .title(getString(R.string.draw_marker_options_title_retiro))
                        .snippet(getString(R.string.draw_marker_options_snippet_retiro)));
                // Real Palace
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(40.418425, -3.713100))
                        .title(getString(R.string.draw_marker_options_title_palace))
                        .snippet(getString(R.string.draw_marker_options_snippet_palace)));
            }
        });

        return v;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public void onMapReady(MapboxMap mapboxMap) {
        MapFragment.this.mapboxMap = mapboxMap;
//        enableLocationComponent();
    }
//
//    @SuppressWarnings( {"MissingPermission"})
//    private void enableLocationComponent() {
//// Check if permissions are enabled and if not request
//        if (PermissionsManager.areLocationPermissionsGranted(this)) {
//
//            LocationComponentOptions options = LocationComponentOptions.builder(this)
//                    .trackingGesturesManagement(true)
//                    .accuracyColor(ContextCompat.getColor(this, R.color.mapboxGreen))
//                    .build();
//
//            // Get an instance of the component
//            LocationComponent locationComponent = mapboxMap.getLocationComponent();
//
//            // Activate with options
//            locationComponent.activateLocationComponent(this, options);
//
//            // Enable to make component visible
//            locationComponent.setLocationComponentEnabled(true);
//
//            // Set the component's camera mode
//            locationComponent.setCameraMode(CameraMode.TRACKING);
//            locationComponent.setRenderMode(RenderMode.COMPASS);
//        } else {
//            permissionsManager = new PermissionsManager(this);
//            permissionsManager.requestLocationPermissions(this);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
//
//    @Override
//    public void onExplanationNeeded(List<String> permissionsToExplain) {
//        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onPermissionResult(boolean granted) {
//        if (granted) {
//            enableLocationComponent();
//        } else {
//            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
//            finish();
//        }
//    }

    @Override
    @SuppressWarnings( {"MissingPermission"})
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}