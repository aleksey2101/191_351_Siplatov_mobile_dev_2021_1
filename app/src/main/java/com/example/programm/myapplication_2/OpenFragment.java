package com.example.programm.myapplication_2;

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

public class OpenFragment extends Fragment {
    final static String TAG="myLogs";
    private static View rootView;

    private Bundle webViewBundle;

    public OpenFragment() {
        // Required empty public constructor
    }

    public static OpenFragment newInstance(/*String param1, String param2*/) {
        OpenFragment fragment = new OpenFragment();
        Bundle args = new Bundle();
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
