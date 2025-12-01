package com.example.sampleapp.Interface;

import com.example.sampleapp.Entity.Buttons.IActivatable;
import com.example.sampleapp.Entity.Inventory.LootObj;
import com.example.sampleapp.Scenes.GameLevel.GameLevelScene;
import com.example.sampleapp.Scenes.MainGameScene;
import com.example.sampleapp.mgp2d.core.GameScene;

public class RotateLoot implements IActivatable {
    private GameLevelScene scene;

    public RotateLoot(GameLevelScene gamescene) {
        this.scene = gamescene;
    }

    @Override
    public void execute() {
        if(scene.draggingObj instanceof LootObj)
        {
            LootObj loot = (LootObj) scene.draggingObj;
            loot.rotate90();
            loot.sourceButton.rotate90();
        }
    }
}
