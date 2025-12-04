package com.example.sampleapp.Entity.Player;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import com.example.sampleapp.Collision.Colliders.CircleCollider2D;
import com.example.sampleapp.Core.HealthSystem;
import com.example.sampleapp.Entity.Abilities.Ability;
import com.example.sampleapp.Entity.Abilities.DoubleShotAbi;
import com.example.sampleapp.Entity.Abilities.RearShotAbi;
import com.example.sampleapp.Interface.Damageable;
import com.example.sampleapp.Managers.DamageTextManager;
import com.example.sampleapp.Managers.UIManager;
import com.example.sampleapp.PostOffice.Message;
import com.example.sampleapp.PostOffice.MessageSpawnProjectile;
import com.example.sampleapp.PostOffice.MessageSpawnRearProjectile;
import com.example.sampleapp.PostOffice.MessageWRU;
import com.example.sampleapp.PostOffice.ObjectBase;
import com.example.sampleapp.Enums.SpriteAnimationList;
import com.example.sampleapp.PostOffice.PostOffice;
import com.example.sampleapp.UI.Bars.UICDBar;
import com.example.sampleapp.UI.Bars.UIHealthBar;
import com.example.sampleapp.UI.Buttons.UIJoystick;
import com.example.sampleapp.Utilities.Utilities;
import com.example.sampleapp.VisualEffect.OnHitVisualEffect;
import com.example.sampleapp.mgp2d.core.AnimatedSprite;
import com.example.sampleapp.mgp2d.core.EmptyEntity;
import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Singleton;
import com.example.sampleapp.mgp2d.core.Vector2;

import java.util.Vector;
import java.util.Random;

/** @noinspection FieldCanBeLocal*/
public class PlayerObj extends GameEntity implements ObjectBase, Damageable {
    private static PlayerObj instance;
    private UIHealthBar healthBar;
    private UICDBar dashCDBar;
    public HealthSystem healthSystem;
    private OnHitVisualEffect hitVisualEffect;

    private Vector2 inputDirection = null;
    public void SetInputDirection(Vector2 inputDirection) {
        this.inputDirection = inputDirection;
    }

    private final float movementSpeed = 650.0f;
    private final float shootSpeed = 1000.0f;
    private final float fireRate = 1.0f;
    private float shootTimer = 0.0f;

    private final float maxHealth = 1000.0f;

    public int value = 0;
    public Ability currAbility;
    public int strength = 0;
    public int defence = 0;
    public int speed = 0;

    private Vibrator _vibrator;
    public Vector2 targetPos;

    private final Vector2 dashVelocity = new Vector2(0, 0);
    private float dashTimer = 0.0f;
    private final float dashDuration = 0.3f;
    private final float dashSpeed = 4500.0f;
    private float dashCDTimer;
    private final float dashCDDuration = 2.0f;
    private boolean isDashing = false;

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

        _ordinal = 1;
        isActive = true;

        dashCDBar = new UICDBar(150, 150, 150, 150, false);
        dashCDBar.setFillMode(UICDBar.FillMode.CounterClockwise360);
        dashCDBar.setCooldown(dashCDDuration);
        dashCDBar.setTimer(dashCDTimer);
        dashCDBar.setColors(Color.DKGRAY, Color.CYAN);
        dashCDBar.zIndex = 1;
        UIManager.getInstance().addElement(dashCDBar);

        healthSystem = new HealthSystem(this, maxHealth);
        healthBar = new UIHealthBar(pos, 150, 30);
        healthBar.offset.set(0, -125);
        healthBar.setOwner(this);
        healthBar.setValue(maxHealth);
        UIManager.getInstance().addElement(healthBar);

        hitVisualEffect = new OnHitVisualEffect(this);
        healthSystem.setOnDamageListener((damage, hp) -> {
            healthBar.updateValue(hp);
            hitVisualEffect.playHitFlash();
            if(hp <= 0) {
                UIManager.getInstance().removeElement(healthBar);
            }

            float ex = _position.x + Utilities.RandomFloat(-50, 50);
            float ey = _position.y + 50;

            if (damage > 40.0f)
                DamageTextManager.getInstance().spawnText("CRIT " + (int)damage, ex, ey, Color.RED);
            else
                DamageTextManager.getInstance().spawnText("-" + (int)damage, ex, ey, Color.YELLOW);

            float healthPer = hp / maxHealth;
            if ((damage > 40.0f || healthPer < 0.5f) &&
                    _vibrator.hasVibrator() &&
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                _vibrator.vibrate(VibrationEffect.createOneShot(100L, (int) (damage + 50)));
            }
        });
    }

    @Override
    public void onUpdate(float dt) {
        super.onUpdate(dt);
        hitVisualEffect.onUpdate(dt);
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
        if(currAbility != null) {
            currAbility.onUpdate(dt);
        }
    }

    private void FindNearestEnemy() {
        MessageWRU message = new MessageWRU(this, MessageWRU.SEARCH_TYPE.NEAREST_ENEMY, shootSpeed);
        PostOffice.getInstance().send("Scene", message);

        if(targetGO != null) {
            Vector2 dir = targetGO._position.subtract(_position);
            targetPos = dir.multiply(-100f);
        }
    }

    private void HandMovement(float dt) {
        if(!isDashing) {
            Vector2 movementDirection = new Vector2(inputDirection.x, -inputDirection.y);
            _position.x += movementDirection.x * movementSpeed * dt;
            _position.y += -movementDirection.y * movementSpeed * dt;
            if(!movementDirection.equals(new Vector2(0, 0))) {
                facingDirection.set(movementDirection.x, -movementDirection.y);
                _rotationZ = (float) Math.toDegrees(Vector2.Angle(movementDirection, new Vector2(-1, 0)));
            }
        }
        else {
            dashTimer -= dt;

            _position.x += dashVelocity.x * dt;
            _position.y += dashVelocity.y * dt;

            float dashFriction = 8.5f;
            dashVelocity.x -= dashVelocity.x * dashFriction * dt;
            dashVelocity.y -= dashVelocity.y * dashFriction * dt;

            if(dashTimer <= 0.0f) {
                isDashing = false;
                dashCDTimer = dashCDDuration;
                dashVelocity.set(0, 0);
            }
        }

        if(dashCDTimer > 0.0f) {
            dashCDTimer -= dt;
        }

        dashCDBar.setTimer(dashCDTimer);
    }

    @Override
    public void onRender(Canvas canvas) {
        super.onRender(canvas);
    }

    @Override
    public boolean handle(Message message) {
        return false;
    }

    @Override
    public void TakeDamage(float amount) {
        healthSystem.takeDamage(amount);
    }

    public void Dash(Vector2 direction) {
        if (isDashing || dashCDTimer > 0.0f) return;
        isDashing = true;
        dashVelocity.set(direction.multiply(dashSpeed));
        dashTimer = dashDuration;
    }
}
