package com.example.sampleapp.UI.Text;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;

import androidx.core.content.res.ResourcesCompat;

import com.example.sampleapp.R;
import com.example.sampleapp.UI.UIElement;
import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.Vector2;

public class UIDamageText extends UIElement {
    private String text;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float lifetime = 1.0f;    // seconds total
    private float elapsed = 0f;
    private float riseSpeed = 100f;   // pixels per second
    private float alpha = 1f;

    private Vector2 velocity = new Vector2(0, -1); // move upward
    private float startY;

    public void setColor(int color) {
        paint.setColor(color);
    }

    public void setTextSize(float size) {
        paint.setTextSize(size);
    }

    public void setVelocity(Vector2 vel) {
        velocity = vel;
    }

    public boolean isExpired() {
        return elapsed >= lifetime;
    }

    public UIDamageText(String text, float x, float y) {
        super(x, y, 0, 0);
        this.text = text;

        Typeface font = ResourcesCompat.getFont(GameActivity.instance, R.font.alagard);
        paint.setTypeface(font);
        paint.setTextSize(48f);
        paint.setColor(Color.YELLOW);
        paint.setTextAlign(Paint.Align.CENTER);

        startY = y;
    }

    @Override
    public void onUpdate(float dt) {
        elapsed += dt;

        // Move upward
        bounds.offset(velocity.x * dt * riseSpeed, velocity.y * dt * riseSpeed);

        // Fade out
        alpha = 1f - (elapsed / lifetime);
        paint.setAlpha((int)(255 * alpha));
    }

    @Override
    public void onRender(Canvas canvas) {
        if (!visible) return;
        if (alpha <= 0f) return;

        RectF rect = getTransformedBounds();
        canvas.drawText(text, rect.centerX(), rect.centerY(), paint);
    }
}
