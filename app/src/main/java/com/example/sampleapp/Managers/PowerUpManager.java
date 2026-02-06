package com.example.sampleapp.Managers;

import com.example.sampleapp.Enums.PowerUpList;
import com.example.sampleapp.mgp2d.core.Singleton;

import java.util.List;

public class PowerUpManager extends Singleton<PowerUpManager> {

    List<PowerUpList> addVailablePowerUp;

    public PowerUpManager getInstance() {
        return Singleton.getInstance(PowerUpManager.class);
    }

    public void AddPowerUp(PowerUpList powerUp) {
        addVailablePowerUp.add(powerUp);
    }

    public void RemovePowerUp(PowerUpList powerUp) {
        addVailablePowerUp.remove(powerUp);
    }
}
