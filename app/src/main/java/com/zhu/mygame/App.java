package com.zhu.mygame;

import android.app.Application;
import android.content.Context;
import android.os.Handler;


public class App extends Application {

    public static Context mContext;

    public static Handler mHandler;


    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
        mHandler = new Handler();

    }

}
