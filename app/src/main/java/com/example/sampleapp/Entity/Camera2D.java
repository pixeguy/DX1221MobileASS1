package com.example.sampleapp.Entity;

import android.graphics.RectF;
import android.util.Log;

import com.example.sampleapp.Entity.Player.PlayerObj;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Singleton;
import com.example.sampleapp.mgp2d.core.Vector2;

public class Camera2D extends Singleton<Camera2D> {
    public Vector2 _position = new Vector2(0, 0);
    public float zoom = 1f;
    private final Vector2 target = new Vector2(0, 0);
    private float followSpeed = 5f;
    private final Vector2 offset = new Vector2(0, 0);

    private RectF bounds = null;

    public static Camera2D getInstance() {
        return getInstance(Camera2D.class);
    }

    public Camera2D() {
        this.setFollowSpeed(6f);
        //this.setOffset(0, -50); // Keep player slightly below center
        //this.setBounds(new RectF(-2000, -1000, 2000, 1000));
    }

    public void setFollowSpeed(float speed) { this.followSpeed = speed; }
    public void setOffset(float x, float y) { this.offset.set(x, y); }
    public void setBounds(RectF rect) { this.bounds = rect; }

    public void follow(Vector2 targetPos, float dt) {
        _position = targetPos.subtract(PlayerObj.getInstance()._orignalPos);
    }

    // Example: these depend on your renderer
    private float getViewportWidth() { return 1280f / zoom; }
    private float getViewportHeight() { return 720f / zoom; }
}
