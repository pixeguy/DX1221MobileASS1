package com.example.sampleapp.Entity.Projectile;

import com.example.sampleapp.Collision.Colliders.CircleCollider2D;
import com.example.sampleapp.Collision.Detection.Collision;
import com.example.sampleapp.Enums.SpriteAnimationList;
import com.example.sampleapp.Interface.Damageable;
import com.example.sampleapp.PostOffice.Message;
import com.example.sampleapp.PostOffice.ObjectBase;
import com.example.sampleapp.mgp2d.core.AnimatedSprite;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

public class Projectile extends GameEntity implements ObjectBase {
    protected float m_speed = 0.0f;
    protected float currentLifetime = 0.0f;
    protected float maxLifetime = 5.0f;

    public void onCreate(float movementSpeed, Vector2 pos, Vector2 scale) {
        onCreate(pos, scale);
        m_speed = movementSpeed;
    }

    @Override
    public void onUpdate(float dt) {
        super.onUpdate(dt);

        _position.set(getPosition().add(facingDirection.multiply(m_speed * dt)));

        currentLifetime += dt;
        if(currentLifetime >= maxLifetime) destroy();
    }

    @Override
    public boolean handle(Message message) {
        return false;
    }
}
