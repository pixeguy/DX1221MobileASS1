package com.example.sampleapp.Enums;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.sampleapp.R;
import com.example.sampleapp.mgp2d.core.GameActivity;

public enum PowerUpList {
    SHIELD("Shield", "Protects you from one hit.", 80, R.drawable.shield),
    SPEED_BOOST("Speed Boost", "Move 20% faster for 1 game.", 30, R.drawable.speed),
    DOUBLE_COINS("Double Coin", "Earn 2x coins for 1 game.", 200, R.drawable.doublecoin),
    STRENGTH_BOOST("Strength Boost", "Increase 15% strength for 1 game.", 35, R.drawable.strength),
    DEFENCE_BOOST("Defence Boost", "Increase 25% defence for 1 game.", 55, R.drawable.defense),
    ;


    private final String name;
    private final String description;
    private final int price;
    private final int iconRes;
    PowerUpList(String name, String description, int price, int iconRes) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.iconRes = iconRes;
    }

    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getPrice() { return price; }
    public int getIconRes() { return iconRes; }
}
