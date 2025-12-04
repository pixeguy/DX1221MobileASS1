package com.example.sampleapp.Entity;

import com.example.sampleapp.Entity.Enemies.Enemy;
import com.example.sampleapp.Enums.SpriteAnimationList;
import com.example.sampleapp.Utilities.Utilities;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

public class EnemySpawnMarker extends GameEntity {
    private Enemy storedEnemy;

    private float currentTimer = 0.0f;

    private Vector2 originalScale;

    public void onCreate(Enemy enemy, Vector2 pos, Vector2 scale) {
        onCreate(pos, new Vector2(0, 0
        ));
        SetAnimation(SpriteAnimationList.EnemySpawnMarker);
        storedEnemy = enemy;
        enemy.healthBar.visible = false;
        isActive = true;
        originalScale = scale;
    }

    @Override
    public void onUpdate(float dt) {
        super.onUpdate(dt);
        currentTimer += dt;
        float maxWaitTime = 2.0f;
        _scale.x = Utilities.lerp(0f, originalScale.x, currentTimer / maxWaitTime);
        _scale.y = Utilities.lerp(0f, originalScale.y, currentTimer / maxWaitTime);

        if(currentTimer >= maxWaitTime) {
            storedEnemy.isActive = true;
            storedEnemy.healthBar.visible = true;
            destroy();
        }
    }
}
