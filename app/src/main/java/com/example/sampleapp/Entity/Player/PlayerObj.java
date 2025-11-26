package com.example.sampleapp.Entity.Player;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import com.example.sampleapp.Collision.Colliders.CircleCollider2D;
import com.example.sampleapp.Interface.Damageable;
import com.example.sampleapp.PostOffice.Message;
import com.example.sampleapp.PostOffice.MessageSpawnProjectile;
import com.example.sampleapp.PostOffice.MessageWRU;
import com.example.sampleapp.PostOffice.ObjectBase;
import com.example.sampleapp.Enums.SpriteAnimationList;
import com.example.sampleapp.PostOffice.PostOffice;
import com.example.sampleapp.mgp2d.core.AnimatedSprite;
import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

/** @noinspection FieldCanBeLocal*/
public class PlayerObj extends GameEntity implements ObjectBase, Damageable {
    public static PlayerObj instance;
    private Vector2 inputDirection = new Vector2(0, 0);
    public void SetInputDirection(Vector2 inputDirection) {
        this.inputDirection = inputDirection;
    }

    private final float movementSpeed = 500.0f;
    private final float shootSpeed = 1000.0f;
    private final float fireRate = 1.0f;
    private float shootTimer = 0.0f;

    private Vibrator _vibrator;

    public PlayerObj() {
        super();
        instance = this;
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

        currentHealth = 1000.0f;
    }

    @Override
    public void onUpdate(float dt) {
        super.onUpdate(dt);
        HandMovement(dt);
        FindNearestEnemy();
        shootTimer -= dt;
        if(targetGO != null && shootTimer <= 0.0f) {

            Vector2 projectilePosition = _position.add(facingDirection.multiply(100.0f));
            // Send Message To Spawn Projectile

            MessageSpawnProjectile message = new MessageSpawnProjectile(this,
                    MessageSpawnProjectile.PROJECTILE_TYPE.PLAYER_FIRE_MISSILE,
                    1000.0f, projectilePosition);
            PostOffice.getInstance().send("Scene", message);
            shootTimer = fireRate;
        }
    }

    private void FindNearestEnemy() {
        MessageWRU message = new MessageWRU(this, MessageWRU.SEARCH_TYPE.NEAREST_ENEMY, shootSpeed);
        PostOffice.getInstance().send("Scene", message);
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
    }

    @Override
    public boolean handle(Message message) {
        return false;
    }

    @Override
    public void TakeDamage(float amount) {
        if(currentHealth <= 0) {
            currentHealth = 0;
            return;
        }

        if (_vibrator.hasVibrator()) {
            long[] timings = {0, 100, 0, 200}; // Delay, Vibrate, Delay, Vibrate
            int[] amplitudes = {0, 255, 0, 128}; // Amplitudes (0-255)
            int repeatIndex = -1; // -1 for no repeat, or index of pattern to repeat from

            _vibrator.vibrate(VibrationEffect.createOneShot(100, 255));
        }

        Log.d("PlayerObj", "PlayerObj Current Health" + currentHealth);

        currentHealth -= amount;
    }
}
