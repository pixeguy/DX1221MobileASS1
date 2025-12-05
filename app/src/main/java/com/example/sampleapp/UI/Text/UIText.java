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

public class UIText extends UIElement {

    private String text = "";

    public UIText(String text, float textSize, Vector2 pos, float width, float height) {
        super(pos, width, height);
        this.text = text;

        Typeface font = ResourcesCompat.getFont(GameActivity.instance, R.font.alagard);
        paint.setTypeface(font);
        paint.setTextSize(textSize);
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void onRender(Canvas canvas) {
        if (!visible) return;
        RectF rect = getTransformedBounds();
        canvas.drawText(text, rect.centerX(), rect.centerY(), paint);
    }
}
