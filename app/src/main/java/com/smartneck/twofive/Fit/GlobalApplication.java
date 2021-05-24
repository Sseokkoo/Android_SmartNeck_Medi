package com.smartneck.twofive.Fit;

import android.app.Application;
import android.content.Context;

public class GlobalApplication extends Application {
    private static Context appContext;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        context = getApplicationContext();
    }

    public static Context getAppContext() {
        return appContext;
    }
    public static Context getApllication(){
        return context;
    }
}