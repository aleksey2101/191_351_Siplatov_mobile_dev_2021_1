package com.example.programm.myapplication_2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.VideoView;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;

import static android.content.Context.AUDIO_SERVICE;
import static com.example.programm.myapplication_2.MainActivity.TAG;

public class Lab2Fragment extends Fragment {

    public static Lab2Fragment newInstance() {
        return new Lab2Fragment();
    }
    View rootView;
    VideoView videoView;
//    SurfaceView cameraView;
    SeekBar seekBar;
    Boolean isPlaying = false;
    Boolean isStartedOnce = false;
    int stopPosition;
    int volumeLevel;
    SeekBar volumeBar;
    AudioManager audioManager;
    Button stopButton;
    Button playButton;

    SurfaceView surfaceView;
    Camera camera;
    MediaRecorder mediaRecorder;

    Button btnTakePicture;
    Button btnStartRecord;
    Button btnStopRecord;

    File photoFile;
    File videoFile;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //опробуем отображать вертикальную ориентацию
//        Objects.requireNonNull(getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        rootView = inflater.inflate(R.layout.lab2_fragment, container, false);

        volumeBar = (SeekBar) rootView.findViewById(R.id.volumeBar);
        audioManager = (AudioManager) Objects.requireNonNull(getActivity()).getSystemService(AUDIO_SERVICE);
        assert audioManager != null;
        volumeBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volumeLevel = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        videoView = rootView.findViewById(R.id.videoView);
        seekBar = rootView.findViewById(R.id.videoSeekBar);
//        Uri myVideoUri= Uri.parse( "http://techslides.com/demos/samples/sample.mp4");
//        videoView.setVideoURI(myVideoUri);
        String PkgName = BuildConfig.APPLICATION_ID;
        String videoPath = "android.resource://" + PkgName + "/" + R.raw.testvideo;

        Uri myVideoUri = Uri.parse( videoPath);

        Log.i(TAG, "myVideoUri: "+myVideoUri);
        videoView.setVideoURI(myVideoUri);
        MediaController mediaController = new MediaController(getActivity());
        videoView.setMediaController(mediaController);
        mediaController.show();

//        videoView.start(); // начинаем воспроизведение автоматически

        stopPosition = videoView.getCurrentPosition();
        playButton = rootView.findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        stopButton = rootView.findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.stopPlayback();
                videoView.resume();
                stopPosition = videoView.getCurrentPosition();
                isPlaying = false;
                Log.i(TAG, "stop, stopPosition: "+stopPosition);
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
            }
        });

        RadioButton videoRadioBtn = rootView.findViewById(R.id.videoRadioBtn);
//        videoRadioBtn.setOnClickListener(radioButtonClickListener);

        RadioButton cameraRadioBtn = rootView.findViewById(R.id.cameraRadioBtn);
//        cameraRadioBtn.setOnClickListener(radioButtonClickListener);


        surfaceView = rootView.findViewById(R.id.cameraView);

        btnTakePicture = rootView.findViewById(R.id.btnTakePicture);
        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, new PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        try {
                            FileOutputStream fos = new FileOutputStream(photoFile);
                            fos.write(data);
                            fos.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        btnStartRecord = rootView.findViewById(R.id.btnStartRecord);
        btnStartRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prepareVideoRecorder()) {
                    mediaRecorder.start();
                } else {
                    releaseMediaRecorder();
                }
            }
        });

        btnStopRecord = rootView.findViewById(R.id.btnStopRecord);
        btnStopRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaRecorder != null) {
                    mediaRecorder.stop();
                    releaseMediaRecorder();
                }
            }
        });

        File pictures = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        photoFile = new File(pictures, "myphoto.jpg");
        videoFile = new File(pictures, "myvideo.3gp");

        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    camera.setPreviewDisplay(holder);
                    camera.startPreview();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }
        });

        return rootView;
    }

    private Runnable onEverySecond = new Runnable() {

        @Override
        public void run() {

            if(seekBar != null) {

//                Log.i(TAG, "run onEverySec, videoView.getCurrentPosition(): " + videoView.getCurrentPosition());
                seekBar.setProgress(videoView.getCurrentPosition());
            }

//            if(videoView.isPlaying()) {
            assert seekBar != null;
            seekBar.postDelayed(onEverySecond, 100);
//            }

            //здесь же будем обновлять громкость на seekBar
            volumeLevel = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            volumeBar.setProgress(volumeLevel);
        }
    };

