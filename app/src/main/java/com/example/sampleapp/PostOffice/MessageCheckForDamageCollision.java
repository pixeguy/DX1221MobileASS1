package com.example.sampleapp.PostOffice;

import com.example.sampleapp.mgp2d.core.GameEntity;

public class MessageCheckForDamageCollision extends Message{
    public GameEntity gameEntityToCheck;

    public MessageCheckForDamageCollision(GameEntity gameEntityToCheck) {
        this.gameEntityToCheck = gameEntityToCheck;
    }
}
