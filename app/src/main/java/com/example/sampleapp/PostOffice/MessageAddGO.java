package com.example.sampleapp.PostOffice;

import com.example.sampleapp.mgp2d.core.GameEntity;

public class MessageAddGO extends Message{
    public GameEntity go;

    public MessageAddGO(GameEntity go) {
        this.go = go;
    }
}
