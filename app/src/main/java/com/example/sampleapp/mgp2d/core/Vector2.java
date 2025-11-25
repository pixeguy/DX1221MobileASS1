package com.example.sampleapp.mgp2d.core;

import static java.lang.Math.sqrt;

import androidx.annotation.NonNull;

public class Vector2 {
    public float x, y;

    public Vector2(float x, float y) { this.x = x; this.y = y; }
    public Vector2(Vector2 other) { this(other.x, other.y); }

    public Vector2 add(Vector2 b) { return new Vector2(x + b.x, y + b.y); }

    public Vector2 multiply(float scale) { return new Vector2(x * scale, y * scale); }
    public void multiply(Vector2 b) {
        this.x *= b.x;
        this.y *= b.y;
    }

    public Vector2 subtract(Vector2 b) { return this.add(b.multiply(-1)); }

    public float getMagnitude() { return (float)sqrt(x * x + y * y); }

    public float getMagnitudeSquared() { return x * x + y * y; }

    public Vector2 normalize() { return new Vector2(x /= getMagnitude(), y /= getMagnitude()); }

    public Vector2 copy() { return new Vector2(x, y); }

    public Vector2 set(float x, float y) { this.x = x; this.y = y; return this; }

    public Vector2 set(Vector2 other) { return this.set(other.x, other.y); }

    public float dot(Vector2 b) { return x * b.x + y * b.y; }

    public boolean equals(float x, float y) { return this.x == x && this.y == y; }
    public boolean equals(Vector2 other) { return x == other.x && y == other.y; }

    public Vector2 limit(float maxMagnitude) {
        float length = getMagnitude();
        float multiplier = length <= maxMagnitude ? 1 : maxMagnitude / length;
        return new Vector2(x * multiplier, y * multiplier);
    }

    public static float Angle(Vector2 currentDir, Vector2 targetDir) {
        // Normalize both direction vectors
        currentDir.normalize();
        targetDir.normalize();

        // Convert both to angles in radians
        float angleCurrent = (float) Math.atan2(currentDir.y, currentDir.x);
        float angleTarget  = (float) Math.atan2(targetDir.y, targetDir.x);

        // Signed difference
        float diff = angleTarget - angleCurrent;

        // Normalize to (-π, π)
        diff = (float) ((diff + Math.PI) % (2 * Math.PI));
        if (diff > Math.PI) diff -= (float) (2 * Math.PI);

        return diff; // radians; positive = CCW, negative = CW
    }

    public float Distance(Vector2 rhs) {
        return (this.subtract(rhs)).getMagnitude();
    }

    @NonNull
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
