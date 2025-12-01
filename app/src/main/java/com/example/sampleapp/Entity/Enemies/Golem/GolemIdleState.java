package com.example.sampleapp.Entity.Enemies.Golem;

import android.util.Log;

import com.example.sampleapp.Entity.Enemies.Enemy;
import com.example.sampleapp.Enums.SpriteAnimationList;
import com.example.sampleapp.Statemchine.State;
import com.example.sampleapp.mgp2d.core.GameEntity;

import java.util.Random;

public class GolemIdleState extends State {

    private final Random random;
    private float idleTime = 1.0f;

    public GolemIdleState(GameEntity go, String mStateID) {
        super(go, mStateID);
        random = new Random();
    }

    @Override
    public void OnEnter() {
        super.OnEnter();

        if(m_go.facingDirection.equals(0, -1)) {
            m_go.SetAnimation(SpriteAnimationList.GolemIdleBack);
        }
        else if(m_go.facingDirection.equals(0, 1)) {
            m_go.SetAnimation(SpriteAnimationList.GolemIdleFront);
        }
        else if(m_go.facingDirection.equals(-1, 0)) {
            m_go.SetAnimation(SpriteAnimationList.GolemIdleLeft);
        }
        else if(m_go.facingDirection.equals(1, 0)) {
            m_go.SetAnimation(SpriteAnimationList.GolemIdleRight);
        }

        float minIdleTime = 2.0f;
        float maxIdleTime = 2.5f;
        idleTime = random.nextFloat() * (maxIdleTime - minIdleTime) + minIdleTime;
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);

        if(!((Enemy)m_go).CheckIfPlayerNear(Golem.attackRange))
        {
            if(((Enemy)m_go).CheckIfPlayerNear(Golem.detectionRange)){
                m_go.sm.ChangeState("Run");
            }
            else if(stateTimer > idleTime) {
                m_go.sm.ChangeState("Walk");
            }
        }
    }

    @Override
    public void OnExit() {

    }
}
