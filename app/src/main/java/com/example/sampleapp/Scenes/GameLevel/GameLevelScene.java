package com.example.sampleapp.Scenes.GameLevel;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;

import com.example.sampleapp.Collision.ColliderManager;
import com.example.sampleapp.Collision.Colliders.BoxCollider2D;
import com.example.sampleapp.Collision.Colliders.CircleCollider2D;
import com.example.sampleapp.Collision.Colliders.Collider2D;
import com.example.sampleapp.Collision.Detection.Collision;
import com.example.sampleapp.Collision.Detection.PhysicsManifold;
import com.example.sampleapp.Entity.Abilities.Ability;
import com.example.sampleapp.Entity.Abilities.DoubleShotAbi;
import com.example.sampleapp.Entity.Abilities.MagicOrbAbi;
import com.example.sampleapp.Entity.Abilities.RearShotAbi;
import com.example.sampleapp.Entity.BackgroundEntity;
import com.example.sampleapp.Entity.Camera2D;
import com.example.sampleapp.Entity.Projectile.PlayerFlyingOrb;
import com.example.sampleapp.Enums.SoundList;
import com.example.sampleapp.Enums.SpriteList;
import com.example.sampleapp.Input.SwipeGestureDetector;
import com.example.sampleapp.Managers.DamageTextManager;
import com.example.sampleapp.Managers.ScreenManager;
import com.example.sampleapp.Managers.SoundManager;
import com.example.sampleapp.Managers.UIManager;
import com.example.sampleapp.PostOffice.MessageCheckCollision;
import com.example.sampleapp.PostOffice.MessageEndGame;
import com.example.sampleapp.Screens.PausedScreen;
import com.example.sampleapp.UI.Buttons.GenericBtn;
import com.example.sampleapp.UI.Buttons.IActivatable;
import com.example.sampleapp.UI.Buttons.UIButton;
import com.example.sampleapp.UI.Buttons.UIJoystick;
import com.example.sampleapp.UI.Buttons.LootButtonObj;
import com.example.sampleapp.Entity.Enemies.Enemy;
import com.example.sampleapp.Entity.Inventory.LootObj;
import com.example.sampleapp.Entity.Inventory.LootSlot;
import com.example.sampleapp.Entity.Player.PlayerObj;
import com.example.sampleapp.Entity.Projectile.EnemyFireMissile;
import com.example.sampleapp.Entity.Projectile.EnemyToxicMissile;
import com.example.sampleapp.Entity.Projectile.PlayerMagicMissile;
import com.example.sampleapp.Enums.LootType;
import com.example.sampleapp.Enums.SpriteAnimationList;
import com.example.sampleapp.Interface.BackToMenu;
import com.example.sampleapp.Interface.CloseLooting;
import com.example.sampleapp.Interface.Damageable;
import com.example.sampleapp.Interface.RotateLoot;
import com.example.sampleapp.Managers.EnemyManager;
import com.example.sampleapp.PostOffice.Message;
import com.example.sampleapp.PostOffice.MessageAddGO;
import com.example.sampleapp.PostOffice.MessageCheckForDamageCollision;
import com.example.sampleapp.PostOffice.MessageSpawnProjectile;
import com.example.sampleapp.PostOffice.MessageSpawnRearProjectile;
import com.example.sampleapp.PostOffice.MessageWRU;
import com.example.sampleapp.PostOffice.ObjectBase;
import com.example.sampleapp.PostOffice.PostOffice;
import com.example.sampleapp.R;
import com.example.sampleapp.Scenes.MainMenu;
import com.example.sampleapp.mgp2d.core.EmptyEntity;
import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.GameScene;
import com.example.sampleapp.mgp2d.core.Vector2;
import com.example.sampleapp.Managers.GameManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

public class GameLevelScene extends GameScene implements ObjectBase {

    public static int screenWidth;
    public static int screenHeight;
    public static RectF world_bounds;

    private ArrayList<LootButtonObj> lootBtns;
    boolean spawnPhase = false; boolean abilityPhase = false;
    private ArrayList<GenericBtn> lootGenerics;
    private LootSlot[][] slots; //  [col][row].....[x][y]

    //to store reference to current dragging obj
    public GameEntity draggingObj;
    private float initialRot;
    boolean isTouching = false;
    boolean isTouching2 = false;
    Random rand = new Random();
    private ArrayList<GameEntity> m_goAbiLootList;

    private SwipeGestureDetector swipeGestureDetector;

    private UIButton pausedBtn;

