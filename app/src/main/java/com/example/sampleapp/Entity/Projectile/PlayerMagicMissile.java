package com.example.sampleapp.Entity.Projectile;

import android.graphics.Canvas;

import com.example.sampleapp.Collision.Colliders.CircleCollider2D;
import com.example.sampleapp.Collision.Colliders.Collider2D;
import com.example.sampleapp.Collision.Detection.Collision;
import com.example.sampleapp.Entity.Enemies.Enemy;
import com.example.sampleapp.Enums.SpriteAnimationList;
import com.example.sampleapp.Interface.Damageable;
import com.example.sampleapp.PostOffice.Message;
import com.example.sampleapp.PostOffice.MessageCheckCollision;
import com.example.sampleapp.PostOffice.MessageCheckForDamageCollision;
import com.example.sampleapp.PostOffice.ObjectBase;
import com.example.sampleapp.PostOffice.PostOffice;
import com.example.sampleapp.mgp2d.core.AnimatedSprite;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

import java.util.HashMap;
import java.util.List;

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

    public void onCreate(Vector2 direction, float movementSpeed, Vector2 pos, Vector2 scale) {
        onCreate(pos, scale);
        facingDirection.set(direction);
        m_speed = movementSpeed;
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
            super.onUpdate(dt);

            int[] directions = CheckForUnavailableDirection();
            for(int i : directions) {
                if(i != -1){
                    destroy();
                    return;
                }
            }
        }
    }
}
