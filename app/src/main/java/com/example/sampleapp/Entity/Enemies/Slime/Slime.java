package com.example.sampleapp.Entity.Enemies.Slime;

import android.graphics.Canvas;
import android.util.Log;

import com.example.sampleapp.Collision.Colliders.CircleCollider2D;
import com.example.sampleapp.Collision.Colliders.Collider2D;
import com.example.sampleapp.Entity.Enemies.Enemy;
import com.example.sampleapp.Entity.Player.PlayerObj;
import com.example.sampleapp.PostOffice.Message;
import com.example.sampleapp.Statemchine.Statemachine;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

import java.util.Objects;
import java.util.Random;

/** @noinspection FieldCanBeLocal*/
public class Slime extends Enemy {

    public float attackRange = 210.0f;

    private float attackCooldown = 0.0f;
    private final float maxAttackCooldown = 2.0f;
    private final float maxHealth = 150.0f;

    public void SetAttackCooldown() {
        this.attackCooldown = maxAttackCooldown;
    }

    @Override
    public void onCreate(Vector2 pos, Vector2 scale) {
        super.onCreate(pos, scale);
        currentHealth = maxHealth;

        sm.AddState(new SlimeRunState(this, "SlimeRun"));
        sm.AddState(new SlimeIdleState(this, "SlimeIdle"));
        sm.AddState(new SlimeDeathState(this, "Death"));
        sm.AddState(new SlimeAttackState(this, "SlimeAttack"));
        collider = new CircleCollider2D(this, animatedSprite.GetRect(_position, _scale).width() / 2.0f - 130.0f);
    }

    @Override
    public void onUpdate(float dt) {
        super.onUpdate(dt);
        if(!Objects.equals(sm.GetCurrentStateID(), "SlimeAttack") && attackCooldown < 0.0f) {
            if(CheckIfPlayerNear(attackRange) && !Objects.equals(sm.GetCurrentStateID(), "Death")) {
                sm.ChangeState("SlimeAttack");
            }
        }
        else attackCooldown -= dt;

        sm.Update(dt);
    }
}
