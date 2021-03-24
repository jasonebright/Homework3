package com.example.homework3;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MusicService extends Service {

    MusicPlayer musicPlayer;
    private final IBinder iBinder= new MyBinder();

    public static final String COMPLETE_INTENT = "complete intent";
    public static final String MUSICNAME = "music name";

    @Override
    public void onCreate() {
        super.onCreate();
        musicPlayer = new MusicPlayer(this);
    }

    public void startMusic(int index, int indexS1, int indexS2, int indexS3, int prog1, int prog2, int prog3){

        musicPlayer.playMusic(index, indexS1, indexS2, indexS3, prog1, prog2, prog3);
    }

    public void pauseMusic(){

        musicPlayer.pauseMusic();
    }

    public void resumeMusic(int index, int c, int indexS1, int indexS2, int indexS3, int prog1, int prog2, int prog3){

        musicPlayer.resumeMusic(index, c, indexS1, indexS2, indexS3, prog1, prog2, prog3);
    }
    public void restartMusic(){

        musicPlayer.restartMusic();
    }

    public int getPlayingStatus(){

        return musicPlayer.getMusicStatus();
    }

    public void setPlayingStatus(int i){
        musicPlayer.setMusicStatus(i);
    }

    public int getMusicIndex(String str){
        return musicPlayer.getIndex(str);
    }


    public void onUpdateMusicName(String musicname) {
        Intent intent = new Intent(COMPLETE_INTENT);
        intent.putExtra(MUSICNAME, musicname);
        sendBroadcast(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return iBinder;
    }


    public class MyBinder extends Binder {

        MusicService getService(){
            return MusicService.this;
        }
    }
}
