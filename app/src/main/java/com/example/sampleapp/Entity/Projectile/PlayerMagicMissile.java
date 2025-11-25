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
public class PlayerMagicMissile extends GameEntity implements ObjectBase {
    private float m_speed;
    private float currentLifetime = 0.0f;
    private final float maxLifetime = 5.0f;

    public void onCreate(GameEntity target, float movementSpeed, Vector2 pos, Vector2 scale) {
        onCreate(pos, scale);
        targetGO = target;
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
    }

    @Override
    public void onUpdate(float dt) {
        super.onUpdate(dt);
        if(targetGO == null) return;

        Vector2 direction = targetGO.getPosition().subtract(_position);
        direction.normalize();
        _position.set(getPosition().add(direction.multiply(m_speed * dt)));

        if(!direction.equals(new Vector2(0, 0))) {
            direction.x *= -1;
            _rotationZ = (float) Math.toDegrees(Vector2.Angle(direction, new Vector2(-1, 0)));
        }

        currentLifetime += dt;
        if(currentLifetime >= maxLifetime) destroy();

        boolean isCollided = Collision.CollisionDetection(collider, targetGO.collider, new Vector2(0, 0));
        if(isCollided) {
            if(targetGO instanceof Damageable) {
                ((Damageable) targetGO).TakeDamage(10.0f);
            }
            destroy();
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
