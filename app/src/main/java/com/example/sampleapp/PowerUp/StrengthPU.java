package com.example.sampleapp.PowerUp;

import android.graphics.Bitmap;

import com.example.sampleapp.Entity.Player.PlayerObj;

public class StrengthPU extends PowerUp{

    public StrengthPU(int spriteID) {
        super(spriteID);
    }

    @Override
    public void Use() {
        PlayerObj.getInstance().strengthMultiplier = 1.15f;
    }
}
