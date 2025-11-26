package com.example.sampleapp.Scenes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.example.sampleapp.Entity.BackgroundEntity;
import com.example.sampleapp.Entity.Buttons.LootButtonObj;
import com.example.sampleapp.Entity.Buttons.TestingBtn;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class MainGameScene extends GameScene {
    private Bitmap backgroundBitmap;
    private Bitmap backgroundBitmap1;
    private float backgroundPosition = 0;
    private int screenWidth;
    private int screenHeight;

    private PlayerObj player;
    private ArrayList<LootButtonObj> lootBtns;

    TestingBtn testing = new TestingBtn(); boolean spawnPhase = false;
    private LootSlot[][] slots; //  [col][row].....[x][y]
    public final float lootGridSize = 100;

    //to store reference to current dragging obj
    private GameEntity draggingObj;
    boolean isTouching = false;
    Random rand = new Random();


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
        player.onCreate(new Vector2(screenWidth / 2, screenHeight/2),new Vector2(1,1), SpriteList.PlayerIdle);
        //hardcoding in the obj list for now
        m_goList.add(player);

        //init all the slots, vv long so i minimize it first
        {
            slots = new LootSlot[5][5];

            Vector2 slotStartPos = new Vector2(screenWidth / 4, screenHeight/6);
            Vector2 scale = new Vector2(0.06f, 0.06f);

            for (int x = 0; x < 5; x++) {          // column
                for (int y = 0; y < 5; y++) {      // row

                    slots[x][y] = new LootSlot();

                    float worldX = slotStartPos.x + x * (scale.x * lootGridSize * 26f);
                    float worldY = slotStartPos.y + y * (scale.y * lootGridSize * 26f);

                    slots[x][y].onCreate(new Vector2(worldX, worldY), scale);
                    slots[x][y].isActive = true;
                    m_goList.add(slots[x][y]);
                }
            }
        }

        testing.onCreate(new Vector2(screenWidth / 2, screenHeight/2 - 200),new Vector2(1,1),SpriteList.ExamplePause);
        m_goList.add(testing);
    }

    @Override
    public void onUpdate(float dt) {
        backgroundPosition = (backgroundPosition - dt * 500) % (float) screenWidth;
        for(GameEntity obj : m_goList)
        {
            if(obj.isActive){
                obj.onUpdate(dt);
            }
        }

       // HandleTouch();
        MotionEvent e = GameActivity.instance.getMotionEvent();
        if (e != null) {
            Vector2 touchPos = new Vector2(e.getX(),e.getY());

            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //x525.95996y1253.9062
                    //x1080y2400
                    if(!isTouching){ // action down registers as hold on android studio emulator
                        if (testing.checkIfPressed(touchPos)){
                            if(!spawnPhase)
                                StartLootPhase();
                            else
                                EndLootPhase();
                        }
                        if(lootBtns != null) { //whether the list exists
                            for (LootButtonObj lootBtn : lootBtns) {
                                if (lootBtn.checkIfPressed(touchPos)) {
                                    if (!lootBtn.isActive) {
                                        continue;
                                    }
                                    if (!lootBtn.used) { //if the loot obj is already in the grid
                                        lootBtn.OnClick(touchPos);

                                        //creating my loot obj
                                        LootObj lootobj = new LootObj();
                                        //grab lootType ref from btn
                                        lootobj.onCreate(touchPos, lootBtn.loot);
                                        m_goList.add(lootobj);

                                        //assign obj to reference so scene can use it
                                        draggingObj = lootobj;
                                        lootBtn.isActive = false;
                                        //RelayoutLootButtons();
                                        lootobj.sourceButton = lootBtn;
                                    } else { //find the correct loot obj corresponding to the button
                                        for (GameEntity entity : m_goList) {
                                            if (entity instanceof LootObj) {
                                                LootObj loot = (LootObj) entity;
                                                if (loot.sourceButton == lootBtn) {
                                                    draggingObj = loot;
                                                    lootBtn.isActive = false;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        isTouching = true;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(draggingObj != null)
                    {
                        //drag obj with touching pos
                        draggingObj.setPosition(touchPos);

                        //make all slots normal colour
                        for (int a = 0; a < slots.length; a++) {
                            for (int b = 0; b < slots[0].length; b++) {
                                slots[a][b].clearTint();
                            }
                        }

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
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    isTouching = false;

                    if(draggingObj instanceof LootObj)
                    {
                        //get details of the loot obj
                        LootObj loot = (LootObj) draggingObj;
                        int xSize = (int)loot.lootType.itemScale.x;
                        int ySize = (int)loot.lootType.itemScale.y;

                        //loop through the grid. return grid slot index if user touch is on a slot
                        Vector2 slot = findSlotIndexAtPosition(touchPos);
                        if (slot != null) {
                            int i = (int) slot.x;
                            int j = (int) slot.y;

                            //check if item slots are valid
                            if(canPlaceItemInSlot(i,j,xSize,ySize)){
                                draggingObj.setPosition(slots[i][j]._position);

                                if(loot.placed){
                                    for(LootSlot slots : loot.slotsOccupied)
                                    {
                                        slots.occupied = false;
                                    }
                                }

                                //change all slots that it occupies to occupied
                                for (int x = 0; x < xSize; x++) {
                                    for (int y = 0; y < ySize; y++) {
                                        int slotX = i + x;   // remember: i = row (Y), j = column (X)
                                        int slotY = j + y;
                                        slots[slotX][slotY].occupied = true;
                                        loot.slotsOccupied.add(slots[slotX][slotY]);
                                    }
                                }

                                //setting button to loot pos, to press again
                                {
                                    loot.sourceButton.setPosition(draggingObj._position.add(loot.pivotOffset));

                                    float targetWidth = draggingObj.animatedSprite._width * draggingObj._scale.x;
                                    float targetHeight = draggingObj.animatedSprite._height * draggingObj._scale.y;
                                    int originalBw = loot.sourceButton.animatedSprite._width;
                                    int originalBh = loot.sourceButton.animatedSprite._height;
                                    float scaleX = (float) targetWidth / originalBw;
                                    float scaleY = (float) targetHeight / originalBh;

                                    loot.sourceButton._scale = new Vector2(scaleX, scaleY);
                                    loot.sourceButton.isActive = true;
                                    loot.sourceButton.used = true;
                                    loot.placed = true;
                                    loot.placedPos = loot._position;
                                }
                            }
                            else{
                                // if user cannot find any valid slots
                                if(loot.placed)
                                {
                                    loot._position = loot.placedPos;
                                    loot.sourceButton.isActive = true;
                                }
                                else{
                                    loot.sourceButton.isActive = true;
                                    m_goList.remove(draggingObj);
                                    draggingObj = null;
                                }
                            }
                        }
                        else{
                            //user touch is not on any slot
                            if(loot.placed)
                            {
                                loot._position = loot.placedPos;
                                loot.sourceButton.isActive = true;
                            }
                            else{
                                loot.sourceButton.isActive = true;
                                m_goList.remove(draggingObj);
                                draggingObj = null;
                            }
                        }
                    }
                    //make all slots normal colour
                    for (int a = 0; a < 5; a++) {
                        for (int b = 0; b < 5; b++) {
                            slots[a][b].clearTint();
                        }
                    }
                    // remove obj reference when finger let go
                    if(draggingObj != null) {draggingObj = null;}
                    break;
            }
        }

        for (int a = 0; a < slots.length; a++) {
            for (int b = 0; b < slots[0].length; b++) {
                slots[a][b].clearTint();
            }
        }
        for (int a = 0; a < slots.length; a++) {
            for (int b = 0; b < slots[0].length; b++) {
                if(slots[a][b].occupied == true)
                {
                    slots[a][b].setTint(0x5500FF00);
                }
            }
        }
    }

    @Override
    public void onRender(Canvas canvas) {
        canvas.drawBitmap(backgroundBitmap,backgroundPosition,0,null);
        canvas.drawBitmap(backgroundBitmap1,backgroundPosition + screenWidth,0,null);
        for(GameEntity obj : m_goList)
        {
            if(obj.isActive){
                obj.onRender(canvas);
            }
        }
    }

    public void StartLootPhase()
    {
        int noOfLoot = rand.nextInt(5 - 1 + 1) + 1;

        // temporary list to hold new items
        ArrayList<GameEntity> toAdd = new ArrayList<>();

        for (GameEntity entity : m_goList)
        {
            if (entity instanceof LootSlot) {
                entity.isActive = true;
            }
        }

        lootBtns = new ArrayList<>();

        Vector2 startPos = new Vector2(screenWidth/2, screenHeight/2-75);
        Vector2 scale = new Vector2(0.1f, 0.1f);

        // create loot buttons AFTER the loop
        for (int i = 0; i < noOfLoot; i++)
        {
            LootType randomType = LootType.values()[rand.nextInt(LootType.values().length)];
            int offsetY = 200 * i;

            LootButtonObj lootBtnn = new LootButtonObj();
            lootBtnn.onCreate(
                    new Vector2(startPos.x, startPos.y + offsetY),
                    scale,
                    SpriteList.ExampleItem,
                    randomType
            );

            lootBtns.add(lootBtnn);
            toAdd.add(lootBtnn); // store for later adding


            // Now safely add everything at once
            m_goList.addAll(toAdd);
        }
        spawnPhase = true;
    }

    public void EndLootPhase()
    {
        Iterator<GameEntity> iter = m_goList.iterator();

        while (iter.hasNext()) {
            GameEntity entity = iter.next();

            if (entity instanceof LootObj) {
                LootObj loot = (LootObj) entity;
                if (loot.placed) {
                    player.value += loot.lootType.value;
                }
                iter.remove(); // ✔️ SAFE remove
            }
            else if (entity instanceof LootButtonObj) {
                iter.remove(); // ✔️ SAFE remove
            }
            else if (entity instanceof LootSlot) {
                LootSlot slot = (LootSlot) entity;
                slot.occupied = false;
                entity.isActive = false;
                // do NOT remove this one
            }
        }
        System.out.println(player.value);
        lootBtns = null;
        spawnPhase = false;
    }

    private void RelayoutLootButtons() {
        // Starting point and spacing between buttons
        Vector2 startPos = new Vector2(400, 400);
        int spacingX = 400;

        // 1. Copy active buttons into a temp list
        ArrayList<LootButtonObj> activeButtons = new ArrayList<>();
        for (LootButtonObj btn : lootBtns) {
            if (btn.isActive) {
                activeButtons.add(btn);
            }
        }

        // 2. Sort them by current X position so it’s “by position, not spawn index”
        Collections.sort(activeButtons, new Comparator<LootButtonObj>() {
            @Override
            public int compare(LootButtonObj a, LootButtonObj b) {
                return Float.compare(a._position.x, b._position.x);
            }
        });

        // 3. Reposition active buttons compactly
        if (activeButtons.size() == 0)
            return;

// Use the current position of the leftmost active button as the anchor
        float baseX = activeButtons.get(0)._position.x;
        float baseY = activeButtons.get(0)._position.y; // or keep using 400 if Y is fixed

        for (int i = 0; i < activeButtons.size(); i++) {
            LootButtonObj btn = activeButtons.get(i);

            float newX = baseX + spacingX * i;
            float newY = baseY;

            btn.setPosition(new Vector2(newX, newY));
        }

        // (inactive ones keep their old position; they’re invisible / unpressable anyway)
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
        int index = event.getActionIndex();      // THIS is pointerIndex
        int pointerID = event.getPointerId(index); // THIS is pointerID

        switch(action)
        {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
            {
                // THIS MUST USE INDEX, NOT pointerID
                float x = event.getX(index);
                float y = event.getY(index);

                Vector2 pos = new Vector2(x, y);

                LootObj loot = new LootObj();
                //loot.onCreate(pos, loot.loot);
                m_goList.add(loot);

                activeLoot.put(pointerID, loot);
                break;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            {
                if (activeLoot.containsKey(pointerID))
                {
                    LootObj looty = activeLoot.get(pointerID);
                    m_goList.remove(looty);
                    activeLoot.remove(pointerID);
                }
                break;
            }
        }
    }
}
