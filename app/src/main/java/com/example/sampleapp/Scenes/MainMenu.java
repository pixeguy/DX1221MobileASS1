package com.example.sampleapp.Scenes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;  // CORRECT


import com.example.sampleapp.Entity.Player.PlayerObj;
import com.example.sampleapp.Enums.SpriteList;
import com.example.sampleapp.R;
import com.example.sampleapp.Scenes.GameLevel.GameLevelScene;
import com.example.sampleapp.ViewPageAdapter;
import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.GameScene;
import com.example.sampleapp.mgp2d.core.Vector2;

import java.util.Arrays;
import java.util.List;

public class MainMenu extends AppCompatActivity implements View.OnClickListener{
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

        PlayerObj player = PlayerObj.getInstance();
        player.value = 200;

        ViewPager2 viewPager = findViewById(R.id.view_pager);

        // 1. Create an instance of the custom adapter
        ViewPageAdapter adapter = new ViewPageAdapter(this);

        // 2. Set the adapter to the ViewPager2
        viewPager.setAdapter(adapter);

    }

    @Override
    public void onClick(View v){
        if(v == helpButton){
            startActivity(new Intent().setClass(this,HelpPage.class));
        }
        else if(v == startButton)
        {
            startActivity(new Intent().setClass(this, GameActivity.class));
            GameScene.enter(GameLevelScene.class);
        }
    }
}
