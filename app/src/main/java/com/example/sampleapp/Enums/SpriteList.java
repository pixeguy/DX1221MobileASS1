package com.example.sampleapp.Enums;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.sampleapp.R;
import com.example.sampleapp.mgp2d.core.GameActivity;

public enum SpriteList {
    PlayerSprite(R.drawable.playersprite),


    // Projectiles
    PlayerMagicMissileSprite(R.drawable.player_fire_missle),
    EnemyFireMissileSprite(R.drawable.enemy_fire_missle),
    EnemyToxicMissileSprite(R.drawable.enemy_toxic_missle),
    //other Animations
    ExamplePause(R.drawable.pause),
    ExampleItem(R.drawable.splash),
    InventorySlot(R.drawable.inventoryslot),
    TestAbility(R.drawable.testbanner),
    TestIcon(R.drawable.testicon),
    MultiShotBanner(R.drawable.multishotabnner),
    MultiShotIcon(R.drawable.multishot),
    RearShotBanner(R.drawable.rearshotbanner),
    RearShotIcon(R.drawable.rearshot),
    MagicOrbBanner(R.drawable.magicorbbanner),
    MagicOrbIcon(R.drawable.magicshot),

    // Enemies

    SlimeIdleSprite(R.drawable.slime3_idle_full),
    SlimeRunSprite(R.drawable.slime3_run_full),
    SlimeDeathSprite(R.drawable.slime3_death_full),
    SlimeAttackSprite(R.drawable.slime3_attack_full),

    ToxitoIdleSprite(R.drawable.slime2_idle_full),
    ToxitoWalkSprite(R.drawable.slime2_walk_full),
    ToxitoDeathSprite(R.drawable.slime2_death_full),
    ToxitoAttackSprite(R.drawable.slime2_attack_full),

    GolemIdleSprite(R.drawable.golem3_idle_full),
    GolemWalkSprite(R.drawable.golem3_walk_full),
    GolemRunSprite(R.drawable.golem3_run_full),
    GolemDeathSprite(R.drawable.golem3_death_full),
    GolemAttackSprite(R.drawable.golem3_attack_full),

    // Background

    // UI
    ExamplePauseSprite(R.drawable.pause, 0, 0),
    ExampleItemSprite(R.drawable.splash, 0, 0),
    ExampleItem2Sprite(R.drawable.blank_bg, 0, 0),
    InventorySlotSprite(R.drawable.inventoryslot),
    BaseJoystickSprite(R.drawable.controllerposition),
    HandleJoystickSprite(R.drawable.controllerradius),

    RotateButtonSprite(R.drawable.rotate),
    CompleteButtonSprite(R.drawable.completebtn),
    MainMenuButtonSprite(R.drawable.mainmenu),
    GameOverScreen(R.drawable.gameover),
    EnemySpawnMarkerSprite(R.drawable.enemy_spawn_indicator),
    GrassBgSprite(R.drawable.grassbg),
    Crystal(R.drawable.crystal),
    CrystalBtn(R.drawable.crystalbtn),
    Potion(R.drawable.potion),
    PotionBtn(R.drawable.potionbtn),
    Sword(R.drawable.sword),
    SwordBtn(R.drawable.swordbtn),
    Staff(R.drawable.staff),
    StaffBtn(R.drawable.staffbtn),
    Teapot(R.drawable.teapot),
    TeapotBtn(R.drawable.teapotbtn),
    DashIcon(R.drawable.dash_icon)
    ;

    public final int spriteID;
    public Bitmap sprite;

    SpriteList(int spriteID) {
        this.spriteID = spriteID;
        this.sprite = BitmapFactory.decodeResource(GameActivity.instance.getResources(), spriteID);
    }

    SpriteList(int spriteID, int width, int height) {
        this.spriteID = spriteID;
        Bitmap bmp = BitmapFactory.decodeResource(GameActivity.instance.getResources(), spriteID);
        this.sprite = Bitmap.createScaledBitmap(bmp, bmp.getWidth(), bmp.getHeight(), false);
    }
}
