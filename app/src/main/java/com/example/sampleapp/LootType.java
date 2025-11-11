package com.example.sampleapp;

import com.example.sampleapp.mgp2d.core.Vector2;

public enum LootType {
    Loot1(R.drawable.splash,new Vector2(1,1),100),
    Loot2(R.drawable.ic_launcher_foreground,new Vector2(1,1),200);


    final int spriteID;
    final Vector2 itemScale;
    final int value;

    LootType(int spriteID, Vector2 itemScale, int value)
    {
        this.spriteID = spriteID;
        this.itemScale = itemScale;
        this.value = value;
    }
}
