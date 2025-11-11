package com.example.sampleapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

public class ButtonObj extends GameEntity {

    @Override
    public void onCreate(Vector2 pos, Vector2 scale, int spriteID) {
        super.onCreate(pos,scale, spriteID);
    }
    public void OnClick()
    {
        System.out.println("ButtonClicked");
    }
    public boolean checkIfPressed(Vector2 posToCheck)
    {

        float left = _position.x - _scale.x / 2f;
        float right = _position.x + _scale.x / 2f;
        float top = _position.y - _scale.y / 2f;
        float bottom = _position.y + _scale.y / 2f;

        return (posToCheck.x >= left && posToCheck.x <= right &&
                posToCheck.y >= top && posToCheck.y <= bottom);
    }
    @Override
    public void onRender(Canvas canvas) {
        super.onRender(canvas);
    }
}
