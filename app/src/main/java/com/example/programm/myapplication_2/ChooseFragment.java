package com.example.programm.myapplication_2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Objects;


public class ChooseFragment extends Fragment {

    public ChooseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment ChooseFragment.
     */
    public static ChooseFragment newInstance(/*String param1, String param2*/) {
        ChooseFragment fragment = new ChooseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_choose, container, false);
        Button googleButton=(Button)view.findViewById(R.id.button2);
        googleButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                googleButtonClick(v);
            }
        });
        Button yandexButton=(Button)view.findViewById(R.id.button3);
        yandexButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                yandexButtonClick(v);
            }
        });

        return view;
    }

    private void googleButtonClick(View v) {
        ((MainActivity) Objects.requireNonNull(getActivity())).googleButtonClick(v);
    }

    private void yandexButtonClick(View v){
        ((MainActivity)Objects.requireNonNull(getActivity())).yandexButtonClick(v);
    }
}
