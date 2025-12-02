package com.example.sampleapp.Interface;

import com.example.sampleapp.Entity.Buttons.IActivatable;
import com.example.sampleapp.Scenes.GameLevel.GameLevelScene;
import com.example.sampleapp.Scenes.MainGameScene;
import com.example.sampleapp.mgp2d.core.GameScene;

public class CloseLooting implements IActivatable {
    private GameLevelScene scene;

    public CloseLooting(GameLevelScene mainGameScene) {
        this.scene = mainGameScene;
    }

    @Override
    public void execute() {
        scene.EndLootPhase();
    }
}
