package com.example.sampleapp.Enums;

public enum AbilityList {
    RearArrow("Rear Arrow", "Adds 1 backward-firing arrow to your attacks."),
    FireCircle("Fire Circle", "Receive 2 circling Fire orbs that deal 1.25x Attack Power as damage per hit and apply the Blaze effect."),
    Multishot("Multishot", "Adds 1 addition shooting projectile to your attacks."),
    ;
    public final String name;
    public final String description;

    AbilityList(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
