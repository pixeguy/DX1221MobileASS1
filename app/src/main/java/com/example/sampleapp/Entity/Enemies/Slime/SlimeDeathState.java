package com.example.sampleapp.Entity.Enemies.Slime;

import com.example.sampleapp.Enums.SpriteAnimationList;
import com.example.sampleapp.Statemchine.State;
import com.example.sampleapp.mgp2d.core.GameEntity;

public class SlimeDeathState extends State {

    public SlimeDeathState(GameEntity go, String mStateID) {
        super(go, mStateID);
    }

    @Override
    public void OnEnter() {
        if(m_go.facingDirection.equals(0, -1))
            m_go.SetAnimation(SpriteAnimationList.SlimeDeathBack);
        else if(m_go.facingDirection.equals(0, 1))
            m_go.SetAnimation(SpriteAnimationList.SlimeDeathFront);
        else if(m_go.facingDirection.equals(-1, 0))
            m_go.SetAnimation(SpriteAnimationList.SlimeDeathLeft);
        else
            m_go.SetAnimation(SpriteAnimationList.SlimeDeathRight);
    }

    @Override
    public void OnUpdate(float dt) {
        if(m_go.animatedSprite.hasFinished()) {
            m_go.destroy();
        }
    }

    @Override
    public void OnExit() {

    }
}
