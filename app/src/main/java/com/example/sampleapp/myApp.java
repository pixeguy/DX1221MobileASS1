package com.example.sampleapp;

import com.example.sampleapp.Managers.SaveManager;
import com.example.sampleapp.Managers.SoundManager;

public class myApp extends android.app.Application{
    @Override
    public void onCreate()
    {
        super.onCreate();

        SoundManager.getInstance().InitAudio(getApplicationContext());
        SaveManager.getInstance().scores = SaveManager.getInstance().loadScoresJson(getApplicationContext());
    }
}
