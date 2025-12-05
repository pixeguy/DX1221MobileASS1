package com.example.sampleapp.UI.Buttons;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;

import androidx.core.content.res.ResourcesCompat;

import com.example.sampleapp.Enums.SpriteList;
import com.example.sampleapp.R;
import com.example.sampleapp.UI.UIElement;
import com.example.sampleapp.UI.UIIcon;
import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.Vector2;

public class UIButton extends UIElement {
    private String label = "";
    private UIIcon btnIcon = null;
    private final Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float animScale = 1f;
    private float targetScale = 1f;

    private boolean pressed = false;
    private boolean hovered = false;

    private Runnable onClick;
    private Runnable onHover;
    private Runnable onRelease;

    public UIButton(float x, float y, float width, float height, String label) {
        super(x, y, width, height);
        this.label = label;
        paint.setColor(Color.GRAY);
        Typeface font = ResourcesCompat.getFont(GameActivity.instance, R.font.alagard);
        paintText.setTypeface(font);
        paintText.setColor(Color.WHITE);
        paintText.setTextAlign(Paint.Align.CENTER);
        paintText.setTextSize(30f);
    }

    public UIButton(float x, float y, float width, float height, SpriteList icon, Vector2 iconScale) {
        super(x, y, width, height);
        this.label = label;
        paint.setColor(Color.GRAY);
        Typeface font = ResourcesCompat.getFont(GameActivity.instance, R.font.alagard);
        paintText.setTypeface(font);
        paintText.setColor(Color.WHITE);
        paintText.setTextAlign(Paint.Align.CENTER);
        paintText.setTextSize(30f);

        btnIcon = new UIIcon(0, 0, width * iconScale.x, height * iconScale.y);
        btnIcon.setBaseSprite(icon.sprite);
        btnIcon.addParent(this);
    }

    public void setOnClick(Runnable callback) {
        this.onClick = callback;
    }

    public void setOnHover(Runnable callback) {
        this.onHover = callback;
    }

    public void setOnRelease(Runnable callback) {
        this.onRelease = callback;
    }

    public void setTextSize(float size) {
        paintText.setTextSize(size);
    }

    @Override
    public void onUpdate(float dt) {
        // Smooth button scale animation
        animScale += (targetScale - animScale) * 10f * dt;
    }

    @Override
    public void onRender(Canvas canvas) {
        if (!visible) return;

        RectF rect = getTransformedBounds();

        canvas.save();
        canvas.scale(animScale, animScale, rect.centerX(), rect.centerY());
        // Background
        paint.setStyle(Paint.Style.FILL);
        if(paint.getAlpha() == 0)
            paint.setColor(Color.TRANSPARENT);
        else
            paint.setColor(pressed ? Color.DKGRAY : hovered ? Color.LTGRAY : Color.GRAY);
        canvas.drawRoundRect(rect, 16f, 16f, paint);

        // Icon or text
        if (btnIcon != null) {
            btnIcon.setColor(pressed ? Color.DKGRAY : hovered ? Color.LTGRAY : Color.WHITE);
            btnIcon.onRender(canvas);
        }
        else
            canvas.drawText(label, rect.centerX(), rect.centerY() + 12f, paintText);
        canvas.restore();
    }

    @Override
    public void onTouchDown(float x, float y) {
        if (onClick != null) onClick.run();
        pressed = true;
        targetScale = 0.85f;
    }

    @Override
    public void onTouchMove(float x, float y) {
        if(!isPointInside(x, y) && pressed) {
            pressed = false;
            targetScale = 1f;
            return;
        };

        hovered = pressed;
        if(hovered && onHover != null) {
            onHover.run();
        }
    }

    @Override
    public void onTouchUp() {
        if(!pressed) return;
        hovered = false;
        if(onRelease != null) {
            onRelease.run();
        }
        pressed = false;
        targetScale = 1f;
    }
}
