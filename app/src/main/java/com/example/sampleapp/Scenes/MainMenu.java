package com.example.sampleapp.Scenes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;  // CORRECT


import com.example.sampleapp.Entity.Player.PlayerObj;
import com.example.sampleapp.Enums.SoundList;
import com.example.sampleapp.Enums.SpriteList;
import com.example.sampleapp.Managers.SoundManager;
import com.example.sampleapp.R;
import com.example.sampleapp.Scenes.GameLevel.GameLevelScene;
import com.example.sampleapp.UI.Core.UIElement;
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

        ViewPager2 viewPager = findViewById(R.id.view_pager);

        // 1. Create an instance of the custom adapter
        ViewPageAdapter adapter = new ViewPageAdapter(this);

        // 2. Set the adapter to the ViewPager2
        viewPager.setAdapter(adapter);

    }

    @Override
    public void onClick(View v){

    }

    private boolean goingToGame = false;

    public void StartGame() {
        goingToGame = true;
        startActivity(new Intent(this, GameActivity.class));
        GameScene.clearClass();
        GameScene.enter(GameLevelScene.class);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!goingToGame) {
            SoundManager.getInstance().pauseSounds();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        goingToGame = false;
        SoundManager.getInstance().resumeSounds();
    }

    private boolean showSettingsDialog = false;
    public void showSettingsDialog() {
        if (showSettingsDialog) return;

        showSettingsDialog = true;

        // Use 'this' instead of GameActivity.instance because we are in MainMenu
        this.runOnUiThread(() -> {
            // 1. Check if activity is still alive to prevent crashes
            if (isFinishing()) return;

            // 2. Use the current activity's layout inflater
            View v = getLayoutInflater().inflate(R.layout.settings_screen, null);

            // 3. Use 'this' for the AlertDialog Builder
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setView(v)
                    .setCancelable(false)
                    .create();

            // Setup SeekBars
            SeekBar master = v.findViewById(R.id.seek_master);
            SeekBar music = v.findViewById(R.id.seek_music);

            // Set initial values from SoundManager
            master.setProgress((int)(SoundManager.getInstance().getMasterVolume() * 100));
            music.setProgress((int)(SoundManager.getInstance().getMusicVolume() * 100));

            master.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override public void onProgressChanged(SeekBar s, int p, boolean b) {
                    SoundManager.getInstance().setMasterVolume(p / 100f);
                }
                @Override public void onStartTrackingTouch(SeekBar s) {
                    SoundManager.getInstance().PlayAudio(SoundList.Click, .6f, 0.5f, 0.8f);
                }
                @Override public void onStopTrackingTouch(SeekBar s) {}
            });

            music.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override public void onProgressChanged(SeekBar s, int p, boolean b) {
                    SoundManager.getInstance().setMusicVolume(p / 100f);
                }
                @Override public void onStartTrackingTouch(SeekBar s) {
                    SoundManager.getInstance().PlayAudio(SoundList.Click, .6f, 0.5f, 0.8f);
                }
                @Override public void onStopTrackingTouch(SeekBar s) {}
            });

            v.findViewById(R.id.btn_back).setOnClickListener(view -> {
                dialog.dismiss();
                showSettingsDialog = false;
                SoundManager.getInstance().PlayAudio(SoundList.Click, .6f, 0.5f, 0.8f);
            });

            // Make background transparent for rounded corners
            if (dialog.getWindow() != null)
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            dialog.show();
        });
    }
}
