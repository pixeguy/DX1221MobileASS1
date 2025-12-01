package com.example.sampleapp.Entity;

import com.example.sampleapp.Enums.SpriteAnimationList;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

public class EnemySpawnMarker extends GameEntity {
    private GameEntity storedEnemy;

    private float currentTimer = 0.0f;

    public void onCreate(GameEntity enemy, Vector2 pos, Vector2 scale) {
        onCreate(pos, scale);
        SetAnimation(SpriteAnimationList.EnemySpawnMarker);
        storedEnemy = enemy;
        isActive = true;
    }

    @Override
    public void onUpdate(float dt) {
        super.onUpdate(dt);
        currentTimer += dt;
        float maxWaitTime = 2.0f;
        if(currentTimer >= maxWaitTime) {
            storedEnemy.isActive = true;
            destroy();
        }
    }
}
