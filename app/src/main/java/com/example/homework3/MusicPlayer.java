package com.example.homework3;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

public class MusicPlayer implements MediaPlayer.OnCompletionListener {

    MyAsyncTask asyncTask;
    MediaPlayer player, s1, s2, s3;
    int count=0;
    int currentPosition, s1Pos, s2Pos, s3Pos, timer = 0;
    int p1,p2,p3;
    int musicIndex, indS1, indS2, indS3 = 0;
    int copy1, copy2, copy3;
    private int musicStatus = 0;//0: before playing, 1 playing, 2 paused
    private MusicService musicService;
    boolean paused;


    static final int[] MUSICPATH = new int[]{
            R.raw.gotechgo,
            R.raw.techtriumph,
            R.raw.vpivictorymarch
    };
    static final int[] MUSICEFFECT = new int[]{
            R.raw.cheering,
            R.raw.clapping,
            R.raw.lestgohokies
    };

    static final String[] MUSICNAME = new String[]{
            "Go Tech Go!",
            "Tech Triumph",
            "VPI Victory March"
    };
    static final String[] EFFECT = new String[]{
            "Cheering",
            "Clapping",
            "Go Hokies!"
    };

    public MusicPlayer(MusicService service) {

        this.musicService = service;

    }


    public int getMusicStatus() {

        return musicStatus;
    }

    public String getMusicName() {

        return MUSICNAME[musicIndex];
    }

    public int getIndex(String str){
        for( int i = 0; i<MUSICNAME.length; i++){
            if(str.compareTo( MUSICNAME[i]) == 0){
                //musicIndex=i;
                return i;
            }

        }
        return -1;
    }

    public void playMusic(int index, int indexS1, int indexS2, int indexS3, int prog1, int prog2, int prog3) {
        musicIndex=index;
        paused = false;
        player= MediaPlayer.create(this.musicService, MUSICPATH[musicIndex]);

        player.start();
        player.setOnCompletionListener(this);
        musicService.onUpdateMusicName(getMusicName());
        musicStatus = 1;
        copy1= prog1;
        copy2 = prog2;
        copy3 = prog3;
        p1 = prog1*player.getDuration()/100000;
        p2 = prog2*player.getDuration()/100000;
        p3 = prog3*player.getDuration()/100000;
        indS1 = indexS1;
        indS2 = indexS2;
        indS3 = indexS3;

        asyncTask = new MyAsyncTask();
        asyncTask.execute();






    }


    public void pauseMusic() {
        paused = true;
        if(player!= null && player.isPlaying()){
            player.pause();
            currentPosition= player.getCurrentPosition();
            musicStatus= 2;
        }
        if(s1!= null && s1.isPlaying()){
            s1.pause();
            s1Pos= s1.getCurrentPosition();
            musicStatus= 2;
        }
        if(s2!= null && s2.isPlaying()){
            s2.pause();
            s2Pos= s2.getCurrentPosition();
            musicStatus= 2;
        }
        if(s3!= null && s3.isPlaying()){
            s3.pause();
            s3Pos= s3.getCurrentPosition();
            musicStatus= 2;
        }
    }

    public void resumeMusic(int index, int c, int indexS1, int indexS2, int indexS3, int prog1, int prog2, int prog3) {
        if(player!= null){
            if(c == count){
                Log.i("Continue Playing", count+"");
                paused = false;

                player.seekTo(currentPosition);
                player.start();
                if(s1 != null){
                    s1.seekTo(s1Pos);
                    s1.start();
                }
                if(s2 != null){
                    s2.seekTo(s2Pos);
                    s2.start();
                }
                if(s3 != null){
                    s3.seekTo(s3Pos);
                    s3.start();
                }
                asyncTask = new MyAsyncTask();


                asyncTask.execute();
                musicStatus=1;
            }
            else {
                Log.i("NewSong", c+"");
                player.release();
                player = null;

                if(s1 != null){
                    s1.release();
                    s1 = null;
                }
                if(s2 != null){
                    s2.release();
                    s2 = null;
                }
                if(s3 != null){
                    s3.release();
                    s3 = null;
                }
                count = c;
                musicIndex = index;
                playMusic(index, indexS1, indexS2, indexS3, prog1, prog2, prog3);

            }


        }
    }

