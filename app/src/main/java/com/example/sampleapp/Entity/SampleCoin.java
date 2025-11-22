package com.example.sampleapp.Entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.example.sampleapp.R;
import com.example.sampleapp.mgp2d.core.AnimatedSprite;
import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

public class SampleCoin extends GameEntity {
    private AnimatedSprite spritess;
    private final RectF bounds = new RectF();
    private int value;
    private long lastTouchTime = -1L;

    public void onCreate(Vector2 position, int value){
        this._position = position;
        this.value = value;

        Bitmap bmp = BitmapFactory.decodeResource(GameActivity.instance.getResources(), R.drawable.flystar);
        this.spritess = new AnimatedSprite(bmp,1,5,24);
    }

    private void UpdateBounds()
    {
        Rect rect = spritess.GetRect();
        bounds.set(rect);
    }

    @Override
    public void setPosition(Vector2 position)
    {
        super.setPosition(position);
        UpdateBounds();
    }

    @Override
    public void onUpdate(float dt) {
        super.onUpdate(dt);
        if(canDestroy())
        {return;}

        MotionEvent event = GameActivity.instance.getMotionEvent();
        if(event == null){return;}
        if (event.getAction() != MotionEvent.ACTION_DOWN){
            return;
        }
        long t = event.getEventTime();
        if(t == lastTouchTime)
            return;
        lastTouchTime = t;
        float x = event.getX();
        float y = event.getY();

        if(bounds.contains(x,y))
        {
            destroy();
        }
    }

    @Override
    public void onRender(Canvas canvas){
        if(canDestroy())
            return;

        spritess.render(canvas, (int) _position.x, (int) _position.y, new Vector2(1,1), null);
        UpdateBounds();
     }
}
