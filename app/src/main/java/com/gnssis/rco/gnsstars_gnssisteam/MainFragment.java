package com.gnssis.rco.gnsstars_gnssisteam;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements DataViewer{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LayoutInflater inflaterConstellations;
    private View viewConstellation;
    private TextView headerOption;
    private LinearLayout constellation;
    private ImageView imageViewConstellation;
    private TextView textConstellation;
    private RelativeLayout swithOption;
    private Spinner spinner;
    private Button positionButton;

    private View viewPlots;
    private LayoutInflater inflaterPlots;
    private LinearLayout plot;
    private ImageView imageViewPlot;
    private Button saveButton;
    private Button discardButton;

    private boolean positionButtonPressed = false;

    private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view;
        view = inflater.inflate(R.layout.fragment_main, container, false);

        initializeElements(view);

        /* Constellation Roulette*/
        for (int i=0; i<5; i++){

            viewConstellation = inflaterConstellations.inflate(R.layout.constellation, constellation, false);
            imageViewConstellation = viewConstellation.findViewById(R.id.imageViewConstellation);
            textConstellation = viewConstellation.findViewById(R.id.textWith);

            switch (i){
                case 0: //UE
                    textConstellation.setText(R.string.EUConstellation);
                    imageViewConstellation.setImageResource(R.drawable.eu);
                    break;
                case 1: //USA
                    textConstellation.setText(R.string.USAConstellation);
                    imageViewConstellation.setImageResource(R.drawable.usa);
                    break;
                case 2: //UE+USA
                    textConstellation.setText(R.string.EUUSAConstellation);
                    imageViewConstellation.setImageResource(R.drawable.both);
                    break;
                case 3: //GLONASS
                    textConstellation.setText(R.string.RussiaConstellation);
                    imageViewConstellation.setImageResource(R.drawable.rusia);
                    break;
                case 4: //BeiDou
                    textConstellation.setText(R.string.ChinaConstellation);
                    imageViewConstellation.setImageResource(R.drawable.china);
                    break;
            }

            constellation.addView(viewConstellation);
        }

        /*Plots scroll*/
        for (int i=0; i<5; i++){

            viewPlots = inflaterPlots.inflate(R.layout.plot_scrollview, plot, false);
            imageViewPlot = viewPlots.findViewById(R.id.imageViewPlot);

            switch (i){
                case 0: //UE
                    imageViewPlot.setImageResource(R.drawable.plot1);
                    break;
                case 1: //USA
                    imageViewPlot.setImageResource(R.drawable.plot2);
                    break;
                case 2: //UE+USA
                    imageViewPlot.setImageResource(R.drawable.plot3);
                    break;
                case 3: //GLONASS
                    imageViewPlot.setImageResource(R.drawable.plot4);
                    break;
                case 4: //BeiDou
                    imageViewPlot.setImageResource(R.drawable.plot1);
                    break;
            }

            plot.addView(viewPlots);
        }

        plot.setVisibility(view.INVISIBLE);

        /* Spinner creation */
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.singleCorrections, R.layout.main_spinner_option);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_options);
        spinner.setSelection(0);
        spinner.setAdapter(spinnerAdapter);
        spinner.setPopupBackgroundResource(R.color.white);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {

             }

        });

        defineBehaviourPositionButton(view);
        defineBehaviourSaveButton(view);
        defineBehaviourDiscardButton(view);

        // Inflate the layout for this fragment
        return view;
    }

    private void defineBehaviourPositionButton(final View view) {

        positionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

            plot.setVisibility(view.VISIBLE);

            spinner.setVisibility(view.INVISIBLE);
            constellation.setVisibility(view.INVISIBLE);
            swithOption.setVisibility(view.INVISIBLE);
            headerOption.setVisibility(view.INVISIBLE);

            if(positionButtonPressed) {

                Toast.makeText(getApplicationContext(), R.string.positionStopText, Toast.LENGTH_SHORT).show();
                positionButton.setText(R.string.textMainButton);
                saveButton.setVisibility(view.VISIBLE);
                discardButton.setVisibility(view.VISIBLE);


                positionButtonPressed = false;

            } else {

                Toast.makeText(getApplicationContext(), R.string.positionStartText, Toast.LENGTH_SHORT).show();
                positionButton.setText(R.string.textMainButtonPressed);
                saveButton.setVisibility(view.INVISIBLE);
                discardButton.setVisibility(view.INVISIBLE);

                positionButtonPressed = true;
            }

            }
        });

    }

    private void defineBehaviourSaveButton(final View view) {

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), R.string.savedGNSSData, Toast.LENGTH_SHORT).show();

                plot.setVisibility(view.INVISIBLE);
                saveButton.setVisibility(view.INVISIBLE);
                discardButton.setVisibility(view.INVISIBLE);

                spinner.setVisibility(view.VISIBLE);
                constellation.setVisibility(view.VISIBLE);
                swithOption.setVisibility(view.VISIBLE);
                headerOption.setVisibility(view.VISIBLE);

            }
        });

    }

    private void defineBehaviourDiscardButton(final View view) {

        discardButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), R.string.discardedGNSSData, Toast.LENGTH_SHORT).show();

                plot.setVisibility(view.INVISIBLE);
                saveButton.setVisibility(view.INVISIBLE);
                discardButton.setVisibility(view.INVISIBLE);

                spinner.setVisibility(view.VISIBLE);
                constellation.setVisibility(view.VISIBLE);
                swithOption.setVisibility(view.VISIBLE);
                headerOption.setVisibility(view.VISIBLE);

            }
        });

    }

    private void initializeElements(View view) {

        inflaterConstellations = LayoutInflater.from(getApplicationContext());
        headerOption = view.findViewById(R.id.headerOptions);
        constellation = view.findViewById(R.id.constellations);
        swithOption = view.findViewById(R.id.containerCorrectionFrequency);
        spinner = view.findViewById(R.id.spinnerCorrection);
        positionButton = view.findViewById(R.id.positionButton);

        inflaterPlots = LayoutInflater.from(getApplicationContext());
        plot = view.findViewById(R.id.plot_scrollview);
        saveButton = view.findViewById(R.id.saveButon);
        discardButton = view.findViewById(R.id.discardButon);


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onLocationFromGoogleServicesResult(Location location) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
