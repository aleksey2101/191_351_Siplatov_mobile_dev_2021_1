package com.example.programm.myapplication_2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.util.Objects;

public class Main2ActivityWebView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_web_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawWebView();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void drawWebView ()
    {
        //отрисовываем webview
        WebView webView = (WebView) findViewById(R.id.WebView);

        WebSettings webSettings = webView.getSettings();

        //webSettings.setJavaScriptEnabled(true);
        //скрытие всплывающего окна, но отключение рекламы на сайте
        try {


            String UrlForWebView = getIntent().getStringExtra("UrlForWebView");
            Log.d("UrlForWebView=",UrlForWebView);
            webView.loadUrl(UrlForWebView);
        }catch (Exception e){
            Log.d("UrlForWebView ошибка",e.toString());
        }

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        WebView webView = (WebView) findViewById(R.id.WebView);
        webView.saveState(outState);
        //Log.d("kyky_onSIT","method_is_running");
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        WebView webView = (WebView) findViewById(R.id.WebView);
        webView.restoreState(savedInstanceState);
        //Log.d("kyky_onRIS","method_is_running");
    }

}
