package com.example.sampleapp.Scenes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.sampleapp.R;

public class SplashPage extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
