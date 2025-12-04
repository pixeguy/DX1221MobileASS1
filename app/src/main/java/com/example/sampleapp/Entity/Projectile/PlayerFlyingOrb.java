package com.example.sampleapp.Entity.Projectile;

import com.example.sampleapp.Collision.Colliders.CircleCollider2D;
import com.example.sampleapp.Collision.Colliders.Collider2D;
import com.example.sampleapp.Entity.Enemies.Enemy;
import com.example.sampleapp.Entity.Player.PlayerObj;
import com.example.sampleapp.Enums.SpriteAnimationList;
import com.example.sampleapp.PostOffice.MessageCheckCollision;
import com.example.sampleapp.PostOffice.PostOffice;
import com.example.sampleapp.mgp2d.core.AnimatedSprite;
import com.example.sampleapp.mgp2d.core.Vector2;

import java.util.HashMap;

public class PlayerFlyingOrb extends Projectile {

    HashMap<Collider2D, Boolean> collisionList = new HashMap<>();

    private float radius = 300f;
    private float angle = 0f;

    public void onCreate(float movementSpeed, float startingAngle, Vector2 scale) {
        destroyable = false;
        onCreate(movementSpeed, new Vector2(0, 0), scale);
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
        maxLifetime = 20.0f;
        angle = startingAngle;
    }

    @Override
    public void onUpdate(float dt) {
        super.onUpdate(dt);

        angle += m_speed * dt;

        Vector2 playerPos = PlayerObj.getInstance()._position;
        _position.x = playerPos.x + (float)Math.cos(angle) * radius;
        _position.y = playerPos.y + (float)Math.sin(angle) * radius;

        facingDirection.x = -(float)Math.sin(angle);
        facingDirection.y = (float)Math.cos(angle);
        facingDirection.set(facingDirection.normalize());

        MessageCheckCollision msg = new MessageCheckCollision(this);
        PostOffice.getInstance().send("Scene", msg);
        if(collisionList.isEmpty()) {
            for(Collider2D collider : msg.collisionList) {
                collisionList.put(collider, false);
            }
        }
        else {
            for(Collider2D collider : msg.collisionList) {
                if(!collisionList.containsKey(collider)) {
                    collisionList.put(collider, false);
                }
                else if(collisionList.containsKey(collider) &&
                        Boolean.TRUE.equals(collisionList.get(collider))) {
                    collisionList.remove(collider);
                }
            }
        }

        for(Collider2D collider : collisionList.keySet()) {
            if(Boolean.FALSE.equals(collisionList.get(collider))) {
                ((Enemy) collider.gameEntity).TakeDamage(500.0f * dt);
                collisionList.put(collider, true);
            }
        }
    }

}
