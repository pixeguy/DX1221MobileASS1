package com.example.sampleapp.Scenes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.sampleapp.Managers.SoundManager;
import com.example.sampleapp.R;

public class SplashPage extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.splash);

        Thread splashThread = new Thread()
        {
            @Override
            public void run()
            {
                super.run();
                try {
                    sleep(3000);
                    startActivity(new Intent().setClass(SplashPage.this, MainMenu.class));
                }
                catch (Exception e){
                    throw new RuntimeException(e);
                }
            }
        };
        splashThread.start();
    }
}
