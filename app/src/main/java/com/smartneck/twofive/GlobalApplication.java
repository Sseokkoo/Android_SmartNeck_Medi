package com.smartneck.twofive;

import android.app.Application;
import android.content.Context;
import android.util.Log;


import com.smartneck.twofive.SQ.DBHelper;
import com.smartneck.twofive.util.User.UserPreference;

import static com.smartneck.twofive.util.Constants.TAG;

public class GlobalApplication extends Application {
    public  static Context appContext;
    public  static Context context;
    public static DBHelper helper;
    public static UserPreference userPreference;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: - - - - - - - - - - GlobalApplication");
        appContext = getApplicationContext();
        context = getApplicationContext();
        userPreference = new UserPreference(getApplicationContext());

    }

    public static Context getAppContext() {
        return appContext;
    }
    public static Context getApllication(){
        return context;
    }
}