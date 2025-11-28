package com.example.sampleapp.Entity.Buttons;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.example.sampleapp.Enums.SpriteAnimationList;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

public class ButtonObj extends GameEntity {

    RectF bounds = new RectF();

    @Override
    public void onCreate(Vector2 pos, Vector2 scale, SpriteAnimationList spriteAnim) {
        super.onCreate(pos,scale,spriteAnim);
    }

    protected void UpdateBounds()
    {
        Rect rect = animatedSprite.GetRect();
        bounds.set(rect);
    }

    public void OnClick()
    {

    }

    public boolean checkIfPressed(Vector2 posToCheck)
    {
        return bounds.contains(posToCheck.x, posToCheck.y);
    }

    @Override
    public void onRender(Canvas canvas) {
        super.onRender(canvas);
        UpdateBounds();
    }
}
