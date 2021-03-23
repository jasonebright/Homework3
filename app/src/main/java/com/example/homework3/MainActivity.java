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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView music;
    Button play;
    Spinner mainSong, soundEffect1, soundEffect2, soundEffect3;
    SeekBar seekBar1, seekBar2, seekBar3;
    ArrayList<String> mainSongs, soundEffects;
    ArrayAdapter<String> main, s1, s2, s3;
    int indexMain, indexS1, indexS2, indexS3;
    int count =0;


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
        setContentView(R.layout.activity_main);

        mainSong = (Spinner) findViewById(R.id.spinnerMain);
        mainSongs = new ArrayList<String>();
        mainSongs.add("Go Tech Go!");
        mainSongs.add("Tech Triumph");
        mainSongs.add("VPI Victory March");
        main = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mainSongs);
        main.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mainSong.setAdapter(main);

        soundEffect1 = (Spinner) findViewById(R.id.spinnerEffect1);
        soundEffect2 = (Spinner) findViewById(R.id.spinnerEffect2);
        soundEffect3 = (Spinner) findViewById(R.id.spinnerEffect3);
        soundEffects = new ArrayList<String>();
        soundEffects.add("Cheering");
        soundEffects.add("Clapping");
        soundEffects.add("Go Hokies!");
        s1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, soundEffects);
        s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        soundEffect1.setAdapter(s1);
        s2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, soundEffects);
        s2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        soundEffect2.setAdapter(s2);
        s3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, soundEffects);
        s3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        soundEffect3.setAdapter(s3);


        play= (Button) findViewById(R.id.play);
        play.setOnClickListener(this);

        seekBar1 = (SeekBar) findViewById(R.id.seekBar);
        seekBar1.setMax(100);
        seekBar2 = (SeekBar) findViewById(R.id.seekBar2);
        seekBar2.setMax(100);
        seekBar3 = (SeekBar) findViewById(R.id.seekBar3);
        seekBar3.setMax(100);

    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(this, PlayActivity.class);

        i.putExtra("main", mainSong.getSelectedItem().toString());
        i.putExtra("effect1", soundEffect1.getSelectedItem().toString());
        i.putExtra("effect2", soundEffect2.getSelectedItem().toString());
        i.putExtra("effect3", soundEffect3.getSelectedItem().toString());
        i.putExtra("count", count);
        i.putExtra("progress1", seekBar1.getProgress());
        i.putExtra("progress2", seekBar2.getProgress());
        i.putExtra("progress3", seekBar3.getProgress());
        count++;
        startActivity(i);

    }




    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(INITIALIZE_STATUS, isInitialized);
        super.onSaveInstanceState(outState);
    }


}
