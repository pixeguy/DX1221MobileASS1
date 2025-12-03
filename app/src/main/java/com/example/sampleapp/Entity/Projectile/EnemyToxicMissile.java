package com.example.sampleapp.Entity.Projectile;

import com.example.sampleapp.Collision.Colliders.CircleCollider2D;
import com.example.sampleapp.Collision.Detection.Collision;
import com.example.sampleapp.Entity.Player.PlayerObj;
import com.example.sampleapp.Enums.SpriteAnimationList;
import com.example.sampleapp.PostOffice.ObjectBase;
import com.example.sampleapp.mgp2d.core.Vector2;

import java.util.Random;

public class EnemyToxicMissile extends Projectile implements ObjectBase {
    private float acceleration = 0.0f;
    private boolean isGettingPosition = false;

    private float acclerationRate;

    @Override
    public void onCreate(float movementSpeed, Vector2 pos, Vector2 scale) {
        super.onCreate(movementSpeed, pos, scale);
        SetAnimation(SpriteAnimationList.EnemyShootToxicMissile);
        collider = new CircleCollider2D(this, animatedSprite.GetRect(_position, _scale).width() / 10.0f);
        collider.isTrigger = true;
        acclerationRate = movementSpeed;
        m_speed = 0.0f;
    }

    @Override
    public void onUpdate(float dt) {
        acceleration += acclerationRate * dt;
        m_speed += acceleration * dt;

        if(!isGettingPosition)
        {
            Vector2 diff = PlayerObj.getInstance().getPosition().subtract(getPosition());
            diff.set(diff.normalize());
            facingDirection.set(diff.x, diff.y);
            isGettingPosition = true;
        }

        super.onUpdate(dt);

        int[] directions = CheckForUnavailableDirection();
        for(int i : directions) {
            if(i != -1){
                destroy();
                return;
            }
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
