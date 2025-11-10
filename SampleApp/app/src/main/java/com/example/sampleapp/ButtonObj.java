package com.example.sampleapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

public class ButtonObj extends GameEntity {
    private Bitmap buttonBitmap;

    @Override
    public void onCreate() {
        super.onCreate();
        Bitmap bmp = BitmapFactory.decodeResource(GameActivity.instance.getResources()
                , R.drawable.pause);
        _position = new Vector2(500,500);
        _scale = new Vector2(500,500);
        buttonBitmap = Bitmap.createScaledBitmap(bmp,(int)_scale.x,(int)_scale.y,true);

    }
    public void OnClick()
    {
        //System.out.println("ButtonClicked");
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
        //render at center of position, position is not top left anymore
        float drawX = _position.x - (_scale.x / 2f);
        float drawY = _position.y - (_scale.y / 2f);
        canvas.drawBitmap(buttonBitmap, drawX, drawY, null);
    }
}
