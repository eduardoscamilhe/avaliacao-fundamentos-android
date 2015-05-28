package com.example.administrador.myapplication;

import android.app.Application;

import com.example.administrador.myapplication.util.AppUtil;

/**
 * Created by Administrador on 25/05/2015.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppUtil.CONTEXT = getApplicationContext();

    }
}
