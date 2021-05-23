package com.example.programm.myapplication_2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OpenFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OpenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OpenFragment extends Fragment {
    final static String TAG="myLogs";
    // Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
   /* private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //Rename and change types of parameters
    private String mParam1;
    private String mParam2;*/

    private static View rootView;

    private OnFragmentInteractionListener mListener;

    private Bundle webViewBundle;

    public OpenFragment() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment OpenFragment.
//     */
    //  Rename and change types and number of parameters
    public static OpenFragment newInstance(/*String param1, String param2*/) {
        OpenFragment fragment = new OpenFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public boolean isActionBarClosed = false;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, "setUserVisibleHint is working");

        Log.d(TAG, "isVisibleToUser = "+isVisibleToUser);

        if(isVisibleToUser){
            Log.d(TAG, "Закрываеи экшн бар");
            //скрываем actionbar
            Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).hide();
            //скрыто ли уже
            isActionBarClosed=true;
        }
        else if(isActionBarClosed){
            Log.d(TAG, "Открываем экшн бар");
            Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).show();
            isActionBarClosed=false;
        }

//        Toast toast = Toast.makeText(rootView.getContext(), "Neither does this", Toast.LENGTH_LONG);
//        toast.show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).hide();

        //Log.i(TAG+" proky","onCreate is working...");
        /*
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        */
        setRetainInstance(true);

        /*WebView mWebView = (WebView) rootView.findViewById(R.id.WebView);
        //попробоавть перенести в активити класс всё это (WebView)
        mWebView.setWebViewClient(new MyWebViewClient());*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_open, container, false);
        //Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).hide();

//        Objects.requireNonNull(Objects.requireNonNull(getActivity()).getActionBar()).hide();
//        ((MainActivity)Objects.requireNonNull(getActivity())).getActionBar().hide();

        WebView webView = rootView.findViewById(R.id.WebView);

        if (webViewBundle != null)
        {

            webView.restoreState(webViewBundle);
        } else {
            //todo webview пока пустая
            String htmlText = "<html><body>Percent test: 100% </body></html>";

            webView.loadData(htmlText, "text/html", "en_US");
        }
        //Log.i(TAG+" proky","onCreateView is working...");

        //попробоавть перенести в активити класс всё это (WebView)

//        WebView.setWebViewClient(new MyWebViewClient());

        webView.setWebViewClient(new MyWebViewClient());
        //переопределяет ли он этим метод вызова setWebView

        return rootView;
    }

    // Rename method, update argument and hook method into UI event
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

    public static void setWebView(String url) {
        WebView webView = rootView.findViewById(R.id.WebView);

        //webView.setBackgroundColor(getResources().getColor(R.color.));

        WebSettings webSettings = webView.getSettings();

        webSettings.setDefaultTextEncodingName("utf-8");
        // устанавливаем Zoom control
        //webView.getSettings().setBuiltInZoomControls(true);

        //webSettings.setJavaScriptEnabled(true);
        //скрытие всплывающего окна, но отключение рекламы на сайте

        //webView.loadDataWithBaseURL(null,);
        webView.loadUrl(url);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        WebView webView = rootView.findViewById(R.id.WebView);
        webViewBundle = new Bundle();
        webView.saveState(webViewBundle);
        //Log.i(TAG+" proky","onPause is working...");
    }

    private static class MyWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            //todo в зависимости он выбранной настройки - открывать внутри или в браузере (опционально)

            //todo определить список ссылок которые надо открывать внутри приложения
            view.loadUrl(url);
            return true;

            /*
            // все остальные ссылки будут спрашивать какой браузер открывать
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            activity.startActivity(intent);
            */
        }
    }
}
