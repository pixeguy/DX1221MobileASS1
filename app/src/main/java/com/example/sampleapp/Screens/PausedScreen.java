package com.example.sampleapp.Screens;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

import com.example.sampleapp.Enums.SoundList;
import com.example.sampleapp.Managers.GameManager;
import com.example.sampleapp.Managers.ScreenManager;
import com.example.sampleapp.Managers.SoundManager;
import com.example.sampleapp.Managers.UIManager;
import com.example.sampleapp.R;
import com.example.sampleapp.Scenes.GameLevel.GameLevelScene;
import com.example.sampleapp.Scenes.MainMenu;
import com.example.sampleapp.UI.Buttons.UIButton;
import com.example.sampleapp.UI.Text.UIText;
import com.example.sampleapp.UI.Core.UIElement;
import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.Vector2;

public class PausedScreen extends BaseScreen{
    public PausedScreen() {
        super();
    }

    private static final Vector2 buttonSize = new Vector2(840, 240);
    private static final float textSize = 100.0f;

    UIButton resume;
    UIButton quit;
    UIButton settings;

    @Override
    protected void init() {
        // Background dim
        UIElement dim = new UIElement(0, 0, 0, 0) {
            @Override
            public void onRender(Canvas canvas) {
                paint.setColor(Color.argb(150, 0, 0, 0));
                canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);
            }
        };
        uiElementList.add(dim);

        UIText label = new UIText("Paused", 200, new Vector2(GameLevelScene.screenWidth * 0.5f, 200), buttonSize.x, buttonSize.y);

        // Buttons
        resume = new UIButton(GameLevelScene.screenWidth * 0.5f, 400, buttonSize.x, buttonSize.y, "Resume");
        resume.setColor(Color.RED);
        resume.setTextSize(textSize);
        resume.setOnRelease(() -> {
            SoundManager.getInstance().resumeSounds();
            SoundManager.getInstance().PlayAudio(SoundList.Button_Click, 1.0f, 0.9f, 0.8f);
            ScreenManager.setScreen((Class<?>) null);
            GameManager.getInstance().TransitionToState(GameManager.GameState.RUNNING);
        });

        settings = new UIButton(GameLevelScene.screenWidth * 0.5f, 700, buttonSize.x, buttonSize.y, "Settings");
        settings.setColor(Color.RED);
        settings.setTextSize(textSize);
        settings.setOnRelease(() -> {
            SoundManager.getInstance().PlayAudio(SoundList.Button_Click, 1.0f, 0.9f, 0.8f);
            UIManager.getInstance().showSettingsDialog(resume, quit, settings);
        });

        quit = new UIButton(GameLevelScene.screenWidth * 0.5f, 1000, buttonSize.x, buttonSize.y, "Quit");
        quit.setColor(Color.RED);
        quit.setTextSize(textSize);
        quit.setOnRelease(() ->
        {
            SoundManager.getInstance().PlayAudio(SoundList.Button_Click, 1.0f, 0.9f, 0.8f);
            UIManager.getInstance().showWarningDialog(false, resume, quit, settings);
        });

        uiElementList.add(label);
        uiElementList.add(resume);
        uiElementList.add(quit);
        uiElementList.add(settings);

        UIManager.getInstance().addFocusElements(uiElementList);
    }

    @Override
    public void disposed() {
        UIManager.getInstance().removeFocusElements(uiElementList);
    }
}
