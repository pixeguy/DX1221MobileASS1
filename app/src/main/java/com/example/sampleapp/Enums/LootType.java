package com.example.sampleapp.Enums;

import com.example.sampleapp.mgp2d.core.Vector2;

public enum LootType {
    Crystal(SpriteAnimationList.Crystal, SpriteAnimationList.CrystalBtn, new Vector2(0.09412f,0.09412f), new Vector2(1,1),100),
    Potion(SpriteAnimationList.Potion, SpriteAnimationList.PotionBtn, new Vector2(0.09412f,0.09412f), new Vector2(1,1),100),
    Sword(SpriteAnimationList.Sword, SpriteAnimationList.SwordBtn, new Vector2(0.09412f,0.09412f), new Vector2(2,1),100),
    Staff(SpriteAnimationList.Staff, SpriteAnimationList.StaffBtn, new Vector2(0.09412f,0.09412f), new Vector2(3,1),100),
    Teapot(SpriteAnimationList.Teapot, SpriteAnimationList.TeapotBtn, new Vector2(0.04706f,0.04706f), new Vector2(2,2),100),
    ;

    public SpriteAnimationList spriteID;
    public SpriteAnimationList spriteBtnID;

    public final Vector2 actualScale;

    //itemScale != render scale. just item slot size
    public final Vector2 itemScale;
    public final int value;

    LootType(SpriteAnimationList spriteID, SpriteAnimationList spriteBtnID,Vector2 actualScale, Vector2 itemScale, int value)
    {
        this.spriteID = spriteID;
        this.spriteBtnID = spriteBtnID;
        this.actualScale = actualScale;
        this.itemScale = itemScale;
        this.value = value;
    }
}
