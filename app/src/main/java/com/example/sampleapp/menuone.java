package com.example.sampleapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.sampleapp.Entity.Player.PlayerObj;
import com.example.sampleapp.Enums.SoundList;
import com.example.sampleapp.Managers.SoundManager;
import com.example.sampleapp.Managers.UIManager;
import com.example.sampleapp.Scenes.GameLevel.GameLevelScene;
import com.example.sampleapp.Scenes.MainMenu;
import com.example.sampleapp.UI.Core.UIElement;
import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.GameScene;

public class menuone extends Fragment implements View.OnClickListener {
    public void menuone()
    {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_menuone, container, false);
        Log.d("FRAGMENT", "Host = " + getActivity().getClass().getSimpleName());
        SoundManager.getInstance().startMusic();

        //setting up buttons
        Button PlayButton = rootView.findViewById(R.id.button3);
        if (PlayButton != null){
            PlayButton.setOnClickListener(this);
        }

        Button settingBtn = rootView.findViewById(R.id.button4);
        if (settingBtn != null) {
            settingBtn.setOnClickListener(v -> {
                SoundManager.getInstance().PlayAudio(SoundList.Button_Click, 1.0f, 0.9f, 0.8f);
                MainMenu menu = (MainMenu) getActivity();
                if (menu != null) {
                    menu.showSettingsDialog();
                }
            });
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.button3) {
            SoundManager.getInstance().PlayAudio(SoundList.Button_Click, 1.0f, 0.9f, 0.8f);
            MainMenu menu = (MainMenu) getActivity();
            if (menu != null) {
                menu.StartGame();   // <<< THIS reproduces MAINMENU behavior
            }
        }
    }
}