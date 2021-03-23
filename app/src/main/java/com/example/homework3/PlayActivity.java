package com.example.homework3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;


public class PlayActivity extends AppCompatActivity implements View.OnClickListener {
    TextView music;
    Button play, restart;
    String main, s1,s2,s3;
    int indexMain, indexS1,indexS2,indexS3, prog1, prog2, prog3;
    int count = -1;


    MusicService musicService;
    MusicCompletionReceiver musicCompletionReceiver;
    Intent startMusicServiceIntent;
    boolean isBound = false;
    boolean isInitialized = false;

    public static final String INITIALIZE_STATUS = "intialization status";
    public static final String MUSIC_PLAYING = "music playing";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        music= (TextView) findViewById(R.id.songName);
        play= (Button) findViewById(R.id.buttonPlay);
        play.setOnClickListener(this);
        restart = (Button) findViewById(R.id.buttonRestart);
        restart.setOnClickListener(this);

        Bundle b1 = getIntent().getExtras();
        main = b1.getString("main");
        music.setText(main);
        //indexMain = musicService.getMusicIndex(main);
        int temp = count;
        count = b1.getInt("count");
        if(temp != count){
            play.setText("Play");
        }
        s1 = b1.getString("effect1");
        s2 = b1.getString("effect2");
        s3 = b1.getString("effect3");
        indexS1 = effectIndex(s1);
        indexS2 = effectIndex(s2);
        indexS3 = effectIndex(s3);

        prog1 = b1.getInt("progress1");
        prog2 = b1.getInt("progress2");
        prog3 = b1.getInt("progress3");



        if(savedInstanceState != null){
            isInitialized = savedInstanceState.getBoolean(INITIALIZE_STATUS);
            music.setText(savedInstanceState.getString(MUSIC_PLAYING));
        }

        startMusicServiceIntent= new Intent(this, MusicService.class);

        if(!isInitialized){
            startService(startMusicServiceIntent);
            isInitialized= true;
        }

        musicCompletionReceiver = new MusicCompletionReceiver(this);

    }

    @Override
    public void onClick(View view) {


        if (isBound) {
            switch (view.getId()) {
                case R.id.buttonPlay:
                    switch (musicService.getPlayingStatus()) {
                        case 0:
                            indexMain = musicService.getMusicIndex(main);
                            musicService.startMusic(indexMain, indexS1, indexS2, indexS3, prog1, prog2, prog3);
                            play.setText("Pause");
                            break;
                        case 1:
                            musicService.pauseMusic();
                            play.setText("Resume");
                            break;
                        case 2:
                            indexMain = musicService.getMusicIndex(main);
                            musicService.resumeMusic(indexMain, count, indexS1, indexS2, indexS3, prog1, prog2, prog3);
                            play.setText("Pause");
                            break;
                    }
                    break;
                case R.id.buttonRestart:
                    musicService.restartMusic();
                    play.setText("Play");
                    break;
            }

        }
    }


    public void updateName(String musicName) {

        music.setText(musicName);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(isInitialized && !isBound){
            bindService(startMusicServiceIntent, musicServiceConnection, Context.BIND_AUTO_CREATE);
        }

        registerReceiver(musicCompletionReceiver, new IntentFilter(MusicService.COMPLETE_INTENT));
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(isBound){
            unbindService(musicServiceConnection);
            isBound= false;
        }

        unregisterReceiver(musicCompletionReceiver);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(INITIALIZE_STATUS, isInitialized);
        outState.putString(MUSIC_PLAYING, music.getText().toString());
        super.onSaveInstanceState(outState);
    }

    public int effectIndex(String str){
        if(str.compareTo("Cheering") == 0){
            return 0;
        }
        if(str.compareTo("Clapping") == 0){
            return 1;
        }
        if(str.compareTo("Go Hokies!") == 0){
            return 2;
        }
        return -1;

    }


    private ServiceConnection musicServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MyBinder binder = (MusicService.MyBinder) iBinder;
            musicService = binder.getService();
            isBound = true;

            switch (musicService.getPlayingStatus()) {
                case 0:
                    play.setText("Play");
                    break;
                case 1:
                    play.setText("Pause");
                    break;
                case 2:
                    play.setText("Resume");
                    break;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicService = null;
            isBound = false;
        }
    };
}