package com.example.sampleapp.PowerUp;

public abstract class PowerUp {
    public int spriteID;
    protected PowerUp(int spriteID) {
        this.spriteID = spriteID;
    }

    public abstract void Use();
}
