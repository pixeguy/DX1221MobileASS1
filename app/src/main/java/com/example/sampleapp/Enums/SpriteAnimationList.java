package com.example.sampleapp.Enums;

import android.graphics.Bitmap;

public enum SpriteAnimationList {
    //PlayerAnimations
    PlayerIdle(SpriteList.PlayerSprite.sprite, 1, 1, 1, 0, 0),

    PlayerShootMissile(SpriteList.PlayerMagicMissileSprite.sprite, 1, 5, 12, 0, 4),

    //Slime Animations
    SlimeIdleFront(SpriteList.SlimeIdleSprite.sprite,4,6,12, 0, 5),
    SlimeIdleBack(SpriteList.SlimeIdleSprite.sprite,4,6,12, 6, 11),
    SlimeIdleLeft(SpriteList.SlimeIdleSprite.sprite,4,6,12, 12, 17),
    SlimeIdleRight(SpriteList.SlimeIdleSprite.sprite,4,6,12, 18, 23),

    SlimeRunFront(SpriteList.SlimeRunSprite.sprite,4,8,20, 0, 7),
    SlimeRunBack(SpriteList.SlimeRunSprite.sprite,4,8,20, 8, 15),
    SlimeRunLeft(SpriteList.SlimeRunSprite.sprite,4,8,20, 16, 23),
    SlimeRunRight(SpriteList.SlimeRunSprite.sprite,4,8,20, 24, 31),

    SlimeDeathFront(SpriteList.SlimeDeathSprite.sprite,4,10,12, 0, 9),
    SlimeDeathBack(SpriteList.SlimeDeathSprite.sprite,4,10,12, 10, 19),
    SlimeDeathLeft(SpriteList.SlimeDeathSprite.sprite,4,10,12, 20, 29),
    SlimeDeathRight(SpriteList.SlimeDeathSprite.sprite,4,10,12, 30, 39),


    //other Animations
    ExamplePause(SpriteList.ExamplePauseSprite.sprite,1,1,1),
    ExampleItem(SpriteList.ExampleItemSprite.sprite,1,1,1),
    InventorySlot(SpriteList.InventorySlotSprite.sprite,1,1,1);

    public final int rows;
    public final int columns;
    public final int fps;
    public final int startFrame;
    public final int endFrame;
    public final Bitmap sprite;

    SpriteAnimationList(Bitmap bmp, int rows, int columns, int fps) {
        this(bmp, rows, columns, fps, 0, rows * columns - 1); // Play full sheet by default
    }

    SpriteAnimationList(Bitmap bmp, int rows, int columns, int fps, int startFrame, int endFrame) {
        this.sprite = bmp;
        this.rows = rows;
        this.columns = columns;
        this.fps = fps;
        this.startFrame = startFrame;
        this.endFrame = endFrame;
    }
}
