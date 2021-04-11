package com.example.programm.myapplication_2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.programm.myapplication_2.predictor.PredictResp;
import com.example.programm.myapplication_2.predictor.PredictService;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditorFragment extends Fragment  {
    final static String TAG="myLogs";
    // Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
   /* private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //Rename and change types of parameters
    private String mParam1;
    private String mParam2;*/
    //todo убрать static в переменной
    private static View rootView;

    private EditText query;
    private TextView textView;
    private Retrofit retrofit;
    private int pos=0;

    private OnFragmentInteractionListener mListener;

    public EditorFragment() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment EditorFragment.
//     */
    //  Rename and change types and number of parameters
    public static EditorFragment newInstance(/*String param1, String param2*/) {
        EditorFragment fragment = new EditorFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_editor, container, false);

        query = (EditText) rootView.findViewById(R.id.editText3);
        textView = (TextView) rootView.findViewById(R.id.predict);
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://predictor.yandex.net/")
                .build();
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo переделать выводимый текст
                String text = textView.getText().toString();
                CharSequence chars=textView.getText();


                String str = ((EditText) rootView.findViewById(R.id.editText3)).getText().toString();
                Log.i(TAG,"pos = "+pos);
                Log.i(TAG,"str = "+str);
                if(pos<0) {
                        if (!str.equals(""))
                        str = str.substring(0, str.length() + pos);
                }
                else {
                    StringBuilder strBuilder = new StringBuilder(str);
                    while(pos>0){
                        strBuilder.append(" ");
                        pos--;
                    }
                    str = strBuilder.toString();
                }
                Log.i(TAG,"pre settext");
                query.setText(str + textView.getText());
                //setSection
                query.setSelection(query.getText().length());
                Log.i(TAG,"post settext");
            }
        });
        query.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = charSequence.toString();
                str = str.replace(' ', '+');
                if (!str.equals("")) {
                    final String finalStr = str;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Call<PredictResp> call = retrofit
                                    .create(PredictService.class)
                                    .complete(getString(R.string.API_KEY), "ru", finalStr);
                            //todo смена языка
                            Callback<PredictResp> callback = new Callback<PredictResp>() {
                                @Override
                                public void onResponse(Call<PredictResp> call, Response<PredictResp> response) {
                                    //maybe null
                                    List<String> list = response.body().getText();
                                    if (list.size() > 0)
                                        textView.setText(list.get(0));
                                    pos = response.body().getPos();
                                }

                                @Override
                                public void onFailure(Call<PredictResp> call, Throwable t) {
                                    //ошибка из-за активити
                                    //Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                                    Log.e(TAG+" kyky",t.getMessage());
                                }
                            };
                            call.enqueue(callback);
                        }
                    }, 100);
                } else{
                    //empty
                    textView.setText(R.string.hint_editor_textview);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //ывшая кнопка камеры
//        ImageButton cameraButton=(ImageButton) rootView.findViewById(R.id.imageButton2);
//        cameraButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // Perform action on click
//                cameraButtonClick(v);
//            }
//        });
        ImageButton enterButton = (ImageButton)rootView.findViewById(R.id.imageButton);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterButtonClick(v);
            }
        });
        return rootView;
    }

    //  Rename method, update argument and hook method into UI event
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
        // Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


//    public static void printText(){
//        ((EditText)rootView.findViewById(R.id.editText3)).setText(MainActivity.GlobalStringRequest);
//    }

    public static String readText(){
        if(((EditText) rootView.findViewById(R.id.editText3)).getText().toString()!=null) {
            String text = ((EditText) rootView.findViewById(R.id.editText3)).getText().toString();
            //MainActivity.GlobalStringRequest = text;
            return text;
        }
        return null;
    }

    void enterButtonClick(View v){
        //MainActivity.setChooseFragment();
        ((MainActivity) Objects.requireNonNull(getActivity())).setCurrentItemChooseFragment();
    }
}
