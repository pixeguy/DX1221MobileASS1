package com.example.sampleapp.Entity.Enemies.Slime;

import com.example.sampleapp.Enums.SpriteAnimationList;
import com.example.sampleapp.Statemchine.State;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

import java.util.Random;

public class SlimeIdleState extends State {

    private float idleDuration = 0.0f;

    public SlimeIdleState(GameEntity go, String mStateID) {
        super(go, mStateID);
    }

    @Override
    public void OnEnter() {
        super.OnEnter();
        if(((Slime)m_go).CheckIfPlayerNear(((Slime)m_go).ATTACK_RANGE)) {
            if(m_go.facingDirection.equals(0, -1))
                m_go.SetAnimation(SpriteAnimationList.SlimeIdleBack);
            else if(m_go.facingDirection.equals(0, 1))
                m_go.SetAnimation(SpriteAnimationList.SlimeIdleFront);
            else if(m_go.facingDirection.equals(-1, 0))
                m_go.SetAnimation(SpriteAnimationList.SlimeIdleLeft);
            else
                m_go.SetAnimation(SpriteAnimationList.SlimeIdleRight);
            return;
        }

        Random random = new Random();
        int randomDirection;
        do {
            randomDirection = random.nextInt(4);
            Vector2 direction = new Vector2(0, 0);
            switch (randomDirection) {
                case 0:
                    direction = new Vector2(0, -1);
                    break;
                case 1:
                    direction = new Vector2(0, 1);
                    break;
                case 2:
                    direction = new Vector2(-1, 0);
                    break;
                case 3:
                    direction = new Vector2(1, 0);
                    break;
            }
            boolean isNotAvailable = m_go.CheckIfOutsideWorldBound(direction, 50);
            if(isNotAvailable) {
                randomDirection = -1;
            }
        }while (randomDirection == -1);

        switch (randomDirection) {
            case 0:
                m_go.SetAnimation(SpriteAnimationList.SlimeIdleBack);
                m_go.facingDirection = new Vector2(0, -1);
                break;
            case 1:
                m_go.SetAnimation(SpriteAnimationList.SlimeIdleFront);
                m_go.facingDirection = new Vector2(0, 1);
                break;
            case 2:
                m_go.SetAnimation(SpriteAnimationList.SlimeIdleLeft);
                m_go.facingDirection = new Vector2(-1, 0);
                break;
            case 3:
                m_go.SetAnimation(SpriteAnimationList.SlimeIdleRight);
                m_go.facingDirection = new Vector2(1, 0);
                break;
        }

        float minIdleTime = 1.0f;
        float maxIdleTime = 2.0f;
        idleDuration = random.nextFloat() * (maxIdleTime - minIdleTime) + minIdleTime;
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);

        if(stateTimer >= idleDuration && !((Slime)m_go).CheckIfPlayerNear(((Slime)m_go).ATTACK_RANGE)) {
            m_go.sm.ChangeState("SlimeRun");
        }
    }


    @Override
    public void OnExit() {

    }
}
