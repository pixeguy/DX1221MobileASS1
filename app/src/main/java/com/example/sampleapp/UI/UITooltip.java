package com.example.sampleapp.UI;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;

import androidx.core.content.res.ResourcesCompat;

import com.example.sampleapp.R;
import com.example.sampleapp.Scenes.GameLevel.GameLevelScene;
import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.Vector2;

import java.util.ArrayList;
import java.util.List;

public class UITooltip extends UIElement {

    public enum SlideMode {
        NONE,
        LEFT_TO_RIGHT,
        RIGHT_TO_LEFT,
        UP,
        DOWN
    }

    private String title = "";
    private String text = "";

    private final Paint paintTitle = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float alpha = 0f;          // current transparency (0–1)
    private float targetAlpha = 0f;    // goal transparency
    private float animSpeed = 20f;      // fade speed
    private final Vector2 offset = new Vector2(0, 0);       // for slide animation

    private SlideMode slideMode = SlideMode.UP;

    private List<String> lines = new ArrayList<>();

    public UITooltip(String title, String text, Vector2 pos, float width, float height) {
        super(pos, width, height);
        this.text = text;
        this.title = title;

        paint.setColor(Color.argb(200, 20, 20, 20));

        Typeface font = ResourcesCompat.getFont(GameActivity.instance, R.font.alagard);
        paintTitle.setColor(Color.rgb(255, 230, 120));
        paintTitle.setTextSize(30f);
        paintTitle.setFakeBoldText(true);
        paintTitle.setTypeface(font);

        paintText.setColor(Color.WHITE);
        paintText.setTextSize(24f);
        paintText.setTypeface(font);

        wrapText(width - 32f);
    }

    public void show() { targetAlpha = 1f; }
    public void hide() { targetAlpha = 0f; }

    public void setText(String t) {
        this.text = t;
        wrapText(bounds.width());
    }

    public void setOffset(Vector2 offset) {
        this.offset.set(offset.x, offset.y);
    }

    public void setSlideMode(SlideMode mode) {
        this.slideMode = mode;
    }

    @Override
    public void onUpdate(float dt) {
        super.onUpdate(dt);
        alpha += (targetAlpha - alpha) * animSpeed * dt;

        if (Math.abs(targetAlpha - alpha) < 0.01f)
            alpha = targetAlpha;

        float targetOffset = (targetAlpha > 0 ? 15f : 0f);
        switch (slideMode) {
            case LEFT_TO_RIGHT:
                offset.x += (targetOffset - offset.x) * animSpeed * dt;
                if (Math.abs(targetOffset - offset.x) < 0.5f) offset.x = targetOffset;
                break;
            case RIGHT_TO_LEFT:
                targetOffset = (targetAlpha > 0 ? -15f : 0f);
                offset.x += (targetOffset - offset.x) * animSpeed * dt;
                if (Math.abs(targetOffset - offset.x) < 0.5f) offset.x = targetOffset;
                break;
            case UP:
                targetOffset = (targetAlpha > 0 ? -5f : 10f);
                offset.y += (targetOffset - offset.y) * animSpeed * dt;
                if (Math.abs(targetOffset - offset.y) < 0.5f) offset.y = targetOffset;
                break;
            case DOWN:
                targetOffset = (targetAlpha > 0 ? 5f : -10f);
                offset.y += (targetOffset - offset.y) * animSpeed * dt;
                if (Math.abs(targetOffset - offset.y) < 0.5) offset.y = targetOffset;
                break;
        }
    }

    @Override
    public void onRender(Canvas canvas) {
        if (!visible || text.isEmpty()) return;

        RectF bounds = getTransformedBounds();
        int a = (int)(alpha * 255);
        paint.setAlpha(a);
        paintText.setAlpha(a);
        paintTitle.setAlpha(a);

        float drawMinX = bounds.left + offset.x;
        float drawMinY = bounds.top + offset.y;

        // Background box
        canvas.drawRoundRect(drawMinX, drawMinY, drawMinX + bounds.width(), drawMinY + bounds.height(), 10f, 10f, paint);

        // Text
        float titleY = drawMinY + 35f;
        canvas.drawText(title, drawMinX + 16f, titleY, paintTitle);

        Paint linePaint = new Paint();
        linePaint.setColor(Color.argb((int)(180 * alpha), 255, 255, 255));
        linePaint.setStrokeWidth(2f);
        canvas.drawLine(drawMinX + 10f, titleY + 10f, drawMinX + bounds.width() - 10f, titleY + 10f, linePaint);

        float textY = (title.isEmpty()) ? drawMinY + 35f : titleY + 35f;
        for (String line : lines) {
            canvas.drawText(line, drawMinX + 16f, textY, paintText);
            textY += paintText.getTextSize() + 8f;
        }
    }

    private void wrapText(float maxWidth) {
        lines.clear();
        if (text == null || text.isEmpty()) return;

        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        for (String word : words) {
            String test = line.length() == 0 ? word : line + " " + word;
            float width = paintText.measureText(test);
            if (width > maxWidth && line.length() > 0) {
                lines.add(line.toString());
                line = new StringBuilder(word);
            } else {
                line = new StringBuilder(test);
            }
        }
        if (line.length() > 0) lines.add(line.toString());
    }
}

