package com.example.homework3;

import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;

public class MusicPlayer implements MediaPlayer.OnCompletionListener {

    MediaPlayer player, s1, s2, s3;
    int count=-1;
    int currentPosition, s1Pos, s2Pos, s3Pos, timer = 0;
    int p1, p2, p3;
    int musicIndex = 0;
    private int musicStatus = 0;//0: before playing, 1 playing, 2 paused
    private MusicService musicService;
    Handler hand1, hand2, hand3, handTime;
    Runnable run1, run2, run3, runTime;

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
        player= MediaPlayer.create(this.musicService, MUSICPATH[musicIndex]);
        s1 = MediaPlayer.create(this.musicService, MUSICEFFECT[indexS1]);
        s2 = MediaPlayer.create(this.musicService, MUSICEFFECT[indexS2]);
        s3 = MediaPlayer.create(this.musicService, MUSICEFFECT[indexS3]);
        player.start();
        player.setOnCompletionListener(this);
        s1.setOnCompletionListener(this);
        Log.i("duration", player.getDuration()+"");
        Log.i("prog1", prog1+"");
        Log.i("prog2", prog2+"");
        Log.i("prog3", prog3+"");
        prog1 = playEffect(prog1, player.getDuration());
        prog2 = playEffect(prog2, player.getDuration());
        prog3 = playEffect(prog3, player.getDuration());
        //Log.i("duration", player.getDuration()+"");
        Log.i("prog1", prog1+"");
        Log.i("prog2", prog2+"");
        Log.i("prog3", prog3+"");
        p1 = prog1;
        p2 = prog2;
        p3 = prog3;

        /*hand1 = new Handler();
        run1 = new Runnable(){
            @Override
            public void run(){
                s1.start();
                hand1.postDelayed(run1, p1);
            }
        };
        run1.run();

        hand2 = new Handler();
        run2 = new Runnable(){
            @Override
            public void run(){
                s2.start();
                hand2.postDelayed(run2, p2);
            }
        };
        run2.run();

        hand3 = new Handler();
        run3 = new Runnable(){
            @Override
            public void run(){
                s3.start();
                hand3.postDelayed(run3, p3);
            }
        };
        run3.run();

        handTime = new Handler();
        runTime = new Runnable(){
            @Override
            public void run(){
                timer++;
                handTime.postDelayed(runTime, 1000);
            }
        };
        runTime.run();*/


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                s1.start();

            }
        }, prog1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                s2.start();
            }
        }, prog2);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                s3.start();
            }
        }, prog3);
        //s1 = MediaPlayer.create(this.musicService, MUSICEFFECT[0]);
        //s1.start();

        //player.setOnCompletionListener(this);
        //s1.setOnCompletionListener(this);
        //s2.setOnCompletionListener(this);
        //s3.setOnCompletionListener(this);
        musicService.onUpdateMusicName(getMusicName());
        musicStatus = 1;
    }

    public int playEffect(int progress, int duration){
        return (progress * duration)/100;

    }

    public void pauseMusic() {
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
                player.seekTo(currentPosition);
                player.start();
            }
            else{
                count = c;
                musicIndex=index;
                player= MediaPlayer.create(this.musicService, MUSICPATH[musicIndex]);
                s1 = MediaPlayer.create(this.musicService, MUSICEFFECT[indexS1]);
                s2 = MediaPlayer.create(this.musicService, MUSICEFFECT[indexS2]);
                s3 = MediaPlayer.create(this.musicService, MUSICEFFECT[indexS3]);
                player.start();
                prog1 = playEffect(prog1, player.getDuration());
                prog2 = playEffect(prog2, player.getDuration());
                prog3 = playEffect(prog3, player.getDuration());
                p1 = prog1;
                p2 = prog2;
                p3 = prog3;
                new Handler().postDelayed(new Runnable() {
                    //private MusicService musicService;
                    @Override
                    public void run() {
                        s1.start();

                    }
                }, prog1);

                new Handler().postDelayed(new Runnable() {
                    //private MusicService musicService;
                    @Override
                    public void run() {
                        // >> here's choose your song file
                        s2.start();
                    }
                }, prog2);
                new Handler().postDelayed(new Runnable() {
                    //private MusicService musicService;
                    @Override
                    public void run() {
                        // >> here's choose your song file
                        s3.start();
                    }
                }, prog3);
                /*hand1 = new Handler();
                run1 = new Runnable(){
                    @Override
                    public void run(){
                        s1.start();
                        hand1.postDelayed(run1, p1);
                    }
                };
                run1.run();

                hand2 = new Handler();
                run2 = new Runnable(){
                    @Override
                    public void run(){
                        s2.start();
                        hand2.postDelayed(run2, p2);
                    }
                };
                run2.run();

                hand3 = new Handler();
                run3 = new Runnable(){
                    @Override
                    public void run(){
                        s3.start();
                        hand3.postDelayed(run3, p3);
                    }
                };
                run3.run();*/
                //player.setOnCompletionListener(this);

                musicService.onUpdateMusicName(getMusicName());

            }
            musicStatus=1;

        }
    }

    public void restartMusic() {
        if(player!= null && player.isPlaying()){
            player.stop();
            currentPosition= 0;
            musicStatus= 0;
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
}