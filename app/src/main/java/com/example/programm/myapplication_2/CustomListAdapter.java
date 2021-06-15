package com.example.programm.myapplication_2;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static com.example.programm.myapplication_2.Lab5Fragment.TAG;

public class CustomListAdapter  extends BaseAdapter {
    private List<Podcast> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public CustomListAdapter(Context aContext,  List<Podcast> listData) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private ViewHolder holder;
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_layout, null);
            holder = new ViewHolder();
            holder.imgView = (ImageView) convertView.findViewById(R.id.imageView_flag);
            holder.podcastNameView = (TextView) convertView.findViewById(R.id.textView_countryName);
            holder.linkView = (TextView) convertView.findViewById(R.id.textView_population);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Podcast podcast = this.listData.get(position);
        holder.podcastNameView.setText(podcast.getPodcastName());
        holder.linkView.setText("Link: " + podcast.getUrl());

//        int imageId = this.getMipmapResIdByName(country.getFlagName());

        new ThreadImgPodcasts().execute(podcast.getImgLink());
//        try {
//            URL url = new URL(podcast.getImgLink());
//            HttpURLConnection httpConn = null;
//                httpConn = (HttpURLConnection) url.openConnection();
//
//
//            httpConn.connect();
//            int resCode = httpConn.getResponseCode();
//
//            if (resCode == HttpURLConnection.HTTP_OK) {
//                InputStream in = httpConn.getInputStream();
//                Bitmap bitmap = BitmapFactory.decodeStream(in);
//
//    //            this.flagView.setImageBitmap(bitmap);
//
//                holder.imgView.setImageBitmap(bitmap);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return convertView;
    }

    // Find Image ID corresponding to the name of the image (in the directory mipmap).
    public int getMipmapResIdByName(String resName)  {
        String pkgName = context.getPackageName();
        // Return 0 if not found.
        int resID = context.getResources().getIdentifier(resName , "mipmap", pkgName);
        Log.i("CustomListView", "Res Name: "+ resName+"==> Res ID = "+ resID);
        return resID;
    }

    static class ViewHolder {
        ImageView imgView;
        TextView podcastNameView;
        TextView linkView;
    }
//    @SuppressLint("StaticFieldLeak")
    private class ThreadImgPodcasts extends AsyncTask<String, Void, Bitmap> {
        private String StringRequest;

        @Override
        protected Bitmap doInBackground(String... podcasts) {
//            Log.i(TAG + "AsyncTask", podcasts);
            String podcastURLImg = podcasts[0];

            Log.i(TAG + "AsyncTask", podcastURLImg);
            Bitmap bitmap =null;
            try {
                URL url = new URL(podcastURLImg);
                HttpURLConnection httpConn = null;
                httpConn = (HttpURLConnection) url.openConnection();


                httpConn.connect();
                int resCode = httpConn.getResponseCode();

                if (resCode == HttpURLConnection.HTTP_OK) {
                    InputStream in = httpConn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(in);

                    //            this.flagView.setImageBitmap(bitmap);

//                    holder.imgView.setImageBitmap(bitmap);

                    return bitmap;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
//            Log.i(TAG,"onPostExecute is working");
            holder.imgView.setImageBitmap(bitmap);
        }

    }
}
