package com.example.sampleapp.UI.Buttons;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.example.sampleapp.UI.UIElement;
import com.example.sampleapp.Utilities.Utilities;
import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.Vector2;

@SuppressWarnings("FieldCanBeLocal")
public class UIJoystick extends UIElement {
    // Joystick Properties
    private float handleRadius = 2f;
    private final Vector2 handlePos; // relative offset from center
    private final Vector2 output;    // normalized
    private boolean isDragging = false;

    private Bitmap handleSprite  = null;
    private final int baseColor = Color.DKGRAY;
    private final int handleColor = Color.LTGRAY;
    private final Vector2 _originalPos = new Vector2(0, 0);

    public UIJoystick(Vector2 center, float baseRadius, float handleRadius) {
        super(center, baseRadius);
        _originalPos.set(center.x, center.y);
        this.handleRadius = handleRadius;
        this.handlePos = new Vector2(center.x, center.y);
        this.output = new Vector2(0, 0);
    }

    public void setSprites(Bitmap base, Bitmap handle) {
        this.baseSprite = base;
        this.handleSprite = handle;
    }

    public Vector2 getOutput() {
        return output;
    }

    @Override
    public void onUpdate(float dt) {
        if (!isDragging) {
            // Slowly reset handle to center
            handlePos.x = Utilities.lerp(handlePos.x, _originalPos.x, 10f * dt);
            handlePos.y = Utilities.lerp(handlePos.y, _originalPos.y, 10f * dt);
            output.x *= 0.9f;
            output.y *= 0.9f;
        }
    }

    @Override
    public void onRender(Canvas canvas) {
        if (!visible) return;

        RectF rect = getTransformedBounds();
        float baseR = radius * scale.x;
        float handleR = handleRadius * scale.x;

        // Draw base
        if (baseSprite != null)
            canvas.drawBitmap(baseSprite, null, rect, paint);
        else {
            paint.setColor(baseColor);
            canvas.drawCircle(rect.centerX(), rect.centerY(), baseR, paint);
        }

        // Draw handle
        float hx = handlePos.x;
        float hy = handlePos.y;
        if (handleSprite != null)
            canvas.drawBitmap(handleSprite, null, new RectF(hx - handleR, hy - handleR, hx + handleR, hy + handleR), paint);
        else {
            paint.setColor(handleColor);
            canvas.drawCircle(hx, hy, handleR, paint);
        }
    }

    @Override
    public void onTouchDown(float x, float y) {
        if (isPointInside(x, y)) {
            isDragging = true;
            updateHandle(x, y);
        }
        else {
            isDragging = true;
            position.set(x, y);
            handlePos.set(x, y);
            updateHandle(x, y);
        }
    }

    @Override
    public void onTouchMove(float x, float y) {
        if (isDragging) updateHandle(x, y);
    }

    @Override
    public void onTouchUp() {
        isDragging = false;
        output.set(0, 0);
        position.set(_originalPos.x, _originalPos.y);
        handlePos.set(_originalPos.x, _originalPos.y);
    }

    private void updateHandle(float px, float py) {
        Vector2 center = getCenter();
        float dx = px - center.x;
        float dy = py - center.y;
        float dist = (float)Math.sqrt(dx * dx + dy * dy);

        float maxDist = radius - handleRadius;
        if (dist > maxDist) {
            dx = dx / dist * maxDist;
            dy = dy / dist * maxDist;
        }

        handlePos.x = center.x + dx;
        handlePos.y = center.y + dy;

        float angle = Utilities.cal_angle(dx, dy);
        output.set(get8Direction(angle).normalize());
    }

    private Vector2 getCenter() {
        RectF rect = getTransformedBounds();
        return new Vector2(rect.centerX(), rect.centerY());
    }

    public Vector2 get8Direction(float angle) {
        if(angle >= 247.5 && angle < 292.5 ) {
            return new Vector2(0, -1);
        } else if(angle >= 292.5 && angle < 337.5 ) {
            return new Vector2(1, -1);
        } else if(angle >= 337.5 || angle < 22.5 ) {
            return new Vector2(1, 0);
        } else if(angle >= 22.5 && angle < 67.5 ) {
            return new Vector2(1, 1);
        } else if(angle >= 67.5 && angle < 112.5 ) {
            return new Vector2(0, 1);
        } else if(angle >= 112.5 && angle < 157.5 ) {
            return new Vector2(-1, 1);
        } else if(angle >= 157.5 && angle < 202.5 ) {
            return new Vector2(-1, 0);
        } else if(angle >= 202.5 && angle < 247.5 ) {
            return new Vector2(-1, -1);
        }
        return new Vector2(0, 0);
    }
}
