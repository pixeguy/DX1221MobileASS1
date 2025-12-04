package com.example.sampleapp.mgp2d.core;

import android.graphics.Canvas;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

public abstract class GameScene {

    // region Static props for managing multiple scenes
    private static GameScene _current = null;

    private static GameScene _next = null;

    public static GameScene getCurrent() { return _current; }

    public static GameScene getNext() { return _next; }

    private static final HashMap<Class<?>, GameScene> map = new HashMap<>();

    // Scene Data
    protected  Vector<GameEntity> m_goList = new Vector<>();
    protected Vector<GameEntity> m_goListToAdd = new Vector<>();
    protected Vector<GameEntity> m_goListToRemove = new Vector<>();

    public static void clearClass()
    {
        map.clear();
    }

    public static void enter(Class<?> gameSceneClass) {
        if (!map.containsKey(gameSceneClass)) {
            try {
                map.put(gameSceneClass, (GameScene) gameSceneClass.newInstance());
            } catch (IllegalAccessException | InstantiationException e) {
                throw new RuntimeException(e);
            }
        }
        _next = map.get(gameSceneClass);
    }

    public static void enter(GameScene gameScene) {
        if (_current != null) _current.onExit();
        _current = gameScene;
        if (_current == null) return;
        _current.onEnter();
    }

    public static void exitCurrent() {
        if (_current == null) return;
        _current.onExit();
        _current = null;
    }

    // endregion

    // region Props for handling internal behaviour of game scene
    private boolean _isCreated = false;
    public void onCreate() { _isCreated = true; }
    public void onEnter() { if (!_isCreated) onCreate(); }
    public abstract void onUpdate(float dt);
    public abstract void onRender(Canvas canvas);
    public void onExit() {}

    public void UpdateGoList() {
        if (!m_goListToAdd.isEmpty()) {
            m_goList.addAll(m_goListToAdd);
            m_goListToAdd.clear();
        }
        if (!m_goListToRemove.isEmpty()) {
            for (GameEntity go : m_goListToRemove) {
                m_goList.remove(go);
            }
            m_goListToRemove.clear();
        }
    }

    //endregion
}
