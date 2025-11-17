package com.example.sampleapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

public class LootObj extends GameEntity {
    public LootType lootType;
    private Vector2 pivotOffset = new Vector2(0, 0); // applied automatically when drawing

    public void onCreate(Vector2 pos,LootType loot) {
        super.onCreate(pos,loot.itemScale, loot.spriteID);
        lootType = loot;
        computePivot();
    }

    private void computePivot() {
        // pivot = shift the center so (0,0) is the top-left grid cell
        int frameWidth = animatedSprite._width;
        int frameHeight = animatedSprite._height;

        pivotOffset = new Vector2(
                (lootType.itemScale.x - 1) * frameWidth * 0.5f,
                (lootType.itemScale.y - 1) * frameHeight * 0.5f
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
