package com.example.sampleapp.Entity.Buttons;

import android.util.Log;

import com.example.sampleapp.Enums.LootType;
import com.example.sampleapp.Enums.SpriteList;
import com.example.sampleapp.mgp2d.core.Vector2;

public class LootButtonObj extends ButtonObj {
    public LootType loot;
    public boolean used = false;

    public void onCreate(Vector2 pos, Vector2 scale, SpriteList animSprite, LootType loot) {
        super.onCreate(pos, scale, animSprite);
        this.loot = loot;
    }

    public void OnClick(Vector2 pos) {
        super.OnClick();
    }
}
