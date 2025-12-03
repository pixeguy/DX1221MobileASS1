package com.example.sampleapp.Interface;

import com.example.sampleapp.UI.Buttons.IActivatable;
import com.example.sampleapp.Scenes.GameLevel.GameLevelScene;

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
