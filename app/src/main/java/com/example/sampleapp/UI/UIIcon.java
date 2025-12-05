package com.example.sampleapp.UI;

import android.graphics.Canvas;
import android.graphics.RectF;

public class UIIcon extends UIElement {
    public UIIcon(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    @Override
    public void onRender(Canvas canvas) {
        if (!visible) return;
        RectF rectF = getTransformedBounds();
        canvas.drawBitmap(baseSprite, null, rectF, paint);
    }
}