    @Override
    public void onCreate() {
        super.onCreate();
        PostOffice.getInstance().register("Scene", this);
        screenHeight = GameActivity.instance.getResources().getDisplayMetrics().heightPixels;
        screenWidth = GameActivity.instance.getResources().getDisplayMetrics().widthPixels;
        world_bounds = new RectF(0, -screenHeight, screenWidth, screenHeight);

        m_goAbiLootList = new ArrayList<>();

        UIJoystick playerMovementJoystick = new UIJoystick(new Vector2(screenWidth / 2.0f, screenHeight / 2.0f + 900.0f), 200, 100);
        playerMovementJoystick.setSprites(SpriteList.BaseJoystickSprite.sprite, SpriteList.HandleJoystickSprite.sprite);
        playerMovementJoystick.zIndex = 1;
        UIManager.getInstance().addElement(playerMovementJoystick);

        BackgroundEntity bg1 = new BackgroundEntity(R.drawable.grassbg);
        m_goList.add(bg1);

        BackgroundEntity bg2 = new BackgroundEntity(R.drawable.grassbg);
        bg2._position.y = -screenHeight;
        m_goList.add(bg2);

        Camera2D.getInstance().setBounds(world_bounds);
        Camera2D.getInstance().setPosition(new Vector2(screenWidth / 2.0f, screenHeight / 2.0f));

        PlayerObj player = PlayerObj.getInstance();
        player.onCreate(new Vector2(screenWidth / 2.0f,screenHeight / 2.0f + 400.0f), new Vector2(1,1), SpriteAnimationList.PlayerForward);

        player.SetInputDirection(playerMovementJoystick.getOutput());
        player.currAbility = null;
        m_goList.add(player);

        pausedBtn = new UIButton(100, 100, 200, 200, SpriteList.PausedBtnIcon, new Vector2(0.4f, 0.4f));
        pausedBtn.setAlpha(0);
        pausedBtn.zIndex = 1;
        pausedBtn.setOnRelease(() -> {
            GameManager.getInstance().TransitionToState(GameManager.GameState.PAUSED);
            ScreenManager.setScreen(new PausedScreen());
        });
        UIManager.getInstance().addElement(pausedBtn);

        SoundManager.getInstance().InitAudio();

        InitAbiLoot();
        StartAbilityPhase();

        GameManager.getInstance().startGame();

        swipeGestureDetector = new SwipeGestureDetector();
        swipeGestureDetector.setOnSwipeListener(new SwipeGestureDetector.OnSwipeListener() {
            @Override
            public void onSwipeUp() { player.Dash(new Vector2(0,-1)); }

            @Override
            public void onSwipeDown() { player.Dash(new Vector2(0,1)); }

            @Override
            public void onSwipeLeft() { player.Dash(new Vector2(-1,0)); }

            @Override
            public void onSwipeRight() { player.Dash(new Vector2(1,0)); }
        });
    }

    @Override public void onEnter()
    {
        super.onEnter();
        SoundManager.getInstance().InitAudio();
        SoundManager.getInstance().startMusic();
    }

    @Override
    public void onUpdate(float dt) {
        for(GameEntity obj : m_goAbiLootList) {
            if(obj.isActive) {
                obj.onUpdate(dt);
            }
        }

        HandleTouch();
        GameManager.getInstance().updateGame(dt);
        UIManager.getInstance().update(dt);
        if(GameManager.getInstance().getCurrentState() == GameManager.GameState.PAUSED) {
            return;
        }

        DamageTextManager.getInstance().onUpdate();

        onUpdateGameObjects(dt);
        onPhysicsUpdate();
        onHandleIfOutsideWorldBound();
    }

    @Override
    public void onRender(Canvas canvas) {
        canvas.save();
        Camera2D.getInstance().follow(PlayerObj.getInstance()._position);
        canvas.translate(
                -Camera2D.getInstance().target.x,
                -Camera2D.getInstance().target.y
        );
        m_goList.sort(Comparator.comparingInt(GameEntity::getOrdinal)); // sort by ordinal
        for (GameEntity go : m_goList) {
            if(go.canDestroy() || !go.isActive) continue;
            if(!go.visible) continue;
            go.onRender(canvas);
        }
        canvas.restore();

        m_goAbiLootList.sort(Comparator.comparingInt(GameEntity::getOrdinal)); // sort by ordinal
        for(GameEntity go : m_goAbiLootList) {
            if(go.canDestroy() || !go.isActive) continue;
            if(!go.visible) continue;
            go.onRender(canvas);
        }

        UIManager.getInstance().onRender(canvas);
    }

