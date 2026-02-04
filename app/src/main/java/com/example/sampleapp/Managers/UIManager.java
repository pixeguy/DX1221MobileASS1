package com.example.sampleapp.Managers;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

import com.example.sampleapp.Enums.SoundList;
import com.example.sampleapp.R;
import com.example.sampleapp.Scenes.MainMenu;
import com.example.sampleapp.UI.Core.UIElement;
import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.Singleton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UIManager extends Singleton<UIManager> {
    private final Map<Integer, UIElement> activeTouches = new HashMap<>();
    private final List<UIElement> elements = new ArrayList<>();
    private final List<UIElement> focusElements = new ArrayList<>();
    private final List<UIElement> toRemoveFocusElements = new ArrayList<>();
    private final List<UIElement> toAdd = new ArrayList<>();
    private final List<UIElement> toRemove = new ArrayList<>();
    private UIElement focusedElement;

    public static UIManager getInstance() {
        return getInstance(UIManager.class);
    }

    public void addElement(UIElement element) {
        toAdd.add(element);
    }

    public void addElements(List<UIElement> elements) {
        toAdd.addAll(elements);
    }

    public void addFocusElements(List<UIElement> elements) {
        focusElements.addAll(elements);
    }

    public void removeElement(UIElement element) {
        toRemove.add(element);
    }

    public void removeElements(List<UIElement> elements) {
        toRemove.addAll(elements);
    }

    public void removeFocusElements(List<UIElement> elements) {
        toRemoveFocusElements.addAll(elements);
    }

    public void update(float dt) {
        focusElements.removeAll(toRemoveFocusElements);
        toRemoveFocusElements.clear();

        for (UIElement e : focusElements)
            e.onUpdate(dt);

        if(GameManager.getInstance().getCurrentState() == GameManager.GameState.PAUSED) return;

        elements.addAll(toAdd);
        toAdd.clear();
        elements.removeAll(toRemove);
        toRemove.clear();

        for (UIElement e : elements)
            e.onUpdate(dt);
    }

    public void onRender(Canvas canvas) {
        focusElements.sort(Comparator.comparingDouble(e -> e.zIndex));
        for (UIElement e : focusElements)
            if (e.visible) e.onRender(canvas);
        if(GameManager.getInstance().getCurrentState() == GameManager.GameState.PAUSED) return;
        // Sort by zIndex before drawing
        elements.sort(Comparator.comparingDouble(e -> e.zIndex));
        for (UIElement e : elements)
            if (e.visible) e.onRender(canvas);
    }

    public void handleTouch(MotionEvent event) {
        int action = event.getActionMasked();
        int pointerIndex = event.getActionIndex(); // get the index of the pointer
        int pointerId = event.getPointerId(pointerIndex);

        float x = event.getX(pointerIndex);
        float y = event.getY(pointerIndex);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                assignTouchToElement(pointerId, x, y);
                break;

            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < event.getPointerCount(); i++) {
                    int id = event.getPointerId(i);
                    float mx = event.getX(i);
                    float my = event.getY(i);
                    UIElement e = activeTouches.get(id);
                    if (e != null) e.onTouchMove(mx, my);
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                releaseTouch(pointerId);
                break;
        }
    }

    private void releaseTouch(int pointerId) {
        UIElement e = activeTouches.get(pointerId);
        if (e != null) {
            e.onTouchUp();
            activeTouches.remove(pointerId);
        }
    }

    private void assignTouchToElement(int pointerId, float x, float y) {
        for (UIElement e : elements) {
            if (e.isPointInside(x, y) && e.interactable) {
                e.onTouchDown(x, y);
                activeTouches.put(pointerId, e);
            }
        }

        for (UIElement e : focusElements) {
            if (e.isPointInside(x, y) && e.interactable) {
                e.onTouchDown(x, y);
                activeTouches.put(pointerId, e);
            }
        }
    }

    private boolean showWarningDialog = false;

    public void showWarningDialog(boolean unPauseGame, UIElement... elementsToDisable) {
        if (showWarningDialog) return;

        for (UIElement element : elementsToDisable) {
            if (element != null) element.interactable = false;
        }

        GameActivity.instance.runOnUiThread(() -> {
            if (GameActivity.instance == null || GameActivity.instance.isFinishing()) {
                showWarningDialog = false;
                return;
            }

            // 1. Inflate a FRESH view every time to avoid "View already has a parent" crash
            View dialogView = GameActivity.instance.getLayoutInflater().inflate(R.layout.dialog_warning, null);

            // 2. Build the dialog
            AlertDialog dialog = new AlertDialog.Builder(GameActivity.instance)
                    .setView(dialogView)
                    .setCancelable(false)
                    .create();

            // 3. Setup YES button
            dialogView.findViewById(R.id.btn_yes).setOnClickListener(v -> {
                SoundManager.getInstance().PlayAudio(SoundList.Button_Click, 1.0f, 0.5f, 0.8f);
                dialog.dismiss();
                GameActivity.instance.startActivity(new Intent().setClass(GameActivity.instance, MainMenu.class));
            });

            // 4. Setup NO button
            dialogView.findViewById(R.id.btn_no).setOnClickListener(v -> {
                SoundManager.getInstance().PlayAudio(SoundList.Button_Click, 1.0f, 0.5f, 0.8f);
                for (UIElement element : elementsToDisable) {
                    if (element != null) element.interactable = true;
                }
                showWarningDialog = false;
                dialog.dismiss();
                if(unPauseGame)
                {
                    GameManager.getInstance().TransitionToState(GameManager.GameState.RUNNING);
                }
            });

            // Make background transparent so rounded corners/custom shapes show correctly
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }

            dialog.show();
        });
    }

    private boolean showSettingsDialog = false;
    public void showSettingsDialog(UIElement... elementsToDisable) {
        if(showSettingsDialog) return;

        showSettingsDialog = true;

        // Disable background buttons
        for (UIElement e : elementsToDisable) if (e != null) e.interactable = false;

        GameActivity.instance.runOnUiThread(() -> {
            View v = GameActivity.instance.getLayoutInflater().inflate(R.layout.settings_screen, null);

            AlertDialog dialog = new AlertDialog.Builder(GameActivity.instance)
                    .setView(v)
                    .setCancelable(false)
                    .create();

            // Setup SeekBars
            SeekBar master = v.findViewById(R.id.seek_master);
            SeekBar music = v.findViewById(R.id.seek_music);

            // Set initial values from SoundManager (0.0 to 1.0 -> 0 to 100)
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
                // Re-enable buttons
                for (UIElement e : elementsToDisable) if (e != null) e.interactable = true;
                dialog.dismiss();
                showSettingsDialog = false;
                SoundManager.getInstance().PlayAudio(SoundList.Click, .6f, 0.5f, 0.8f);
            });

            if (dialog.getWindow() != null)
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            dialog.show();
        });
    }

    public void clear() {
        elements.clear();
        toAdd.clear();
        toRemove.clear();
    }

    public boolean isTouched(int pointerId) {
        return activeTouches.containsKey(pointerId);
    }
}
