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
import android.widget.Button;

import java.util.Objects;

public class Lab4Fragment extends Fragment {

    private static final int ID_APP = 7861242;

    public static Lab4Fragment newInstance() {
        return new Lab4Fragment();
    }

    final static String TAG="myLogs";
    private View rootView;
//    private OnFragmentInteractionListener mListener;
    private Bundle webViewBundle;

    public Lab4Fragment() {
        // Required empty public constructor
    }


    boolean isActionBarClosed = false;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, "setUserVisibleHint is working");

        Log.d(TAG, "isVisibleToUser = "+isVisibleToUser);

        if(isVisibleToUser){
            //скрываем actionbar
            Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).hide();
            //скрыто ли уже
            isActionBarClosed=true;
        }
        else if(isActionBarClosed){
            Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).show();
            isActionBarClosed=false;
        }

//        Toast toast = Toast.makeText(rootView.getContext(), "Neither does this", Toast.LENGTH_LONG);
//        toast.show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        /*WebView mWebView = (WebView) rootView.findViewById(R.id.VKWebView);
        //попробоавть перенести в активити класс всё это (WebView)
        mWebView.setWebViewClient(new MyWebViewClient());*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.lab4_fragment, container, false);
        WebView webView = rootView.findViewById(R.id.VKWebView);

        if (webViewBundle != null)
        {

            webView.restoreState(webViewBundle);
        } else {
            //todo webview пока пустая
            String htmlText = "<html><body>Percent test: 100% </body></html>";

            webView.loadData(htmlText, "text/html", "en_US");
        }

        webView.setWebViewClient(new MyWebViewClient());
        //переопределяет ли он этим метод вызова setWebView

        Button VKBtn = rootView.findViewById(R.id.enterVKBtn);
        VKBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWebView("https://oauth.vk.com/authorize?client_id=7861242&display=page&redirect_uri=https://www.google.com/&scope=friends&response_type=token&v=5.131&state=1337");
            }
        });

        return rootView;
    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OpenFragment.OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    public void setWebView(String url) {
        WebView webView = rootView.findViewById(R.id.VKWebView);

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
        WebView webView = rootView.findViewById(R.id.VKWebView);
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
