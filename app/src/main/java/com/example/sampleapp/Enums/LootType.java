package com.example.sampleapp.Enums;

import com.example.sampleapp.mgp2d.core.Vector2;

public enum LootType {
    Loot1(SpriteAnimationList.Example2Item,new Vector2(0.12f,0.08f), new Vector2(3,1),100),
    Loot2(SpriteAnimationList.ExamplePause,new Vector2(0.8f,0.8f), new Vector2(1,1),200);

    public SpriteAnimationList spriteID;

    public final Vector2 actualScale;

    //itemScale != render scale. just item slot size
    public final Vector2 itemScale;
    public final int value;

    LootType(SpriteAnimationList spriteID,Vector2 actualScale, Vector2 itemScale, int value)
    {
        this.spriteID = spriteID;
        this.actualScale = actualScale;
        this.itemScale = itemScale;
        this.value = value;
    }
}
