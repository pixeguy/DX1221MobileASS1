package com.example.sampleapp.Collision.Detection;

import com.example.sampleapp.Collision.Colliders.Collider2D;
import com.example.sampleapp.mgp2d.core.Vector2;

public class PhysicsManifold {
    Collider2D colliderA = null;
    Collider2D colliderB = null;
    Vector2 normal = null;
    float depth = 0;

    public PhysicsManifold(Collider2D colliderA, Collider2D colliderB,
                           Vector2 normal, float depth) {
        this.colliderA = colliderA;
        this.colliderB = colliderB;
        this.normal = normal;
        this.depth = depth;
    }
}
