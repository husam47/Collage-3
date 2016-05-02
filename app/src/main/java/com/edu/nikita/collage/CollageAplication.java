package com.edu.nikita.collage;

import android.app.Application;

import com.vk.sdk.VKSdk;

/**
 * Created by Nikita on 01.05.2016.
 * Application c инициализайией VKSdk
 */
public class CollageAplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(this);
    }
}
