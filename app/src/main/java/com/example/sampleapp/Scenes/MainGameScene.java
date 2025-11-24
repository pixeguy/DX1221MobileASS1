package com.example.sampleapp.Scenes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import com.example.sampleapp.Entity.BackgroundEntity;
import com.example.sampleapp.Entity.Buttons.LootButtonObj;
import com.example.sampleapp.Entity.Inventory.LootObj;
import com.example.sampleapp.Entity.Inventory.LootSlot;
import com.example.sampleapp.Enums.LootType;
import com.example.sampleapp.Entity.Player.PlayerObj;
import com.example.sampleapp.R;
import com.example.sampleapp.Entity.SampleCoin;
import com.example.sampleapp.Enums.SpriteList;
import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.GameScene;
import com.example.sampleapp.mgp2d.core.Vector2;

import java.util.ArrayList;
import java.util.HashMap;

public class MainGameScene extends GameScene {
    private Bitmap backgroundBitmap;
    private Bitmap backgroundBitmap1;
    private float backgroundPosition = 0;
    private int screenWidth;
    private int screenHeight;

    private PlayerObj player;
    private LootButtonObj lootBtn;
    private LootSlot[][] slots; //  [col][row].....[x][y]
    public final float lootGridSize = 100;

    //to store reference to current dragging obj
    private GameEntity draggingObj;
    boolean isTouching = false;


    @Override
    public void onCreate() {
        super.onCreate();

        screenWidth = GameActivity.instance.getResources().getDisplayMetrics().widthPixels;
        screenHeight = GameActivity.instance.getResources().getDisplayMetrics().heightPixels;
        Bitmap bmp = BitmapFactory.decodeResource(GameActivity.instance.getResources()
                , R.drawable.gamescene);
        backgroundBitmap = Bitmap.createScaledBitmap(bmp,screenWidth,screenHeight,true);

        backgroundBitmap1 = Bitmap.createScaledBitmap(bmp,screenWidth,screenHeight,true);

        //init player
        player = new PlayerObj();
        player.onCreate(new Vector2(0,0),new Vector2(1,1), SpriteList.PlayerIdle);

        //init temporary button
        lootBtn = new LootButtonObj();
        lootBtn.onCreate(new Vector2(400,400),new Vector2(1,3),SpriteList.ExamplePause, LootType.Loot1);

        BackgroundEntity GreyOverlay = new BackgroundEntity(R.drawable.greyoverlay);
        m_goList.add(GreyOverlay);

        //init all the slots, vv long so i minimize it first
        {
            slots = new LootSlot[5][5];

            Vector2 slotStartPos = new Vector2(700, 500);
            Vector2 scale = new Vector2(1, 1);

            for (int x = 0; x < 5; x++) {          // column
                for (int y = 0; y < 5; y++) {      // row

                    slots[x][y] = new LootSlot();

                    float worldX = slotStartPos.x + x * (scale.x * lootGridSize);
                    float worldY = slotStartPos.y + y * (scale.y * lootGridSize);

                    slots[x][y].onCreate(new Vector2(worldX, worldY), scale);
                    m_goList.add(slots[x][y]);
                }
            }
        }

        //hardcoding in the obj list for now
        m_goList.add(player);
        m_goList.add(lootBtn);


        SampleCoin coin = new SampleCoin();
        coin.onCreate(new Vector2(700,700),5);
        m_goList.add(coin);
    }

