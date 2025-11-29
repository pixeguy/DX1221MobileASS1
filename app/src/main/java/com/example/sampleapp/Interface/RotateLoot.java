package com.example.sampleapp.Interface;

import com.example.sampleapp.Entity.Buttons.IActivatable;
import com.example.sampleapp.Entity.Inventory.LootObj;
import com.example.sampleapp.Scenes.MainGameScene;

public class RotateLoot implements IActivatable {
    private MainGameScene scene;

    public RotateLoot(MainGameScene mainGameScene) {
        this.scene = mainGameScene;
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
