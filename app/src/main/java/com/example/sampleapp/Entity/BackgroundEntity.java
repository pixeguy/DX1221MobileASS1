package com.example.sampleapp.Entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.sampleapp.R;
import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.GameEntity;


public class BackgroundEntity extends GameEntity {
    public Bitmap backgroundBitmap;
    private final int screenWidth;
    private final int screenHeight;
    public int imageID;

    public BackgroundEntity(int imageID)
    {
        this.imageID = imageID;
        screenWidth = GameActivity.instance.getResources().getDisplayMetrics().widthPixels;
        screenHeight = GameActivity.instance.getResources().getDisplayMetrics().heightPixels;

        Bitmap bmp = BitmapFactory.decodeResource(GameActivity.instance.getResources(), imageID);
        backgroundBitmap = Bitmap.createScaledBitmap(bmp, screenWidth + screenWidth / 15, screenHeight + screenHeight / 15, true);
        isActive = true;
    }

    public BackgroundEntity(int imageID, int width, int height)
    {
        this.imageID = imageID;
        screenWidth = GameActivity.instance.getResources().getDisplayMetrics().widthPixels;
        screenHeight = GameActivity.instance.getResources().getDisplayMetrics().heightPixels;

        Bitmap bmp = BitmapFactory.decodeResource(GameActivity.instance.getResources(), imageID);
        backgroundBitmap = Bitmap.createScaledBitmap(bmp, width, height, false);
        isActive = true;
    }

    @Override
    public void onUpdate(float dt) {
    }

    @Override
    public void onRender(Canvas canvas) {
        canvas.drawBitmap(backgroundBitmap, _position.x, _position.y, null);
    }
}
