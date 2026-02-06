package com.example.sampleapp.PowerUp;

import android.graphics.Bitmap;

import com.example.sampleapp.Entity.Player.PlayerObj;

public class ShieldPU extends PowerUp{


    public ShieldPU(int spriteID) {
        super(spriteID);
    }

    @Override
    public void Use() {
        PlayerObj.getInstance().shieldActivate = true;
    }
}
