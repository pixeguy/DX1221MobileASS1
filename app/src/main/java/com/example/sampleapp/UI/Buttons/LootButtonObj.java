package com.example.sampleapp.UI.Buttons;

import android.graphics.Canvas;

import com.example.sampleapp.Enums.LootType;
import com.example.sampleapp.Enums.SpriteAnimationList;
import com.example.sampleapp.mgp2d.core.Vector2;

public class LootButtonObj extends ButtonObj {
    public LootType loot;
    public boolean used = false;
    public float rotationAngle = 0.0f;
    public Vector2 targetPos = null;   // where the button should move to
    public float lerpSpeed = 10f;      // tune this


    public void onCreate(Vector2 pos, Vector2 scale, SpriteAnimationList animSprite, LootType loot) {
        super.onCreate(pos, scale, animSprite);
        this.loot = loot;
    }

    @Override
    public void onUpdate(float dt) {
        super.onUpdate(dt);

        if (targetPos != null) {
            // Smoothly move toward target
            float t = lerpSpeed * dt;
            _position.x = _position.x + (targetPos.x - _position.x) * t;
            _position.y = _position.y + (targetPos.y - _position.y) * t;

            // Close enough → snap + stop lerping
            if (Math.abs(_position.x - targetPos.x) < 1f &&
                    Math.abs(_position.y - targetPos.y) < 1f) {
                _position = targetPos;
                targetPos = null; // stop lerping
            }
        }
    }

    public void OnClick(Vector2 pos) {
        super.OnClick();
    }


    public void rotate90() {
        // If the current angle is 0 degrees, set it to 90 degrees.
        if (this.rotationAngle == 0.0f) {
            this.rotationAngle = 90.0f;
        }
        // If the current angle is 90 degrees, set it back to 0 degrees.
        else if (this.rotationAngle == 90.0f) {
            this.rotationAngle = 0.0f;
        }
    }

    @Override
    public void onRender(Canvas canvas) {

        // Assuming you have access to animatedSprite here (based on your previous code):
        animatedSprite.render(canvas, (int) _position.x, (int) _position.y, _scale,rotationAngle, null);

        UpdateBounds();
    }
}
