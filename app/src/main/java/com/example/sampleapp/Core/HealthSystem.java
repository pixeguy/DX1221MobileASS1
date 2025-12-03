package com.example.sampleapp.Core;

import android.util.Log;

import com.example.sampleapp.mgp2d.core.GameEntity;

public class HealthSystem {
    private final GameEntity entity;
    private float maxHealth = 100f;
    private float currentHealth = 100f;

    public HealthSystem(GameEntity entity) {
        this.entity = entity;
    }

    public HealthSystem(GameEntity entity, float maxHealth) {
        this.entity = entity;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
    }

    public float getCurrentHealth() {
        return currentHealth;
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }

    private OnDamageListener listener;

    public interface OnDamageListener {
        void onDamaged(float damageAmount, float newHealth);
    }

    public void setOnDamageListener(OnDamageListener listener) {
        this.listener = listener;
    }

    public void takeDamage(float damage) {
        if (!entity.isAlive) return;

        currentHealth -= damage;
        if (currentHealth < 0) currentHealth = 0;

        if (listener != null)
            listener.onDamaged(damage, currentHealth);

        if (currentHealth <= 0)
            die();
    }

    public void die() {
        entity.isAlive = false;
        // Trigger death effect here
        if(entity.sm != null) entity.sm.ChangeState("Death");
    }
}
