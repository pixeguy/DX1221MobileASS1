package com.example.sampleapp.Entity.Inventory;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.example.sampleapp.Enums.SpriteList;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

public class LootSlot extends GameEntity {
    public boolean occupied;
    public RectF bounds = new RectF();

    public void onCreate(Vector2 pos, Vector2 scale) {
        super.onCreate(pos,scale, SpriteList.InventorySlot);
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
