package com.example.sampleapp.Entity.Buttons;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.example.sampleapp.R;
import com.example.sampleapp.Ultilies.Utilies;
import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

public class JoystickObj extends GameEntity {
    // Joystick Properties
    private Bitmap joystickOuterSprite;
    private float joystickInnerRadius;
    private float joystickOuterRadius;

    private final Rect _joysticRect;
    private Vector2 joystickPosition = new Vector2(0, 0);
    private final Rect _src;
    private final Rect _dst;

    private float offsetY = 0.0f;
    int scaledWidthOuter = 0;
    int scaledHeightOuter = 0;
    int scaledWidthInner = 0;
    int scaledHeightInner = 0;

    private int prevMotion;
    private Vector2 _originalPos = new Vector2(0, 0);

    private Vector2 inputDirection = new Vector2(0, 0);
    public Vector2 getInputDirection()
    {
        return inputDirection;
    }

    public JoystickObj(Vector2 pos, float outerRadius) {
        onCreate(pos, new Vector2(1, 1));
        _originalPos.set(pos);

        joystickOuterRadius = outerRadius;
        joystickInnerRadius = outerRadius / 2.5f;

        joystickPosition = pos;

        joystickOuterSprite = BitmapFactory.decodeResource(GameActivity.instance.getResources(), R.drawable.controllerposition);
        joystickOuterSprite = Bitmap.createScaledBitmap(joystickOuterSprite, 100, 100, true);
        sprite = BitmapFactory.decodeResource(GameActivity.instance.getResources(), R.drawable.controllerradius);
        sprite = Bitmap.createScaledBitmap(sprite, 100, 100, true);

        int scaledWidth = (int) (sprite.getWidth() * joystickInnerRadius);
        int scaledHeight = (int) (sprite.getHeight() * joystickInnerRadius);

        int left = (int) (_position.x - (float) scaledWidth / 2);
        int top = (int) (_position.y - (float) scaledHeight / 2);

        _src = new Rect();
        _dst = new Rect(left, top, left + scaledWidth, top + scaledHeight);
        _joysticRect = new Rect(_dst);

        scaledWidthOuter = (int) (joystickOuterSprite.getWidth() * joystickOuterRadius);
        scaledHeightOuter = (int) (joystickOuterSprite.getHeight() * joystickOuterRadius);
        scaledWidthInner = (int) (sprite.getWidth() * joystickInnerRadius);
        scaledHeightInner = (int) (sprite.getHeight() * joystickInnerRadius);

        offsetY = scaledHeightInner / 2.0f;
    }

    @Override
    public void onUpdate(float dt) {
        MotionEvent event = GameActivity.instance.getMotionEvent();
        if(event == null)
            return;

        inputDirection = new Vector2(0, 0);

        if(event.getAction() == MotionEvent.ACTION_UP)
        {
            inputDirection = new Vector2(0, 0);

            _position.set(_originalPos);

            joystickPosition.x = _position.x;
            joystickPosition.y = _position.y;
        }

        if(event.getAction() == MotionEvent.ACTION_MOVE)
        {
            float x = event.getX();
            float y = event.getY();

            if(!_joysticRect.contains((int) x, (int) y) && prevMotion != MotionEvent.ACTION_MOVE)
            {
                _position.x = x;
                _position.y = y + offsetY;
                return;
            }

            joystickPosition.x = x;
            joystickPosition.y = y;

            float dx = joystickPosition.x - _position.x;
            float dy = joystickPosition.y - _position.y;

            float angle = Utilies.cal_angle(dx, dy);

            inputDirection = get8Direction(angle);
            if(inputDirection.x != 0 && inputDirection.y != 0)
                inputDirection.normalize();

            float distance = (float) Math.sqrt(dx * dx + dy * dy);
            if(distance + scaledWidthInner / 2.0f >= scaledWidthOuter / 2.0f)
            {
                float ratio = (scaledWidthOuter / 2.0f - scaledWidthInner / 2.0f) / distance;
                joystickPosition.x = _position.x + dx * ratio;
                joystickPosition.y = _position.y + dy * ratio;
            }
        }

        prevMotion = event.getAction();
    }

    @Override
    public void onRender(Canvas canvas) {
        // Center relative to x and y
        int left = (int) (_position.x - (float) scaledWidthOuter / 2);
        int top = (int) ((_position.y - offsetY) - (float) scaledHeightOuter / 2);

        _src.set(0, 0, joystickOuterSprite.getWidth(), joystickOuterSprite.getHeight());
        _dst.set(left, top, left + scaledWidthOuter, top + scaledHeightOuter);
        _joysticRect.set(_dst);

        canvas.drawBitmap(joystickOuterSprite, _src, _dst, null);

        left = (int) (joystickPosition.x - (float) scaledWidthInner / 2);
        top = (int) ((joystickPosition.y - offsetY) - (float) scaledHeightInner / 2);

        _dst.set(left, top, left + scaledWidthInner, top + scaledHeightInner);

        canvas.drawBitmap(sprite, null, _dst, null);
    }

    public Vector2 get8Direction(float angle) {
        if(angle >= 247.5 && angle < 292.5 ) {
            return new Vector2(0, -1);
        } else if(angle >= 292.5 && angle < 337.5 ) {
            return new Vector2(1, -1);
        } else if(angle >= 337.5 || angle < 22.5 ) {
            return new Vector2(1, 0);
        } else if(angle >= 22.5 && angle < 67.5 ) {
            return new Vector2(1, 1);
        } else if(angle >= 67.5 && angle < 112.5 ) {
            return new Vector2(0, 1);
        } else if(angle >= 112.5 && angle < 157.5 ) {
            return new Vector2(-1, 1);
        } else if(angle >= 157.5 && angle < 202.5 ) {
            return new Vector2(-1, 0);
        } else if(angle >= 202.5 && angle < 247.5 ) {
            return new Vector2(-1, -1);
        }
        return new Vector2(0, 0);
    }
}
