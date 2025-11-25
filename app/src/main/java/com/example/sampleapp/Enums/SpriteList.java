package com.example.sampleapp.Enums;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.sampleapp.R;
import com.example.sampleapp.mgp2d.core.GameActivity;

public enum SpriteList {
    PlayerSprite(R.drawable.playersprite),
    PlayerMagicMissileSprite(R.drawable.player_fire_missle),

    SlimeIdleSprite(R.drawable.slime3_idle_full),
    SlimeRunSprite(R.drawable.slime3_run_full),
    SlimeDeathSprite(R.drawable.slime3_death_full),
    SlimeAttackSprite(R.drawable.slime3_attack_full),

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
