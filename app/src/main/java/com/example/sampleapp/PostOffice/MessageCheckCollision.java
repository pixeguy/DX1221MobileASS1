package com.example.sampleapp.PostOffice;

import com.example.sampleapp.Collision.Colliders.Collider2D;
import com.example.sampleapp.mgp2d.core.GameEntity;

import java.util.ArrayList;
import java.util.List;

public class MessageCheckCollision extends Message {
    public GameEntity gameEntityToCheck;

    public List<Collider2D> collisionList = new ArrayList<>();

    public MessageCheckCollision(GameEntity gameEntityToCheck) {
        this.gameEntityToCheck = gameEntityToCheck;
    }
}
