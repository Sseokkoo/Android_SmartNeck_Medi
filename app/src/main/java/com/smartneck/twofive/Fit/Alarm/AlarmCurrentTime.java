package com.smartneck.twofive.Fit.Alarm;

import java.util.Calendar;

// 서비스에서 Calendar.getInstance() api 24부터 호출가능

public class AlarmCurrentTime {
    public AlarmCurrentTime() {
    }

    public String getToday() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        StringBuilder sb = new StringBuilder();

        if (hour > 12){
            sb.append("오후 ");
            hour -=12;
        }else{
            sb.append("오전 ");
        }

        if (hour < 10){
            sb.append("0");
        }
        sb.append(hour);
        sb.append(":");
        if (minute < 10){
            sb.append("0");
        }
        sb.append(minute);



        return sb.toString();
    }
}
