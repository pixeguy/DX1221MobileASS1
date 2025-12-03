package com.example.sampleapp.VisualEffect;

import android.graphics.Color;

import com.example.sampleapp.mgp2d.core.GameEntity;

public class OnHitVisualEffect {
    private float flashDuration = 0.2f;
    private float flashTimer = 0f;
    private boolean flashing = false;

    private int originalColor = Color.TRANSPARENT;
    private int flashColor = Color.WHITE;

    private final GameEntity entity;

    public OnHitVisualEffect(GameEntity entity) {
        this.entity = entity;
    }

    public void playHitFlash() {
        flashing = true;
        flashTimer = flashDuration;
        entity.setTint(flashColor);
    }

    public void onUpdate(float dt) {
        if (!flashing) return;

        flashTimer -= dt;
        if (flashTimer <= 0f) {
            flashing = false;
            entity.setTint(originalColor);
        }
    }
}
