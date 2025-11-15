package com.example.sampleapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

public class LootObj extends GameEntity {
    public LootType lootType;
    private Vector2 pivotOffset = new Vector2(0, 0); // applied automatically when drawing

    public void onCreate(Vector2 pos,LootType loot) {
        super.onCreate(pos,loot.itemScale.multiply(100), loot.spriteID);
        lootType = loot;
        computePivot();
    }

    private void computePivot() {
        // pivot = shift the center so (0,0) is the top-left grid cell
        pivotOffset = new Vector2(
                (lootType.itemScale.x - 1) * 100 / 2f,
                (lootType.itemScale.y - 1) * 100 / 2f
        );
    }

    @Override
    public void onRender(Canvas canvas) {
        // apply pivot offset
        Vector2 finalPos = _position.add(pivotOffset);

        float drawX = finalPos.x - (_scale.x / 2f);
        float drawY = finalPos.y - (_scale.y / 2f);

        canvas.drawBitmap(sprite, drawX, drawY, null);
    }
}
