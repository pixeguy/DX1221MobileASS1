package com.example.sampleapp.Entity.Projectile;

import android.graphics.Canvas;

import com.example.sampleapp.Collision.Colliders.CircleCollider2D;
import com.example.sampleapp.Collision.Detection.Collision;
import com.example.sampleapp.Enums.SpriteAnimationList;
import com.example.sampleapp.Interface.Damageable;
import com.example.sampleapp.PostOffice.Message;
import com.example.sampleapp.PostOffice.ObjectBase;
import com.example.sampleapp.mgp2d.core.AnimatedSprite;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

/** @noinspection FieldCanBeLocal*/
public class PlayerMagicMissile extends Projectile {

    public void onCreate(GameEntity target, float movementSpeed, Vector2 pos, Vector2 scale) {
        onCreate(pos, scale);
        m_speed = movementSpeed;
        targetGO = target;
        animatedSprite = new AnimatedSprite(
                SpriteAnimationList.PlayerShootMissile.sprite,
                SpriteAnimationList.PlayerShootMissile.rows,
                SpriteAnimationList.PlayerShootMissile.columns,
                SpriteAnimationList.PlayerShootMissile.fps,
                SpriteAnimationList.PlayerShootMissile.startFrame,
                SpriteAnimationList.PlayerShootMissile.endFrame);
        collider = new CircleCollider2D(this, animatedSprite.GetRect(_position, _scale).width() / 10.0f);
        collider.isTrigger = true;
        isActive = true;
    }

    public void onCreate(Vector2 targetPos, float movementSpeed, Vector2 pos, Vector2 scale) {
        onCreate(pos, scale);
        m_speed = movementSpeed;
        this.targetPos = targetPos;
        animatedSprite = new AnimatedSprite(
                SpriteAnimationList.PlayerShootMissile.sprite,
                SpriteAnimationList.PlayerShootMissile.rows,
                SpriteAnimationList.PlayerShootMissile.columns,
                SpriteAnimationList.PlayerShootMissile.fps,
                SpriteAnimationList.PlayerShootMissile.startFrame,
                SpriteAnimationList.PlayerShootMissile.endFrame);
        collider = new CircleCollider2D(this, animatedSprite.GetRect(_position, _scale).width() / 10.0f);
        collider.isTrigger = true;
        isActive = true;
    }

    @Override
    public void onUpdate(float dt) {
        if (targetGO != null)
        {
            Vector2 direction = targetGO.getPosition().subtract(_position);
            direction.normalize();
            facingDirection.set(direction);

            super.onUpdate(dt);

            boolean isCollided = Collision.CollisionDetection(collider, targetGO.collider, new Vector2(0, 0));
            if(isCollided) {
                if(targetGO instanceof Damageable) {
                    float minDamage = 50.0f;
                    float maxDamage = 60.0f;
                    float damage = (float) (Math.random() * (maxDamage - minDamage) + minDamage);
                    ((Damageable) targetGO).TakeDamage(damage);
                }
                destroy();
            }
        }
        else {
            Vector2 direction = targetPos.subtract(_position);
            direction.normalize();
            facingDirection.set(direction);
            System.out.println(targetPos);

            super.onUpdate(dt);
        }
    }
}
