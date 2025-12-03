package com.example.sampleapp.Entity.Player;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import com.example.sampleapp.Collision.Colliders.CircleCollider2D;
import com.example.sampleapp.Entity.Abilities.Ability;
import com.example.sampleapp.Entity.Abilities.DoubleShotAbi;
import com.example.sampleapp.Entity.Abilities.RearShotAbi;
import com.example.sampleapp.Interface.Damageable;
import com.example.sampleapp.PostOffice.Message;
import com.example.sampleapp.PostOffice.MessageSpawnProjectile;
import com.example.sampleapp.PostOffice.MessageSpawnRearProjectile;
import com.example.sampleapp.PostOffice.MessageWRU;
import com.example.sampleapp.PostOffice.ObjectBase;
import com.example.sampleapp.Enums.SpriteAnimationList;
import com.example.sampleapp.PostOffice.PostOffice;
import com.example.sampleapp.mgp2d.core.AnimatedSprite;
import com.example.sampleapp.mgp2d.core.EmptyEntity;
import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

import java.util.Vector;

/** @noinspection FieldCanBeLocal*/
public class PlayerObj extends GameEntity implements ObjectBase, Damageable {
    private static PlayerObj instance;
    private Vector2 inputDirection = new Vector2(0, 0);
    public void SetInputDirection(Vector2 inputDirection) {
        this.inputDirection = inputDirection;
    }

    private final float movementSpeed = 650.0f;
    private final float shootSpeed = 1000.0f;
    private final float fireRate = 0.5f;
    private float shootTimer = 0.0f;

    private final float maxHealth = 1000.0f;

    public int value = 0;
    public Ability currAbility;
    public int strength = 0;

    private Vibrator _vibrator;
    public Vector2 targetPos;

    public static PlayerObj getInstance() {
        if(instance == null) {
            instance = new PlayerObj();
        }
        return instance;
    }

    @Override
    public void onCreate(Vector2 pos, Vector2 scale, SpriteAnimationList spriteAnim) {
        onCreate(pos, scale);
        sprite = spriteAnim.sprite;
        animatedSprite = new AnimatedSprite(sprite, spriteAnim.rows, spriteAnim.columns, spriteAnim.fps, spriteAnim.startFrame, spriteAnim.endFrame);
        animatedSprite.setLooping(false);
        collider = new CircleCollider2D(this, animatedSprite.GetRect(_position, _scale).width() / 2.0f);
        PostOffice.getInstance().register(String.valueOf(_id), this);
        _vibrator = (Vibrator) GameActivity.instance.getApplicationContext().
                getSystemService(Context.VIBRATOR_SERVICE);

        currentHealth = maxHealth;
        _ordinal = 1;
        isActive = true;
    }

    @Override
    public void onUpdate(float dt) {
        super.onUpdate(dt);
        HandMovement(dt);
        FindNearestEnemy();
        shootTimer -= dt;
        if(targetGO != null && shootTimer <= 0.0f && inputDirection.equals(0, 0)) {
            Vector2 projectilePosition = _position.add(facingDirection.multiply(100.0f));
            // Send Message To Spawn Projectile
            MessageSpawnProjectile message = new MessageSpawnProjectile(this,
                    MessageSpawnProjectile.PROJECTILE_TYPE.PLAYER_FIRE_MISSILE,
                    1000.0f, projectilePosition);
            if (currAbility != null)
            {
                if(currAbility instanceof RearShotAbi)
                {
                    Vector2 diff = targetGO._position.subtract(_position);
                    Vector2 projectilePosition2 = _position.add(facingDirection.multiply(-100.0f));
                    MessageSpawnRearProjectile message2 = new MessageSpawnRearProjectile(diff.normalize().multiply(-1),
                            MessageSpawnRearProjectile.PROJECTILE_TYPE.PLAYER_FIRE_MISSILE,
                            1000.0f, projectilePosition2);
                    PostOffice.getInstance().send("Scene", message2);
                }
                else if (currAbility instanceof DoubleShotAbi)
                {
                    Vector2 projectilePosition2 = _position.add(facingDirection.multiply(-100.0f));
                    MessageSpawnProjectile message2 = new MessageSpawnProjectile(this,
                            MessageSpawnProjectile.PROJECTILE_TYPE.PLAYER_FIRE_MISSILE,
                            1000.0f, projectilePosition2);
                    PostOffice.getInstance().send("Scene", message2);
                }
            }
            PostOffice.getInstance().send("Scene", message);
            shootTimer = fireRate;
        }

        if(currAbility != null)
        {
            currAbility.onUpdate(dt);
        }
    }

    private void FindNearestEnemy() {
        MessageWRU message = new MessageWRU(this, MessageWRU.SEARCH_TYPE.NEAREST_ENEMY, shootSpeed);
        PostOffice.getInstance().send("Scene", message);

        if(targetGO != null)
        {
            Vector2 dir = targetGO._position.subtract(_position);
            targetPos = dir.multiply(-100f);
        }
    }

    private void HandMovement(float dt) {
        _position.x += inputDirection.x * movementSpeed * dt;
        _position.y += inputDirection.y * movementSpeed * dt;

        Vector2 movementDirection = new Vector2(inputDirection.x, -inputDirection.y);

        if(!movementDirection.equals(new Vector2(0, 0))) {
            facingDirection.set(movementDirection.x, -movementDirection.y);
            _rotationZ = (float) Math.toDegrees(Vector2.Angle(movementDirection, new Vector2(-1, 0)));
        }
    }

    @Override
    public void onRender(Canvas canvas) {
        super.onRender(canvas);
        if (currAbility != null)
        {
            currAbility.onRender(canvas);
        }
    }

    @Override
    public boolean handle(Message message) {
        return false;
    }

    @Override
    public void TakeDamage(float amount) {
        if(currentHealth <= 0) {
            isAlive = false;
            currentHealth = 0;
            return;
        }

        float healthPer = currentHealth / maxHealth;
        if ((amount > 40.0f || healthPer < 0.5f) &&
                _vibrator.hasVibrator() &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            _vibrator.vibrate(VibrationEffect.createOneShot(100L, (int) (amount + 50)));
        }

        currentHealth -= amount;
    }
}
