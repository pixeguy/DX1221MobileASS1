package com.example.sampleapp.PostOffice;

import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

public class MessageSpawnRearProjectile extends Message{

    public enum PROJECTILE_TYPE {
        PLAYER_FIRE_MISSILE,
    }

    public MessageSpawnRearProjectile.PROJECTILE_TYPE projectileType;
    public GameEntity go;
    public Vector2 targetPos;
    public float movementSpeed;
    public Vector2 pos;
    public Vector2 facingDirection = new Vector2(0, 0);


    public MessageSpawnRearProjectile(Vector2 targetpos, MessageSpawnRearProjectile.PROJECTILE_TYPE projectileType,
                                  float movementSpeed, Vector2 pos){
        this.projectileType = projectileType;
        this.targetPos = targetpos;
        this.movementSpeed = movementSpeed;
        this.pos = pos;
    }
}
