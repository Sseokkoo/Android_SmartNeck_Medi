package com.smartneck.fit.tmp.Alarm;

//DataModel
public class AlarmItem {
    //알람 타입 (0 = oneDay, 1 = weekDay, 2 = date);
    int type;
    //사용여부
    boolean use;
    //요일별 알람사용
    boolean mon, tue, wed, thu, fri, sat, sun;
    //시간
    int hour;
    //분
    int minute;
    //오전/오후
    String dayTime;
    //삭제구분
    boolean delete;
    //
    int month;
    int year;
    int date;


    public AlarmItem(int type, int hour, int minute) {
        this.use = true;
        this.type = type;
        this.hour = hour;
        this.minute = minute;
        dayTimeEdit(hour);
    }

    public AlarmItem(int type, boolean mon, boolean tue,
                     boolean wed, boolean thu, boolean fri,
                     boolean sat, boolean sun, int hour, int minute) {
        this.use = true;
        this.type = type;
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thu = thu;
        this.fri = fri;
        this.sat = sat;
        this.sun = sun;
        this.hour = hour;
        this.minute = minute;
        dayTimeEdit(hour);
    }

    public AlarmItem(int type, int year, int month, int date, int hour, int minute) {
        this.use = true;
        this.type = type;
        this.hour = hour;
        this.minute = minute;
        this.month = month;
        this.year = year;
        this.date = date;
        dayTimeEdit(hour);
    }


    void dayTimeEdit(int hour) {
        if (hour > 12) {
            this.dayTime = "오후";
        } else {
            this.dayTime = "오전";
        }
    }


    public boolean isUse() {
        return use;
    }

    public void setUse(boolean use) {
        this.use = use;
    }

    public boolean isMon() {
        return mon;
    }

    public void setMon(boolean mon) {
        this.mon = mon;
    }

    public boolean isTue() {
        return tue;
    }

    public void setTue(boolean tue) {
        this.tue = tue;
    }

    public boolean isWed() {
        return wed;
    }

    public void setWed(boolean wed) {
        this.wed = wed;
    }

    public boolean isThu() {
        return thu;
    }

    public void setThu(boolean thu) {
        this.thu = thu;
    }

    public boolean isFri() {
        return fri;
    }

    public void setFri(boolean fri) {
        this.fri = fri;
    }

    public boolean isSat() {
        return sat;
    }

    public void setSat(boolean sat) {
        this.sat = sat;
    }

    public boolean isSun() {
        return sun;
    }

    public void setSun(boolean sun) {
        this.sun = sun;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getDayTime() {
        return dayTime;
    }

    public void setDayTime(String dayTime) {
        this.dayTime = dayTime;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

