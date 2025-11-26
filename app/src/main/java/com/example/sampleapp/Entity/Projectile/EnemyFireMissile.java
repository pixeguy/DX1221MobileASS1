package com.example.sampleapp.Entity.Projectile;

import android.graphics.Canvas;

import com.example.sampleapp.Collision.Colliders.CircleCollider2D;
import com.example.sampleapp.Collision.Detection.Collision;
import com.example.sampleapp.Entity.Player.PlayerObj;
import com.example.sampleapp.Enums.SpriteAnimationList;
import com.example.sampleapp.Interface.Damageable;
import com.example.sampleapp.PostOffice.Message;
import com.example.sampleapp.PostOffice.ObjectBase;
import com.example.sampleapp.mgp2d.core.AnimatedSprite;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

import java.util.Random;

/** @noinspection FieldCanBeLocal*/
public class EnemyFireMissile extends GameEntity implements ObjectBase {
    private float m_speed;
    private float currentLifetime = 0.0f;
    private final float maxLifetime = 5.0f;

    public void onCreate(GameEntity target, float movementSpeed, Vector2 pos, Vector2 scale) {
        onCreate(pos, scale);
        targetGO = target;
        m_speed = movementSpeed;
        animatedSprite = new AnimatedSprite(
                SpriteAnimationList.EnemyShootMissile.sprite,
                SpriteAnimationList.EnemyShootMissile.rows,
                SpriteAnimationList.EnemyShootMissile.columns,
                SpriteAnimationList.EnemyShootMissile.fps,
                SpriteAnimationList.EnemyShootMissile.startFrame,
                SpriteAnimationList.EnemyShootMissile.endFrame);
        collider = new CircleCollider2D(this, animatedSprite.GetRect(_position, _scale).width() / 10.0f);
        collider.isTrigger = true;
    }

    @Override
    public void onUpdate(float dt) {
        super.onUpdate(dt);

        _position.set(getPosition().add(facingDirection.multiply(m_speed * dt)));

        if(!facingDirection.equals(new Vector2(0, 0))) {
            _rotationZ = (float) Math.toDegrees(Vector2.Angle(facingDirection, new Vector2(-1, 0)));
        }

        currentLifetime += dt;
        if(currentLifetime >= maxLifetime || CheckIfOutsideWorldBound(facingDirection, 50))
            destroy();

        if(PlayerObj.instance != null) {
            boolean isCollided = Collision.CollisionDetection(collider, PlayerObj.instance.collider, new Vector2(0, 0));
            if(isCollided) {
                Random rand = new Random();
                float minDamage = 15.0f;
                float maxDamage = 30.0f;
                float randDamage = rand.nextFloat() * (maxDamage - minDamage) + minDamage;
                PlayerObj.instance.TakeDamage(randDamage);
                destroy();
            }
        }
    }

    @Override
    public void onRender(Canvas canvas) {
        super.onRender(canvas);
    }

    @Override
    public boolean handle(Message message) {
        return false;
    }
}
