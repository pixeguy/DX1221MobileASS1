package com.example.sampleapp.PowerUp;

import android.graphics.Bitmap;

import com.example.sampleapp.Entity.Player.PlayerObj;

public class DoubleCoinPU extends PowerUp{

    public DoubleCoinPU(int spriteId) {
        super(spriteId);
    }

    @Override
    public void Use() {
        PlayerObj.getInstance().coinMultiplier = 2.0f;
    }
}
