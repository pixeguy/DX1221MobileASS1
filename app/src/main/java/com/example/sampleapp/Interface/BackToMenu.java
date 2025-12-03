package com.example.sampleapp.Interface;

import android.content.Intent;

import com.example.sampleapp.UI.Buttons.IActivatable;
import com.example.sampleapp.Scenes.MainMenu;
import com.example.sampleapp.mgp2d.core.GameActivity;

public class BackToMenu implements IActivatable {

    public BackToMenu() {
    }

    @Override
    public void execute() {
        GameActivity.instance.startActivity(new Intent().setClass(GameActivity.instance, MainMenu.class));
    }
}
