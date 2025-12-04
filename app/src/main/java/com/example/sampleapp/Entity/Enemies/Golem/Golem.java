package com.example.sampleapp.Entity.Enemies.Golem;

import com.example.sampleapp.Collision.Colliders.CircleCollider2D;
import com.example.sampleapp.Core.HealthSystem;
import com.example.sampleapp.Entity.Enemies.Enemy;
import com.example.sampleapp.Managers.UIManager;
import com.example.sampleapp.UI.Bars.UIHealthBar;
import com.example.sampleapp.mgp2d.core.Vector2;

import java.util.Objects;
import java.util.Random;

/** @noinspection FieldCanBeLocal*/
public class Golem extends Enemy {
    // --- Stats ---
    public static int MAX_NUM_SLAM = 3;
    public static float DETECT_RANGE = 800.0f;
    public static float WALK_SPEED = 100.0f;
    public static float RUN_SPEED = 300.0f;
    public static float ATTACK_RANGE = 260.0f;
    private final float MAX_SLAM_CD = 2.0f;
    private final float MAX_SPIN_CD = 10.0f;
    private final float MAX_HEALTH = 2500000.0f;

    // --- Properties ---
    private float slamAttackCooldown = 0.0f;
    private float spinAttackCD = 0.0f;

    public boolean isAttacking = false;

    public void SetAttackCooldown() {
        this.slamAttackCooldown = MAX_SLAM_CD;
    }

    public void SetSpinAttackCooldown() {
        this.spinAttackCD = MAX_SPIN_CD;
    }

    @Override
    public void onCreate(Vector2 pos, Vector2 scale) {
        super.onCreate(pos, scale);
        Init(pos, MAX_HEALTH);
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

        if(!isAttacking && !Objects.equals(sm.GetCurrentStateID(), "Death"))
        {
            if(!Objects.equals(sm.GetCurrentStateID(), "SlamAttack") && slamAttackCooldown <= 0.0f) {
                if(CheckIfPlayerNear(ATTACK_RANGE)) {
                    sm.ChangeState("SlamAttack");
                }
            }

            if(!Objects.equals(sm.GetCurrentStateID(), "SpinAttack") && spinAttackCD <= 0.0f) {
                Random rand = new Random();
                float percent = rand.nextFloat();
                if(!CheckIfPlayerNear(DETECT_RANGE) && percent < 0.1f) {
                    sm.ChangeState("SpinAttack");
                }
            }
        }

        if(spinAttackCD > 0.0f) spinAttackCD -= dt;
        if(slamAttackCooldown > 0.0f) slamAttackCooldown -= dt;

        sm.Update(dt);
    }
}
