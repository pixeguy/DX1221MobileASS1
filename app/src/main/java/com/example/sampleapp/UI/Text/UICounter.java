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

public class UICounter extends UIElement {
    private String text;

    public UICounter(float x, float y, float width, float height, float fontSize) {
        super(x, y, width, height);

        Typeface font = ResourcesCompat.getFont(GameActivity.instance, R.font.alagard);
        paint.setTypeface(font);
        paint.setTextSize(fontSize);
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.CENTER);
    }

    public void setColor(int color) {
        paint.setColor(color);
    }

    public void setText(String title, String value) {
        if(title == null || value == null) return;
        if(title.isEmpty()) this.text = value;
        else this.text = title + ": " + value;
    }

    @Override
    public void onRender(Canvas canvas) {
        RectF rectF = getTransformedBounds();
        canvas.drawText(text, rectF.centerX(), rectF.centerY(), paint);
    }
}
