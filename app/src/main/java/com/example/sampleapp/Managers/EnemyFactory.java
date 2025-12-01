package com.example.sampleapp.Managers;

import com.example.sampleapp.Entity.Enemies.Enemy;
import com.example.sampleapp.Entity.Enemies.Golem.Golem;
import com.example.sampleapp.Entity.Enemies.Slime.Slime;
import com.example.sampleapp.Entity.Enemies.Toxito.Toxito;
import com.example.sampleapp.mgp2d.core.Singleton;
import com.example.sampleapp.mgp2d.core.Vector2;

public class EnemyFactory {
    public enum EnemyType {
        Slime,
        Toxito,
        Golem
    }

    public static Enemy CreateEnemy(EnemyType enemyType, Vector2 position) {
        Enemy enemy = null;
        Vector2 scale = new Vector2(0, 0);
        switch (enemyType)
        {
            case Slime:
                enemy = new Slime();
                scale.set(2, 2);
                break;
            case Toxito:
                enemy = new Toxito();
                scale.set(2, 2);
                break;
            case Golem:
                enemy = new Golem();
                scale.set(1.5f, 1.5f);
                break;
        }

        enemy.onCreate(position, scale);
        enemy.isActive = false;
        return enemy;
    }
}
