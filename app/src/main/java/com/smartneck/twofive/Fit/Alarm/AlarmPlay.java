package com.smartneck.twofive.Fit.Alarm;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.Log;

import static com.smartneck.twofive.Fit.util.Fit_Constants.TAG;

public class AlarmPlay {
    static MediaPlayer mediaPlayer;
    Vibrator vibrator;
    int timer = 300;
    public AlarmPlay(Context context) {
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (mediaPlayer == null){
//            mediaPlayer = MediaPlayer.create(context, R.raw.alarm);
            mediaPlayer.setLooping(true);//미디어파일 무한반복
        }

    }


    public void play(){
        //미디어파일 재생
        mediaPlayer.start();
        //진동
        vibrator.vibrate(
                new long[]{100, 1000, 100, 500, 100, 500, 100, 1000}
                , 0);
        //알람 timer 세팅만큼 지속
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(timer * 1000);
                    stop();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void stop(){
        Log.i(TAG, "========== Alarm stop ==========");
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        vibrator.cancel();
    }
}
