package com.smartneck.twofive.util.User;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.Serializable;

public class pastPreset implements Serializable {
    public static int strength = 3;
    public static int count;
    public static int set;
    public static int stop;
    public static int seat;
    public static int setup;
    public static int MaxWeight;
    public static int MaxHeight;
    public static String soundType;
    public static float userHeightSetting = 1f;
    public static String DEVICE_NAME;
    public static int breakTime;
    public static boolean isBreakTime = false;




    public static void getEntrySelection(Context context){
        SharedPreferences preferences = context.getSharedPreferences("heightEntry" , Context.MODE_PRIVATE);
        int selection = preferences.getInt("entry" , 8);

        switch (selection){
            case 0:
                userHeightSetting = 0.4f;
                break;
            case 1:
                userHeightSetting = 0.45f;
                break;
            case 2:
                userHeightSetting = 0.5f;
                break;
            case 3:
                userHeightSetting = 0.55f;
                break;
            case 4:
                userHeightSetting = 0.6f;
                break;
            case 5:
                userHeightSetting = 0.65f;
                break;
            case 6:
                userHeightSetting = 0.7f;
                break;
            case 7:
                userHeightSetting = 0.75f;
                break;
            case 8:
                userHeightSetting = 0.8f;
                break;
            case 9:
                userHeightSetting = 0.85f;
                break;
            case 10:
                userHeightSetting = 0.9f;
                break;
            case 11:
                userHeightSetting = 0.95f;
                break;
            case 12:
                userHeightSetting = 1f;
                break;
        }


    }

}
