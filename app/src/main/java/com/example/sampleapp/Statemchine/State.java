package com.example.sampleapp.Statemchine;

import com.example.sampleapp.mgp2d.core.GameEntity;

public abstract class State {
    private final String m_stateID;
    protected GameEntity m_go;

    protected float stateTimer = 0.0f;

    public String GetStateID() {
        return m_stateID;
    }

    public State(GameEntity go, String mStateID) {
        m_stateID = mStateID;
        m_go = go;
    }

    public void OnEnter() {
        stateTimer = 0.0f;
    }
    public void OnUpdate(float dt) {
        stateTimer += dt;
    }
    public abstract void OnExit();
}
