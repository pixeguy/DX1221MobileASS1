package com.example.sampleapp.mgp2d.core;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

import com.example.sampleapp.Enums.SpriteList;

public abstract class GameEntity {
    private static int _totalEntitiesCreated = 0;
    public int _id = 0;
    public Vector2 _scale = new Vector2(1,1);
    public Vector2 _position = new Vector2(0, 0);
    public Bitmap sprite;
    public AnimatedSprite animatedSprite;
    private boolean _isCreated = false;
    public boolean isActive = false;

    public void onCreate(Vector2 pos, Vector2 scale) {
        _isCreated = true;
        _id = _totalEntitiesCreated++;
        _position = new Vector2(pos.x,pos.y);
        _scale = new Vector2(scale.x,scale.y);
    }

    public void onCreate(Vector2 pos, Vector2 scale, SpriteList spriteAnim) {
        _isCreated = true;
        _id = _totalEntitiesCreated++;
        _position = new Vector2(pos.x,pos.y);
        _scale = new Vector2(scale.x,scale.y);

        Bitmap bmp = BitmapFactory.decodeResource(GameActivity.instance.getResources(), spriteAnim.spriteSheetID);
        sprite = Bitmap.createScaledBitmap(bmp,bmp.getWidth(),bmp.getHeight(),true);
        animatedSprite = new AnimatedSprite(sprite,spriteAnim.rows, spriteAnim.columns,spriteAnim.fps);
        isActive = true;
    }

    public void SetAnimation(SpriteList nextAnim)
    {
        animatedSprite = new AnimatedSprite(sprite,nextAnim.rows,nextAnim.columns,nextAnim.fps,nextAnim.startFrame, nextAnim.endFrame);
    }

    public Vector2 getPosition() { return _position.copy(); }
    public void setPosition(Vector2 position) { _position = position; }

    protected int _ordinal = 0;
    public int getOrdinal() { return _ordinal; }

    private boolean _isDone = false;
    public void destroy() { _isDone = true; }
    public boolean canDestroy() { return _isDone; }

    public void onUpdate(float dt) {
        if(animatedSprite != null){
        animatedSprite.update(dt);}
    }

    public void onRender(Canvas canvas){
        //float drawX = _position.x - (_scale.x / 2f);
        //float drawY = _position.y - (_scale.y / 2f);
        //canvas.drawBitmap(sprite, drawX, drawY, paint);

        animatedSprite.render(canvas,(int)_position.x,(int)_position.y, _scale, paint);
    }

    public Paint paint = new Paint();
    private boolean tinted = false;
    private int tintColor = 0x77FF0000; // default: semi-transparent red

    public void setTint(int color) {
        tinted = true;
        tintColor = color;

        ColorFilter filter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        paint.setColorFilter(filter);
    }

    public void clearTint() {
        tinted = false;
        paint.setColorFilter(null);
    }
}
