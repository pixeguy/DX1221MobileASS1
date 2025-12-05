package com.example.sampleapp.Managers;

import com.example.sampleapp.Screens.BaseScreen;
import com.example.sampleapp.mgp2d.core.GameScene;
import com.example.sampleapp.mgp2d.core.Singleton;

import java.util.HashMap;

public class ScreenManager extends Singleton<ScreenManager> {

    private static BaseScreen _current = null;

    private static BaseScreen _next = null;

    public static BaseScreen getCurrent() { return _current; }

    public static BaseScreen getNext() { return _next; }

    private static final HashMap<Class<?>, BaseScreen> map = new HashMap<>();

    public static ScreenManager getInstance() {
        return getInstance(ScreenManager.class);
    }

    public static void setScreen(Class<?> screenClass) {
        if (_current != null) _current.disposed();
        _current = map.getOrDefault(screenClass, null);
    }

    public static void setScreen(BaseScreen gameScene) {
        if (_current != null) _current.disposed();
        _current = gameScene;
    }

    public static void exitCurrent() {
        if (_current == null) return;
        _current.disposed();
        _current = null;
    }
}
