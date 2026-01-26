package com.example.sampleapp.Interface;

import com.example.sampleapp.Entity.Player.PlayerRecordEntry;
import com.example.sampleapp.Scenes.GameLevel.GameLevelScene;
import com.example.sampleapp.Scenes.MainGameScene;
import com.example.sampleapp.UI.Buttons.IActivatable;

public class SaveNewScore  implements IActivatable {
    private MainGameScene scene;

    public SaveNewScore(MainGameScene mainGameScene) {
        this.scene = mainGameScene;
    }

    @Override
    public void execute() {
        PlayerRecordEntry entry = new PlayerRecordEntry();
        entry.score = 50;
        scene.scores.add(entry);
        scene.saveScoresJson(scene.scores);
    }
}
