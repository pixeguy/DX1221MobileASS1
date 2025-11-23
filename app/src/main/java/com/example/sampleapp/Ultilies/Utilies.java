package com.example.sampleapp.Ultilies;

public class Utilies {
    public static float lerp(float a, float b, float t, float duration) {
        t = Math.max(0, Math.min(1, t / duration));
        return a + (b - a) * t;
    }

    public static float lerp(float a, float b, float t) {
        if(a > b)
            return b;
        return a + (b - a) * t;
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
}