    public void restartMusic() {
        paused = true;
        timer = 0;
        if(player!= null && player.isPlaying()) {
            player.stop();
            currentPosition = 0;
            musicStatus =2 ;
            asyncTask.cancel(true);


            if (s1 != null && s1.isPlaying()) {
                s1.stop();
                s1Pos = 0;
                s1.release();
                s1 = null;
            } else {
                s1Pos = 0;
            }
            if (s2 != null && s2.isPlaying()) {
                s2.stop();
                s2Pos = 0;
                s2.release();
                s2 = null;
            } else {
                s2Pos = 0;
            }
            if (s3 != null && s3.isPlaying()) {
                s3.stop();
                s3Pos = 0;
                s3.release();
                s3 = null;
            } else {
                s3Pos = 0;
            }
            musicService.onUpdateMusicName(MUSICNAME[musicIndex]);
            //paused = false;
            playMusic(musicIndex, indS1, indS2, indS3, copy1, copy2, copy3);
        }
        else if (player!= null){
            currentPosition = 0;
            musicStatus =2 ;


            if (s1 != null && s1.isPlaying()) {
                s1.stop();
                s1Pos = 0;
                s1.release();
                s1 = null;
            } else {
                s1Pos = 0;
            }
            if (s2 != null && s2.isPlaying()) {
                s2.stop();
                s2Pos = 0;
                s2.release();
                s2 = null;
            } else {
                s2Pos = 0;
            }
            if (s3 != null && s3.isPlaying()) {
                s3.stop();
                s3Pos = 0;
                s3.release();
                s3 = null;
            } else {
                s3Pos = 0;
            }
            musicService.onUpdateMusicName(MUSICNAME[musicIndex]);
            //paused = false;
            playMusic(musicIndex, indS1, indS2, indS3, copy1, copy2, copy3);

        }

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
       /*if(mediaPlayer.equals(s1)){
           //hand1.removeCallbacks(run1);
           s1.release();
           s1 = null;
       }
        if(mediaPlayer == s2){
            hand2.removeCallbacks(run2);
            s2.release();
            s2 = null;
        }

        if(mediaPlayer == s3){
            hand3.removeCallbacks(run3);
            s3.release();
            s3 = null;
        }*/

    }

    public void setMusicStatus(int i) {
        musicStatus = 0;
    }

    private class MyAsyncTask extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected Void doInBackground(Integer... integers) {
            while(!paused){
                if (isCancelled())
                    break;
                try{
                    if (timer == p1) {
                        s1 = MediaPlayer.create(musicService, MUSICEFFECT[indS1]);
                        s1.start();
                       musicService.onUpdateMusicName(EFFECT[indS1]);
                    }
                    if (timer == p2) {
                        s2 = MediaPlayer.create(musicService, MUSICEFFECT[indS2]);
                        s2.start();
                        musicService.onUpdateMusicName(EFFECT[indS2]);
                    }
                    if (timer == p3) {
                        s3 = MediaPlayer.create(musicService, MUSICEFFECT[indS3]);
                        s3.start();
                        musicService.onUpdateMusicName(EFFECT[indS3]);
                    }

                    if(timer >= p1+7 && (timer -p2 >= 7 || timer-p2 <0)  && (timer -p3 >= 7 || timer-p3 <0)){
                        musicService.onUpdateMusicName(MUSICNAME[musicIndex]);
                    }
                    if(timer >= p2+7 && (timer -p1 >= 7 || timer-p1 <0)  && (timer -p3 >= 7 || timer-p3 <0)){
                        musicService.onUpdateMusicName(MUSICNAME[musicIndex]);
                    }
                    if(timer >= p3+7 && (timer -p2 >= 7 || timer-p2 <0)  && (timer -p1 >= 7 || timer-p1 <0)){
                        musicService.onUpdateMusicName(MUSICNAME[musicIndex]);
                    }

                    Thread.sleep(1000);
                    timer++;
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values){
            super.onProgressUpdate(values);

        }
    }
}
