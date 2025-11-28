package com.example.sampleapp.Interface;

import com.example.sampleapp.Entity.Buttons.IActivatable;
import com.example.sampleapp.Scenes.MainGameScene;

public class CloseLooting implements IActivatable {
    private MainGameScene scene;

    public CloseLooting(MainGameScene mainGameScene) {
        this.scene = mainGameScene;
    }

    @Override
    public void execute() {
        scene.EndLootPhase();
    }
}
