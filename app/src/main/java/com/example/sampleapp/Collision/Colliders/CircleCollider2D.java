package com.example.sampleapp.Collision.Colliders;

import com.example.sampleapp.mgp2d.core.GameEntity;

public class CircleCollider2D extends Collider2D{

    public float radius;

    protected CircleCollider2D(GameEntity gameEntity) {
        super(gameEntity);
        numVertices = 1;
        shapeType = SHAPE_TYPE.CIRCLE;
    }

    public CircleCollider2D(GameEntity gameEntity, float radius) {
        this(gameEntity);
        this.radius = radius;
    }
}
