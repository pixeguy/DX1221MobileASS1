package com.example.sampleapp.Input;

import android.view.MotionEvent;

import com.example.sampleapp.Managers.UIManager;
import com.example.sampleapp.mgp2d.core.GameActivity;

public class SwipeGestureDetector {
    private static final int SWIPE_THRESHOLD = 200;
    private static final int SWIPE_TIME_LIMIT = 400;

    private float startX, startY;
    private long startTime;

    public interface OnSwipeListener {
        void onSwipeUp();
        void onSwipeDown();
        void onSwipeLeft();
        void onSwipeRight();
    }

    private OnSwipeListener listener;

    public void setOnSwipeListener(OnSwipeListener listener) {
        this.listener = listener;
    }

    public void onTouchEvent(MotionEvent event) {

        int action = event.getActionMasked();
        int index = event.getActionIndex();
        int pointerId = event.getPointerId(index);

        if(UIManager.getInstance().isTouched(pointerId)) return;

        float x = event.getX(index);
        float y = event.getY(index);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                startX = x;
                startY = y;
                startTime = System.currentTimeMillis();
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                long endTime = System.currentTimeMillis();

                float dx = x - startX;
                float dy = y - startY;
                long dt = endTime - startTime;

                float absDX = Math.abs(dx);
                float absDY = Math.abs(dy);

                if (dt < SWIPE_TIME_LIMIT) { // ignore slow drags
                    if (absDX > absDY && absDX > SWIPE_THRESHOLD) {
                        if (dx > 0) listener.onSwipeRight();
                        else listener.onSwipeLeft();
                    } else if (absDY > SWIPE_THRESHOLD) {
                        if (dy > 0) listener.onSwipeDown();
                        else listener.onSwipeUp();
                    }
                }
                break;
        }
    }
}
