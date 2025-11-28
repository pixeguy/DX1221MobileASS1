package com.example.sampleapp.Statemchine;

import java.util.HashMap;

public class Statemachine {
    private final HashMap<String, State> m_states;

    private State m_currentState = null;
    private State m_nextState = null;

    public Statemachine() {
        m_states = new HashMap<String, State>();
    }

    public void AddState(State newState) {
        if(newState == null) return;
        if(m_states.containsKey(newState.GetStateID())) return;
        if(m_currentState == null)
        {
            m_currentState = newState;
            m_nextState = newState;
            m_currentState.OnEnter();
        }
        m_states.put(newState.GetStateID(), newState);
    }

    public boolean ChangeState(String stateID) {
        if(!m_states.containsKey(stateID))
            return false;

        m_nextState = m_states.get(stateID);
        return true;
    }

    public final String GetCurrentStateID() {
        if(m_currentState != null)
            return m_currentState.GetStateID();
        return "<No states>";
    }

    public void Update(float dt) {
        if(m_nextState != m_currentState)
        {
            m_currentState.OnExit();
            m_currentState = m_nextState;
            m_currentState.OnEnter();
        }
        m_currentState.OnUpdate(dt);
    }
}
