package com.example.sampleapp.Entity.Enemies.Toxito;

import android.util.Log;

import com.example.sampleapp.Entity.Enemies.Enemy;
import com.example.sampleapp.Entity.Player.PlayerObj;
import com.example.sampleapp.Enums.SpriteAnimationList;
import com.example.sampleapp.Statemchine.State;
import com.example.sampleapp.Ultilies.Utilies;
import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

import java.util.ArrayList;
import java.util.Random;

public class ToxitoMoveState extends State {

    private final Random random;
    private Vector2 target = new Vector2(0, 0);

    private boolean moveRight, moveLeft, moveUp, moveDown = true;
    private float moveTime;

    public ToxitoMoveState(GameEntity go, String mStateID) {
        super(go, mStateID);
        random = new Random();
    }

    @Override
    public void OnEnter() {
        super.OnEnter();
        m_go.SetAnimation(SpriteAnimationList.ToxitoWalkFront);
        target.set(m_go._position);

        moveRight = moveLeft = moveUp = moveDown = true;

        float minMoveTime = 3.0f;
        float maxMoveTime = 4.0f;
        moveTime = random.nextFloat() * (maxMoveTime - minMoveTime) + minMoveTime;
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);

        if(stateTimer >= moveTime && !((Enemy) m_go).CheckIfPlayerNear(Toxito.detectionRange)) {
            m_go.sm.ChangeState("Idle");
            return;
        }

        moveRight = moveLeft = moveUp = moveDown = true;
        Vector2 playerToEnemy = m_go._position.subtract(PlayerObj.getInstance().getPosition());
        if(playerToEnemy.getMagnitude() < Toxito.detectionRange) {
            if(PlayerObj.getInstance().isAlive) {
                Vector2 playerPos = PlayerObj.getInstance().getPosition();
                if(playerPos.x > m_go._position.x)
                    moveRight = false;
                else
                    moveLeft = false;

                if(playerPos.y > m_go._position.y)
                    moveDown = false;
                else
                    moveUp = false;
            }
        }

        if(m_go.CheckIfOutsideWorldBound(m_go.facingDirection, 50)){
            target.set(m_go._position);

            int[] directions = m_go.CheckForUnvailableDirection();

            if(directions[0] == 0) {
                m_go.facingDirection = new Vector2(0, 1);
                m_go.SetAnimation(SpriteAnimationList.ToxitoWalkFront);
            }
            else if(directions[1] == 1) {
                m_go.facingDirection = new Vector2(0, -1);
                m_go.SetAnimation(SpriteAnimationList.ToxitoWalkBack);
            }
            else if(directions[2] == 2) {
                m_go.facingDirection = new Vector2(1, 0);
                m_go.SetAnimation(SpriteAnimationList.ToxitoWalkRight);
            }
            else if(directions[3] == 3) {
                m_go.facingDirection = new Vector2(-1, 0);
                m_go.SetAnimation(SpriteAnimationList.ToxitoWalkLeft);
            }
            target = m_go._position.add(m_go.facingDirection.multiply(Toxito.baseSpeed));
        }

        Vector2 dir = target.subtract(m_go._position);
        if(dir.getMagnitude() < Toxito.baseSpeed * dt) {
            target.set(m_go._position);
            int directions = random.nextInt(4);
            if(directions == 0 && moveDown) {
                m_go.facingDirection = new Vector2(0, 1);
                m_go.SetAnimation(SpriteAnimationList.ToxitoWalkFront);
            }
            else if(directions == 1 && moveUp) {
                m_go.facingDirection = new Vector2(0, -1);
                m_go.SetAnimation(SpriteAnimationList.ToxitoWalkBack);
            }
            else if(directions == 2 && moveRight) {
                m_go.facingDirection = new Vector2(1, 0);
                m_go.SetAnimation(SpriteAnimationList.ToxitoWalkRight);
            }
            else if(directions == 3 && moveLeft) {
                m_go.facingDirection = new Vector2(-1, 0);
                m_go.SetAnimation(SpriteAnimationList.ToxitoWalkLeft);
            }

            target = m_go._position.add(m_go.facingDirection.multiply(Toxito.baseSpeed));
        }
        else {
            try {
                m_go.setPosition(m_go._position.add(m_go.facingDirection.multiply(Toxito.baseSpeed * dt)));
            }catch (Exception ignored){

            }

        }
    }

    @Override
    public void OnExit() {

    }
}
