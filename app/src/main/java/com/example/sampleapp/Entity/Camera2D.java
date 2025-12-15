package com.example.sampleapp.Entity;

import android.graphics.RectF;
import android.util.Log;

import com.example.sampleapp.Entity.Player.PlayerObj;
import com.example.sampleapp.Scenes.GameLevel.GameLevelScene;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Singleton;
import com.example.sampleapp.mgp2d.core.Vector2;

public class Camera2D extends Singleton<Camera2D> {
    public Vector2 _position = new Vector2(0, 0);
    public Vector2 original_position = new Vector2(0, 0);
    public final Vector2 target = new Vector2(0, 0);
    public final Vector2 prevTarget = new Vector2(0, 0);
    public float zoom = 1f;
    private float followSpeed = 5f;
    private final Vector2 offset = new Vector2(0, 0);

    private RectF bounds;

    public static Camera2D getInstance() {
        return getInstance(Camera2D.class);
    }

    public Camera2D() {
        this.setFollowSpeed(6f);
    }

    public void setFollowSpeed(float speed) { this.followSpeed = speed; }
    public void setOffset(float x, float y) { this.offset.set(x, y); }
    public void setBounds(RectF rect) { this.bounds = rect; }
    public void setPosition(Vector2 pos) {
        this._position.set(pos);
        this.original_position.set(pos);
    }

    public void follow(Vector2 targetPos) {
        float halfScreenHeight = getViewportHeight() / 2.0f;
        if(targetPos.y + halfScreenHeight > bounds.bottom) {
            return;
        }

        if(targetPos.y - halfScreenHeight < bounds.top) {
            return;
        }

        Vector2 diff = targetPos.subtract(original_position);
        target.set(0.0f, diff.y);
        if(!target.equals(prevTarget)) {
            _position.y = target.y;
            prevTarget.set(target);
        }
    }

    public void Reset()
    {
        original_position.set(0,0);
        _position.set(original_position);
        target.set(0,0);
        prevTarget.set(0,0);
        zoom = 1f;
    }

    // Example: these depend on your renderer
    private float getViewportWidth() { return GameLevelScene.screenWidth / zoom; }
    private float getViewportHeight() { return GameLevelScene.screenHeight / zoom; }
}
