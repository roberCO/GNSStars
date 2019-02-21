package com.gnssis.rco.gnsstars_gnssisteam;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.gnssis.rco.gnsstars_gnssisteam.entity.Message;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonElement;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements View.OnClickListener, DataViewer, OnMapReadyCallback,
        MapboxMap.OnMapClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private MapboxMap map;
    private Marker featureMarker;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    final Handler handler = new Handler();
    private SymbolManager symbolManager;
    private List<Symbol> symbols = new ArrayList<>();
    private MapView mapView;
    private boolean gps_selected;
    private boolean galileo_selected;
    private boolean location_enabled;
    private boolean satellite_style;
    private LocalPoint point;
    private Message message;
    private Double test_latitude;

    Thread threadObj = new Thread() {
        public void run() {

            // Asynctask
            System.out.println("Perform call");
            Layer layer = map.getStyle().getLayer("layer-position");
            map.getStyle().removeLayer("layer-position"); // Should remove layer
            System.out.println("layer = " + layer);
            // if (layer != null) {
            // System.out.println("layer not null");
            /*
            if (VISIBLE.equals(layer.getVisibility().getValue())) {
                layer.setProperties(visibility(NONE));
            } else {
                layer.setProperties(visibility(VISIBLE));
            }
            */
            //}
            // delay
            handler.postDelayed(this, 1000);
        }
    };
    private MapView mMapView;
    private ImageButton buttonSelectConstellation;
    private ImageButton buttonGetLocation;
    private ImageButton buttonSelectLayer;
    private Style myStyle;

    public static MapFragment create() {
        return new MapFragment();
    }

    public void retrievePoints(int id) {
        // FIREBASE SAMPLE
        // Get a reference to points
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("points/" + id);

        // Attach a listener to read the data at our points reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                point = dataSnapshot.getValue(LocalPoint.class);
                System.out.println("Retrieved point from Firebase: " + point);
                System.out.println("Retrieved latitude from Firebase: " + point.latitude);
                System.out.println("Retrieved longitude from Firebase: " + point.longitude);
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
        location_enabled = false;
        satellite_style = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Context ctx = getActivity().getApplicationContext();
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        // Register buttons
        buttonSelectConstellation = v.findViewById(R.id.selectConstellation);
        // registerForContextMenu(v.findViewById(R.id.selectConstellation));
        registerForContextMenu(buttonSelectConstellation);
        buttonGetLocation = v.findViewById(R.id.getLocation);
        buttonGetLocation.setOnClickListener(this);
        buttonSelectLayer = v.findViewById(R.id.selectLayer);
        buttonSelectLayer.setOnClickListener(this);

        // Create map
        mMapView = v.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState); // If this line is omitted there is a native crash in the MapBox library
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                map = mapboxMap;
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        map.addOnMapClickListener(MapFragment.this);
                        // Add the initial position using custom marker
                        style.addImage("marker-icon-id",
                                BitmapFactory.decodeResource(getResources(),
                                        R.drawable.mapbox_marker_icon_default));

                        GeoJsonSource geoJsonSource = new GeoJsonSource("user-position",
                                Feature.fromGeometry(Point.fromLngLat(-3.703790, 40.416775)));
                        style.addSource(geoJsonSource);

                        SymbolLayer symbolLayer = new SymbolLayer("layer-position", "user-position");
                        symbolLayer.withProperties(
                                PropertyFactory.iconImage("marker-icon-id")
                        );
                        buttonSelectLayer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                toggleLayer();
                            }
                        });
                        buttonGetLocation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getLocation();
                            }
                        });
                        style.addLayer(symbolLayer);
                    }
                });
            }
        });
        return v;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.constellations, menu);
        // loop for menu items
        for (int i = 0; i < menu.size(); ++i) {
            MenuItem mi = menu.getItem(i);
            // check the Id as you wish
            if (mi.getItemId() == R.id.gps) {
                if (gps_selected) {
                    mi.setChecked(true);
                }
            }
            if (mi.getItemId() == R.id.galileo) {
                if (galileo_selected) {
                    mi.setChecked(true);
                }
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.gps:
                gps_selected = !gps_selected;
                return true;
            case R.id.galileo:
                galileo_selected = !galileo_selected;
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onLocationFromGoogleServicesResult(Location location) {

    }

    @Override
    public void onClick(View v) {

    }

    public void getLocation() {
        System.out.println("location_enabled" + location_enabled);
        if (!location_enabled) {
            threadObj.start();
            System.out.println("gps" + gps_selected);
            System.out.println("galileo" + galileo_selected);
            location_enabled = true;
        } else {
            handler.removeCallbacks(threadObj);
            location_enabled = false;
        }
    }

    // By now there are only two different layers
    private void toggleLayer() {
        if (satellite_style) {
            map.setStyle(Style.MAPBOX_STREETS);
            satellite_style = false;
            System.out.println("Streets style enabled");
        } else {
            map.setStyle(Style.SATELLITE_STREETS);
            satellite_style = true;
            System.out.println("Satellite style enabled");
        }
    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        final PointF pixel = map.getProjection().toScreenLocation(point);
        List<Feature> features = map.queryRenderedFeatures(pixel);

        DetailFragment detailFragment = new DetailFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.map, detailFragment)
                .addToBackStack(null)
                .commit();

        if (features.size() > 0) {
            Feature feature = features.get(0);
            String property;
            StringBuilder stringBuilder = new StringBuilder();
            if (feature.properties() != null) {
                for (Map.Entry<String, JsonElement> entry : feature.properties().entrySet()) {
                    stringBuilder.append(String.format("%s - %s", entry.getKey(), entry.getValue()));
                    stringBuilder.append(System.getProperty("line.separator"));
                }

                featureMarker = map.addMarker(new MarkerOptions()
                        .position(point)
                        .title(feature.toJson())
                        .snippet(stringBuilder.toString())
                );

            }
        }
        map.selectMarker(featureMarker);
        return true;
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}