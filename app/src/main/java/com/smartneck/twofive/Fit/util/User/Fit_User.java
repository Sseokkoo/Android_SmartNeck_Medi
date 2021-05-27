package com.smartneck.twofive.Fit.util.User;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.smartneck.twofive.Fit.util.Fit_Constants;

import static com.smartneck.twofive.Fit.util.Fit_Constants.TAG;

public class Fit_User {
    public static int MemberNo;
    public static String name;
    public static String birth;
    public static String gender;
    public static String phone;
    public static String country;
    public static String token;
    public static int age;
    public static String language;

    public static boolean isHomeDevice(){
        if (Fit_Constants.deviceType.equals("HOM")){
            return true;
        }
        return false;
    }

    public static void setToken(Context context){

        SharedPreferences preferences = context.getSharedPreferences("user" , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token" , token);
        editor.apply();
    }

    public static void getToken(Context context){
        SharedPreferences preferences = context.getSharedPreferences("user" , Context.MODE_PRIVATE);
        token = preferences.getString("token" , "non");
    }

    public static void getUserInfoJson(Context context , String JsonString) {

        try {
            JSONArray jsonArray = new JSONArray(JsonString);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            Fit_User.MemberNo= jsonObject.getInt("MemberNo");
            Fit_User.token= jsonObject.getString("Token");
            Fit_User.name = jsonObject.getString("Name");
            Fit_User.birth = jsonObject.getString("Birth");
            Fit_User.gender = jsonObject.getString("Gender");
            Fit_User.phone = jsonObject.getString("Phone");
            Fit_User.country = jsonObject.getString("Country");

//            Log.d(TAG, "getUserInfoJson: MemberNo -> " + Fit_User.MemberNo);
//            Log.d(TAG, "getUserInfoJson: Token -> " + Fit_User.token);
//            Log.d(TAG, "getUserInfoJson: Name -> " + Fit_User.name);
//            Log.d(TAG, "getUserInfoJson: Birth -> " + Fit_User.birth);
//            Log.d(TAG, "getUserInfoJson: Gender -> " + Fit_User.gender);
//            Log.d(TAG, "getUserInfoJson: Phone -> " + Fit_User.phone);
//            Log.d(TAG, "getUserInfoJson: Country -> " + Fit_User.country);


            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            String getYear = sdf.format(date);
            int currentYear = Integer.parseInt(getYear);




            String[] split = Fit_User.birth.split("-");

            age = currentYear - Integer.parseInt(split[0]) + 1;
            Log.d(TAG, "getUserInfoJson: Age -> " + age);
            Fit_User.setToken(context);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static boolean getAutoLoginState(Context context){
        SharedPreferences preferences = context.getSharedPreferences("user" , Context.MODE_PRIVATE);
        return preferences.getBoolean("auto" , false);

    }

    public static void setAutologinState(Context context , boolean auto){
        SharedPreferences preferences = context.getSharedPreferences("user" , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("auto" , auto);
        editor.apply();
    }

}