    @Override
    public void onUpdate(float dt) {
        backgroundPosition = (backgroundPosition - dt * 500) % (float) screenWidth;
        for(GameEntity obj : m_goList)
        {
            obj.onUpdate(dt);
        }
        HandleTouch();
//        MotionEvent e = GameActivity.instance.getMotionEvent();
//        if (e != null) {
//            Vector2 touchPos = new Vector2(e.getX(),e.getY());
//
//            switch (e.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    if(!isTouching){ // action down registers as hold on android studio emulator
//                        if(lootBtn.checkIfPressed(touchPos))
//                        {
//                            lootBtn.OnClick(touchPos);
//
//                            //creating my loot obj
//                            LootObj lootobj = new LootObj();
//                            //grab lootType ref from btn
//                            lootobj.onCreate(touchPos,lootBtn.loot);
//                            m_goList.add(lootobj);
//
//                            //assign obj to reference so scene can use it
//                            draggingObj = lootobj;
//                        }
//                        isTouching = true;
//                    }
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    if(draggingObj != null)
//                    {
//                        //drag obj with touching pos
//                        draggingObj.setPosition(touchPos);
//
//                        //make all slots normal colour
//                        for (int a = 0; a < slots.length; a++) {
//                            for (int b = 0; b < slots[0].length; b++) {
//                                slots[a][b].clearTint();
//                            }
//                        }
//
//                        if (draggingObj instanceof LootObj)
//                        {
//                            LootObj loot = (LootObj) draggingObj;
//                            int xSize = (int) loot.lootType.itemScale.x;
//                            int ySize = (int) loot.lootType.itemScale.y;
//
//                            boolean tinted = false;
//
//                            //loop through the grid. return grid slot index if user touch is on a slot
//                            Vector2 slot = findSlotIndexAtPosition(touchPos);
//                            if (slot != null) {
//                                int i = (int) slot.x;
//                                int j = (int) slot.y;
//
//                                boolean canPlace = canPlaceItemInSlot(i, j, xSize, ySize);
//                                int color = canPlace ? 0x5500FF00 : 0x77FF0000; //green or red
//
//                                //loop through the size of the object
//                                for (int x = 0; x < xSize; x++) {
//                                    for (int y = 0; y < ySize; y++) {
//
//                                        int slotX = i + x;
//                                        int slotY = j + y;
//
//                                        //if is out of bounds, dont try to tint it
//                                        if (inBounds(slotX, slotY)) {
//                                            slots[slotX][slotY].setTint(color);
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    break;
//                case MotionEvent.ACTION_UP:
//                    isTouching = false;
//
//                    if(draggingObj instanceof LootObj)
//                    {
//                        //get details of the loot obj
//                        LootObj loot = (LootObj) draggingObj;
//                        int xSize = (int)loot.lootType.itemScale.x;
//                        int ySize = (int)loot.lootType.itemScale.y;
//
//                        //loop through the grid. return grid slot index if user touch is on a slot
//                        Vector2 slot = findSlotIndexAtPosition(touchPos);
//                        if (slot != null) {
//                            int i = (int) slot.x;
//                            int j = (int) slot.y;
//
//                            //check if item slots are valid
//                            if(canPlaceItemInSlot(i,j,xSize,ySize)){
//                                draggingObj.setPosition(slots[i][j]._position);
//                                //change all slots that it occupies to occupied
//                                for (int x = 0; x < xSize; x++) {
//                                    for (int y = 0; y < ySize; y++) {
//                                        int slotX = i + x;   // remember: i = row (Y), j = column (X)
//                                        int slotY = j + y;
//                                        slots[slotX][slotY].occupied = true;
//                                    }
//                                }
//                            }
//                            else{
//                                // if user cannot find any valid slots
//                                m_goList.remove(draggingObj);
//                                draggingObj = null;
//                            }
//                        }
//                        else{
//                            //user touch is not on any slot
//                            m_goList.remove(draggingObj);
//                            draggingObj = null;
//                        }
//                    }
//                    //make all slots normal colour
//                    for (int a = 0; a < 5; a++) {
//                        for (int b = 0; b < 5; b++) {
//                            slots[a][b].clearTint();
//                        }
//                    }
//                    // remove obj reference when finger let go
//                    if(draggingObj != null) {draggingObj = null;}
//                    break;
//            }
//        }
    }

    @Override
    public void onRender(Canvas canvas) {
        canvas.drawBitmap(backgroundBitmap,backgroundPosition,0,null);
        canvas.drawBitmap(backgroundBitmap1,backgroundPosition + screenWidth,0,null);
        for(GameEntity obj : m_goList)
        {
            obj.onRender(canvas);
        }
    }

    //check if item was dropped within an item slot
    boolean isInsideSlot(Vector2 touchPos, LootSlot slot)
    {
        return slot.bounds.contains(touchPos.x,touchPos.y);
    }

    //checking for array size for inventory slots
    private boolean inBounds(int x, int y) {
        return (x >= 0 && x < slots.length &&
                y >= 0 && y < slots[0].length);
    }

    //whether the slot is valid for loot or not considering its size etc.
    private boolean canPlaceItemInSlot(int slotX, int slotY, int xSize, int ySize) {

        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {

                //all the slots to check for that the object occupies
                int checkX = slotX + x;
                int checkY = slotY + y;

                // Check boundary
                if (!inBounds(checkX, checkY)) {
                    return false;
                }

                // Check if occupied
                if (slots[checkX][checkY].occupied) {
                    return false;
                }
            }
        }
        return true;
    }

    //this is a function because it is constantly being used for looting
    private Vector2 findSlotIndexAtPosition(Vector2 touchPos) {
        for (int i = 0; i < slots.length; i++) {
            for (int j = 0; j < slots[0].length; j++) {
                if (isInsideSlot(touchPos, slots[i][j])) {
                    return new Vector2(i, j);   // store grid index
                }
            }
        }
        return null; // no slot found
    }

    private HashMap<Integer, LootObj> activeLoot = new HashMap<>();
    private void HandleTouch()
    {
        MotionEvent event = GameActivity.instance.getMotionEvent();
        if (event == null)
            return;

        int action = event.getActionMasked();
        int index = event.getActionIndex();
        int pointerID = event.getPointerId(index);
        Log.d("Test", index + " : " + String.valueOf(pointerID));
        Vector2 touchPos = new Vector2(event.getX(), event.getY());

        switch(action)
        {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                Vector2 pos = new Vector2(event.getX(pointerID), event.getY(pointerID));

                LootObj loot = new LootObj();
                loot.onCreate(pos, lootBtn.loot);
                m_goList.add(loot);

                activeLoot.put(pointerID, loot);
                break;
            case MotionEvent.ACTION_UP:
                if (activeLoot.containsKey(pointerID)) {

                    LootObj looty = activeLoot.get(pointerID);
                    Log.d("Test", String.valueOf(pointerID));
                    m_goList.remove(looty);

                    activeLoot.remove(pointerID);
                }
                break;
        }
    }
}
