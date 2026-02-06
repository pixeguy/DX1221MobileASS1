package com.example.sampleapp.PowerUp;

import android.graphics.Bitmap;

import com.example.sampleapp.Entity.Player.PlayerObj;

public class DefensePU extends PowerUp {

    public DefensePU(int spriteID) {
        super(spriteID);
    }

    @Override
    public void Use() {
        PlayerObj.getInstance().defenseMultiplier = 1.25f;
    }
}
