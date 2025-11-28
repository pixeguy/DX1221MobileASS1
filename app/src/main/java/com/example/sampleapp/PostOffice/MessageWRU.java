package com.example.sampleapp.PostOffice;

import com.example.sampleapp.mgp2d.core.GameEntity;

public class MessageWRU extends Message{
    public enum SEARCH_TYPE {
        SEARCH_NONE,
        NEAREST_ENEMY
    }

    public MessageWRU(GameEntity go, SEARCH_TYPE searchType, float threshold) {
        this.searchType = searchType;
        this.go = go;
        this.threshold = threshold;
    }

    public SEARCH_TYPE searchType;
    public GameEntity go;
    public float threshold;
}
