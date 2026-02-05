package com.example.sampleapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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

        TextInputLayout nameLayout = rootView.findViewById(R.id.nameInputLayout);
        TextInputEditText nameInput = rootView.findViewById(R.id.nameInput);

        //setting up buttons
        Button PlayButton = rootView.findViewById(R.id.button3);
        PlayButton.setEnabled(false);

        nameInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString().trim();
                boolean ok = !text.isEmpty();

                PlayButton.setEnabled(ok);

                // optional: show error when empty
                if (!ok) nameLayout.setError("Required");
                else nameLayout.setError(null);
            }
        });

        if (PlayButton != null){
            PlayButton.setOnClickListener(v -> {
                SoundManager.getInstance().PlayAudio(SoundList.Button_Click, 1.0f, 0.9f, 0.8f);
                MainMenu menu = (MainMenu) getActivity();
                if (menu != null) {
                    String name = "";
                    if (nameInput.getText() != null)
                        name = nameInput.getText().toString().trim();

                    menu.StartGame(name);
                }
            });
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

        }
    }
}