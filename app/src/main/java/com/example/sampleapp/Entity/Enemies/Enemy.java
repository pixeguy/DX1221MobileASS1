package com.example.sampleapp.Entity.Enemies;

import com.example.sampleapp.Entity.Player.PlayerObj;
import com.example.sampleapp.Interface.Damageable;
import com.example.sampleapp.PostOffice.PostOffice;
import com.example.sampleapp.PostOffice.Message;
import com.example.sampleapp.PostOffice.ObjectBase;
import com.example.sampleapp.Statemchine.Statemachine;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

import java.util.Random;

public class Enemy extends GameEntity implements ObjectBase, Damageable {
    @Override
    public void onCreate(Vector2 pos, Vector2 scale){
        super.onCreate(pos, scale);
        PostOffice.getInstance().register(String.valueOf(_id), this);
        Random random = new Random();
        int randomDirection = random.nextInt(4);
        switch (randomDirection) {
            case 0:
                facingDirection = new Vector2(0, -1);
                break;
            case 1:
                facingDirection = new Vector2(0, 1);
                break;
            case 2:
                facingDirection = new Vector2(-1, 0);
                break;
            case 3:
                facingDirection = new Vector2(1, 0);
                break;
        }
        sm = new Statemachine();
    }

    @Override
    public boolean handle(Message message) {
        return false;
    }

    @Override
    public void TakeDamage(float amount) {
        if(currentHealth < 0) {
            if(sm != null) sm.ChangeState("Death");
            isAlive = false;
            currentHealth = 0;
            return;
        }

        currentHealth -= amount;
    }

    public boolean CheckIfPlayerNear(float threshold) {
        if(PlayerObj.getInstance() == null) return false;
        return getPosition().Distance(PlayerObj.getInstance().getPosition()) <= threshold;
    }
}
