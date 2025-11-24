package com.example.sampleapp.Entity.Inventory;

import android.graphics.Canvas;

import com.example.sampleapp.Entity.Buttons.LootButtonObj;
import com.example.sampleapp.Enums.LootType;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

import java.util.ArrayList;

public class LootObj extends GameEntity {
    public LootType lootType;
    public LootButtonObj sourceButton;
    public boolean placed = false;
    public ArrayList<LootSlot> slotsOccupied= new ArrayList<LootSlot>();
    public Vector2 placedPos;
    public Vector2 pivotOffset = new Vector2(0, 0); // applied automatically when drawing

    public void onCreate(Vector2 pos,LootType loot) {
        super.onCreate(pos,new Vector2(0.8f,0.8f), loot.spriteID);
        lootType = loot;
        computePivot();
    }

    private void computePivot() {
        // scaled size of ONE grid cell for this sprite
        float cellW = animatedSprite._width  * (_scale.x / lootType.itemScale.x);
        float cellH = animatedSprite._height * (_scale.y / lootType.itemScale.y);

        pivotOffset = new Vector2(
                cellW * (lootType.itemScale.x - 1) * 0.5f,
                cellH * (lootType.itemScale.y - 1) * 0.5f
        );
    }

    @Override
    public void onRender(Canvas canvas) {
        // apply pivot offset
        Vector2 finalPos = _position.add(pivotOffset);

        //change scale later to normal size.
        animatedSprite.render(canvas, (int) finalPos.x, (int) finalPos.y, _scale, null);
    }
}
