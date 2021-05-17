package com.example.programm.myapplication_2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;

import static com.example.programm.myapplication_2.OpenFragment.setWebView;

public class MainActivity extends AppCompatActivity implements EditorFragment.OnFragmentInteractionListener,ChooseFragment.OnFragmentInteractionListener,OpenFragment.OnFragmentInteractionListener {

    final static String TAG="myLogs";
    //public static String GlobalStringRequest;
    final int REQUEST_CODE_PHOTO_CAMERA = 2;
    //final int REQUEST_CODE_ALIGN = 2;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_IS_ON_EXTERNAL_BROWSER = "is on external browser";
    SharedPreferences mSettings;
    private boolean IsExternalBrowserOn;
    private String LAST_RECOGNITION_TEXT;
    @Override
    public void onFragmentInteraction(Uri uri) {
        //empty
    }

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        /*
        if (mSettings.contains(APP_PREFERENCES_IS_ON_EXTERNAL_BROWSER)) {
            // Получаем число из настроек
            IsExternalBrowserOn = mSettings.getBoolean(APP_PREFERENCES_IS_ON_EXTERNAL_BROWSER,false);
            // Выводим на экран данные из настроек
            Log.d("onResume",IsExternalBrowserOn+"");
        }
        */


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){
                case 0: return Lab1Fragment.newInstance();
                case 1: return Lab2Fragment.newInstance();
                case 2: return Lab3Fragment.newInstance();
                case 3: return EditorFragment.newInstance();
                case 4: return ChooseFragment.newInstance();
                case 5: return OpenFragment.newInstance();
                case 6: return Lab4Fragment.newInstance();
            }
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 7;
        }

        int getOpenFragment() {
            // Show 5 total pages.
            return 5;
        }

        int getChooseFragment(){
            return 4;
        }
    }


    void loadLastRecognitionText() {
        mSettings= getSharedPreferences("my_file",MODE_PRIVATE);
        //SharedPreferences.Editor editor=mSettings.edit();

        LAST_RECOGNITION_TEXT = mSettings.getString("LAST_RECOGNITION_TEXT",null);
        SharedPreferences.Editor editor=mSettings.edit();
        editor.putString("LAST_RECOGNITION_TEXT",null);//""
        editor.apply();
        //return LAST_RECONGNITION_TEXT;
    }

    //todo заменить onResume на onActivityResult + onSaveInstanceState + onRestoreInstanceState (возможно имелось ввиду вебактивити(?)

    @Override
    protected void onResume() {
        /** Ставим текст из фотоопределения в тестовое поле */
        Log.d("kyky_onResume","method is going");
        mSettings= getSharedPreferences("my_file",MODE_PRIVATE);
        if (mSettings.contains("APP_PREFERENCES_IS_ON_EXTERNAL_BROWSER")) {
            // Получаем число из настроек
            IsExternalBrowserOn = mSettings.getBoolean(APP_PREFERENCES_IS_ON_EXTERNAL_BROWSER,false);
            // Выводим на экран данные из настроек
            Log.d("onResume",IsExternalBrowserOn+"");
        }
        if (mSettings.contains("LAST_RECOGNITION_TEXT")) {
            loadLastRecognitionText();

            Log.d("kyky_onResume", "3 if LAST_RECOGNITION_TEXT " + LAST_RECOGNITION_TEXT);
            if (LAST_RECOGNITION_TEXT != null) {
                Log.d("kyky_onResume", "3 if is going");
                EditText editText = findViewById(R.id.editText3);
                editText.setText(LAST_RECOGNITION_TEXT);
                LAST_RECOGNITION_TEXT = null;
            }
        }
        super.onResume();
    }

    /** классы для запроса по сайтам */
    public static class SiteGoogle {
        String answer="";
        //ссылка
        SiteGoogle(String request) {
            Document doc=null;
            String linkst="";
            final String ENCODING = "UTF-8";

            //String google = "https://www.google.com/search?#q=";
            String google = "https://www.google.ru/search?q=";
            //https://www.google.ru/search?q=123&oq=123
            try {
                linkst = google + URLEncoder.encode(request, ENCODING)+"&gws_rd=ssl";
            } catch (UnsupportedEncodingException wow) {
                throw new RuntimeException(wow.getMessage(), wow);
            }

            //Log.d(TAG,"s="+s);
            Log.i(TAG,"s = "+linkst);

            answer=linkst;
        }
        public String getString(){
            return answer;
        }

        public static String percentEncode(String s) {
            final String ENCODING = "UTF-8";
            //Log.d(TAG,"s="+s);
            Log.i(TAG,"s = "+s);
            if (s == null) {
                return "";
            }
            try {
                return URLEncoder.encode(s, ENCODING)
                        // OAuth encodes some characters differently:
                        .replace("+", "%20").replace("*", "%2A")
                        .replace("%7E", "~");
                // This could be done faster with more hand-crafted code.
            } catch (UnsupportedEncodingException wow) {
                throw new RuntimeException(wow.getMessage(), wow);
            }
        }

    }

    /*private class ThreadGoog extends AsyncTask<String, Void, String> {
        //здесь не нужен AsyncTask
        // Метод выполняющий запрос в фоне, в версиях выше 4 андроида, запросы в главном потоке выполнять
        // нельзя, поэтому все что вам нужно выполнять - выносите в отдельный тред
        private String StringRequest;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String text = EditorFragment.readText();
            if (text != null) {
                StringRequest = text;
            }
        }
        @Override
        protected String doInBackground(String... arg) {
            SiteGoogle syte1 = new SiteGoogle(StringRequest);
            String result=syte1.getString();
            //класс который захватывает страницу
            return result;
        }
        @Override
        protected void onPostExecute(String url) {
            setWebView(url);
            //открывает 3 фрагмент с веб вью
            setCurrentItemOpenFragment();

            *//*WebView webView = (WebView) findViewById(R.id.webview1);

            //webView.setBackgroundColor(getResources().getColor(R.color.));

            //WebSettings webSettings = webView.getSettings();

            // устанавливаем Zoom control
            //webView.getSettings().setBuiltInZoomControls(true);

            //webSettings.setJavaScriptEnabled(true);
            //скрытие всплывающего окна, но отключение рекламы на сайте
            webView.loadUrl(result);
            *//*

            //webView.loadDataWithBaseURL(null,);

            //OpenWebActivity(result);
            Log.i("kyky","AsyncTask is going...");

        }

    }*/

    //https://yandex.ru/search/?text=123&lr=967
    //https://yandex.ru/search/?text=123
    public static class SiteYandex {
        String answer="";
        //ссылка
        SiteYandex(String request) {
            Document doc=null;
            String linkst="";
            final String ENCODING = "UTF-8";
            String google = "https://yandex.ru/search/?text=";;

            try {
                linkst = google + URLEncoder.encode(request, ENCODING);
            } catch (UnsupportedEncodingException wow) {
                throw new RuntimeException(wow.getMessage(), wow);
            }

            //Log.d(TAG,"s="+s);
            Log.i(TAG,"s = "+linkst);

            answer=linkst;
        }
        public String getString(){
            return answer;
        }

        public static String percentEncode(String s) {
            final String ENCODING = "UTF-8";
            //Log.d(TAG,"s="+s);
            Log.i(TAG,"s = "+s);
            if (s == null) {
                return "";
            }
            try {
                return URLEncoder.encode(s, ENCODING)
                        // OAuth encodes some characters differently:
                        .replace("+", "%20").replace("*", "%2A")
                        .replace("%7E", "~");
                // This could be done faster with more hand-crafted code.
            } catch (UnsupportedEncodingException wow) {
                throw new RuntimeException(wow.getMessage(), wow);
            }
        }

    }

    void setCurrentItemChooseFragment(){
//        mViewPager.setCurrentItem(1);
//        mViewPager.setCurrentItem(mSectionsPagerAdapter.getCount()-2);
        mViewPager.setCurrentItem(mSectionsPagerAdapter.getChooseFragment());
    }

    void setCurrentItemOpenFragment(){
//        mViewPager.setCurrentItem(2);
//        mViewPager.setCurrentItem(mSectionsPagerAdapter.getCount()-1);
        mViewPager.setCurrentItem(mSectionsPagerAdapter.getOpenFragment());
    }



    public void googleButtonClick(View v) {
//        new ThreadGoog().execute();
        String text = EditorFragment.readText();
        if (text != null) {

            SiteGoogle syte1 = new SiteGoogle(text);
            String url=syte1.getString();
            setWebView(url);
            //открывает 3 фрагмент с веб вью
            setCurrentItemOpenFragment();
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),"Can`t send request to google.ru",Toast.LENGTH_SHORT);
            //но здесь не будет ошибки
            //todo реализовать текст ошибок на разных языках
            toast.show();
        }
    }

    public void yandexButtonClick(View v){
        String text = EditorFragment.readText();
        if (text != null) {

            SiteYandex syte1 = new SiteYandex(text);
            String url=syte1.getString();
            //todo разобраться с получением фрагмента по типу активности как getactivity
            OpenFragment.setWebView(url);
            //открывает 3 фрагмент с веб вью
            setCurrentItemOpenFragment();
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),"Can`t send request to yandex.ru",Toast.LENGTH_SHORT);
            //но здесь не будет ошибки
            //todo реализовать текст ошибок на разных языках
            toast.show();
        }


        //открывает 3 фрагмент с веб вью
        setCurrentItemOpenFragment();
    }

    void hideActionBar(){
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

}
//todo реализовать переход назад по фрагментам с послед выходом из мриложения
