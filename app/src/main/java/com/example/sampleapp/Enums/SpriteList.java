package com.example.sampleapp.Enums;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.sampleapp.R;
import com.example.sampleapp.mgp2d.core.GameActivity;

public enum SpriteList {
    PlayerSprite(R.drawable.playersprite),

    // Projectiles
    PlayerMagicMissileSprite(R.drawable.player_fire_missle),
    EnemyFireMissileSprite(R.drawable.enemy_fire_missle),
    EnemyToxicMissileSprite(R.drawable.enemy_toxic_missle),

    // Enemies

    SlimeIdleSprite(R.drawable.slime3_idle_full),
    SlimeRunSprite(R.drawable.slime3_run_full),
    SlimeDeathSprite(R.drawable.slime3_death_full),
    SlimeAttackSprite(R.drawable.slime3_attack_full),

    ToxitoIdleSprite(R.drawable.slime2_idle_full),
    ToxitoWalkSprite(R.drawable.slime2_walk_full),
    ToxitoDeathSprite(R.drawable.slime2_death_full),
    ToxitoAttackSprite(R.drawable.slime2_attack_full),

    // UI
    ExamplePauseSprite(R.drawable.pause),
    ExampleItemSprite(R.drawable.splash),
    InventorySlotSprite(R.drawable.inventoryslot)

    ;

    public final int spriteID;
    public final Bitmap sprite;

    SpriteList(int spriteID) {
        this.spriteID = spriteID;
        this.sprite = BitmapFactory.decodeResource(GameActivity.instance.getResources(), spriteID);
    }
}
