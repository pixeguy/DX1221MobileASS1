package com.example.sampleapp.Collision.Colliders;

import android.graphics.Rect;
import android.graphics.RectF;

import com.example.sampleapp.mgp2d.core.GameEntity;

public class BoxCollider2D extends Collider2D{
    private final RectF bound;
    public float width = 0.0f;
    public float height = 0.0f;

    public BoxCollider2D(GameEntity gameEntity, float width, float height) {
        super(gameEntity);
        this.width = width;
        this.height = height;
        bound = new RectF();
        numVertices = 4;
    }

    public RectF getBound() {
        float left = gameEntity._position.x - width / 2.0f;
        float top = gameEntity._position.y - height / 2.0f;
        bound.set(left, top, left + width, top + height);
        return bound;
    }
}
