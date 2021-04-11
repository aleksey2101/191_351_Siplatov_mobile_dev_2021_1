package com.example.programm.myapplication_2;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Lab1Fragment extends Fragment {

    private Lab1ViewModel mViewModel;

    public static Lab1Fragment newInstance() {
        return new Lab1Fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lab1_fragment, container, false);
//        return inflater.inflate(R.layout.lab1_grid_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(Lab1ViewModel.class);
        // TODO: Use the ViewModel
    }

}
