package com.example.finalproject_weather_clock;

import android.app.Application;
import android.content.Context;

/**
 * Created by 黃筠珊 on 2017/12/24.
 */

public class getcontext extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }
}
