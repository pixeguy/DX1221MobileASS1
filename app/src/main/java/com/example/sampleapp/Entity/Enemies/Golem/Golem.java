package com.example.sampleapp.Entity.Enemies.Golem;

import com.example.sampleapp.Collision.Colliders.CircleCollider2D;
import com.example.sampleapp.Entity.Enemies.Enemy;
import com.example.sampleapp.Entity.Enemies.Slime.SlimeAttackState;
import com.example.sampleapp.Entity.Enemies.Slime.SlimeDeathState;
import com.example.sampleapp.Entity.Enemies.Slime.SlimeIdleState;
import com.example.sampleapp.Entity.Enemies.Slime.SlimeRunState;
import com.example.sampleapp.mgp2d.core.Vector2;

import java.util.Objects;
import java.util.Random;

/** @noinspection FieldCanBeLocal*/
public class Golem extends Enemy {
    public static int numSlam = 3;
    public static float detectionRange = 800.0f;
    public static float walkSpeed = 100.0f;

    public static float runSpeed = 300.0f;

    public static float attackRange = 260.0f;

    private float slamAttackCooldown = 0.0f;

    private float spinAttackCD = 0.0f;

    private final float maxAttackCooldown = 2.0f;
    private final float maxSpinAttackCD = 5.0f;
    private final float maxHealth = 250.0f;

    public boolean isAttacking = false;

    public void SetAttackCooldown() {
        this.slamAttackCooldown = maxAttackCooldown;
    }

    public void SetSpinAttackCooldown() {
        this.spinAttackCD = maxSpinAttackCD;
    }

    @Override
    public void onCreate(Vector2 pos, Vector2 scale) {
        super.onCreate(pos, scale);
        currentHealth = maxHealth;
        mass = 0.0f;

        int random = (int) (Math.random() * 4);
        switch (random){
            case 0:
                facingDirection.set(0, -1);
                break;
            case 1:
                facingDirection.set(0, 1);
                break;
            case 2:
                facingDirection.set(-1, 0);
                break;
            case 3:
                facingDirection.set(1, 0);
                break;
        }

        sm.AddState(new GolemIdleState(this, "Idle"));
        sm.AddState(new GolemWalkState(this, "Walk"));
        sm.AddState(new GolemRunState(this, "Run"));
        sm.AddState(new GolemDeathState(this, "Death"));
        sm.AddState(new GolemSlamAttackState(this, "SlamAttack"));
        sm.AddState(new GolemSpinAttackState(this, "SpinAttack"));
        collider = new CircleCollider2D(this, animatedSprite.GetRect(_position, _scale).width() / 2.0f - 150.0f);
    }

    @Override
    public void onUpdate(float dt) {
        super.onUpdate(dt);

        if(!isAttacking)
        {
            if(!Objects.equals(sm.GetCurrentStateID(), "SlamAttack") && slamAttackCooldown <= 0.0f) {
                if(CheckIfPlayerNear(attackRange) && !Objects.equals(sm.GetCurrentStateID(), "Death")) {
                    sm.ChangeState("SlamAttack");
                }
            }

            if(!Objects.equals(sm.GetCurrentStateID(), "SpinAttack") && spinAttackCD <= 0.0f) {
                Random rand = new Random();
                float percent = rand.nextFloat();
                if(!Objects.equals(sm.GetCurrentStateID(), "Death") && percent < 0.1f) {
                    sm.ChangeState("SpinAttack");
                }
            }
        }

        if(spinAttackCD > 0.0f) spinAttackCD -= dt;
        if(slamAttackCooldown > 0.0f) slamAttackCooldown -= dt;

        sm.Update(dt);
    }
}
