package com.example.sampleapp.Scenes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sampleapp.R;
import com.example.sampleapp.Scenes.GameLevel.GameLevelScene;
import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.GameScene;

public class MainMenu extends Activity implements View.OnClickListener{
    private Button helpButton;
    private Button startButton;
    @Override
    protected void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.layoutmainmenu);
        helpButton = findViewById(R.id.button2);
        helpButton.setOnClickListener(this);

        startButton = findViewById(R.id.button);
        startButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        if(v == helpButton){
            startActivity(new Intent().setClass(this,HelpPage.class));
        }
        else if(v == startButton)
        {
            startActivity(new Intent().setClass(this, GameActivity.class));
            GameScene.enter(MainGameScene.class);
        }
    }
}
