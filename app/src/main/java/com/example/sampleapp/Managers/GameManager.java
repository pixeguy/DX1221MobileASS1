package com.example.sampleapp.Managers;

import android.content.Intent;
import android.util.Log;

import com.example.sampleapp.Entity.Player.PlayerObj;
import com.example.sampleapp.PostOffice.Message;
import com.example.sampleapp.PostOffice.MessageEndGame;
import com.example.sampleapp.PostOffice.ObjectBase;
import com.example.sampleapp.PostOffice.PostOffice;
import com.example.sampleapp.Scenes.GameLevel.GameLevelScene;
import com.example.sampleapp.Scenes.MainMenu;
import com.example.sampleapp.UI.Text.UICounter;
import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.Singleton;

public class GameManager extends Singleton<GameManager> implements ObjectBase {

    public enum GameState {
        MAIN_MENU,
        RUNNING,
        PAUSED,
        GAME_OVER
    }
    private boolean isGameOver = false;

    private GameState _currentState = GameState.RUNNING;

    public GameState getCurrentState() {
        return _currentState;
    }

    private GameManager() {
        PostOffice.getInstance().register("GameManager", this);
    }

    private float timer = 0.0f;
    private UICounter timerCounter;

    public void TransitionToState(GameState newState)
    {
        _currentState = newState;

        // Perform actions based on the new state
        switch (newState)
        {
            case MAIN_MENU:
                // Load the Main Menu scene
                Log.d("GameManager", "Transitioning to Main Menu");
                break;
            case RUNNING:
                // Load the main game level
                Log.d("GameManager", "Transitioning to Running");
                break;
            case PAUSED:
                // Handle pause logic (e.g., show pause menu)
                Log.d("GameManager", "Transitioning to Paused");
                break;
            case GAME_OVER:
                // Handle game over logic (e.g., show game over screen)
                Log.d("GameManager", "Transitioning to Game Over");
                break;
        }
    }


    public static GameManager getInstance() {
        return Singleton.getInstance(GameManager.class);
    }

    public void startGame() {
        // Start the game logic here
        EnemyManager.getInstance().numWaves = 0;
        timer = 0.0f;
        timerCounter = new UICounter(GameLevelScene.world_bounds.width() / 2.0f, 200, 300, 300, 70.0f);
        timerCounter.zIndex = 1;
        timerCounter.setText("Timer", "00:00");
        UIManager.getInstance().addElement(timerCounter);

        EnemyManager.getInstance().waveCounter = new UICounter(GameLevelScene.screenWidth / 2.0f, 100, 300, 300, 100.0f);
        EnemyManager.getInstance().waveCounter.zIndex = 1;
        UIManager.getInstance().addElement(EnemyManager.getInstance().waveCounter);

        EnemyManager.getInstance().startWave();
    }

    public void updateGame(float deltaTime) {
        // Update the game logic here
        if(isGameOver) return;

        if (EnemyManager.getInstance().numWaves == EnemyManager.MAX_WAVE && EnemyManager.getInstance().GetNumOfEnemies() == 0)
        {
            //win
            MessageEndGame msg = new MessageEndGame(MessageEndGame.END_CONDITION.LOOTING_PHASE);
            PostOffice.getInstance().send("Scene",msg);
            isGameOver = true;
            return;
        }
        if(PlayerObj.getInstance().healthSystem.getCurrentHealth() <= 0)
        {
            //lose
            MessageEndGame msg = new MessageEndGame(MessageEndGame.END_CONDITION.LOSE_PHASE);
            PostOffice.getInstance().send("Scene",msg);
            isGameOver = true;
            return;
        }
        EnemyManager.getInstance().updateWave(deltaTime);
        timer += deltaTime;
        timerCounter.setText("Timer", String.format("%02d:%02d", (int) (timer / 60), (int) (timer % 60)));
    }

    public void endGame() {
        // End the game logic here
    }

    @Override
    public boolean handle(Message message) {
        return false;
    }
}