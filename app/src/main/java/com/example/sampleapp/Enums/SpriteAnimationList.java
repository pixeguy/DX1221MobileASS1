package com.example.sampleapp.Enums;

import android.graphics.Bitmap;

import com.example.sampleapp.R;

public enum SpriteAnimationList {
    //PlayerAnimations
    PlayerIdle(SpriteList.PlayerSprite.sprite, 1, 1, 1, 0, 0, false),

    PlayerForward(SpriteList.PlayerSpriteSheet.sprite, 8,24,12,0,24,true),
    PlayerUpRight(SpriteList.PlayerSpriteSheet.sprite, 8,24,12,25,48,true),
    PlayerRight(SpriteList.PlayerSpriteSheet.sprite, 8,24,12,49,72,true),
    PlayerDownRight(SpriteList.PlayerSpriteSheet.sprite, 8,24,12,73,96,true),
    PlayerDown(SpriteList.PlayerSpriteSheet.sprite, 8,24,12,97,121,true),
    PlayerDownLeft(SpriteList.PlayerSpriteSheet.sprite, 8,24,12,122,144,true),
    PlayerLeft(SpriteList.PlayerSpriteSheet.sprite, 8,24,12,145,168,true),
    PlayerUpLeft(SpriteList.PlayerSpriteSheet.sprite, 8,24,12,169,192,true),

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

    GolemIdleFront(SpriteList.GolemIdleSprite.sprite, 4, 4, 12, 0, 3, true),
    GolemIdleBack(SpriteList.GolemIdleSprite.sprite, 4, 4, 12, 4, 7, true),
    GolemIdleLeft(SpriteList.GolemIdleSprite.sprite, 4, 4, 12, 8, 11, true),
    GolemIdleRight(SpriteList.GolemIdleSprite.sprite, 4, 4, 12, 12, 15, true),

    GolemWalkFront(SpriteList.GolemWalkSprite.sprite, 4, 8, 12, 0, 7, true),
    GolemWalkBack(SpriteList.GolemWalkSprite.sprite, 4, 8, 12, 8, 15, true),
    GolemWalkLeft(SpriteList.GolemWalkSprite.sprite, 4, 8, 12, 16, 23, true),
    GolemWalkRight(SpriteList.GolemWalkSprite.sprite, 4, 8, 12, 24, 31, true),

    GolemRunFront(SpriteList.GolemRunSprite.sprite, 4, 8, 12, 0, 7, true),
    GolemRunBack(SpriteList.GolemRunSprite.sprite, 4, 8, 12, 8, 15, true),
    GolemRunLeft(SpriteList.GolemRunSprite.sprite, 4, 8, 12, 16, 23, true),
    GolemRunRight(SpriteList.GolemRunSprite.sprite, 4, 8, 12, 24, 31, true),

    GolemDeathFront(SpriteList.GolemDeathSprite.sprite, 4, 8, 12, 0, 7, false),
    GolemDeathBack(SpriteList.GolemDeathSprite.sprite, 4, 8, 12, 8, 15, false),
    GolemDeathLeft(SpriteList.GolemDeathSprite.sprite, 4, 8, 12, 16, 23, false),
    GolemDeathRight(SpriteList.GolemDeathSprite.sprite, 4, 8, 12, 24, 31, false),

    GolemSlamAttackFront(SpriteList.GolemAttackSprite.sprite, 4, 9, 12, 0, 8, false),
    GolemSlamAttackBack(SpriteList.GolemAttackSprite.sprite, 4, 9, 12, 9, 17, false),
    GolemSlamAttackLeft(SpriteList.GolemAttackSprite.sprite, 4, 9, 12, 18, 26, false),
    GolemSlamAttackRight(SpriteList.GolemAttackSprite.sprite, 4, 9, 12, 27, 35, false),

    GolemSpinAttackFront(SpriteList.GolemAttackSprite.sprite, 4, 9, 6, 0, 1, false),

    //other Animations
    ExamplePause(SpriteList.ExamplePauseSprite.sprite, 1, 1, 1, false),
    ExampleItem(SpriteList.ExampleItemSprite.sprite, 1, 1, 1, 0, 0, false),
    Example2Item(SpriteList.ExampleItem2Sprite.sprite, 1, 1, 1, 0, 0, false),
    InventorySlot(SpriteList.InventorySlotSprite.sprite, 1, 1, 1, false),

    TestAbility(SpriteList.TestAbility.sprite,1,1,1,false),

    TestIcon(SpriteList.TestIcon.sprite, 1, 1, 1, false),

    MultiShotBanner(SpriteList.MultiShotBanner.sprite,1,1,1,false),
    MultiShotIcon(SpriteList.MultiShotIcon.sprite, 1, 1, 1, false),
    RearShotBanner(SpriteList.RearShotBanner.sprite,1,1,1,false),
    RearShotIcon(SpriteList.RearShotIcon.sprite, 1, 1, 1, false),
    MagicOrbBanner(SpriteList.MagicOrbBanner.sprite,1,1,1,false),
    MagicOrbIcon(SpriteList.MagicOrbIcon.sprite, 1, 1, 1, false),

    RotateBtn(SpriteList.RotateButtonSprite.sprite, 1,1,1,0,0,false),
    CompleteBtn(SpriteList.CompleteButtonSprite.sprite, 1,1,1,0,0,false),
    MainMenuBtn(SpriteList.MainMenuButtonSprite.sprite, 1,1,1,0,0,false),
    GameOverScreen(SpriteList.GameOverScreen.sprite, 1,1,1,0,0,false),

    EnemySpawnMarker(SpriteList.EnemySpawnMarkerSprite.sprite, 1, 1, 1, false),
    Crystal(SpriteList.Crystal.sprite, 1, 1, 1, false),
    CrystalBtn(SpriteList.CrystalBtn.sprite, 1, 1, 1, false),
    Potion(SpriteList.Potion.sprite, 1, 1, 1, false),
    PotionBtn(SpriteList.PotionBtn.sprite, 1, 1, 1, false),
    Sword(SpriteList.Sword.sprite, 1, 1, 1, false),
    SwordBtn(SpriteList.SwordBtn.sprite, 1, 1, 1, false),
    Staff(SpriteList.Staff.sprite, 1, 1, 1, false),
    StaffBtn(SpriteList.StaffBtn.sprite, 1, 1, 1, false),
    Teapot(SpriteList.Teapot.sprite, 1, 1, 1, false),
    TeapotBtn(SpriteList.TeapotBtn.sprite, 1, 1, 1, false),

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
