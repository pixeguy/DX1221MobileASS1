package com.example.sampleapp.Collision.Detection;

import android.util.Log;

import com.example.sampleapp.Collision.Colliders.CircleCollider2D;
import com.example.sampleapp.Collision.Colliders.Collider2D;
import com.example.sampleapp.mgp2d.core.Vector2;

public class Collision {
    public static boolean OverlapCircle2Circle(CircleCollider2D body1, CircleCollider2D body2, Vector2 MTV) {
        Vector2 normal;
        float depth;

        float distance = body1.GetPosition().Distance(body2.GetPosition());
        float sumRadius = body1.radius + body2.radius;
        if (distance > sumRadius) {
            return false;
        }
        Log.d("Distance", String.valueOf(distance));

        normal = body1.GetPosition().subtract(body2.GetPosition());
        normal.normalize();
        depth = sumRadius - distance;

        MTV.x = normal.x * depth;
        MTV.y = normal.y * depth;

        return true;
    }

    public static boolean CollisionDetection(Collider2D body1, Collider2D body2, Vector2 MTV) {
        int total_vertices = body1.numVertices + body2.numVertices;

        if(total_vertices == 2)
            return OverlapCircle2Circle((CircleCollider2D)body1, (CircleCollider2D)body2, MTV);

        return false;
    }

    public static void ResolveCollision(PhysicsManifold contact) {
        if(contact.colliderA.isTrigger && contact.colliderB.isTrigger) return;

        float amountToMoveX = contact.normal.x * contact.depth;
        float amountToMoveY = contact.normal.y * contact.depth;

        contact.colliderA.gameEntity._position.x += amountToMoveX / 2.0f;
        contact.colliderA.gameEntity._position.y += amountToMoveY / 2.0f;

        contact.colliderB.gameEntity._position.x -= amountToMoveX / 2.0f;
        contact.colliderB.gameEntity._position.y -= amountToMoveY / 2.0f;

    }
}
