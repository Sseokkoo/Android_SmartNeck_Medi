package com.smartneck.fit.tmp.Alarm;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.smartneck.fit.R;



public class AlarmService extends Service {
    PowerManager.WakeLock wakeLock;
    String TAG = "alarmService";


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public AlarmService() {

    }

    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onCreate() {
        super.onCreate();
        AlarmActivity.alarm = true;//alarm이 false가 되면 알람종료(mainActivity에서 컨트롤)
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PowerManager pm = (PowerManager)getApplicationContext().getSystemService(Context.POWER_SERVICE);
        boolean screen = pm.isInteractive();
        Log.e(TAG, "onStartCommand: isLocked" +  screen);

        if (!screen) {
        wakeUpDevice();
        Intent i = new Intent(this, AlarmLockedActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        }else{
            show();
        }

        return START_NOT_STICKY;
    }

    @SuppressLint("InvalidWakeLockTag")
    private void wakeUpDevice() {


        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK|
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.ON_AFTER_RELEASE, "hi");

        wakeLock.acquire();
        Log.e("service", "wakeUpDevice: ");
        if (wakeLock != null) {
            wakeLock.release();
            wakeLock = null;
        }

    }

    private void show() {

        //알림 클릭시 이동할 액티비티
        Intent intent = new Intent(this, AlarmActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        //메인 액티비티로 이동시 진동 끄기
        intent.putExtra("vibe", false);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //헤드업 알림 커스텀 뷰
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.activity_headup);

        //Calendar.getInstance() api 24부터 호출가능
        //알림에 시간 세팅
        AlarmCurrentTime currentTime = new AlarmCurrentTime();
        contentView.setTextViewText(R.id.alarm_headup_time, currentTime.getToday());


        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, "default")
                        .setFullScreenIntent(pendingIntent, true)//알림 클릭 전까지 알림이 사라지지 않게
                        .setSmallIcon(R.mipmap.ic_launcher)//앱 아이콘
                        .setTicker("알람")//알림이 오면 상단에 출력되는 메시지
                        .setContentTitle("알람")//알림제목
                        .setContentText("Content")//알림 내용
                        .setPriority(Notification.PRIORITY_MAX)//MAX or HIGH로 세팅하지 않으면 헤드업 알림이 동작하지않음
                        .setCustomHeadsUpContentView(contentView)//헤드업 알림 커스텀뷰
                        .setContentIntent(pendingIntent);//인텐트 세팅

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("default", "기본 채널",
                    NotificationManager.IMPORTANCE_DEFAULT));
        }else{
            manager.notify(1, builder.build());
        }


        //미디어 재생, 진동 시작
        AlarmPlay alarmPlay = new AlarmPlay(getApplicationContext());
        alarmPlay.play();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
