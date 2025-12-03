package com.example.sampleapp.Entity.Enemies.Slime;

import com.example.sampleapp.Collision.Colliders.CircleCollider2D;
import com.example.sampleapp.Core.HealthSystem;
import com.example.sampleapp.Entity.Enemies.Enemy;
import com.example.sampleapp.Managers.UIManager;
import com.example.sampleapp.UI.Bars.UIHealthBar;
import com.example.sampleapp.mgp2d.core.Vector2;

import java.util.Objects;

/** @noinspection FieldCanBeLocal*/
public class Slime extends Enemy {

    // --- Stats ---
    public static final float ATTACK_RANGE = 210.0f;
    private static final float MAX_ATTACK_COOLDOWN = 2.0f;
    private static final float MAX_HEALTH = 150.0f;

    // --- Properties ---
    private float attackCooldown = 0.0f;

    public void SetAttackCooldown() {
        this.attackCooldown = MAX_ATTACK_COOLDOWN;
    }

    @Override
    public void onCreate(Vector2 pos, Vector2 scale) {
        super.onCreate(pos, scale);
        Init(pos, MAX_HEALTH);

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
            if(CheckIfPlayerNear(ATTACK_RANGE) && !Objects.equals(sm.GetCurrentStateID(), "Death")) {
                sm.ChangeState("SlimeAttack");
            }
        }
        else attackCooldown -= dt;

        sm.Update(dt);
    }
}
