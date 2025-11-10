package com.example.sampleapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.GameScene;
import com.example.sampleapp.mgp2d.core.Vector2;

public class MainGameScene extends GameScene {
    private Bitmap backgroundBitmap;
    private Bitmap backgroundBitmap1;
    private float backgroundPosition = 0;
    private int screenWidth;
    private int screenHeight;

    private PlayerObj player;
    private ButtonObj[] buttons;
    @Override
    public void onCreate() {
        super.onCreate();
        screenWidth = GameActivity.instance.getResources().getDisplayMetrics().widthPixels;
        screenHeight = GameActivity.instance.getResources().getDisplayMetrics().heightPixels;
        Bitmap bmp = BitmapFactory.decodeResource(GameActivity.instance.getResources()
                , R.drawable.gamescene);
        backgroundBitmap = Bitmap.createScaledBitmap(bmp,screenWidth,screenHeight,true);

        backgroundBitmap1 = Bitmap.createScaledBitmap(bmp,screenWidth,screenHeight,true);

        player = new PlayerObj();
        player.onCreate();

        buttons = new ButtonObj[1];
        for (int i = 0; i < buttons.length; i++)
        {
            buttons[i] = new ButtonObj();
            buttons[i].onCreate();
        }
    }

    @Override
    public void onUpdate(float dt) {
        backgroundPosition = (backgroundPosition - dt * 500) % (float) screenWidth;
        player.onUpdate(dt);
        for(ButtonObj button : buttons)
        {
            //button.onUpdate(dt);
        }

        MotionEvent e = GameActivity.instance.getMotionEvent();
        if (e != null) {
            Vector2 touchPos = new Vector2(e.getX(),e.getY());

            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // Example: move player up when screen is touched
                    for(ButtonObj button : buttons)
                    {
                        if(button.checkIfPressed(touchPos))
                        {
                            System.out.println("Wohoo");
                        }
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    // Handle drag
                    //float x = e.getX();
                    //float y = e.getY();

                    //player._position = new Vector2(x,y);
                    break;
                case MotionEvent.ACTION_UP:
                    // Touch released
                    break;
            }

        }
    }

    @Override
    public void onRender(Canvas canvas) {
        canvas.drawBitmap(backgroundBitmap,backgroundPosition,0,null);
        canvas.drawBitmap(backgroundBitmap1,backgroundPosition + screenWidth,0,null);
        player.onRender(canvas);
        for(ButtonObj button : buttons)
        {
            button.onRender(canvas);
        }
    }

}
