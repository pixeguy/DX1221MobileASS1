package com.example.sampleapp.Entity.Enemies.Golem;

import com.example.sampleapp.Enums.SpriteAnimationList;
import com.example.sampleapp.Statemchine.State;
import com.example.sampleapp.mgp2d.core.GameEntity;

public class GolemDeathState extends State {
    public GolemDeathState(GameEntity go, String mStateID) {
        super(go, mStateID);
    }

    @Override
    public void OnEnter() {
        super.OnEnter();
        m_go.isAlive = false;
        m_go._rotationZ = 0.0f;
        if(m_go.facingDirection.equals(0, -1))
            m_go.SetAnimation(SpriteAnimationList.GolemDeathBack);
        else if(m_go.facingDirection.equals(0, 1))
            m_go.SetAnimation(SpriteAnimationList.GolemDeathFront);
        else if(m_go.facingDirection.equals(-1, 0))
            m_go.SetAnimation(SpriteAnimationList.GolemDeathLeft);
        else
            m_go.SetAnimation(SpriteAnimationList.GolemDeathRight);
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);

        if(m_go.animatedSprite.hasFinished())
        {
            m_go.destroy();
        }
    }

    @Override
    public void OnExit() {

    }
}
