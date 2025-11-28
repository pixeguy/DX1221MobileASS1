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
public class EnemyFireMissile extends Projectile implements ObjectBase {

    @Override
    public void onCreate(float movementSpeed, Vector2 pos, Vector2 scale) {
        super.onCreate(movementSpeed, pos, scale);
        m_speed = movementSpeed;
        SetAnimation(SpriteAnimationList.EnemyShootFireMissile);
        collider = new CircleCollider2D(this, animatedSprite.GetRect(_position, _scale).width() / 10.0f);
        collider.isTrigger = true;
    }

    @Override
    public void onUpdate(float dt) {
        super.onUpdate(dt);

        if(CheckIfOutsideWorldBound(facingDirection, 30)) {
            destroy();
        }

        if(PlayerObj.getInstance() != null) {
            boolean isCollided = Collision.CollisionDetection(collider, PlayerObj.getInstance().collider, new Vector2(0, 0));
            if(isCollided) {
                Random rand = new Random();
                float minDamage = 15.0f;
                float maxDamage = 20.0f;
                float randDamage = rand.nextFloat() * (maxDamage - minDamage) + minDamage;
                PlayerObj.getInstance().TakeDamage(randDamage);
                destroy();
            }
        }
    }
}
