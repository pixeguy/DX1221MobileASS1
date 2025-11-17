package com.example.sampleapp;

import android.util.Log;

import com.example.sampleapp.mgp2d.core.Vector2;

public class LootButtonObj extends ButtonObj {
    public LootType loot;

    public void onCreate(Vector2 pos, Vector2 scale, SpriteList animSprite, LootType loot) {
        super.onCreate(pos, scale, animSprite);
        this.loot = loot;
    }

    public void OnClick(Vector2 pos) {
        super.OnClick();
        Log.d("button checker", "HEY CLICKED");
    }
}