    protected void onUpdateGameObjects(float dt) {
        for(GameEntity go : m_goList) {
            if(go.canDestroy()) {
                m_goListToRemove.add(go);
                if(go instanceof Enemy) {
                    EnemyManager.getInstance().RemoveEnemy((Enemy) go);
                }
                continue;
            }
            else if(!go.isActive) continue;
            go.onUpdate(dt);
        }
    }

    @Override
    public void onExit() {
        super.onExit();
        UIManager.getInstance().clear();
        PostOffice.getInstance().clear();
        ScreenManager.exitCurrent();
        EnemyManager.getInstance().ClearEnemies();
        m_goAbiLootList.clear();
        for(int i = 0; i < m_goList.size(); ++i) {
            if(m_goList.get(i) instanceof PlayerObj) continue;
            m_goList.remove(m_goList.get(i));
        }
        m_goListToAdd.clear();
        m_goListToRemove.clear();
        SoundManager.getInstance().stopAudio();

        Camera2D.getInstance().Reset();
    }

    protected void onPhysicsUpdate() {
        int size = ColliderManager.GetInstance().m_colliders.size();
        for(int iteration = 0; iteration < 10; ++iteration) {
            for (int i = 0; i < size - 1; ++i) {
                Collider2D colliderA = ColliderManager.GetInstance().m_colliders.get(i);
                if (colliderA.gameEntity.canDestroy() || !colliderA.gameEntity.isAlive) continue;

                for (int j = i + 1; j < size; ++j) {
                    Collider2D colliderB = ColliderManager.GetInstance().m_colliders.get(j);
                    if (colliderB.gameEntity.canDestroy() ||
                            !colliderB.gameEntity.isAlive) continue;

                    if(colliderA.gameEntity instanceof Enemy &&
                            colliderB.gameEntity instanceof Enemy) continue;

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
            if(go.canDestroy() || !go.isAlive || collider2D.isTrigger) continue;

            float minX = world_bounds.left;
            float minY = world_bounds.top;
            float maxX = world_bounds.right;
            float maxY = world_bounds.bottom;

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
            else {
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
                if(go2.canDestroy() || !go2.isAlive || !go2.isActive) continue;
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
                case PLAYER_FIRE_MISSILE:
                    PlayerMagicMissile playerProjectile = new PlayerMagicMissile();
                    playerProjectile.onCreate(go.targetGO,
                            messageSpawnProjectile.movementSpeed,
                            messageSpawnProjectile.pos,
                            new Vector2(0.05f, 0.05f));
                    m_goListToAdd.add(playerProjectile);
                    //SoundManager.getInstance().PlayAudio(SoundList.PlayerShoot,1);
                    break;
                case PLAYER_FLYING_ORB:
                    PlayerFlyingOrb playerFlyingOrb = new PlayerFlyingOrb();
                    playerFlyingOrb.onCreate(messageSpawnProjectile.movementSpeed, 0,
                            new Vector2(0.075f, 0.075f));
                    m_goListToAdd.add(playerFlyingOrb);

                    PlayerFlyingOrb playerFlyingOrb2 = new PlayerFlyingOrb();
                    playerFlyingOrb2.onCreate(messageSpawnProjectile.movementSpeed, (float) Math.PI,
                            new Vector2(0.075f, 0.075f));
                    m_goListToAdd.add(playerFlyingOrb2);
                    break;
                case ENEMY_FIRE_MISSILE:
                    EnemyFireMissile enemyFireProjectile = new EnemyFireMissile();
                    enemyFireProjectile.onCreate(
                            messageSpawnProjectile.movementSpeed,
                            messageSpawnProjectile.pos,
                            new Vector2(0.05f, 0.05f));
                    enemyFireProjectile.facingDirection = messageSpawnProjectile.facingDirection;
                    m_goListToAdd.add(enemyFireProjectile);
                    break;
                case ENEMY_TOXIC_MISSILE:
                    EnemyToxicMissile enemyToxicProjectile = new EnemyToxicMissile();
                    enemyToxicProjectile.onCreate(
                            messageSpawnProjectile.movementSpeed,
                            messageSpawnProjectile.pos,
                            new Vector2(0.05f, 0.05f));
                    m_goListToAdd.add(enemyToxicProjectile);
                    break;
            }
            return true;
        }

        if(message instanceof MessageSpawnRearProjectile) {
            MessageSpawnRearProjectile messageSpawnProjectile = (MessageSpawnRearProjectile) message;
            Vector2 targetPos = messageSpawnProjectile.direction;
            switch (messageSpawnProjectile.projectileType) {
                case PLAYER_FIRE_MISSILE:
                    PlayerMagicMissile playerProjectile = new PlayerMagicMissile();
                    playerProjectile.onCreate(targetPos,
                            messageSpawnProjectile.movementSpeed,
                            messageSpawnProjectile.pos,
                            new Vector2(0.05f, 0.05f));
                    m_goListToAdd.add(playerProjectile);
                    break;
            }
        }

        if(message instanceof MessageCheckForDamageCollision) {
            MessageCheckForDamageCollision msgCollisionCheck = (MessageCheckForDamageCollision) message;
            GameEntity entityToCheck = msgCollisionCheck.gameEntityToCheck;
            for(GameEntity entity : m_goList) {
                if(entity == entityToCheck) continue;
                if(entity.collider == null || !entity.isActive) {
                    continue;
                }
                if(Collision.CollisionDetection(entityToCheck.collider, entity.collider, new Vector2(0, 0))) {

                    if (entity instanceof Enemy) {
                        float minDamage = 50.0f;
                        float maxDamage = 60.0f;
                        float damage = (float) (Math.random() * (maxDamage - minDamage) + minDamage);
                        ((Enemy) entity).TakeDamage(damage);
                        entityToCheck.destroy();
                        break;
                    }
                }
            }
            return true;
        }

        if(message instanceof MessageCheckCollision) {
            MessageCheckCollision messageCheckCollision = (MessageCheckCollision) message;
            GameEntity entityToCheck = messageCheckCollision.gameEntityToCheck;
            for(GameEntity entity : m_goList) {
                if (entity == entityToCheck) continue;
                if (entity.collider == null || !entity.isActive) {
                    continue;
                }

                if (Collision.CollisionDetection(entityToCheck.collider, entity.collider, new Vector2(0, 0))) {
                    if (entity instanceof Enemy) {
                        messageCheckCollision.collisionList.add(entity.collider);
                    }
                }
            }

            return true;
        }

        if(message instanceof MessageAddGO) {
            MessageAddGO messageAddGO = (MessageAddGO) message;
            m_goListToAdd.add(messageAddGO.go);
            return true;
        }

        if (message instanceof MessageEndGame) {
            MessageEndGame msg = (MessageEndGame) message;
            if (msg.condition == MessageEndGame.END_CONDITION.LOOTING_PHASE)
            {
                StartLootPhase();
            }else { LoseScreen(); }
        }

        return false;
    }

    public void InitAbiLoot() {
        lootGenerics = new ArrayList<GenericBtn>();
        GenericBtn rotate = new GenericBtn(new RotateLoot(this));
        rotate.onCreate(new Vector2(screenWidth* 0.1f, screenHeight * 0.95f),new Vector2(3,3),SpriteAnimationList.RotateBtn);
        lootGenerics.add(rotate);
        rotate.setOrdinal(4);
        m_goAbiLootList.add(rotate);

        GenericBtn endLootBtn = new GenericBtn(new CloseLooting(this));
        endLootBtn.onCreate(new Vector2(screenWidth/2, screenHeight * 0.95f), new Vector2(3,3),SpriteAnimationList.CompleteBtn);
        lootGenerics.add(endLootBtn);
        endLootBtn.setOrdinal(4);
        m_goAbiLootList.add(endLootBtn);

        //init all the slots, vv long so i minimize it first
        {
            slots = new LootSlot[5][4];

            Vector2 scale = new Vector2(0.06f, 0.06f);
            float GAP = 0 * GameActivity.instance.getResources().getDisplayMetrics().density;

            for (int x = 0; x < slots.length; x++) {
                for (int y = 0; y < slots[0].length; y++) {

                    slots[x][y] = new LootSlot();
                    slots[x][y].onCreate(new Vector2(0, 0), scale);
                    slots[x][y].isActive = false;
                    slots[x][y].setOrdinal(4);
                    m_goAbiLootList.add(slots[x][y]);

                    //calculating grid position
                    {
                        // Calculate cell spacing
                        float cellWidth  = (slots[x][y].animatedSprite._width  * scale.x) + GAP;
                        float cellHeight = (slots[x][y].animatedSprite._height * scale.y) + GAP;

                        // Grid total size
                        float gridSize  = cellWidth  * 5;

                        // Center X
                        float startX = (screenWidth / 2f) - (gridSize / 2.5f);
                        // Higher Y (25% from top)
                        float startY = (screenHeight * 0.235f) - (gridSize/2.5f);

                        float worldX = startX + x * cellWidth;
                        float worldY = startY + y * cellHeight;
                        slots[x][y].setPosition(new Vector2(worldX,worldY));}
                }
            }
        }
    }

    public void StartAbilityPhase() {
        GameManager.getInstance().TransitionToState(GameManager.GameState.PAUSED);

        BackgroundEntity bg = new BackgroundEntity(R.drawable.greyoverlay);
        bg.isActive = true;
        bg.setOrdinal(3);
        m_goAbiLootList.add(bg);

        for (GameEntity entity : m_goAbiLootList)
        {
            if(entity instanceof BackgroundEntity)
            {
                entity.isActive = true;
            }
        }
        int count = 3;
        float spacing = 350f; // distance between abilities (tweak this)
        float centerX = screenWidth / 2f;
        float centerY = screenHeight / 2f;

        // Starting position so that 3 items are centered
        float startX = centerX - spacing;

        int[] abilities = {0, 1, 2};

        // Randomize the order of abilities
        Random random = new Random();
        for (int i = abilities.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = abilities[i];
            abilities[i] = abilities[j];
            abilities[j] = temp;
        }

        // ---- CREATE 3 ABILITIES ----
        for (int i = 0; i < count; i++)
        {
            Ability ability;
            if(abilities[i] == 0)
                ability = new MagicOrbAbi();
            else if(abilities[i] == 1)
                ability = new RearShotAbi();
            else
                ability = new DoubleShotAbi();

            float xPos = startX + (i * spacing);
            ability.onCreate(new Vector2(xPos, centerY), new Vector2(0.35f, 0.4f));
            GenericBtn btn = ability.selfBtn;
            ability.setOrdinal(4);
            btn.setOrdinal(4);
            m_goAbiLootList.add(btn);
            m_goAbiLootList.add(ability);

            ability.isActive = true;
            btn.isActive = true;
        }
        abilityPhase = true;
    }

    public void EndAbilityPhase() {
        GameManager.getInstance().TransitionToState(GameManager.GameState.RUNNING);
        for(GameEntity entity : m_goAbiLootList) {
            if (entity instanceof BackgroundEntity){
                BackgroundEntity bg = (BackgroundEntity) entity;
                entity.isActive = false;
                if (bg.imageID == R.drawable.grassbg)
                {
                    entity.isActive = true;
                }
            }
            if (entity instanceof Ability) {
                Ability abi = (Ability) entity;
                abi.selfBtn.isActive = false;
                abi.isActive = false;
                abi.paint.setAlpha(128);
                if(!abi.gotten) abi.iconUI.visible = false;
            }
        }
    }

    public void StartLootPhase() {
        GameManager.getInstance().TransitionToState(GameManager.GameState.PAUSED);

        BackgroundEntity bg = new BackgroundEntity(R.drawable.greyoverlay);
        bg.isActive = false;
        bg.setOrdinal(2);
        m_goAbiLootList.add(bg);


        int noOfLoot = rand.nextInt(5 - 1 + 1) + 1;

        // temporary list to hold new items
        ArrayList<GameEntity> toAdd = new ArrayList<>();

        for (GameEntity entity : m_goAbiLootList)
        {
            if (entity instanceof LootSlot) {
                entity.isActive = true;
            }
            if(entity instanceof BackgroundEntity) {
                entity.isActive = true;
            }
        }
        for(GenericBtn btn : lootGenerics)
        {
            btn.isActive = true;
        }


        lootBtns = new ArrayList<>();

        Vector2 startPos = new Vector2(screenWidth/2, screenHeight/2-135);
        Vector2 scale = new Vector2(0.07f, 0.07f);
        float spacing = 50 * GameActivity.instance.getResources().getDisplayMetrics().density;
        // create loot buttons AFTER the loop
        for (int i = 0; i < 5; i++)
        {
            LootType randomType = LootType.values()[rand.nextInt(LootType.values().length)];
            int offsetY = (int)spacing * i;

            LootButtonObj lootBtnn = new LootButtonObj();
            lootBtnn.onCreate(
                    new Vector2(startPos.x, startPos.y + offsetY),
                    scale,
                    randomType
            );
            lootBtnn.isActive = true;
            lootBtnn.setOrdinal(4);
            lootBtns.add(lootBtnn);
            toAdd.add(lootBtnn); // store for later adding


            // Now safely add everything at once
            m_goAbiLootList.addAll(toAdd);
        }
        spawnPhase = true;
    }

    public void EndLootPhase() {
        GameManager.getInstance().TransitionToState(GameManager.GameState.RUNNING);

        for (int i = m_goAbiLootList.size() - 1; i >= 0; i--) {
            GameEntity entity = m_goAbiLootList.get(i);

            if (entity instanceof LootObj) {
                LootObj loot = (LootObj) entity;
                if (loot.placed) {
                    PlayerObj.getInstance().value += loot.lootType.value;
                }
                m_goAbiLootList.remove(i);
            }
            else if (entity instanceof LootButtonObj) {
                m_goAbiLootList.remove(i);
            }
            else if (entity instanceof LootSlot) {
                LootSlot slot = (LootSlot) entity;
                slot.occupied = false;
                entity.isActive = false;
            }
            else if (entity instanceof BackgroundEntity) {
                BackgroundEntity bg = (BackgroundEntity) entity;
                if (bg.imageID != R.drawable.grassbg) {
                    entity.isActive = false;
                } else {
                    entity.isActive = true;
                }
            }
        }
        for(GenericBtn btn : lootGenerics)
        {
            btn.isActive = false;
        }

        System.out.println(PlayerObj.getInstance().value);
        lootBtns = null;
        spawnPhase = false;
        onExit();
        GameActivity.instance.startActivity(new Intent().setClass(GameActivity.instance, MainMenu.class));
    }

    public void LoseScreen() {
        GameManager.getInstance().TransitionToState(GameManager.GameState.PAUSED);
        for (GameEntity entity : m_goAbiLootList)
        {
            if(entity instanceof BackgroundEntity) {
                entity.isActive = true;
            }
        }
        IActivatable target = new BackToMenu();
        GenericBtn menuBtn = new GenericBtn(target);
        menuBtn.onCreate(new Vector2((float) screenWidth /2, screenHeight * 0.80f), new Vector2(3.5f,3.5f),SpriteAnimationList.MainMenuBtn);
        menuBtn.setOrdinal(4);
        menuBtn.isActive = true;
        m_goAbiLootList.add(menuBtn);

        EmptyEntity gameOverScreen = new EmptyEntity();
        gameOverScreen.onCreate(new Vector2((float) screenWidth /2, screenHeight * 0.40f), new Vector2(3.5f,3.5f),SpriteAnimationList.GameOverScreen);
        gameOverScreen.setOrdinal(4);
        gameOverScreen.isActive = true;
        m_goAbiLootList.add(gameOverScreen);
    }

    private void HandleTouch() {
        MotionEvent event = GameActivity.instance.getMotionEvent();
        if (event == null) { return; }
        UIManager.getInstance().handleTouch(event);
        swipeGestureDetector.onTouchEvent(event);

        int action = event.getActionMasked();
        int index = event.getActionIndex();
        int pointerID = event.getPointerId(index);
        Vector2 touchPos = new Vector2(event.getX(index),event.getY(index));
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if(!isTouching) { // action down registers as hold on android studio emulator
                    for(GameEntity entity : m_goAbiLootList) {
                        if (entity instanceof GenericBtn)
                        {
                            GenericBtn btn = (GenericBtn) entity;
                            if(btn.checkIfPressed(touchPos))
                            {
                                if (btn.getCallback() instanceof Ability) {
                                    if(PlayerObj.getInstance().currAbility == null)  {
                                        btn.OnClick();
                                        EndAbilityPhase();
                                    }
                                }

                                IActivatable target = new CloseLooting(this);
                                if(btn.getCallback().getClass() == target.getClass())
                                {
                                    btn.OnClick();
                                }
                                IActivatable target2 = new BackToMenu();
                                if(btn.getCallback().getClass() == target2.getClass())
                                {
                                    btn.OnClick();
                                }
                                break;
                            }
                        }
                    }
                    if(lootBtns != null) { //whether the list exists
                        for (LootButtonObj lootBtn : lootBtns) {
                            if (lootBtn.checkIfPressed(touchPos)) {
                                if (!lootBtn.isActive) {
                                    continue;
                                }
                                if (!lootBtn.used) { //if the loot obj is not in the grid
                                    lootBtn.OnClick(touchPos);

                                    //creating my loot obj
                                    LootObj lootobj = new LootObj();
                                    //grab lootType ref from btn
                                    lootobj.onCreate(touchPos, lootBtn.loot);
                                    lootobj.isActive = true;
                                    lootobj.setOrdinal(4);
                                    m_goAbiLootList.add(lootobj);

                                    //assign obj to reference so scene can use it
                                    draggingObj = lootobj;
                                    lootBtn.isActive = false;
                                    //RelayoutLootButtons();
                                    lootBtn.SetALPHATOZERO();
                                    lootobj.sourceButton = lootBtn;
                                    lootobj.sourceButton.used = true;
                                    initialRot = 0;
                                    RebuildLootBtns();
                                }
                                else { //find the correct loot obj corresponding to the button
                                    for (GameEntity entity : m_goAbiLootList) {
                                        if (entity instanceof LootObj) {
                                            LootObj loot = (LootObj) entity;
                                            if (loot.sourceButton == lootBtn) {
                                                draggingObj = loot;
                                                lootBtn.isActive = false;
                                                lootBtn.paint.setAlpha(0);
                                                initialRot = loot.rotationAngle;

                                                if(loot.placed){
                                                    for(LootSlot slots : loot.slotsOccupied)
                                                    {
                                                        slots.occupied = false;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    isTouching = true;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if(!isTouching2){
                    for(GameEntity entity : m_goAbiLootList)
                    {
                        if(entity instanceof GenericBtn)
                        {
                            if(((GenericBtn) entity).checkIfPressed(touchPos))
                            {
                                GenericBtn btn = (GenericBtn) entity;
                                IActivatable target = new RotateLoot(this);
                                if(btn.getCallback().getClass() == target.getClass())
                                {
                                    btn.OnClick();
                                }
                            }
                        }
                    }
                    isTouching2 = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(draggingObj != null) {
                    //drag obj with touching pos
                    draggingObj.setPosition(touchPos);

                    //make all slots normal colour
                    for (int a = 0; a < slots.length; a++) {
                        for (int b = 0; b < slots[0].length; b++) {
                            slots[a][b].clearTint();
                        }
                    }

                    if (draggingObj instanceof LootObj)
                    {
                        LootObj loot = (LootObj) draggingObj;
                        int xSize = (int) loot.instanceScale.x;
                        int ySize = (int) loot.instanceScale.y;

                        boolean tinted = false;

                        //loop through the grid. return grid slot index if user touch is on a slot
                        Vector2 slot = findSlotIndexAtPosition(touchPos);
                        if (slot != null) {
                            int i = (int) slot.x;
                            int j = (int) slot.y;

                            boolean canPlace = canPlaceItemInSlot(i, j, xSize, ySize);
                            int color = canPlace ? 0x5500FF00 : 0x77FF0000; //green or red

                            //loop through the size of the object
                            for (int x = 0; x < xSize; x++) {
                                for (int y = 0; y < ySize; y++) {

                                    int slotX = i + x;
                                    int slotY = j + y;

                                    //if is out of bounds, dont try to tint it
                                    if (inBounds(slotX, slotY)) {
                                        slots[slotX][slotY].setTint(color);
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isTouching = false;

                if(draggingObj instanceof LootObj) {
                    //get details of the loot obj
                    LootObj loot = (LootObj) draggingObj;
                    int xSize = (int)loot.instanceScale.x;
                    int ySize = (int)loot.instanceScale.y;

                    //loop through the grid. return grid slot index if user touch is on a slot
                    Vector2 slot = findSlotIndexAtPosition(touchPos);
                    if (slot != null) {
                        int i = (int) slot.x;
                        int j = (int) slot.y;

                        //check if item slots are valid
                        if(canPlaceItemInSlot(i,j,xSize,ySize)){
                            draggingObj.setPosition(slots[i][j]._position);
                            loot.slotsOccupied.clear(); //clear all previous ones
                            //change all slots that it occupies to occupied
                            for (int x = 0; x < xSize; x++) {
                                for (int y = 0; y < ySize; y++) {
                                    int slotX = i + x;   // remember: i = row (Y), j = column (X)
                                    int slotY = j + y;
                                    slots[slotX][slotY].occupied = true;
                                    loot.slotsOccupied.add(slots[slotX][slotY]);
                                }
                            }

                            //setting button to loot pos, to press again
                            {
                                if(loot.rotationAngle == 90)
                                {
                                    Vector2 newPiv = new Vector2(0,0);
                                    newPiv.x = loot.pivotOffset.y;
                                    newPiv.y = loot.pivotOffset.x;
                                    loot.sourceButton.setPosition(draggingObj._position.add(newPiv));
                                }
                                else {
                                    loot.sourceButton.setPosition(draggingObj._position.add(loot.pivotOffset));
                                }
                                float targetWidth = draggingObj.animatedSprite._width * draggingObj._scale.x;
                                float targetHeight = draggingObj.animatedSprite._height * draggingObj._scale.y;
                                int originalBw = loot.sourceButton.animatedSprite._width;
                                int originalBh = loot.sourceButton.animatedSprite._height;
                                float scaleX = (float) targetWidth / originalBw;
                                float scaleY = (float) targetHeight / originalBh;

                                loot.sourceButton._scale = new Vector2(scaleX, scaleY);
                                loot.sourceButton.isActive = true;
                                loot.sourceButton.paint.setAlpha(0);
                                loot.placed = true;
                                loot.placedPos = loot._position;
                            }
                        }
                        else{
                            // if user cannot find any valid slots
                            if(loot.placed)
                            {
                                loot.rotationAngle = initialRot;
                                if(loot.rotationAngle == 90)
                                {
                                    Log.d("Pointer","A");
                                    Vector2 newPiv = new Vector2(0,0);
                                    newPiv.x = loot.pivotOffset.y;
                                    newPiv.y = loot.pivotOffset.x;
                                    loot._position = loot.sourceButton._position.subtract(newPiv);
                                }
                                else{
                                    Log.d("Pointer","B");
                                    loot._position = loot.sourceButton._position.subtract(loot.pivotOffset);
                                }
                                for(LootSlot slots : loot.slotsOccupied)
                                {
                                    slots.occupied = true;
                                }
                                loot.sourceButton.rotationAngle = initialRot;
                                loot.sourceButton.isActive = true;

                            }
                            else{
                                Log.d("Pointer","C");
                                loot.sourceButton.rotationAngle = 0;
                                loot.sourceButton._scale = new Vector2(0.07f,0.07f);
                                loot.sourceButton.used = false;
                                loot.sourceButton.isActive = true;
                                m_goAbiLootList.remove(draggingObj);
                                m_goAbiLootList.remove(draggingObj);
                                draggingObj = null;
                                RebuildLootBtns();
                            }
                        }
                    }
                    else{
                        loot.sourceButton.rotationAngle = 0;
                        loot.sourceButton._scale = new Vector2(0.07f,0.07f);
                        loot.sourceButton.used = false;
                        loot.sourceButton.isActive = true;
                        m_goAbiLootList.remove(draggingObj);
                        m_goAbiLootList.remove(draggingObj);
                        draggingObj = null;
                        RebuildLootBtns();
                    }
                }
                //make all slots normal colour
                for (int a = 0; a < slots.length; a++) {
                    for (int b = 0; b < slots[0].length; b++) {
                        slots[a][b].clearTint();
                    }
                }
                // remove obj reference when finger let go
                if(draggingObj != null) {draggingObj = null;}
                break;
            case MotionEvent.ACTION_POINTER_UP:
                isTouching2 = false;
                break;
            case MotionEvent.ACTION_CANCEL:

                break;
        }
    }

    public void RebuildLootBtns() {
        ArrayList<LootButtonObj> unusedButtons = new ArrayList<>();
        // Loop through your actual button list
        for (LootButtonObj btn : lootBtns) {
            if (!btn.used) {
                unusedButtons.add(btn);
            }
        }

        Vector2 startPos = new Vector2(screenWidth/2, screenHeight/2-135);
        float spacing = 50 * GameActivity.instance.getResources().getDisplayMetrics().density;

        for(int i = 0; i < unusedButtons.size(); i++)
        {
            int offsetY = (int)spacing * i;
            LootButtonObj btn = unusedButtons.get(i);
            btn.paint.setAlpha(255);
            btn.targetPos = new Vector2(startPos.x, startPos.y + offsetY);
        }
    }

    //check if item was dropped within an item slot
    boolean isInsideSlot(Vector2 touchPos, LootSlot slot) {
        return slot.bounds.contains(touchPos.x,touchPos.y);
    }

    //checking for array size for inventory slots
    private boolean inBounds(int x, int y) {
        return (x >= 0 && x < slots.length &&
                y >= 0 && y < slots[0].length);
    }

    //whether the slot is valid for loot or not considering its size etc.
    private boolean canPlaceItemInSlot(int slotX, int slotY, int xSize, int ySize) {

        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {

                //all the slots to check for that the object occupies
                int checkX = slotX + x;
                int checkY = slotY + y;

                // Check boundary
                if (!inBounds(checkX, checkY)) {
                    return false;
                }

                // Check if occupied
                if (slots[checkX][checkY].occupied) {
                    return false;
                }
            }
        }
        return true;
    }

    //this is a function because it is constantly being used for looting
    private Vector2 findSlotIndexAtPosition(Vector2 touchPos) {
        for (int i = 0; i < slots.length; i++) {
            for (int j = 0; j < slots[0].length; j++) {
                if (isInsideSlot(touchPos, slots[i][j])) {
                    return new Vector2(i, j);   // store grid index
                }
            }
        }
        return null; // no slot found
    }
}
