package com.example.sampleapp.Managers;

import android.graphics.RectF;
import android.util.Log;

import com.example.sampleapp.Entity.Enemies.Enemy;
import com.example.sampleapp.Entity.EnemySpawnMarker;
import com.example.sampleapp.PostOffice.Message;
import com.example.sampleapp.PostOffice.MessageAddGO;
import com.example.sampleapp.PostOffice.MessageCount;
import com.example.sampleapp.PostOffice.ObjectBase;
import com.example.sampleapp.PostOffice.PostOffice;
import com.example.sampleapp.Scenes.GameLevel.GameLevelScene;
import com.example.sampleapp.UI.Text.UICounter;
import com.example.sampleapp.mgp2d.core.Singleton;
import com.example.sampleapp.mgp2d.core.Vector2;

import java.util.Vector;

public class EnemyManager extends Singleton<EnemyManager> implements ObjectBase {

    int[] enemySpawnPattern = { 4, 2, 2 }; // 4 Slime, 2 Toxito, 2 Golem
    public int numWaves = 0;

    public static int MAX_WAVE = 2;

    public static EnemyManager getInstance() {
        return Singleton.getInstance(EnemyManager.class);
    }

    private final Vector<Enemy> enemiesList = new Vector<>();;

    public UICounter waveCounter;

    private EnemyManager() {
        PostOffice.getInstance().register("EnemyManager", this);
    }

    public void AddEnemy(Enemy enemy) {
        enemiesList.add(enemy);
    }

    public void RemoveEnemy(Enemy enemy) {
        enemiesList.remove(enemy);
    }

    public Vector<Enemy> GetEnemies() {
        return enemiesList;
    }

    public Enemy GetEnemy(int index) {
        return enemiesList.get(index);
    }

    public int GetNumOfEnemies() {
        return enemiesList.size();
    }

    public int GetNumOfEnemies(String enemyName) {
        int count = 0;
        for(Enemy enemy : enemiesList)
        {
            if(enemy.getClass().getSimpleName().equals(enemyName)) {
                MessageCount message = new MessageCount();
                count += PostOffice.getInstance().send(String.valueOf(enemy._id), message) ? 1 : 0;
            }
        }
        return count;
    }

    public void ClearEnemies() {
        enemiesList.clear();
    }

    public void startWave() {
        // Start the wave logic here
        for(int i = 0; i < enemySpawnPattern.length; ++i) {
            for(int j = 0; j < enemySpawnPattern[i]; ++j) {

                RectF world_bounds = GameLevelScene.world_bounds;

                float randxPos = (float)Math.random() * world_bounds.right;
                float minY = world_bounds.top;
                float maxY = world_bounds.bottom;
                float randyPos = (float)Math.random() * (maxY - minY) + minY;
                Enemy enemy = EnemyFactory.CreateEnemy(EnemyFactory.EnemyType.values()[i], new Vector2(randxPos, randyPos));
                AddEnemy(enemy);
                MessageAddGO message = new MessageAddGO(enemy);
                PostOffice.getInstance().send("Scene", message);

                EnemySpawnMarker marker = new EnemySpawnMarker();
                marker.onCreate(enemy, new Vector2(randxPos, randyPos), new Vector2(0.1f, 0.1f));
                message = new MessageAddGO(marker);
                PostOffice.getInstance().send("Scene", message);
            }
        }
        numWaves++;
        waveCounter.setText("Wave", String.valueOf(numWaves));
    }

    public void updateWave(float deltaTime) {
        // Update the wave logic here
        if(GetNumOfEnemies() == 0 && numWaves < MAX_WAVE) {
            startWave();
        }
    }

    @Override
    public boolean handle(Message message) {
        return false;
    }
}
