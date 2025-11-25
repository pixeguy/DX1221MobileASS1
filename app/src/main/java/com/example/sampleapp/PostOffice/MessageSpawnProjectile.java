package com.example.sampleapp.PostOffice;

import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

public class MessageSpawnProjectile extends Message{

    public enum PROJECTILE_TYPE {
        PLAYER_MAGIC_MISSLE,
        ENEMY_MAGIC_MISSLE
    }

    public PROJECTILE_TYPE projectileType;
    public GameEntity go;
    public float movementSpeed;
    public Vector2 pos;

    public MessageSpawnProjectile(GameEntity go, PROJECTILE_TYPE projectileType, float movementSpeed, Vector2 pos) {
        this.projectileType = projectileType;
        this.go = go;
        this.movementSpeed = movementSpeed;
        this.pos = pos;
    }
}
