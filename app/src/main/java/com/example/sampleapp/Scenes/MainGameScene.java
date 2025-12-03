package com.example.sampleapp.Scenes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import com.example.sampleapp.Entity.Abilities.Ability;
import com.example.sampleapp.Entity.BackgroundEntity;
import com.example.sampleapp.UI.Buttons.GenericBtn;
import com.example.sampleapp.UI.Buttons.IActivatable;
import com.example.sampleapp.UI.Buttons.LootButtonObj;
import com.example.sampleapp.UI.Buttons.TestingBtn;
import com.example.sampleapp.Entity.Abilities.TestAbility;
import com.example.sampleapp.Entity.Inventory.LootObj;
import com.example.sampleapp.Entity.Inventory.LootSlot;
import com.example.sampleapp.Enums.LootType;
import com.example.sampleapp.Entity.Player.PlayerObj;
import com.example.sampleapp.Interface.CloseLooting;
import com.example.sampleapp.Interface.RotateLoot;
import com.example.sampleapp.R;
import com.example.sampleapp.Enums.SpriteAnimationList;
import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.GameScene;
import com.example.sampleapp.mgp2d.core.Vector2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    boolean spawnPhase = false; boolean abilityPhase = false;
    private ArrayList<GenericBtn> lootGenerics;
    private LootSlot[][] slots; //  [col][row].....[x][y]

    //to store reference to current dragging obj
    public GameEntity draggingObj;
    private float initialRot;
    boolean isTouching = false;
    boolean isTouching2 = false;
    Random rand = new Random();

    TestingBtn testing = new TestingBtn();
    @Override
    public void onCreate() {
        super.onCreate();

        screenWidth = GameActivity.instance.getResources().getDisplayMetrics().widthPixels;
        screenHeight = GameActivity.instance.getResources().getDisplayMetrics().heightPixels;
        Bitmap bmp = BitmapFactory.decodeResource(GameActivity.instance.getResources()
                , R.drawable.gamescene);
        backgroundBitmap = Bitmap.createScaledBitmap(bmp,screenWidth,screenHeight,true);

        backgroundBitmap1 = Bitmap.createScaledBitmap(bmp,screenWidth,screenHeight,true);

        player = PlayerObj.getInstance();
        player.onCreate(new Vector2(screenWidth / 2, screenHeight/2),new Vector2(1,1), SpriteAnimationList.PlayerIdle);
        //hardcoding in the obj list for now
        player.isActive = true;
        m_goList.add(player);

        InitAbilityPhase();
        StartAbilityPhase();

        testing.onCreate(new Vector2(screenWidth / 2, screenHeight/2 - 200),new Vector2(1, 1),SpriteAnimationList.ExamplePause);
        testing.isActive = true;
        m_goList.add(testing);;
    }

    public void InitAbilityPhase(){
        BackgroundEntity bg = new BackgroundEntity(R.drawable.greyoverlay);
        bg.isActive = false;
        m_goList.add(bg);

        lootGenerics = new ArrayList<GenericBtn>();
        GenericBtn rotate = new GenericBtn(new RotateLoot(null));
        rotate.onCreate(new Vector2(screenWidth* 0.1f, screenHeight * 0.95f),new Vector2(3,3),SpriteAnimationList.RotateBtn);
        lootGenerics.add(rotate);
        m_goList.add(rotate);
        GenericBtn endLootBtn = new GenericBtn(new CloseLooting(null));
        endLootBtn.onCreate(new Vector2(screenWidth/2, screenHeight * 0.95f), new Vector2(3,3),SpriteAnimationList.CompleteBtn);
        lootGenerics.add(endLootBtn);
        m_goList.add(endLootBtn);

        //init all the slots, vv long so i minimize it first
        {
            slots = new LootSlot[5][4];

            Vector2 scale = new Vector2(0.06f, 0.06f);
            float GAP = 0 * GameActivity.instance.getResources().getDisplayMetrics().density;

            for (int x = 0; x < slots.length; x++) {
                for (int y = 0; y < slots[0].length; y++) {

                    slots[x][y] = new LootSlot();
                    slots[x][y].onCreate(new Vector2(0, 0), scale);
                    slots[x][y].isActive = false;
                    m_goList.add(slots[x][y]);

                    //calculating grid position
                    {
                        // Calculate cell spacing
                        float cellWidth  = (slots[x][y].animatedSprite._width  * scale.x) + GAP;
                        float cellHeight = (slots[x][y].animatedSprite._height * scale.y) + GAP;

                        // Grid total size
                        float gridSize  = cellWidth  * 5;

                        // Center X
                        float startX = (screenWidth / 2f) - (gridSize / 2.5f);
                        // Higher Y (25% from top)
                        float startY = (screenHeight * 0.235f) - (gridSize/2.5f);

                        float worldX = startX + x * cellWidth;
                        float worldY = startY + y * cellHeight;
                        slots[x][y].setPosition(new Vector2(worldX,worldY));}
                }
            }
        }
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

        HandleTouch();

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

    public void StartLootPhase() {
        int noOfLoot = rand.nextInt(5 - 1 + 1) + 1;

        // temporary list to hold new items
        ArrayList<GameEntity> toAdd = new ArrayList<>();

        for (GameEntity entity : m_goList)
        {
            if (entity instanceof LootSlot) {
                entity.isActive = true;
            }
            if(entity instanceof BackgroundEntity) {
                entity.isActive = true;
            }
        }
        for(GenericBtn btn : lootGenerics)
        {
            btn.isActive = true;
        }


        lootBtns = new ArrayList<>();

        Vector2 startPos = new Vector2(screenWidth/2, screenHeight/2-135);
        Vector2 scale = new Vector2(0.1f, 0.1f);
        float spacing = 72 * GameActivity.instance.getResources().getDisplayMetrics().density;
        // create loot buttons AFTER the loop
        for (int i = 0; i < 5; i++)
        {
            LootType randomType = LootType.values()[rand.nextInt(LootType.values().length)];
            int offsetY = (int)spacing * i;

            LootButtonObj lootBtnn = new LootButtonObj();
            lootBtnn.onCreate(
                    new Vector2(startPos.x, startPos.y + offsetY),
                    scale,
                    SpriteAnimationList.ExampleItem,
                    randomType
            );
            lootBtnn.isActive = true;
            lootBtns.add(lootBtnn);
            toAdd.add(lootBtnn); // store for later adding


            // Now safely add everything at once
            m_goList.addAll(toAdd);
        }
        spawnPhase = true;
    }

    public void RebuildLootBtns() {
        ArrayList<LootButtonObj> unusedButtons = new ArrayList<>();
        // Loop through your actual button list
        for (LootButtonObj btn : lootBtns) {
            if (!btn.used) {
                unusedButtons.add(btn);
            }
        }

        Vector2 startPos = new Vector2(screenWidth/2, screenHeight/2-135);
        float spacing = 72 * GameActivity.instance.getResources().getDisplayMetrics().density;

        for(int i = 0; i < unusedButtons.size(); i++)
        {
            int offsetY = (int)spacing * i;
            LootButtonObj btn = unusedButtons.get(i);
            btn.targetPos = new Vector2(startPos.x, startPos.y + offsetY);
        }
    }

    public void EndLootPhase() {
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
            else if (entity instanceof BackgroundEntity){
                entity.isActive = false;
            }
        }
        for(GenericBtn btn : lootGenerics)
        {
            btn.isActive = false;
        }

        System.out.println(player.value);
        lootBtns = null;
        spawnPhase = false;
    }

    public void StartAbilityPhase() {
        for (GameEntity entity : m_goList)
        {
            if(entity instanceof BackgroundEntity)
            {
                entity.isActive = true;
            }
        }
        Ability ability = new TestAbility();
        ability.onCreate(new Vector2(screenWidth/2, screenHeight/2 + 400), new Vector2(1,1));
        GenericBtn btn = ability.selfBtn;
        m_goList.add(btn);
        m_goList.add(ability);
        ability.isActive = true;
        btn.isActive = true;
        abilityPhase = true;
    }

    public void EndAbilityPhase()
    {
        for(GameEntity entity : m_goList)
        {
            if (entity instanceof BackgroundEntity){
                entity.isActive = false;
            }
            if (entity instanceof Ability){
                Ability abi = (Ability) entity;
                abi.selfBtn.isActive = false;
                abi.isActive = false; // ability update will be called through player;
                abi._position = new Vector2(abi.iconAnim._width - 50,screenHeight/2);
                abi.paint.setAlpha(128);
            }
        }
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
    boolean isInsideSlot(Vector2 touchPos, LootSlot slot) {
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

    private void HandleTouch()
    {
        MotionEvent event = GameActivity.instance.getMotionEvent();
        if (event == null) {return;}

        int action = event.getActionMasked();
        int index = event.getActionIndex();
        int pointerID = event.getPointerId(index);
        Vector2 touchPos = new Vector2(event.getX(index),event.getY(index));
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                if(!isTouching){ // action down registers as hold on android studio emulator
                    for(GameEntity entity : m_goList)
                    {
                        if (entity instanceof GenericBtn)
                        {
                            GenericBtn btn = (GenericBtn) entity;
                            if(btn.checkIfPressed(touchPos))
                            {
                                if (btn.getCallback() instanceof Ability) {
                                    EndAbilityPhase();
                                }
                                btn.OnClick();
                                break;
                            }
                        }
                    }
                    if (testing.checkIfPressed(touchPos)){
                        if(!spawnPhase)
                            StartAbilityPhase();
                        else
                            EndLootPhase();
                    }
                    if(lootBtns != null) { //whether the list exists
                        for (LootButtonObj lootBtn : lootBtns) {
                            if (lootBtn.checkIfPressed(touchPos)) {
                                if (!lootBtn.isActive) {
                                    continue;
                                }
                                if (!lootBtn.used) { //if the loot obj is not in the grid
                                    lootBtn.OnClick(touchPos);

                                    //creating my loot obj
                                    LootObj lootobj = new LootObj();
                                    //grab lootType ref from btn
                                    lootobj.onCreate(touchPos, lootBtn.loot);
                                    lootobj.isActive = true;
                                    m_goList.add(lootobj);

                                    //assign obj to reference so scene can use it
                                    draggingObj = lootobj;
                                    lootBtn.isActive = false;
                                    //RelayoutLootButtons();
                                    lootobj.sourceButton = lootBtn;
                                    lootobj.sourceButton.used = true;
                                    initialRot = 0;
                                    RebuildLootBtns();
                                }
                                else { //find the correct loot obj corresponding to the button
                                    for (GameEntity entity : m_goList) {
                                        if (entity instanceof LootObj) {
                                            LootObj loot = (LootObj) entity;
                                            if (loot.sourceButton == lootBtn) {
                                                draggingObj = loot;
                                                lootBtn.isActive = false;
                                                initialRot = loot.rotationAngle;

                                                if(loot.placed){
                                                    for(LootSlot slots : loot.slotsOccupied)
                                                    {
                                                        slots.occupied = false;
                                                    }
                                                }
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
            case MotionEvent.ACTION_POINTER_DOWN:
                if(!isTouching2){
                    for(GameEntity entity : m_goList)
                    {
                        if(entity instanceof GenericBtn)
                        {
                            if(((GenericBtn) entity).checkIfPressed(touchPos))
                            {
                                GenericBtn btn = (GenericBtn) entity;
                                IActivatable target = new RotateLoot(null);
                                if(btn.getCallback().getClass() == target.getClass())
                                {
                                    btn.OnClick();
                                }
                            }
                        }
                    }
                    isTouching = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(draggingObj != null) {
                    //drag obj with touching pos
                    draggingObj.setPosition(touchPos);

                    //make all slots normal colour
                    for (int a = 0; a < slots.length; a++) {
                        for (int b = 0; b < slots[0].length; b++) {
                            slots[a][b].clearTint();
                        }
                    }

                    if (draggingObj instanceof LootObj)
                    {
                        LootObj loot = (LootObj) draggingObj;
                        int xSize = (int) loot.instanceScale.x;
                        int ySize = (int) loot.instanceScale.y;

                        boolean tinted = false;

                        //loop through the grid. return grid slot index if user touch is on a slot
                        Vector2 slot = findSlotIndexAtPosition(touchPos);
                        if (slot != null) {
                            int i = (int) slot.x;
                            int j = (int) slot.y;

                            boolean canPlace = canPlaceItemInSlot(i, j, xSize, ySize);
                            int color = canPlace ? 0x5500FF00 : 0x77FF0000; //green or red

                            //loop through the size of the object
                            for (int x = 0; x < xSize; x++) {
                                for (int y = 0; y < ySize; y++) {

                                    int slotX = i + x;
                                    int slotY = j + y;

                                    //if is out of bounds, dont try to tint it
                                    if (inBounds(slotX, slotY)) {
                                        slots[slotX][slotY].setTint(color);
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isTouching = false;

                if(draggingObj instanceof LootObj)
                {
                    //get details of the loot obj
                    LootObj loot = (LootObj) draggingObj;
                    int xSize = (int)loot.instanceScale.x;
                    int ySize = (int)loot.instanceScale.y;

                    //loop through the grid. return grid slot index if user touch is on a slot
                    Vector2 slot = findSlotIndexAtPosition(touchPos);
                    if (slot != null) {
                        int i = (int) slot.x;
                        int j = (int) slot.y;

                        //check if item slots are valid
                        if(canPlaceItemInSlot(i,j,xSize,ySize)){
                            draggingObj.setPosition(slots[i][j]._position);
                            loot.slotsOccupied.clear(); //clear all previous ones
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
                                if(loot.rotationAngle == 90)
                                {
                                    Vector2 newPiv = new Vector2(0,0);
                                    newPiv.x = loot.pivotOffset.y;
                                    newPiv.y = loot.pivotOffset.x;
                                    loot.sourceButton.setPosition(draggingObj._position.add(newPiv));
                                }
                                else {
                                    loot.sourceButton.setPosition(draggingObj._position.add(loot.pivotOffset));
                                }
                                float targetWidth = draggingObj.animatedSprite._width * draggingObj._scale.x;
                                float targetHeight = draggingObj.animatedSprite._height * draggingObj._scale.y;
                                int originalBw = loot.sourceButton.animatedSprite._width;
                                int originalBh = loot.sourceButton.animatedSprite._height;
                                float scaleX = (float) targetWidth / originalBw;
                                float scaleY = (float) targetHeight / originalBh;

                                loot.sourceButton._scale = new Vector2(scaleX, scaleY);
                                loot.sourceButton.isActive = true;
                                loot.placed = true;
                                loot.placedPos = loot._position;
                            }
                        }
                        else{
                            // if user cannot find any valid slots
                            if(loot.placed)
                            {
                                loot.rotationAngle = initialRot;
                                if(loot.rotationAngle == 90)
                                {
                                    Log.d("Pointer","A");
                                    Vector2 newPiv = new Vector2(0,0);
                                    newPiv.x = loot.pivotOffset.y;
                                    newPiv.y = loot.pivotOffset.x;
                                    loot._position = loot.sourceButton._position.subtract(newPiv);
                                }
                                else{
                                    Log.d("Pointer","B");
                                    loot._position = loot.sourceButton._position.subtract(loot.pivotOffset);
                                }
                                for(LootSlot slots : loot.slotsOccupied)
                                {
                                    slots.occupied = true;
                                }
                                loot.sourceButton.rotationAngle = initialRot;
                                loot.sourceButton.isActive = true;

                            }
                            else{
                                Log.d("Pointer","C");
                                loot.sourceButton.rotationAngle = 0;
                                loot.sourceButton._scale = new Vector2(0.1f,0.1f);
                                loot.sourceButton.used = false;
                                loot.sourceButton.isActive = true;
                                m_goList.remove(draggingObj);
                                draggingObj = null;
                                RebuildLootBtns();
                            }
                        }
                    }
                    else{
                        loot.sourceButton.rotationAngle = 0;
                        loot.sourceButton._scale = new Vector2(0.1f,0.1f);
                        loot.sourceButton.used = false;
                        loot.sourceButton.isActive = true;
                        m_goList.remove(draggingObj);
                        draggingObj = null;
                        RebuildLootBtns();
                    }
                }
                //make all slots normal colour
                for (int a = 0; a < slots.length; a++) {
                    for (int b = 0; b < slots[0].length; b++) {
                        slots[a][b].clearTint();
                    }
                }
                // remove obj reference when finger let go
                if(draggingObj != null) {draggingObj = null;}
                break;
            case MotionEvent.ACTION_POINTER_UP:
                isTouching2 = false;
                break;
            case MotionEvent.ACTION_CANCEL:

                break;
        }
    }
}
