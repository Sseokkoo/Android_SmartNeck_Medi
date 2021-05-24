package com.smartneck.twofive.Fit.util.User;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.smartneck.twofive.Fit.Main.Fit_WeightSettingFragment.tmpSetup;
import static com.smartneck.twofive.Fit.util.Fit_Constants.TAG;

public class Fit_Preset {

    public static int count;
    public static int set;
    public static int stop;
    public static int seat;
    public static int setup;
    public static int MaxWeight;
    public static int MaxHeight;
    public static String soundType;
    public static float userHeightSetting = 0.8f;
    public static String DEVICE_NAME;
    public static int strength = 3;
    public static int breakTime;
    public static boolean isBreakTime = false;
    public static int measureSetup;

    public static void getPresetJson(Context context, String JsonString) {

        try {
            JSONArray jsonArray = new JSONArray(JsonString);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            count = jsonObject.getInt("Count");
            set = jsonObject.getInt("Sett");
            stop = jsonObject.getInt("Stop");
            seat = jsonObject.getInt("Seat");
            setup = jsonObject.getInt("Setup");
            measureSetup = jsonObject.getInt("MeasureSetup");
            tmpSetup = Fit_Preset.setup;


            MaxWeight = jsonObject.getInt("MaxWeight");
            MaxHeight = jsonObject.getInt("MaxHeight");
            strength = jsonObject.getInt("Strength");
            breakTime = jsonObject.getInt("BreakTime");
            Log.d(TAG, "getPresetJson: Count -> " + count);
            Log.d(TAG, "getPresetJson: Set -> " + set);
            Log.d(TAG, "getPresetJson: Stop -> " + stop);
            Log.d(TAG, "getPresetJson: Seat -> " + seat);
            Log.d(TAG, "getPresetJson: Setup -> " + setup);
            Log.d(TAG, "getPresetJson: MaxWeight -> " + MaxWeight);
            Log.d(TAG, "getPresetJson: MaxHeight -> " + MaxHeight);
            Log.d(TAG, "getPresetJson: Strength -> " + strength);

            Fit_User.setToken(context);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static int getHomeHeightValue(int height) {
        return height;
//        int reverseHeight = (height - (height * 2) + 90);

//        if (reverseHeight == 90){
//            return 0;
//        }

//        return reverseHeight;
    }

    public static int getHomeHeightValue(int height , int weight) {

//        int reverseHeight = (height - (height * 2) + 90);
//
//        if (reverseHeight == 90){
//            if (weight == 0){
//
//            return 0;
//            }else{
//                return 90;
//            }
//        }
//
//        return reverseHeight;
        return height;
    }

    public static void getEntrySelection(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("heightEntry", Context.MODE_PRIVATE);
        int selection = preferences.getInt("entry", 8);

        switch (selection) {
            case 0:

                userHeightSetting = 0.4f;

                break;
            case 2:

                userHeightSetting = 0.45f;

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
