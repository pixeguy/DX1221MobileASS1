package com.example.sampleapp.Scenes.GameLevel;

import android.graphics.Canvas;
import android.util.Log;

import com.example.sampleapp.Collision.ColliderManager;
import com.example.sampleapp.Collision.Colliders.Collider2D;
import com.example.sampleapp.Collision.Detection.Collision;
import com.example.sampleapp.Collision.Detection.PhysicsManifold;
import com.example.sampleapp.Entity.BackgroundEntity;
import com.example.sampleapp.Entity.Buttons.JoystickObj;
import com.example.sampleapp.Entity.Player.PlayerObj;
import com.example.sampleapp.Entity.SampleCoin;
import com.example.sampleapp.Enums.SpriteList;
import com.example.sampleapp.R;
import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.GameScene;
import com.example.sampleapp.mgp2d.core.Vector2;

public class GameLevelScene extends GameScene {

    private int screenWidth;
    private int screenHeight;

    private JoystickObj playerMovementJoystick;

    private SampleCoin coin;

    @Override
    public void onCreate() {
        super.onCreate();

        screenHeight = GameActivity.instance.getResources().getDisplayMetrics().heightPixels;
        screenWidth = GameActivity.instance.getResources().getDisplayMetrics().widthPixels;

        playerMovementJoystick = new JoystickObj(new Vector2(screenWidth / 2.0f + 700.0f, screenHeight / 2.0f), 4);

        m_goList.add(new BackgroundEntity(R.drawable.grassbg));

        GameEntity player = new PlayerObj();
        player.onCreate(new Vector2(screenWidth / 2.0f,screenHeight / 2.0f),new Vector2(2,2), SpriteList.PlayerIdle);
        m_goList.add(player);

        coin = new SampleCoin();
        coin.onCreate(new Vector2(screenWidth / 2.0f - 500,screenHeight / 2.0f), 10);
        m_goList.add(coin);
    }

    @Override
    public void onUpdate(float dt) {
        playerMovementJoystick.onUpdate(dt);
        PlayerObj.instance.SetInputDirection(playerMovementJoystick.getInputDirection());

        for(GameEntity go : m_goList)
            go.onUpdate(dt);

        int size = ColliderManager.GetInstance().m_colliders.size();

        for(int iteration = 0; iteration < 5; ++iteration) {
            for (int i = 0; i < size - 1; ++i) {
                Collider2D colliderA = ColliderManager.GetInstance().m_colliders.get(i);
                if (colliderA.gameEntity.canDestroy()) continue;

                for (int j = i + 1; j < size; ++j) {
                    Collider2D colliderB = ColliderManager.GetInstance().m_colliders.get(j);
                    if (colliderB.gameEntity.canDestroy()) continue;

                    Vector2 MTV = new Vector2(0, 0);
                    boolean isCollided = Collision.CollisionDetection(colliderA, colliderB, MTV);
                    if(isCollided)
                    {
                        PhysicsManifold physicsManifold = new PhysicsManifold(colliderA, colliderB, MTV.normalize(), MTV.getMagnitude());
                        Collision.ResolveCollision(physicsManifold);
                    }
                }
            }
        }
    }

    @Override
    public void onRender(Canvas canvas) {
        for (GameEntity go : m_goList)
            go.onRender(canvas);
        playerMovementJoystick.onRender(canvas);
    }
}
