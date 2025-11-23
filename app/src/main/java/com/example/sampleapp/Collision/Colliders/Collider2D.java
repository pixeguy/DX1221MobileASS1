package com.example.sampleapp.Collision.Colliders;

import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

public class Collider2D {

    public enum SHAPE_TYPE {
        RECTANGLE,
        CIRCLE
    };

    public SHAPE_TYPE shapeType;

    public GameEntity gameEntity;
    public boolean isEnable = true;
    public boolean isTrigger = false;

    public int numVertices = 0;

    protected Collider2D(GameEntity gameEntity) {
        this.gameEntity = gameEntity;
    }

    public Vector2 GetPosition() {
        return gameEntity._position;
    }
}
