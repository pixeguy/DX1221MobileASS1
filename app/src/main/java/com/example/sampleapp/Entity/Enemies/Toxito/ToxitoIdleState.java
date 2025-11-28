package com.example.sampleapp.Entity.Enemies.Toxito;

import android.util.Log;

import com.example.sampleapp.Entity.Enemies.Enemy;
import com.example.sampleapp.Enums.SpriteAnimationList;
import com.example.sampleapp.Statemchine.State;
import com.example.sampleapp.mgp2d.core.GameEntity;

import java.util.Random;

public class ToxitoIdleState extends State {

    private float idleTime;

    public ToxitoIdleState(GameEntity go, String mStateID) {
        super(go, mStateID);
    }

    @Override
    public void OnEnter() {
        super.OnEnter();
        m_go.SetAnimation(SpriteAnimationList.ToxitoIdleFront);

        Random random = new Random();
        float minIdleTime = 1.0f;
        float maxIdleTime = 2.0f;
        idleTime = random.nextFloat() * (maxIdleTime - minIdleTime) + minIdleTime;

        if(m_go.facingDirection.equals(0, 1)) {
            m_go.SetAnimation(SpriteAnimationList.ToxitoIdleFront);
        }
        else if(m_go.facingDirection.equals(0, -1)) {
            m_go.SetAnimation(SpriteAnimationList.ToxitoIdleBack);
        }
        else if(m_go.facingDirection.equals(1, 0)) {
            m_go.SetAnimation(SpriteAnimationList.ToxitoIdleRight);
        }
        else if(m_go.facingDirection.equals(-1, 0)) {
            m_go.SetAnimation(SpriteAnimationList.ToxitoIdleLeft);
        }
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);

        if(((Enemy) m_go).CheckIfPlayerNear(Toxito.detectionRange) || stateTimer > idleTime) {
            m_go.sm.ChangeState("Run");
        }
    }

    @Override
    public void OnExit() {

    }
}
