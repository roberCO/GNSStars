package com.gnssis.rco.gnsstars_gnssisteam;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DetailFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup views;
        views = (ViewGroup) inflater.inflate(R.layout.fragment_details, container, false);

        return views;
    }

}
