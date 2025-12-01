package com.example.sampleapp.Entity.Enemies.Golem;

import android.util.Log;

import com.example.sampleapp.Entity.Enemies.Enemy;
import com.example.sampleapp.Entity.Enemies.Toxito.Toxito;
import com.example.sampleapp.Entity.Player.PlayerObj;
import com.example.sampleapp.Enums.SpriteAnimationList;
import com.example.sampleapp.Statemchine.State;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

import java.util.Random;

public class GolemWalkState extends State {
    private final Random random;

    private float moveTime = 0f;

    private Vector2 target = new Vector2(0, 0);

    public GolemWalkState(GameEntity go, String mStateID) {
        super(go, mStateID);
        random = new Random();
    }

    @Override
    public void OnEnter() {
        super.OnEnter();

        if(m_go.facingDirection.equals(0, 1)) {
            m_go.SetAnimation(SpriteAnimationList.GolemWalkFront);
        }
        else if(m_go.facingDirection.equals(0, -1)) {
            m_go.SetAnimation(SpriteAnimationList.GolemWalkBack);
        }
        else if(m_go.facingDirection.equals(1, 0)) {
            m_go.SetAnimation(SpriteAnimationList.GolemWalkRight);
        }
        else if(m_go.facingDirection.equals(-1, 0)) {
            m_go.SetAnimation(SpriteAnimationList.GolemWalkLeft);
        }

        float minMoveTime = 3.0f;
        float maxMoveTime = 4.0f;
        moveTime = random.nextFloat() * (maxMoveTime - minMoveTime) + minMoveTime;
        target.set(m_go._position);
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);

        if(((Enemy) m_go).CheckIfPlayerNear(Golem.detectionRange))
        {
            m_go.sm.ChangeState("Run");
            return;
        }
        else if(stateTimer >= moveTime) {
            m_go.sm.ChangeState("Idle");
            return;
        }

        if(m_go.CheckIfOutsideWorldBound(m_go.facingDirection, 50)){
            target.set(m_go._position);

            int[] directions = m_go.CheckForUnvailableDirection();

            if(directions[0] == 0) {
                m_go.facingDirection = new Vector2(0, 1);
                m_go.SetAnimation(SpriteAnimationList.GolemWalkFront);
            }
            else if(directions[1] == 1) {
                m_go.facingDirection = new Vector2(0, -1);
                m_go.SetAnimation(SpriteAnimationList.GolemWalkBack);
            }
            else if(directions[2] == 2) {
                m_go.facingDirection = new Vector2(1, 0);
                m_go.SetAnimation(SpriteAnimationList.GolemWalkRight);
            }
            else if(directions[3] == 3) {
                m_go.facingDirection = new Vector2(-1, 0);
                m_go.SetAnimation(SpriteAnimationList.GolemWalkLeft);
            }
            target = m_go._position.add(m_go.facingDirection.multiply(Toxito.baseSpeed));
        }

        Vector2 dir = target.subtract(m_go._position);
        if(dir.getMagnitude() < Golem.walkSpeed * dt) {
            target.set(m_go._position);
            int directions = random.nextInt(4);
            if(directions == 0) {
                m_go.facingDirection = new Vector2(0, 1);
                m_go.SetAnimation(SpriteAnimationList.GolemWalkFront);
            }
            else if(directions == 1) {
                m_go.facingDirection = new Vector2(0, -1);
                m_go.SetAnimation(SpriteAnimationList.GolemWalkBack);
            }
            else if(directions == 2) {
                m_go.facingDirection = new Vector2(1, 0);
                m_go.SetAnimation(SpriteAnimationList.GolemWalkRight);
            }
            else {
                m_go.facingDirection = new Vector2(-1, 0);
                m_go.SetAnimation(SpriteAnimationList.GolemWalkLeft);
            }

            target = m_go._position.add(m_go.facingDirection.multiply(Golem.walkSpeed));
        }
        else {
            try {
                m_go.setPosition(m_go._position.add(m_go.facingDirection.multiply(Golem.walkSpeed * dt)));
            }catch (Exception ignored){

            }
        }
    }

    @Override
    public void OnExit() {

    }
}
