package com.example.sampleapp.Entity.Inventory;

import android.graphics.Canvas;
import android.util.Log;

import com.example.sampleapp.UI.Buttons.LootButtonObj;
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
    public float rotationAngle = 0.0f;
    public Vector2 instanceScale = new Vector2(0,0);

    public void onCreate(Vector2 pos,LootType loot) {
        super.onCreate(pos,loot.actualScale, loot.spriteID);
        lootType = loot;
        this.instanceScale = new Vector2(lootType.itemScale.x, lootType.itemScale.y);
        Log.d("Pointer", "scale" + lootType.itemScale);
        computePivot();
        //rotate90();
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
        if (this.rotationAngle == 90.0f) {
            instanceScale.x = lootType.itemScale.y;
            instanceScale.y = lootType.itemScale.x;
        }
        // If the current angle is 90 degrees, set it back to 0 degrees.
        else if (this.rotationAngle == 0.0f) {
            instanceScale.x = lootType.itemScale.x;
            instanceScale.y = lootType.itemScale.y;
        }
        // pivot tile — rotate around THIS
        float px = _position.x;
        float py = _position.y;

        // where to draw sprite (centered)
        Vector2 finalPos = _position.add(pivotOffset);

        canvas.save();

        // Rotate around pivot tile, NOT sprite center
        canvas.translate(px, py);
        canvas.rotate(rotationAngle);
        canvas.translate(-px, -py);

        // Draw the sprite based on pivotOffset
        animatedSprite.render(canvas,
                (int) finalPos.x,
                (int) finalPos.y,
                _scale,
                null);

        canvas.restore();
    }
}
