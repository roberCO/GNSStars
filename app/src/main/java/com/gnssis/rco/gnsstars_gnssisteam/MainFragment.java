package com.gnssis.rco.gnsstars_gnssisteam;

import android.content.Context;
import android.content.res.Resources;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

        ViewGroup views;

        views = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);

        /* Constellation roulette creation*/
        LinearLayout constellation = views.findViewById(R.id.constellations);

        LayoutInflater inflaterConstellations = (LayoutInflater) LayoutInflater.from(getApplicationContext());

        for (int i=0; i<5; i++){

            View viewConstellation = inflaterConstellations.inflate(R.layout.constellation, constellation, false);

            TextView textConstellation = viewConstellation.findViewById(R.id.textWith);

            ImageView imageViewConstellation = viewConstellation.findViewById(R.id.imageViewConstellation);

            Resources res = getResources();
            int[] constellationArray = res.getIntArray(R.array.constellations);

            if(i==1) { //UE
                textConstellation.setText(constellationArray[0]);
                imageViewConstellation.setImageResource(R.drawable.eu);
            } else if(i==0) {//USA
                textConstellation.setText(constellationArray[1]);
                imageViewConstellation.setImageResource(R.drawable.usa);
            } else if(i==2) {//UE+USA
                textConstellation.setText(constellationArray[2]);
                imageViewConstellation.setImageResource(R.drawable.both);
            } else if(i==3) {//GLONASS
                textConstellation.setText(constellationArray[3]);
                imageViewConstellation.setImageResource(R.drawable.rusia);
            } else if(i==4) {//BeiDou
                textConstellation.setText(constellationArray[4]);
                imageViewConstellation.setImageResource(R.drawable.china);
            }

            constellation.addView(viewConstellation);
        }

        /* Spinner creation */
        Spinner spinner = views.findViewById(R.id.spinnerCorrection);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.corrections, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 Toast.makeText(getApplicationContext(), parent.getItemAtPosition(position)+" selected", Toast.LENGTH_LONG);
             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {

             }

        });

        // Inflate the layout for this fragment
        return views;
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
