package com.example.sampleapp.Enums;

import com.example.sampleapp.mgp2d.core.Vector2;

public enum LootType {
    Loot1(SpriteList.ExampleItem,new Vector2(1,5),100),
    Loot2(SpriteList.ExamplePause,new Vector2(1,1),200);


    public SpriteList spriteID;

    //itemScale != render scale. just item slot size
    public final Vector2 itemScale;
    final int value;

    LootType(SpriteList spriteID, Vector2 itemScale, int value)
    {
        this.spriteID = spriteID;
        this.itemScale = itemScale;
        this.value = value;
    }
}
