package com.example.sampleapp.Entity.Enemies;

import com.example.sampleapp.Entity.Player.PlayerObj;
import com.example.sampleapp.Interface.Damageable;
import com.example.sampleapp.PostOffice.PostOffice;
import com.example.sampleapp.PostOffice.Message;
import com.example.sampleapp.PostOffice.ObjectBase;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

public class Enemy extends GameEntity implements ObjectBase, Damageable {
    @Override
    public void onCreate(Vector2 pos, Vector2 scale){
        super.onCreate(pos, scale);
        PostOffice.getInstance().register(String.valueOf(_id), this);
    }

    @Override
    public boolean handle(Message message) {
        return false;
    }

    @Override
    public void TakeDamage(float amount) {
        if(currentHealth < 0) {
            if(sm != null) sm.ChangeState("Death");
            currentHealth = 0;
            return;
        }

        currentHealth -= amount;
    }

    public boolean CheckIfPlayerNear(float threshold) {
        if(PlayerObj.instance == null) return false;
        return getPosition().Distance(PlayerObj.instance.getPosition()) <= threshold;
    }
}
