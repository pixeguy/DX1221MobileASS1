package com.example.sampleapp.Entity.Inventory;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.example.sampleapp.Enums.SpriteAnimationList;
import com.example.sampleapp.mgp2d.core.AnimatedSprite;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

public class LootSlot extends GameEntity {
    public boolean occupied;
    public RectF bounds = new RectF();

    @Override
    public void onCreate(Vector2 pos, Vector2 scale) {
        super.onCreate(pos,scale);
        SetAnimation(SpriteAnimationList.InventorySlot);
    }
    private void UpdateBounds()
    {
        Rect rect = animatedSprite.GetRect();
        bounds.set(rect);
    }
    @Override
    public void onRender(Canvas canvas) {
        super.onRender(canvas);
        UpdateBounds();
    }
}
