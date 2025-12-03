package com.example.sampleapp.Entity.Enemies;

import android.graphics.Color;

import com.example.sampleapp.Core.HealthSystem;
import com.example.sampleapp.Entity.Player.PlayerObj;
import com.example.sampleapp.Interface.Damageable;
import com.example.sampleapp.Managers.DamageTextManager;
import com.example.sampleapp.Managers.UIManager;
import com.example.sampleapp.PostOffice.MessageCount;
import com.example.sampleapp.PostOffice.PostOffice;
import com.example.sampleapp.PostOffice.Message;
import com.example.sampleapp.PostOffice.ObjectBase;
import com.example.sampleapp.Statemchine.Statemachine;
import com.example.sampleapp.UI.Bars.UIHealthBar;
import com.example.sampleapp.Utilities.Utilities;
import com.example.sampleapp.VisualEffect.OnHitVisualEffect;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

import java.util.Random;

public class Enemy extends GameEntity implements ObjectBase, Damageable {
    protected HealthSystem healthSystem;
    protected OnHitVisualEffect hitVisualEffect;
    public UIHealthBar healthBar;

    public void Init(Vector2 pos, float MAX_HEALTH){
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
        isActive = true;

        hitVisualEffect = new OnHitVisualEffect(this);
        healthSystem = new HealthSystem(this, MAX_HEALTH);

        healthBar = new UIHealthBar(pos, 150, 30);
        healthBar.offset.set(0, -125);
        healthBar.setValue(MAX_HEALTH);
        healthBar.setOwner(this);
        UIManager.getInstance().addElement(healthBar);

        healthSystem.setOnDamageListener((damage, hp) -> {
            healthBar.updateValue(hp);
            hitVisualEffect.playHitFlash();

            float ex = _position.x + Utilities.RandomFloat(-50, 50);
            float ey = _position.y + 50; // slightly above head

            if (damage > 45)
                DamageTextManager.getInstance().spawnText("CRIT " + (int)damage, ex, ey, Color.RED);
            else
                DamageTextManager.getInstance().spawnText("-" + (int)damage, ex, ey, Color.YELLOW);

            if(hp <= 0) {
                UIManager.getInstance().removeElement(healthBar);
            }
        });
    }

    @Override
    public void onUpdate(float dt) {
        super.onUpdate(dt);
        hitVisualEffect.onUpdate(dt);
    }

    @Override
    public boolean handle(Message message) {
        if(message instanceof MessageCount) {
            return !canDestroy() && isAlive;
        }

        return false;
    }

    @Override
    public void TakeDamage(float amount) {
        healthSystem.takeDamage(amount);
    }

    public boolean CheckIfPlayerNear(float threshold) {
        if(PlayerObj.getInstance() == null) return false;
        return getPosition().Distance(PlayerObj.getInstance().getPosition()) <= threshold;
    }
}
