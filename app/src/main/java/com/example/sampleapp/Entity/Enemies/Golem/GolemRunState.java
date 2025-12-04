package com.example.sampleapp.Entity.Enemies.Golem;

import android.graphics.RectF;
import android.util.Log;

import com.example.sampleapp.Collision.Colliders.CircleCollider2D;
import com.example.sampleapp.Entity.Enemies.Enemy;
import com.example.sampleapp.Entity.Player.PlayerObj;
import com.example.sampleapp.Enums.SpriteAnimationList;
import com.example.sampleapp.Scenes.GameLevel.GameLevelScene;
import com.example.sampleapp.Statemchine.State;
import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

import java.util.Random;

public class GolemRunState extends State {

    private final Random random;
    private boolean moveRight, moveLeft, moveUp, moveDown = true;
    private Vector2 target = new Vector2(0, 0);

    private RectF world_bounds;

    public GolemRunState(GameEntity go, String mStateID) {
        super(go, mStateID);
        random = new Random();
        world_bounds = GameLevelScene.world_bounds;
    }

    @Override
    public void OnEnter() {
        super.OnEnter();

        if(m_go.facingDirection.equals(0, 1)) {
            m_go.SetAnimation(SpriteAnimationList.GolemRunFront);
        }
        else if(m_go.facingDirection.equals(0, -1)) {
            m_go.SetAnimation(SpriteAnimationList.GolemRunBack);
        }
        else if(m_go.facingDirection.equals(1, 0)) {
            m_go.SetAnimation(SpriteAnimationList.GolemRunRight);
        }
        else if(m_go.facingDirection.equals(-1, 0)) {
            m_go.SetAnimation(SpriteAnimationList.GolemRunLeft);
        }

        moveRight = moveLeft = moveUp = moveDown = true;
        target.set(m_go._position);
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);

        if(!((Enemy)m_go).CheckIfPlayerNear(Golem.DETECT_RANGE))
        {
            m_go.sm.ChangeState("Idle");
            return;
        }

        moveRight = moveLeft = moveUp = moveDown = true;
        if(PlayerObj.getInstance().isAlive) {
            Vector2 playerPos = PlayerObj.getInstance().getPosition();
            if(playerPos.x > m_go._position.x)
                moveLeft = false;
            else
                moveRight = false;

            if(playerPos.y > m_go._position.y)
                moveUp = false;
            else
                moveDown = false;
        }

        Vector2 dir = target.subtract(m_go._position);
        if(dir.getMagnitude() < Golem.RUN_SPEED * dt) {
            target.set(m_go._position);
            int directions = random.nextInt(4);
            if(directions == 0 && moveDown) {
                m_go.facingDirection = new Vector2(0, 1);
                m_go.SetAnimation(SpriteAnimationList.GolemRunFront);
            }
            else if(directions == 1 && moveUp) {
                m_go.facingDirection = new Vector2(0, -1);
                m_go.SetAnimation(SpriteAnimationList.GolemRunBack);
            }
            else if(directions == 2 && moveRight) {
                m_go.facingDirection = new Vector2(1, 0);
                m_go.SetAnimation(SpriteAnimationList.GolemRunRight);
            }
            else if(directions == 3 && moveLeft) {
                m_go.facingDirection = new Vector2(-1, 0);
                m_go.SetAnimation(SpriteAnimationList.GolemRunLeft);
            }

            target = m_go._position.add(m_go.facingDirection.multiply(Golem.RUN_SPEED));
            CircleCollider2D collider = (CircleCollider2D) m_go.collider;
            if(m_go.CheckIfOutsideWorldBound(m_go.facingDirection, (int) Golem.RUN_SPEED))
                target.set(m_go._position);
        }
        else {
            try {
                m_go.setPosition(m_go._position.add(m_go.facingDirection.multiply(Golem.RUN_SPEED * dt)));
            }catch (Exception ignored){

            }
        }
    }

    @Override
    public void OnExit() {

    }
}
