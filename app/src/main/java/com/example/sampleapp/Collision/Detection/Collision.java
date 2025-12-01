package com.example.sampleapp.Collision.Detection;

import android.util.Log;

import com.example.sampleapp.Collision.Colliders.BoxCollider2D;
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

        normal = body1.GetPosition().subtract(body2.GetPosition());
        normal.normalize();
        depth = sumRadius - distance;

        MTV.x = normal.x * depth;
        MTV.y = normal.y * depth;

        return true;
    }

    public static boolean AABB2AABB(BoxCollider2D body1, BoxCollider2D body2, Vector2 MTV) {

        if(!body1.getBound().intersect(body2.getBound())) return false;

        Vector2 min1 = new Vector2(body1.getBound().left, body1.getBound().top);
        Vector2 max1 = new Vector2(body1.getBound().right, body1.getBound().bottom);

        Vector2 min2 = new Vector2(body2.getBound().left, body2.getBound().top);
        Vector2 max2 = new Vector2(body2.getBound().right, body2.getBound().bottom);

        // Find min penetration distance
        float overlapX1 = max1.x - min2.x;
        float overlapX2 = max2.x - min1.x;
        float overlapY1 = max1.y - min2.y;
        float overlapY2 = max2.y - min1.y;
        float minPenetration = Math.min(Math.min(overlapX1, overlapX2), Math.min(overlapY1, overlapY2));

        // Calculate MTV direction and magnitude
        if (minPenetration == overlapX1) {
            MTV.x = -overlapX1;
            MTV.y = 0;
        } else if (minPenetration == overlapX2) {
            MTV.x = overlapX2;
            MTV.y = 0;
        } else if (minPenetration == overlapY1) {
            MTV.x = 0;
            MTV.y = -overlapY1;
        } else {
            MTV.x = 0;
            MTV.y = overlapY2;
        }

        return true;
    }

    public static boolean Circle2AABB(CircleCollider2D body1, BoxCollider2D body2, Vector2 MTV) {

        Vector2 circleCenter = body1.GetPosition();
        float radius = body1.radius;

        Vector2 boxMin = new Vector2(body2.getBound().left, body2.getBound().top);
        Vector2 boxMax = new Vector2(body2.getBound().right, body2.getBound().bottom);

        // Find the closest point on the box to the circle center
        float closestX = Math.max(boxMin.x, Math.min(circleCenter.x, boxMax.x));
        float closestY = Math.max(boxMin.y, Math.min(circleCenter.y, boxMax.y));

        // Vector from closest point to circle center
        Vector2 delta = new Vector2(circleCenter.x - closestX, circleCenter.y - closestY);

        float distanceSquared = delta.getMagnitudeSquared();
        float radiusSquared = radius * radius;

        // Check for intersection
        if (distanceSquared > radiusSquared) {
            return false;
        }

        float distance = (float)Math.sqrt(distanceSquared);

        // Compute MTV
        if (distance == 0) {
            // Circle center is inside box — choose shortest push direction
            float left = circleCenter.x - boxMin.x;
            float right = boxMax.x - circleCenter.x;
            float bottom = circleCenter.y - boxMin.y;
            float top = boxMax.y - circleCenter.y;

            float min = Math.min(Math.min(left, right), Math.min(bottom, top));

            if (min == left) {
                MTV.x = -min;
                MTV.y = 0;
            } else if (min == right) {
                MTV.x = min;
                MTV.y = 0;
            } else if (min == bottom) {
                MTV.x = 0;
                MTV.y = -min;
            } else {
                MTV.x = 0;
                MTV.y = min;
            }
        } else {
            // Normalized collision direction
            float nx = delta.x / distance;
            float ny = delta.y / distance;
            float penetration = radius - distance;

            MTV.x = nx * penetration;
            MTV.y = ny * penetration;
        }

        return true;
    }

    public static boolean AABB2Circle(BoxCollider2D body1, CircleCollider2D body2, Vector2 MTV) {
        boolean temp = Circle2AABB(body2, body1, MTV);
        MTV.x *= -1;
        MTV.y *= -1;

        return temp;
    }


    public static boolean CollisionDetection(Collider2D body1, Collider2D body2, Vector2 MTV) {
        int total_vertices = body1.numVertices + body2.numVertices;

        if(total_vertices == 2)
            return OverlapCircle2Circle((CircleCollider2D)body1, (CircleCollider2D)body2, MTV);
        else if(body1.numVertices == 1)
            return Circle2AABB((CircleCollider2D)body1, (BoxCollider2D)body2, MTV);
        else if(body2.numVertices == 1)
            return AABB2Circle((BoxCollider2D)body1, (CircleCollider2D)body2, MTV);

        return AABB2AABB((BoxCollider2D) body1, (BoxCollider2D) body2, MTV);
    }

    public static void ResolveCollision(PhysicsManifold contact) {
        if(contact.colliderA.isTrigger || contact.colliderB.isTrigger) return;

        float amountToMoveX = contact.normal.x * contact.depth;
        float amountToMoveY = contact.normal.y * contact.depth;

        if(contact.colliderA.gameEntity.mass > 0) {
            contact.colliderA.gameEntity._position.x += amountToMoveX / 2.0f;
            contact.colliderA.gameEntity._position.y += amountToMoveY / 2.0f;
        }

        if(contact.colliderB.gameEntity.mass > 0) {
            contact.colliderB.gameEntity._position.x -= amountToMoveX / 2.0f;
            contact.colliderB.gameEntity._position.y -= amountToMoveY / 2.0f;
        }
    }
}
