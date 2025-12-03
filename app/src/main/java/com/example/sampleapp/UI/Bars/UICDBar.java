package com.example.sampleapp.UI.Bars;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

import com.example.sampleapp.UI.UIElement;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

public class UICDBar extends UIElement {
    public enum FillMode {
        Clockwise360,        // normal clockwise pie fill
        CounterClockwise360, // reverse direction
        TopToBottom,         // vertical fill
        BottomToTop,
        LeftToRight,
        RightToLeft
    }
    private FillMode fillMode = FillMode.LeftToRight;

    private float cooldown = 1f;
    private float timer = 0f;

    private int bgColor = Color.DKGRAY;
    private int fillColor = Color.CYAN;

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float glowTimer = 0f;

    private GameEntity owner;
    private Vector2 offset;

    public void setOwner(GameEntity owner, Vector2 offset) {
        this.owner = owner;
        this.offset = offset;
    }

    public UICDBar(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    public void setFillMode(FillMode mode) {
        this.fillMode = mode;
    }

    public void setCooldown(float cd) {
        this.cooldown = cd;
    }

    public void setTimer(float t) {
        this.timer = Math.max(0f, Math.min(t, cooldown));
    }

    public void setColors(int bg, int fill) {
        this.bgColor = bg;
        this.fillColor = fill;
    }

    public void setSprite(Bitmap sprite) {
        this.baseSprite = sprite;
    }

    @Override
    public void onUpdate(float dt) {
        glowTimer += dt;
    }

    @Override
    public void onRender(Canvas canvas) {
        if (!visible) return;

        RectF rect = getTransformedBounds();
        float ratio = 1f - (timer / cooldown);
        ratio = Math.max(0f, Math.min(ratio, 1f));

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

        if(baseSprite != null) {
            // Create a save layer to clip sprite rendering
            int checkpoint = canvas.saveLayer(rect, null);

            switch (fillMode) {
                case Clockwise360:
                case CounterClockwise360:
                    drawRadialFill(canvas, rect, ratio);
                    break;
                case TopToBottom:
                case BottomToTop:
                case LeftToRight:
                case RightToLeft:
                    drawRectFill(canvas, rect, ratio);
                    break;
            }

            // Draw sprite clipped to the filled region
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(baseSprite, null, rect, paint);
            paint.setXfermode(null);
            canvas.restoreToCount(checkpoint);
        }
        else {
            paint.setColor(bgColor);
            switch (fillMode) {
                case Clockwise360:
                case CounterClockwise360:
                    canvas.drawOval(rect, paint);
                    break;
                case TopToBottom:
                case BottomToTop:
                case LeftToRight:
                case RightToLeft:
                    canvas.drawRect(rect, paint);
                    break;

            }
            paint.setColor(fillColor);

            switch (fillMode) {
                case Clockwise360: {
                    float sweep = 360f * ratio;
                    canvas.drawArc(rect, -90, sweep, true, paint);
                    break;
                }

                case CounterClockwise360: {
                    float sweep = -360f * ratio;
                    canvas.drawArc(rect, -90, sweep, true, paint);
                    break;
                }

                case TopToBottom: {
                    float height = rect.height() * ratio;
                    RectF fill = new RectF(rect.left, rect.top, rect.right, rect.top + height);
                    canvas.drawRect(fill, paint);
                    break;
                }

                case BottomToTop: {
                    float height = rect.height() * ratio;
                    RectF fill = new RectF(rect.left, rect.bottom - height, rect.right, rect.bottom);
                    canvas.drawRect(fill, paint);
                    break;
                }

                case LeftToRight: {
                    float width = rect.width() * ratio;
                    RectF fill = new RectF(rect.left, rect.top, rect.left + width, rect.bottom);
                    canvas.drawRect(fill, paint);
                    break;
                }

                case RightToLeft: {
                    float width = rect.width() * ratio;
                    RectF fill = new RectF(rect.right - width, rect.top, rect.right, rect.bottom);
                    canvas.drawRect(fill, paint);
                    break;
                }
            }
        }



        if (timer <= 0f) {
            float pulse = (float)(Math.sin(glowTimer * 8f) * 0.5f + 0.5f);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(6f);
            paint.setColor(Color.argb((int)(150 + 105 * pulse), 255, 255, 255));
            switch (fillMode) {
                case Clockwise360:
                case CounterClockwise360:
                    canvas.drawOval(rect, paint);
                    break;
                case TopToBottom:
                case BottomToTop:
                case LeftToRight:
                case RightToLeft:
                    canvas.drawRect(rect, paint);
                    break;
            }
            paint.setStyle(Paint.Style.FILL);
        }
    }

    private void drawRadialFill(Canvas canvas, RectF rect, float ratio) {
        Path clipPath = new Path();
        float sweep = 360f * ratio * (fillMode == FillMode.Clockwise360 ? 1 : -1);
        clipPath.moveTo(rect.centerX(), rect.centerY());
        clipPath.addArc(rect, -90, sweep);
        clipPath.lineTo(rect.centerX(), rect.centerY());
        clipPath.close();
        canvas.clipPath(clipPath);
    }

    private void drawRectFill(Canvas canvas, RectF rect, float ratio) {
        RectF fillRect = new RectF(rect);
        switch (fillMode) {
            case TopToBottom:
                fillRect.bottom = rect.top + rect.height() * ratio;
                break;
            case BottomToTop:
                fillRect.top = rect.bottom - rect.height() * ratio;
                break;
            case LeftToRight:
                fillRect.right = rect.left + rect.width() * ratio;
                break;
            case RightToLeft:
                fillRect.left = rect.right - rect.width() * ratio;
                break;
        }
        canvas.clipRect(fillRect);
    }
}
