package com.example.sampleapp.PowerUp;

import android.graphics.Bitmap;

import com.example.sampleapp.Entity.Player.PlayerObj;

public class SpeedBoostPU extends PowerUp{

    public SpeedBoostPU(int spriteID) {
        super(spriteID);
    }

    @Override
    public void Use() {
        PlayerObj.getInstance().speedMultiplier = 1.20f;
    }
}
