package com.example.sampleapp;

import com.example.sampleapp.mgp2d.core.Vector2;

public class LootButtonObj extends ButtonObj {
    public LootType loot;

    public void onCreate(Vector2 pos, Vector2 scale, int spriteID, LootType loot) {
        super.onCreate(pos, scale, spriteID);
        this.loot = loot;
    }

    public void OnClick(Vector2 pos) {
        super.OnClick();
    }
}
