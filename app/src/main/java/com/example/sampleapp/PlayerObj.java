package com.example.sampleapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

public class PlayerObj extends GameEntity {
    private Bitmap playerBitmap;
    public void onCreate()
    {
        super.onCreate();
        Bitmap bmp = BitmapFactory.decodeResource(GameActivity.instance.getResources()
                , R.drawable.pause);
        _scale = new Vector2(100,100);
        playerBitmap = Bitmap.createScaledBitmap(bmp,(int)_scale.x,(int)_scale.y,true);
    }
    @Override
    public void onUpdate(float dt) {
        super.onUpdate(dt);
        _position.x += 50 * dt;
    }

    @Override
    public void onRender(Canvas canvas) {
        //render at center of position, position is not top left anymore
        float drawX = _position.x - (_scale.x / 2f);
        float drawY = _position.y - (_scale.y / 2f);
        canvas.drawBitmap(playerBitmap, drawX, drawY, null);     }
}
