package com.smartneck.fit.tmp.Alarm;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

import static com.smartneck.fit.tmp.Alarm.AlarmActivity.alarms;
import static com.smartneck.fit.util.Constants.TAG;


public class AlarmReceiver extends BroadcastReceiver
{


    Context context;
    int index;
    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onReceive(Context context, Intent intent)
    {
        this.context = context;

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);
        //오늘 요일
        int toDay = calendar.get(Calendar.DAY_OF_WEEK);
        //알람 인덱스
        index = intent.getIntExtra("index" , -1);

        if (toDay == 1 && alarms.get(index).isSun() && alarms.get(index).isUse()){
            serviceStart();
        }else if (toDay == 2 && alarms.get(index).isMon() && alarms.get(index).isUse()){
            serviceStart();
        }else if (toDay == 3 && alarms.get(index).isTue() && alarms.get(index).isUse()){
            serviceStart();
        }else if (toDay == 4 && alarms.get(index).isWed() && alarms.get(index).isUse()){
            serviceStart();
        }else if (toDay == 5 && alarms.get(index).isThu() && alarms.get(index).isUse()){
            serviceStart();
        }else if (toDay == 6 && alarms.get(index).isFri() && alarms.get(index).isUse()){
            serviceStart();
        }else if (toDay == 7 && alarms.get(index).isSat() && alarms.get(index).isUse()){
            serviceStart();
        }
        //oneDay
        else if (alarms.get(index).getType() == 0){
            alarms.get(index).setUse(false);
            Log.e(TAG, "onReceive: " + index + " / "+ alarms.get(index).isUse() );
            AlarmStorage alarmStorage = new AlarmStorage(context);
            alarmStorage.saveData();
            serviceStart();
        }
        //date
        else if (alarms.get(index).getType() == 2 &&
                year == alarms.get(index).getYear() &&
                month == alarms.get(index).getMonth() &&
                date == alarms.get(index).getDate()){
            alarms.get(index).setUse(false);
            serviceStart();

        }


    }

    private void serviceStart(){
        Intent mService = new Intent(context , AlarmService.class);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            context.startForegroundService(mService);
        }else{
            context.startService(mService);
        }
    }
}