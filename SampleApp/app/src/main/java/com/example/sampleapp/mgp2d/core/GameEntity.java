package com.example.sampleapp.mgp2d.core;

import android.graphics.Canvas;

public abstract class GameEntity {
    public Vector2 _scale = new Vector2(1,1);
    public Vector2 _position = new Vector2(0, 0);
    private boolean _isCreated = false;
    public void onCreate() { _isCreated = true; }
    public Vector2 getPosition() { return _position.copy(); }
    public void setPosition(Vector2 position) { _position = position; }

    protected int _ordinal = 0;
    public int getOrdinal() { return _ordinal; }

    private boolean _isDone = false;
    public void destroy() { _isDone = true; }
    public boolean canDestroy() { return _isDone; }

    public void onUpdate(float dt) {}
    public abstract void onRender(Canvas canvas);
}
