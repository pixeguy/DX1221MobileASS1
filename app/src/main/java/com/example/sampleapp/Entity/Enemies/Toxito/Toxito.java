package com.example.sampleapp.Entity.Enemies.Toxito;

import com.example.sampleapp.Collision.Colliders.CircleCollider2D;
import com.example.sampleapp.Core.HealthSystem;
import com.example.sampleapp.Entity.Enemies.Enemy;
import com.example.sampleapp.Managers.UIManager;
import com.example.sampleapp.UI.Bars.UIHealthBar;
import com.example.sampleapp.mgp2d.core.Vector2;

import java.util.Objects;

/** @noinspection FieldCanBeLocal*/
public class Toxito extends Enemy {
    // --- Stats ---
    public static float DETECTION_RANGE = 900.0f;
    public static float MOVE_SPEED = 500.0f;
    private static final float MAX_ATTACK_COOLDOWN = 2.0f;
    private static final float MAX_HEALTH = 100.0f;

    // --- Properties ---
    private float attackCooldown = 2.0f;


    public void SetAttackCooldown() {
        this.attackCooldown = MAX_ATTACK_COOLDOWN;
    }

    @Override
    public void onCreate(Vector2 pos, Vector2 scale) {
        super.onCreate(pos, scale);
        Init(pos, MAX_HEALTH);
        facingDirection.set(0, -1);

        sm.AddState(new ToxitoMoveState(this, "Run"));
        sm.AddState(new ToxitoIdleState(this, "Idle"));
        sm.AddState(new ToxitoDeathState(this, "Death"));
        sm.AddState(new ToxitoAttackState(this, "Attack"));

        collider = new CircleCollider2D(this, animatedSprite.GetRect(_position, _scale).width() / 2.0f - 130.0f);
    }

    @Override
    public void onUpdate(float dt) {
        super.onUpdate(dt);

        if(!Objects.equals(sm.GetCurrentStateID(), "Attack") && attackCooldown < 0.0f) {
            if(!Objects.equals(sm.GetCurrentStateID(), "Death") && !CheckIfPlayerNear(DETECTION_RANGE)) {
                sm.ChangeState("Attack");
            }
        }
        else attackCooldown -= dt;

        sm.Update(dt);
    }
}
