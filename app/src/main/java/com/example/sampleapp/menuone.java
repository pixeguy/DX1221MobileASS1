package com.example.sampleapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.sampleapp.Entity.Player.PlayerObj;
import com.example.sampleapp.Scenes.GameLevel.GameLevelScene;
import com.example.sampleapp.Scenes.MainMenu;
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

        //setting up buttons
        Button PlayButton = rootView.findViewById(R.id.button3);
        if (PlayButton != null){
            PlayButton.setOnClickListener(this);
        }
        return rootView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.button3) {
            MainMenu menu = (MainMenu) getActivity();
            if (menu != null) {
                menu.StartGame();   // <<< THIS reproduces MAINMENU behavior
            }
        }
    }
}