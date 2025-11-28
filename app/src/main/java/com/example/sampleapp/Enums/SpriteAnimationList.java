package com.example.sampleapp.Enums;

import android.graphics.Bitmap;

import com.example.sampleapp.R;

public enum SpriteAnimationList {
    //PlayerAnimations
    PlayerIdle(SpriteList.PlayerSprite.sprite, 1, 1, 1, 0, 0, false),

    //Projectile Animations
    PlayerShootMissile(SpriteList.PlayerMagicMissileSprite.sprite, 1, 5, 12, 0, 4, true),
    EnemyShootFireMissile(SpriteList.EnemyFireMissileSprite.sprite, 1, 5, 12, 0, 4, true),
    EnemyShootToxicMissile(SpriteList.EnemyToxicMissileSprite.sprite, 1, 5, 12, 0, 4, true),

    //Slime Animations
    SlimeIdleFront(SpriteList.SlimeIdleSprite.sprite, 4, 6, 12, 0, 5, true),
    SlimeIdleBack(SpriteList.SlimeIdleSprite.sprite, 4, 6, 12, 6, 11, true),
    SlimeIdleLeft(SpriteList.SlimeIdleSprite.sprite, 4, 6, 12, 12, 17, true),
    SlimeIdleRight(SpriteList.SlimeIdleSprite.sprite, 4, 6, 12, 18, 23, true),

    SlimeRunFront(SpriteList.SlimeRunSprite.sprite, 4, 8, 18, 0, 7, true),
    SlimeRunBack(SpriteList.SlimeRunSprite.sprite, 4, 8, 18, 8, 15, true),
    SlimeRunLeft(SpriteList.SlimeRunSprite.sprite, 4, 8, 18, 16, 23, true),
    SlimeRunRight(SpriteList.SlimeRunSprite.sprite, 4, 8, 18, 24, 31, true),

    SlimeDeathFront(SpriteList.SlimeDeathSprite.sprite, 4, 10, 12, 0, 9, false),
    SlimeDeathBack(SpriteList.SlimeDeathSprite.sprite, 4, 10, 12, 10, 19, false),
    SlimeDeathLeft(SpriteList.SlimeDeathSprite.sprite, 4, 10, 12, 20, 29, false),
    SlimeDeathRight(SpriteList.SlimeDeathSprite.sprite, 4, 10, 12, 30, 39, false),

    SlimeAttackFront(SpriteList.SlimeAttackSprite.sprite, 4, 9, 10, 0, 8, false),
    SlimeAttackBack(SpriteList.SlimeAttackSprite.sprite, 4, 9, 10, 9, 17, false),
    SlimeAttackLeft(SpriteList.SlimeAttackSprite.sprite, 4, 9, 10, 18, 26, false),
    SlimeAttackRight(SpriteList.SlimeAttackSprite.sprite, 4, 9, 10, 27, 35, false),

    // Toxito Animation
    ToxitoIdleFront(SpriteList.ToxitoIdleSprite.sprite, 4, 6, 12, 0, 5, true),
    ToxitoIdleBack(SpriteList.ToxitoIdleSprite.sprite, 4, 6, 12, 6, 11, true),
    ToxitoIdleLeft(SpriteList.ToxitoIdleSprite.sprite, 4, 6, 12, 12, 17, true),
    ToxitoIdleRight(SpriteList.ToxitoIdleSprite.sprite, 4, 6, 12, 18, 23, true),


    ToxitoWalkFront(SpriteList.ToxitoWalkSprite.sprite, 4, 8, 18, 0, 7, true),
    ToxitoWalkBack(SpriteList.ToxitoWalkSprite.sprite, 4, 8, 18, 8, 15, true),
    ToxitoWalkLeft(SpriteList.ToxitoWalkSprite.sprite, 4, 8, 18, 16, 23, true),
    ToxitoWalkRight(SpriteList.ToxitoWalkSprite.sprite, 4, 8, 18, 24, 31, true),

    ToxitoDeathFront(SpriteList.ToxitoDeathSprite.sprite, 4, 10, 12, 0, 9, false),

    ToxitoAttackFront(SpriteList.ToxitoAttackSprite.sprite, 4, 11, 10, 0, 10, false),

    //other Animations
    ExamplePause(SpriteList.ExamplePauseSprite.sprite, 1, 1, 1, false),
    ExampleItem(SpriteList.ExampleItemSprite.sprite, 1, 1, 1, false),
    InventorySlot(SpriteList.InventorySlotSprite.sprite, 1, 1, 1, false),

    TestAbility(SpriteList.TestAbility.sprite,1,1,1,false),

    TestIcon(SpriteList.TestIcon.sprite, 1, 1, 1, false),
    ;

    public final int rows;
    public final int columns;
    public final int fps;
    public final int startFrame;
    public final int endFrame;
    public final Bitmap sprite;
    public final boolean isLooping;

    SpriteAnimationList(Bitmap bmp, int rows, int columns, int fps, boolean isLooping) {
        this(bmp, rows, columns, fps, 0, rows * columns - 1, isLooping); // Play full sheet by default
    }

    SpriteAnimationList(Bitmap bmp, int rows, int columns, int fps, int startFrame, int endFrame, boolean isLooping) {
        this.sprite = bmp;
        this.rows = rows;
        this.columns = columns;
        this.fps = fps;
        this.startFrame = startFrame;
        this.endFrame = endFrame;
        this.isLooping = isLooping;
    }
}
