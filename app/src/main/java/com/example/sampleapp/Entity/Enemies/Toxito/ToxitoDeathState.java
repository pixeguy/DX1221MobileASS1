package com.example.sampleapp.Entity.Enemies.Toxito;

import com.example.sampleapp.Enums.SpriteAnimationList;
import com.example.sampleapp.Statemchine.State;
import com.example.sampleapp.mgp2d.core.GameEntity;

public class ToxitoDeathState extends State {
    public ToxitoDeathState(GameEntity go, String mStateID) {
        super(go, mStateID);
    }

    @Override
    public void OnEnter() {
        super.OnEnter();
        m_go.SetAnimation(SpriteAnimationList.ToxitoDeathFront);
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        if(m_go.animatedSprite.hasFinished()) {
            m_go.destroy();
        }
    }

    @Override
    public void OnExit() {

    }
}
