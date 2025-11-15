package com.example.sampleapp;

import android.graphics.Canvas;

import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

public class LootSlot extends GameEntity {
    int spriteID = R.drawable.inventoryslot;
    public boolean occupied;

    public void onCreate(Vector2 pos, Vector2 scale) {
        super.onCreate(pos,scale, this.spriteID);
    }

    @Override
    public void onRender(Canvas canvas) {
        super.onRender(canvas);
    }
}
