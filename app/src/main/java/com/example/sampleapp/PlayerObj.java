package com.example.sampleapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.sampleapp.mgp2d.core.AnimatedSprite;
import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

public class PlayerObj extends GameEntity {

    private AnimatedSprite animatedSprite;
    @Override
    public void onCreate(Vector2 pos, Vector2 scale, int spriteID) {
        //_isCreated = true;
        Context ctx = GameActivity.instance;
        Bitmap bmp = BitmapFactory.decodeResource(GameActivity.instance.getResources(), spriteID);
        _position = new Vector2(pos.x,pos.y);
        _scale = new Vector2(scale.x,scale.y);
        sprite = Bitmap.createScaledBitmap(bmp,(int)(bmp.getWidth() * 1.5f),(int)(bmp.getHeight() *1.5f),true);

        animatedSprite = new AnimatedSprite(sprite, 1,7,24,0,7);
    }
    @Override
    public void onUpdate(float dt) {
        super.onUpdate(dt);
        _position.x += 50 * dt;
        animatedSprite.update(dt);
    }

    @Override
    public void onRender(Canvas canvas) {
        canvas.save();
        animatedSprite.render(canvas,(int)_position.x,(int)_position.y,null);

        canvas.rotate(90);
        canvas.restore();
        //super.onRender(canvas);
    }
}
