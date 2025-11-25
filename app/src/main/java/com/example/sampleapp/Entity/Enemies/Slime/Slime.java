package com.example.sampleapp.Entity.Enemies.Slime;

import android.graphics.Canvas;
import android.util.Log;

import com.example.sampleapp.Collision.Colliders.CircleCollider2D;
import com.example.sampleapp.Collision.Colliders.Collider2D;
import com.example.sampleapp.Entity.Enemies.Enemy;
import com.example.sampleapp.PostOffice.Message;
import com.example.sampleapp.Statemchine.Statemachine;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

import java.util.Random;

public class Slime extends Enemy {

    @Override
    public void onCreate(Vector2 pos, Vector2 scale) {
        super.onCreate(pos, scale);

        Random random = new Random();
        int randomDirection = random.nextInt(4);
        switch (randomDirection) {
            case 0:
                facingDirection = new Vector2(0, -1);
                break;
            case 1:
                facingDirection = new Vector2(0, 1);
                break;
            case 2:
                facingDirection = new Vector2(-1, 0);
                break;
            case 3:
                facingDirection = new Vector2(1, 0);
                break;
        }

        currentHealth = 100.0f;

        sm = new Statemachine();
        sm.AddState(new SlimeRunState(this, "SlimeRun"));
        sm.AddState(new SlimeIdleState(this, "SlimeIdle"));
        sm.AddState(new SlimeDeathState(this, "Death"));
        collider = new CircleCollider2D(this, animatedSprite.GetRect(_position, _scale).width() / 2.0f - 130.0f);
    }

    @Override
    public void onUpdate(float dt) {
        super.onUpdate(dt);
        sm.Update(dt);
    }

    @Override
    public void onRender(Canvas canvas) {
        super.onRender(canvas);
    }
}
