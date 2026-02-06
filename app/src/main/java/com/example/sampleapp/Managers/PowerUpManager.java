package com.example.sampleapp.Managers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.sampleapp.Entity.Player.PlayerObj;
import com.example.sampleapp.Enums.PowerUpList;
import com.example.sampleapp.PowerUp.DefensePU;
import com.example.sampleapp.PowerUp.DoubleCoinPU;
import com.example.sampleapp.PowerUp.PowerUp;
import com.example.sampleapp.PowerUp.ShieldPU;
import com.example.sampleapp.PowerUp.SpeedBoostPU;
import com.example.sampleapp.PowerUp.StrengthPU;
import com.example.sampleapp.UI.Icon.UIIcon;
import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.Singleton;

import java.util.HashMap;

public class PowerUpManager extends Singleton<PowerUpManager> {

    HashMap<String, PowerUp> addAvailablePowerUp = new HashMap<>();

    public static PowerUpManager getInstance() {
        return Singleton.getInstance(PowerUpManager.class);
    }

    public void addPowerUp(PowerUpList powerUpList) {
        switch (powerUpList)
        {
            case DOUBLE_COINS:
                addAvailablePowerUp.put(powerUpList.getName(), new DoubleCoinPU(powerUpList.getIconRes()));
                break;
            case SPEED_BOOST:
                addAvailablePowerUp.put(powerUpList.getName(), new SpeedBoostPU(powerUpList.getIconRes()));
                break;
            case SHIELD:
                addAvailablePowerUp.put(powerUpList.getName(), new ShieldPU(powerUpList.getIconRes()));
                break;
            case STRENGTH_BOOST:
                addAvailablePowerUp.put(powerUpList.getName(), new StrengthPU(powerUpList.getIconRes()));
                break;
            case DEFENCE_BOOST:
                addAvailablePowerUp.put(powerUpList.getName(), new DefensePU(powerUpList.getIconRes()));
                break;
        }
    }

    public boolean hasPowerUp(String powerUpName) {
        return addAvailablePowerUp.containsKey(powerUpName);
    }

    public void useAll()
    {
        float yPosition = 300.0f;
        float xPositon = GameActivity.instance.getResources().getDisplayMetrics().widthPixels - 100.0f;
        for (PowerUp powerUp : addAvailablePowerUp.values()) {
            UIIcon powerUpIcon = new UIIcon(xPositon, yPosition, 100, 100);
            Bitmap iconBmp = BitmapFactory.decodeResource(GameActivity.instance.getResources(), powerUp.spriteID);
            powerUpIcon.setBaseSprite(iconBmp);
            powerUpIcon.zIndex = 1;
            UIManager.getInstance().addElement(powerUpIcon);
            powerUp.Use();

            if(powerUp.spriteID == PowerUpList.SHIELD.getIconRes())
            {
                PlayerObj.getInstance().shieldIcon = powerUpIcon;
            }

            yPosition += 150.0f;
        }
        addAvailablePowerUp.clear();
    }
}
