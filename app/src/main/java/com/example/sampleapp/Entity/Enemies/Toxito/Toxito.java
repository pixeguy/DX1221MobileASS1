package com.example.sampleapp.Entity.Enemies.Toxito;

import android.graphics.Canvas;

import com.example.sampleapp.Collision.Colliders.CircleCollider2D;
import com.example.sampleapp.Entity.Enemies.Enemy;
import com.example.sampleapp.Entity.Enemies.Slime.SlimeAttackState;
import com.example.sampleapp.Entity.Enemies.Slime.SlimeDeathState;
import com.example.sampleapp.Entity.Enemies.Slime.SlimeIdleState;
import com.example.sampleapp.Entity.Enemies.Slime.SlimeRunState;
import com.example.sampleapp.Interface.Damageable;
import com.example.sampleapp.PostOffice.Message;
import com.example.sampleapp.PostOffice.ObjectBase;
import com.example.sampleapp.Statemchine.Statemachine;
import com.example.sampleapp.mgp2d.core.Vector2;

import java.util.Objects;
import java.util.Random;

/** @noinspection FieldCanBeLocal*/
public class Toxito extends Enemy {
    public static float detectionRange = 900.0f;
    public static float baseSpeed = 500.0f;

    private float attackCooldown = 2.0f;
    private final float maxAttackCooldown = 2.0f;
    private final float maxHealth = 100.0f;

    public void SetAttackCooldown() {
        this.attackCooldown = maxAttackCooldown;
    }

    @Override
    public void onCreate(Vector2 pos, Vector2 scale){
        super.onCreate(pos, scale);
        currentHealth = maxHealth;
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
            if(!Objects.equals(sm.GetCurrentStateID(), "Death") && !CheckIfPlayerNear(detectionRange)) {
                sm.ChangeState("Attack");
            }
        }
        else attackCooldown -= dt;

        sm.Update(dt);
    }
}
