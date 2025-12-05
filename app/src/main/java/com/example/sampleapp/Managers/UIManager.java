package com.example.sampleapp.Managers;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.example.sampleapp.UI.UIElement;
import com.example.sampleapp.mgp2d.core.Singleton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UIManager extends Singleton<UIManager> {
    private final Map<Integer, UIElement> activeTouches = new HashMap<>();
    private final List<UIElement> elements = new ArrayList<>();
    private final List<UIElement> focusElements = new ArrayList<>();
    private final List<UIElement> toRemoveFocusElements = new ArrayList<>();
    private final List<UIElement> toAdd = new ArrayList<>();
    private final List<UIElement> toRemove = new ArrayList<>();
    private UIElement focusedElement;

    public static UIManager getInstance() {
        return getInstance(UIManager.class);
    }

    public void addElement(UIElement element) {
        toAdd.add(element);
    }

    public void addElements(List<UIElement> elements) {
        toAdd.addAll(elements);
    }

    public void addFocusElements(List<UIElement> elements) {
        focusElements.addAll(elements);
    }

    public void removeElement(UIElement element) {
        toRemove.add(element);
    }

    public void removeElements(List<UIElement> elements) {
        toRemove.addAll(elements);
    }

    public void removeFocusElements(List<UIElement> elements) {
        toRemoveFocusElements.addAll(elements);
    }

    public void update(float dt) {
        focusElements.removeAll(toRemoveFocusElements);
        toRemoveFocusElements.clear();

        for (UIElement e : focusElements)
            e.onUpdate(dt);

        if(GameManager.getInstance().getCurrentState() == GameManager.GameState.PAUSED) return;

        elements.addAll(toAdd);
        toAdd.clear();
        elements.removeAll(toRemove);
        toRemove.clear();

        for (UIElement e : elements)
            e.onUpdate(dt);
    }

    public void onRender(Canvas canvas) {
        focusElements.sort(Comparator.comparingDouble(e -> e.zIndex));
        for (UIElement e : focusElements)
            if (e.visible) e.onRender(canvas);
        if(GameManager.getInstance().getCurrentState() == GameManager.GameState.PAUSED) return;
        // Sort by zIndex before drawing
        elements.sort(Comparator.comparingDouble(e -> e.zIndex));
        for (UIElement e : elements)
            if (e.visible) e.onRender(canvas);
    }

    public void handleTouch(MotionEvent event) {
        int action = event.getActionMasked();
        int pointerIndex = event.getActionIndex(); // get the index of the pointer
        int pointerId = event.getPointerId(pointerIndex);

        float x = event.getX(pointerIndex);
        float y = event.getY(pointerIndex);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                assignTouchToElement(pointerId, x, y);
                break;

            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < event.getPointerCount(); i++) {
                    int id = event.getPointerId(i);
                    float mx = event.getX(i);
                    float my = event.getY(i);
                    UIElement e = activeTouches.get(id);
                    if (e != null) e.onTouchMove(mx, my);
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                releaseTouch(pointerId);
                break;
        }
    }

    private void releaseTouch(int pointerId) {
        UIElement e = activeTouches.get(pointerId);
        if (e != null) {
            e.onTouchUp();
            activeTouches.remove(pointerId);
        }
    }

    private void assignTouchToElement(int pointerId, float x, float y) {
        for (UIElement e : elements) {
            if (e.isPointInside(x, y) && e.interactable) {
                e.onTouchDown(x, y);
                activeTouches.put(pointerId, e);
            }
        }

        for (UIElement e : focusElements) {
            if (e.isPointInside(x, y) && e.interactable) {
                e.onTouchDown(x, y);
                activeTouches.put(pointerId, e);
            }
        }
    }

    public void clear() {
        elements.clear();
        toAdd.clear();
        toRemove.clear();
    }

    public boolean isTouched(int pointerId) {
        return activeTouches.containsKey(pointerId);
    }
}
