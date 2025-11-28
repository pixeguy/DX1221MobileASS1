package com.example.sampleapp.Entity.Player;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.example.sampleapp.Collision.Colliders.CircleCollider2D;
import com.example.sampleapp.Collision.Colliders.Collider2D;
import com.example.sampleapp.Entity.Abilities.Ability;
import com.example.sampleapp.R;
import com.example.sampleapp.Enums.SpriteList;
import com.example.sampleapp.Ultilies.Utilies;
import com.example.sampleapp.mgp2d.core.AnimatedSprite;
import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

public class PlayerObj extends GameEntity {
    public static PlayerObj instance = null;
    private Vector2 inputDirection = new Vector2(0, 0);
    public void SetInputDirection(Vector2 inputDirection) {
        this.inputDirection = inputDirection;
    }

    private float movementSpeed = 500.0f;
    private float newRotationZ = 0.0f;
    private float currentRotationZ = 0.0f;

    public Collider2D collider;

    private Vector2 facingDirection = new Vector2(0, 0);

    public int value = 0;

    public Ability currAbility;
    public int strength = 0;

    public PlayerObj() {
        super();
    }

    public static PlayerObj getInstance()
    {
        if (instance == null){
            instance = new PlayerObj();
        }
        return instance;
    }

    @Override
    public void onCreate(Vector2 pos, Vector2 scale, SpriteList spriteAnim) {
        onCreate(pos, scale);
        Bitmap bmp = BitmapFactory.decodeResource(GameActivity.instance.getResources(), R.drawable.playersprite);
        sprite = Bitmap.createScaledBitmap(bmp, 100, 100, false);
        collider = new CircleCollider2D(this, scale.x * sprite.getWidth() - sprite.getWidth() / 2.0f);
    }

    @Override
    public void onUpdate(float dt) {
        super.onUpdate(dt);
        _position.x += inputDirection.x * movementSpeed * dt;
        _position.y += inputDirection.y * movementSpeed * dt;

        Vector2 movementDirection = new Vector2(inputDirection.x, -inputDirection.y);


        if(!movementDirection.equals(new Vector2(0, 0)))
        {
            currentRotationZ = (float) Math.toDegrees(Vector2.Angle(movementDirection, new Vector2(-1, 0)));
        }
    }

    @Override
    public void onRender(Canvas canvas) {
        int scaleWidth = (int) (_scale.x * sprite.getWidth());
        int scaleHeight = (int) (_scale.y * sprite.getHeight());

        int left = (int) (_position.x - (float) scaleWidth / 2);
        int top = (int) (_position.y - (float) scaleHeight / 2);

        Rect _dst = new Rect(left, top, left + scaleWidth, top + scaleHeight);

        canvas.save();
        canvas.rotate(currentRotationZ, _dst.centerX(), _dst.centerY());
        canvas.drawBitmap(sprite, null, _dst, null);
        canvas.restore();
    }
}
