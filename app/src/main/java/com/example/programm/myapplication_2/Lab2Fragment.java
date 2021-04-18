package com.example.programm.myapplication_2;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.VideoView;

import java.util.Objects;

import static android.content.Context.AUDIO_SERVICE;
import static com.example.programm.myapplication_2.MainActivity.TAG;

public class Lab2Fragment extends Fragment {

    public static Lab2Fragment newInstance() {
        return new Lab2Fragment();
    }
    View rootView;
    VideoView videoView;
    SeekBar seekBar;
    Boolean isPlaying = false;
    Boolean isStartedOnce = false;
    int stopPosition;
    int volumeLevel;
    SeekBar volumeBar;
    AudioManager audioManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.lab2_fragment, container, false);


        volumeBar = (SeekBar) rootView.findViewById(R.id.volumeBar);
        audioManager = (AudioManager) Objects.requireNonNull(getActivity()).getSystemService(AUDIO_SERVICE);
        assert audioManager != null;
        volumeBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volumeLevel = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        videoView = rootView.findViewById(R.id.videoView);
        seekBar = rootView.findViewById(R.id.videoSeekBar);
//      установите свой путь к файлу на SD-карточке
//      String videoSource ="/sdcard/Movies/cat.3gp";
//        Uri myVideoUri= Uri.parse( "http://techslides.com/demos/samples/sample.mp4");
//        videoView.setVideoURI(myVideoUri);
        String PkgName = BuildConfig.APPLICATION_ID;
        String videoPath = "android.resource://" + PkgName + "/" + R.raw.testvideo;

        Uri myVideoUri = Uri.parse( videoPath);

        Log.i(TAG, "myVideoUri: "+myVideoUri);
        videoView.setVideoURI(myVideoUri);
//        videoView.setVideoPath("testVideo.mp4");
        MediaController mediaController = new MediaController(getActivity());
        videoView.setMediaController(mediaController);
        mediaController.show();

//        videoView.start(); // начинаем воспроизведение автоматически

        stopPosition = videoView.getCurrentPosition();
        Button playButton = rootView.findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                videoView.start();
                if (!isPlaying) {
                    videoView.seekTo(stopPosition);
                    Log.i(TAG, "start, stopPosition: "+stopPosition);
                    videoView.start();
                    isPlaying = true;
                }else {
                    videoView.pause();
                    stopPosition = videoView.getCurrentPosition();
                    Log.i(TAG, "pause, stopPosition: "+stopPosition);
                    isPlaying = false;
                }
            }
        });

        Button stopButton = rootView.findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.stopPlayback();
                videoView.resume();
            }
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {

                seekBar.setMax(videoView.getDuration());
                seekBar.postDelayed(onEverySecond, 100); //was 1000
//                добавляем зацикливание
                mp.setLooping(true);
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

                if(fromUser) {
                    // this is when actually seekbar has been seeked to a new position
                    videoView.seekTo(progress);
                    stopPosition = videoView.getCurrentPosition();
                }
            }
        });

        volumeBar.setProgress(volumeLevel);
        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                volumeLevel = i;
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volumeLevel, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                volumeBar.setVisibility(View.GONE);
            }
        });


        return rootView;
    }

    private Runnable onEverySecond = new Runnable() {

        @Override
        public void run() {

            if(seekBar != null) {

                Log.i(TAG, "run onEverySec, videoView.getCurrentPosition(): " + videoView.getCurrentPosition());
                seekBar.setProgress(videoView.getCurrentPosition());
            }

//            if(videoView.isPlaying()) {
            assert seekBar != null;
            seekBar.postDelayed(onEverySecond, 100);
//            }

        }
    };



}