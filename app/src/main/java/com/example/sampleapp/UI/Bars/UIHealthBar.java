package com.example.sampleapp.UI.Bars;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;

import com.example.sampleapp.UI.UIElement;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

@SuppressWarnings("FieldCanBeLocal")
public class UIHealthBar extends UIElement {
    private float currentValue;
    private float maxValue;

    private int bgColor = Color.DKGRAY;
    private int fillColor = Color.GREEN;

    private Bitmap fillSprite = null;

    private boolean vertical = false; // false = horizontal, true = vertical

    private GameEntity owner = null;
    public final Vector2 offset = new Vector2(0, 0);

    public UIHealthBar(Vector2 position, float width, float height) {
        super(position, width, height);
    }

    public void setValue(float max) {
        this.currentValue = max;
        this.maxValue = Math.max(1, max);
    }

    public void updateValue(float value) {
        this.currentValue = value;

        float percent = currentValue / maxValue;

        if(percent < 0.35f)
            fillColor = Color.RED;
        else if(percent < 0.75f)
            fillColor = Color.YELLOW;
    }

    public void setSprites(Bitmap bg, Bitmap fill) {
        this.baseSprite = bg;
        this.fillSprite = fill;
    }

    public void setColors(int bg, int fill) {
        this.bgColor = bg;
        this.fillColor = fill;
    }

    public void setVertical(boolean vertical) {
        this.vertical = vertical;
    }

    public float getRatio() {
        return currentValue / maxValue;
    }

    @Override
    public void onRender(Canvas canvas) {
        if (!visible) return;

        RectF rect = getTransformedBounds();
        float ratio = getRatio();

        if(owner != null) {
            float halfWidth = bounds.width() / 2f;
            float halfHeight = bounds.height() / 2f;

            float positionX = owner.getPosition().x + offset.x;
            float positionY = owner.getPosition().y + offset.y;

            if(positionY < 0) {
                positionY = owner.getPosition().y - offset.y;
            }

            rect = new RectF(
                    positionX - halfWidth,
                    positionY - halfHeight,
                    positionX + halfWidth,
                    positionY + halfHeight
            );
        }

        // --- Draw background ---
        if (baseSprite != null)
            canvas.drawBitmap(baseSprite, null, rect, paint);
        else {
            paint.setColor(bgColor);
            canvas.drawRect(rect, paint);
        }

        RectF fillRect = new RectF(rect);
        if (vertical)
            fillRect.top = rect.bottom - rect.height() * ratio; // bottom-to-top
        else
            fillRect.right = rect.left + rect.width() * ratio;  // left-to-right

        // --- Draw fill ---
        if (fillSprite != null)
            canvas.drawBitmap(fillSprite, null, fillRect, paint);
        else {
            paint.setColor(fillColor);
            canvas.drawRect(fillRect, paint);
        }

        // --- Draw children (like icons/text) ---
        for (UIElement child : children)
            child.onRender(canvas);
    }

    public GameEntity getOwner() {
        return owner;
    }

    public void setOwner(GameEntity owner) {
        this.owner = owner;
    }
}
