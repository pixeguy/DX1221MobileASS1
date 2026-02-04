package com.example.sampleapp.Enums;

import android.content.Context;
import android.media.SoundPool;

import com.example.sampleapp.R;

public enum SoundList {
    Bgm(R.raw.bgm,false),
    PlayerShoot(R.raw.shoot,true),
    PlayerDamage(R.raw.damage, true),
    PlayerCompleteLevel(R.raw.level_complete, true),
    PlayerFailLevel(R.raw.level_fail, true),
    PlayerDie(R.raw.die, true),
    PlayerDash(R.raw.dash, true),
    EnemyHit(R.raw.enemy_hit, true),
    EnemyShot(R.raw.enemy_shot, true),
    Slime_Attack(R.raw.slime_attack, true),
    Button_Click(R.raw.button_click, true),
    Click(R.raw.click, true),
    Drop_Loot(R.raw.drop_item_slot, true),
    ;
    public int resourceID;
    public int soundID;
    public boolean loaded;
    public boolean toLoad;

    SoundList(int resourceID, boolean toLoad)
    {
        this.toLoad = toLoad;
        this.loaded = false;
        this.resourceID = resourceID;
    }

    public void load(SoundPool pool, Context ctx)
    {
        if (!toLoad) return;
        soundID = pool.load(ctx, resourceID, 1);
    }

    public void markLoaded(int sampleID) {
        if (!toLoad) return;
        if (sampleID == soundID) loaded = true;
    }
}
