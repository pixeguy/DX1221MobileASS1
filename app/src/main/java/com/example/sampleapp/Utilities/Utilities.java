package com.example.sampleapp.Utilities;

import com.example.sampleapp.mgp2d.core.Vector2;

public class Utilities {
    public static float lerp(float a, float b, float t, float duration) {
        t = Math.max(0, Math.min(1, t / duration));
        return a + (b - a) * t;
    }

    public static float lerp(float a, float b, float t) {
        if(a > b)
            return b;
        return a + (b - a) * t;
    }

    public static float cal_angle(float x, float y) {
        float degrees = (float) Math.toDegrees(Math.atan(y / x));
        if(x >= 0 && y >= 0)
            return degrees;
        else if(x < 0 && y >= 0)
            return degrees + 180;
        else if(x < 0 && y < 0)
            return degrees + 180;
        else if(x >= 0 && y < 0)
            return degrees + 360;
        return 0;
    }

    public static float rotateTowardsAngle(float currentAngle, float targetAngle,
                                           float turnRate, float deltaTime) {
        // Compute shortest signed angle difference (-π, π)
        float diff = targetAngle - currentAngle;
        diff = (float)((diff + Math.PI) % (2 * Math.PI));
        if (diff > Math.PI) diff -= (float)(2 * Math.PI);

        // Clamp how much we can rotate this frame
        float maxStep = turnRate * deltaTime;
        float step = Math.max(-maxStep, Math.min(maxStep, diff));

        // Return new angle
        return currentAngle + step;
    }

    public static Vector2 get4Direction(float angle) {
        // Up, down left right
        if(angle >= 225 && angle < 315 ) {
            return new Vector2(0, 1);
        } else if(angle >= 315 || angle < 45 ) {
            return new Vector2(-1, 0);
        } else if(angle >= 45 && angle < 135 ) {
            return new Vector2(0, -1);
        } else if(angle >= 135 && angle < 225 ) {
            return new Vector2(1, 0);
        }
        return new Vector2(0, 0);
    }
}
