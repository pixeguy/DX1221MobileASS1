package com.example.sampleapp.Scenes.GameLevel;

import android.graphics.Canvas;

import com.example.sampleapp.Collision.ColliderManager;
import com.example.sampleapp.Collision.Colliders.BoxCollider2D;
import com.example.sampleapp.Collision.Colliders.CircleCollider2D;
import com.example.sampleapp.Collision.Colliders.Collider2D;
import com.example.sampleapp.Collision.Detection.Collision;
import com.example.sampleapp.Collision.Detection.PhysicsManifold;
import com.example.sampleapp.Entity.BackgroundEntity;
import com.example.sampleapp.Entity.Buttons.JoystickObj;
import com.example.sampleapp.Entity.Enemies.Enemy;
import com.example.sampleapp.Entity.Enemies.Slime.Slime;
import com.example.sampleapp.Entity.Player.PlayerObj;
import com.example.sampleapp.Entity.Projectile.PlayerMagicMissile;
import com.example.sampleapp.Entity.SampleCoin;
import com.example.sampleapp.Enums.SpriteAnimationList;
import com.example.sampleapp.PostOffice.Message;
import com.example.sampleapp.PostOffice.MessageSpawnProjectile;
import com.example.sampleapp.PostOffice.MessageWRU;
import com.example.sampleapp.PostOffice.ObjectBase;
import com.example.sampleapp.PostOffice.PostOffice;
import com.example.sampleapp.R;
import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.GameScene;
import com.example.sampleapp.mgp2d.core.Vector2;

public class GameLevelScene extends GameScene implements ObjectBase {

    private int screenWidth;
    private int screenHeight;

    private JoystickObj playerMovementJoystick;

    private SampleCoin coin;

    @Override
    public void onCreate() {
        super.onCreate();
        PostOffice.getInstance().register("Scene", this);
        screenHeight = GameActivity.instance.getResources().getDisplayMetrics().heightPixels;
        screenWidth = GameActivity.instance.getResources().getDisplayMetrics().widthPixels;

        playerMovementJoystick = new JoystickObj(new Vector2(screenWidth / 2.0f, screenHeight / 2.0f + 900.0f), 4);

        m_goList.add(new BackgroundEntity(R.drawable.grassbg));

        PlayerObj player = new PlayerObj();
        player.onCreate(new Vector2(screenWidth / 2.0f,screenHeight / 2.0f + 400.0f), new Vector2(0.1f,0.1f), SpriteAnimationList.PlayerIdle);
        m_goList.add(player);

        Slime slime = new Slime();
        slime.onCreate(new Vector2(screenWidth / 2.0f,screenHeight / 2.0f - 600.0f), new Vector2(2,2));
        m_goList.add(slime);
    }

    @Override
    public void onUpdate(float dt) {
        playerMovementJoystick.onUpdate(dt);
        PlayerObj.instance.SetInputDirection(playerMovementJoystick.getInputDirection());

        onUpdateGameObjects(dt);
        onPhysicsUpdate();
        onHandleIfOutsideWorldBound();
    }

    @Override
    public void onRender(Canvas canvas) {
        for (GameEntity go : m_goList)
            go.onRender(canvas);
        playerMovementJoystick.onRender(canvas);
    }

    protected void onUpdateGameObjects(float dt) {
        for(GameEntity go : m_goList) {
            if(go.canDestroy()) {
                m_goListToRemove.add(go);
                continue;
            }
            go.onUpdate(dt);
        }
    }

    protected void onPhysicsUpdate() {
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

    protected void onHandleIfOutsideWorldBound() {
        for(Collider2D collider2D : ColliderManager.GetInstance().m_colliders) {
            GameEntity go = collider2D.gameEntity;
            if(go.canDestroy()) continue;

            float minX = 0;
            float minY = 0;
            float maxX = screenWidth;
            float maxY = screenHeight;

            if(collider2D.numVertices == 1) {
                CircleCollider2D circleCollider2D = (CircleCollider2D) collider2D;
                if (go.getPosition().x + circleCollider2D.radius > maxX) {
                    go._position.x -= go.getPosition().x + circleCollider2D.radius - maxX;
                }
                else if(go.getPosition().x - circleCollider2D.radius < minX) {
                    go._position.x += Math.abs(minX - (go.getPosition().x - circleCollider2D.radius));
                }

                if(go.getPosition().y + circleCollider2D.radius > maxY) {
                    go._position.y -= go.getPosition().y + circleCollider2D.radius - maxY;
                }
                else if(go.getPosition().y - circleCollider2D.radius < minY) {
                    go._position.y += Math.abs(minY - (go.getPosition().y - circleCollider2D.radius));
                }
            }
            else{
                BoxCollider2D boxCollider2D = (BoxCollider2D) collider2D;
                if(boxCollider2D.getBound().left < minX) {
                    go._position.x += minX - boxCollider2D.getBound().left;
                }
                else if(boxCollider2D.getBound().right > maxX) {
                    go._position.x -= boxCollider2D.getBound().right - maxX;
                }

                if(boxCollider2D.getBound().top < minY) {
                    go._position.y += minY - boxCollider2D.getBound().top;
                }
                else if(boxCollider2D.getBound().bottom > maxY) {
                    go._position.y -= boxCollider2D.getBound().bottom - maxY;
                }
            }
        }
    }

    @Override
    public boolean handle(Message message) {

        if(message instanceof MessageWRU) {
            MessageWRU messageWRU = (MessageWRU) message;
            GameEntity go1 = messageWRU.go;
            go1.targetGO = null;
            float nearestDistance = Float.MAX_VALUE;

            for(GameEntity go2 : m_goList) {
                if(go2.canDestroy()) continue;
                if(go2 == go1) continue;
                if(go2 instanceof Enemy) {
                    Enemy enemy = (Enemy) go2;
                    if(messageWRU.searchType == MessageWRU.SEARCH_TYPE.NEAREST_ENEMY) {
                        float distance = go1.getPosition().Distance(go2.getPosition());
                        if(distance < nearestDistance && distance <= messageWRU.threshold) {
                            nearestDistance = distance;
                            go1.targetGO = enemy;
                        }
                    }
                }
            }
            return true;
        }

        if(message instanceof MessageSpawnProjectile) {
            MessageSpawnProjectile messageSpawnProjectile = (MessageSpawnProjectile) message;
            GameEntity go = messageSpawnProjectile.go;
            switch (messageSpawnProjectile.projectileType) {
                case PLAYER_MAGIC_MISSLE:
                    PlayerMagicMissile projectile = new PlayerMagicMissile();
                    projectile.onCreate(go.targetGO,
                            messageSpawnProjectile.movementSpeed,
                            messageSpawnProjectile.pos,
                            new Vector2(0.05f, 0.05f));
                    m_goListToAdd.add(projectile);
                    break;
                case ENEMY_MAGIC_MISSLE:
                    break;
            }
            return true;
        }

        return false;
    }
}
