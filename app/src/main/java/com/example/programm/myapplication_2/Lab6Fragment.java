package com.example.programm.myapplication_2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Lab6Fragment extends Fragment {

    public static Lab6Fragment newInstance() {
        return new Lab6Fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lab6_fragment, container, false);
//        return inflater.inflate(R.layout.lab1_grid_fragment, container, false);
    }

}