public void onActivityCreated (Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    Button videoBtn = (Button) Objects.requireNonNull(getActivity()).findViewById(R.id.videoRadioBtn);
    videoBtn.setOnClickListener(next_Listener);

    Button cameraBtn = (Button) getActivity().findViewById(R.id.cameraRadioBtn);
    cameraBtn.setOnClickListener(next_Listener);

}

    private View.OnClickListener next_Listener = new View.OnClickListener() {
        public void onClick(View v) {

            //xml find out which radio button has been checked ...
//            RadioGroup radio_grp=(RadioGroup) Objects.requireNonNull(getActivity()).findViewById(R.id.radioGroup); //change or leave out this line (I've put it in because you might find it useful later )
            RadioButton videoBtn=(RadioButton) Objects.requireNonNull(getActivity()).findViewById(R.id.videoRadioBtn);  //you dont need to do this again if global ...
            RadioButton cameraBtn=(RadioButton)getActivity().findViewById(R.id.cameraRadioBtn);
            if(videoBtn.isChecked()) {
                Log.i(TAG,"video btn is checked!");
                videoView.setVisibility(View.VISIBLE);
                seekBar.setVisibility(View.VISIBLE);
                playButton.setVisibility(View.VISIBLE);
                stopButton.setVisibility(View.VISIBLE);

                surfaceView.setVisibility(View.INVISIBLE);
                btnTakePicture.setVisibility(View.INVISIBLE);
                btnStartRecord.setVisibility(View.INVISIBLE);
                btnStopRecord.setVisibility(View.INVISIBLE);

            }else if (cameraBtn.isChecked()){
                Log.i(TAG,"camera btn is checked!");
                videoView.stopPlayback();
                videoView.setVisibility(View.INVISIBLE);
                seekBar.setVisibility(View.INVISIBLE);
                playButton.setVisibility(View.INVISIBLE);
                stopButton.setVisibility(View.INVISIBLE);

                surfaceView.setVisibility(View.VISIBLE);
                btnTakePicture.setVisibility(View.VISIBLE);
                btnStartRecord.setVisibility(View.VISIBLE);
                btnStopRecord.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        camera = Camera.open();
        Log.d("Fragment2", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseMediaRecorder();
        if (camera != null)
            camera.release();
        camera = null;
    }

    public void onClickPicture(View view) {
        camera.takePicture(null, null, new PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                try {
                    FileOutputStream fos = new FileOutputStream(photoFile);
                    fos.write(data);
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void onClickStartRecord(View view) {
        if (prepareVideoRecorder()) {
            mediaRecorder.start();
        } else {
            releaseMediaRecorder();
        }
    }

    public void onClickStopRecord(View view) {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            releaseMediaRecorder();
        }
    }

    private boolean prepareVideoRecorder() {
    try{
        camera.unlock();

        mediaRecorder = new MediaRecorder();

        mediaRecorder.setCamera(camera);
//        todo ошибка
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
//        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
////        используем для упрощения формирования настроек
////        mediaRecorder.setProfile(CamcorderProfile
////                .get(CamcorderProfile.QUALITY_HIGH));
//
//        mediaRecorder.setProfile(CamcorderProfile
//                .get(CamcorderProfile.QUALITY_480P));

        CamcorderProfile profile =CamcorderProfile.get(CamcorderProfile.QUALITY_QVGA);

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
//        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//        mediaRecorder.setVideoSize(640,480); /// video size make dynamic based on the requirement or view size
        mediaRecorder.setVideoFrameRate(profile.videoFrameRate); /// from profile
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        mediaRecorder.setOutputFile(videoFile.getAbsolutePath());
        mediaRecorder.setPreviewDisplay(surfaceView.getHolder().getSurface());



    } catch (IllegalStateException e)

    {
        e.printStackTrace();
        Log.e(TAG, e.getMessage());
        return false;
    }
        try {
            mediaRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    private void releaseMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
            camera.lock();
        }
    }

    private void cameraFunc(){
        File pictures = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        photoFile = new File(pictures, "myphoto.jpg");
        videoFile = new File(pictures, "myvideo.3gp");

//        surfaceView = (SurfaceView) rootView.findViewById(R.id.cameraView);

        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    camera.setPreviewDisplay(holder);
                    camera.startPreview();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }
        });
    }


}