package com.example.sampleapp.Managers;

import android.content.Intent;
import android.util.Log;

import com.example.sampleapp.Entity.Player.PlayerObj;
import com.example.sampleapp.PostOffice.Message;
import com.example.sampleapp.PostOffice.ObjectBase;
import com.example.sampleapp.PostOffice.PostOffice;
import com.example.sampleapp.Scenes.MainMenu;
import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.Singleton;

public class GameManager extends Singleton<GameManager> implements ObjectBase {

    public enum GameState {
        MAIN_MENU,
        RUNNING,
        PAUSED,
        GAME_OVER
    }

    private GameState _currentState = GameState.RUNNING;

    public GameState getCurrentState() {
        return _currentState;
    }

    private GameManager() {
        PostOffice.getInstance().register("GameManager", this);
    }

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
        EnemyManager.getInstance().startWave();
    }

    public void updateGame(float deltaTime) {
        // Update the game logic here
        EnemyManager.getInstance().updateWave(deltaTime);
        if (EnemyManager.getInstance().numWaves > 2)
        {
            //win
            TransitionToState(GameState.GAME_OVER);
        }
        if(PlayerObj.getInstance().healthSystem.getCurrentHealth() <= 0)
        {
            //lose
        }
    }

    public void endGame() {
        // End the game logic here
    }

    @Override
    public boolean handle(Message message) {
        return false;
    }
}