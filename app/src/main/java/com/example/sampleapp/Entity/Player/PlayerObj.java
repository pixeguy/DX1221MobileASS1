package com.example.sampleapp.Entity.Player;

import android.graphics.Canvas;

import com.example.sampleapp.Enums.SpriteList;
import com.example.sampleapp.mgp2d.core.AnimatedSprite;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

public class PlayerObj extends GameEntity {

    private AnimatedSprite animatedSpritez;
    @Override
    public void onCreate(Vector2 pos, Vector2 scale, SpriteList spriteAnim) {
        super.onCreate(pos,scale,spriteAnim);
    }
    @Override
    public void onUpdate(float dt) {
        super.onUpdate(dt);
        _position.x += 50 * dt;
    }

    @Override
    public void onRender(Canvas canvas) {

        super.onRender(canvas);
    }
}
