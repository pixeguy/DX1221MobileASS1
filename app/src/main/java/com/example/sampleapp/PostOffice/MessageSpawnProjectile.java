package com.example.sampleapp.PostOffice;

import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

public class MessageSpawnProjectile extends Message{

    public enum PROJECTILE_TYPE {
        PLAYER_FIRE_MISSILE,
        ENEMY_FIRE_MISSILE,
        ENEMY_TOXIC_MISSILE
    }

    public PROJECTILE_TYPE projectileType;
    public GameEntity go;
    public float movementSpeed;
    public Vector2 pos;
    public Vector2 facingDirection = new Vector2(0, 0);

    public MessageSpawnProjectile(GameEntity go,
                                  PROJECTILE_TYPE projectileType,
                                  float movementSpeed, Vector2 pos) {
        this.projectileType = projectileType;
        this.go = go;
        this.movementSpeed = movementSpeed;
        this.pos = pos;
    }

    public MessageSpawnProjectile(GameEntity go,
                                  PROJECTILE_TYPE projectileType,
                                  float movementSpeed, Vector2 pos,
                                  Vector2 facingDirection) {
        this.projectileType = projectileType;
        this.go = go;
        this.movementSpeed = movementSpeed;
        this.pos = pos;
        this.facingDirection = facingDirection;
    }
}
