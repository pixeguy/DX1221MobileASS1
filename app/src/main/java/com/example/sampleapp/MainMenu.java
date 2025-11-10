package com.example.sampleapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.GameScene;

public class MainMenu extends Activity implements View.OnClickListener{
    private Button helpButton;
    private Button startButton;
    @Override
    protected void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
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
