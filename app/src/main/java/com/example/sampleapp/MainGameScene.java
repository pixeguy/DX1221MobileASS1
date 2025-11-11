package com.example.sampleapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.GameScene;
import com.example.sampleapp.mgp2d.core.Vector2;

import java.util.ArrayList;

public class MainGameScene extends GameScene {
    private Bitmap backgroundBitmap;
    private Bitmap backgroundBitmap1;
    private float backgroundPosition = 0;
    private int screenWidth;
    private int screenHeight;
    boolean isTouching = false;

    private PlayerObj player;
    private ButtonObj[] buttons;
    private LootButtonObj lootBtn;
    ArrayList<GameEntity> objList = new ArrayList<>();
    private GameEntity draggingObj;
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
        player.onCreate(new Vector2(0,0),new Vector2(100,100),R.drawable.pause);

        buttons = new ButtonObj[1];
        for (int i = 0; i < buttons.length; i++)
        {
            buttons[i] = new ButtonObj();
            buttons[i].onCreate(new Vector2(1,1),new Vector2(10,10),R.drawable.pause);
        }
        lootBtn = new LootButtonObj();
        lootBtn.onCreate(new Vector2(400,400),new Vector2(200,200),R.drawable.flystar,LootType.Loot1);


        //hardcoding in the obj list for now
        objList.add(player);
        objList.add(buttons[0]);
        objList.add(lootBtn);
    }

    @Override
    public void onUpdate(float dt) {
        backgroundPosition = (backgroundPosition - dt * 500) % (float) screenWidth;
        for(GameEntity obj : objList)
        {
            obj.onUpdate(dt);
        }

        MotionEvent e = GameActivity.instance.getMotionEvent();
        if (e != null) {
            Vector2 touchPos = new Vector2(e.getX(),e.getY());

            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if(!isTouching){

                        if(lootBtn.checkIfPressed(touchPos))
                        {
                            lootBtn.OnClick(touchPos);
                            LootObj lootobj = new LootObj();
                            lootobj.onCreate(touchPos, lootBtn.loot.itemScale.multiply(100), lootBtn.loot.spriteID);
                            objList.add(lootobj);
                            draggingObj = lootobj;
                        }
                        isTouching = true;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(draggingObj != null)
                    {
                        draggingObj._position = touchPos;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    isTouching = false;
                    if(draggingObj != null) {draggingObj = null;}
                    break;
            }

        }
    }

    @Override
    public void onRender(Canvas canvas) {
        canvas.drawBitmap(backgroundBitmap,backgroundPosition,0,null);
        canvas.drawBitmap(backgroundBitmap1,backgroundPosition + screenWidth,0,null);
        for(GameEntity obj : objList)
        {
            obj.onRender(canvas);
        }
    }
}
