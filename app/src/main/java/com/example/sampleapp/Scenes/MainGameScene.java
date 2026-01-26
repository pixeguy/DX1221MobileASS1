package com.example.sampleapp.Scenes;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import com.example.sampleapp.Entity.Abilities.Ability;
import com.example.sampleapp.Entity.BackgroundEntity;
import com.example.sampleapp.Entity.Player.PlayerRecordEntry;
import com.example.sampleapp.Interface.BackToMenu;
import com.example.sampleapp.Interface.SaveNewScore;
import com.example.sampleapp.Managers.UIManager;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class MainGameScene extends GameScene {
    private Bitmap backgroundBitmap;
    private Bitmap backgroundBitmap1;
    private float backgroundPosition = 0;
    private int screenWidth;
    private int screenHeight;

    private static final String FILEBESTSCORE = "best_score.txt";
    public List<PlayerRecordEntry> scores;
    private boolean isTouching = false;

    public void saveScoresJson(List<PlayerRecordEntry> scores)
    {
        JSONArray arr = new JSONArray();
        for (PlayerRecordEntry e : scores)
        {
            JSONObject o = new JSONObject();
            try {
                o.put("score", e.score);
                // o.put("name", e.name);
                arr.put(o);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        try (OutputStream os = GameActivity.instance.openFileOutput(FILEBESTSCORE, Context.MODE_PRIVATE))
        {
            os.write(arr.toString().getBytes(StandardCharsets.UTF_8));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    private static String readAllText(InputStream input) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line); // no newline needed for JSON
            }
        }
        return sb.toString();
    }
    public List<PlayerRecordEntry> loadScoresJson()
    {
        List<PlayerRecordEntry> list = new ArrayList<>();

        try (InputStream input = GameActivity.instance.openFileInput(FILEBESTSCORE))
        {
            String json = readAllText(input);
            JSONArray arr = new JSONArray(json);

            for (int i = 0; i < arr.length(); i++)
            {
                JSONObject o = arr.getJSONObject(i);
                PlayerRecordEntry e = new PlayerRecordEntry();
                e.score = o.getInt("score");
                list.add(e);
            }
        }
        catch (FileNotFoundException f) { }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        return list;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        screenWidth = GameActivity.instance.getResources().getDisplayMetrics().widthPixels;
        screenHeight = GameActivity.instance.getResources().getDisplayMetrics().heightPixels;
        Bitmap bmp = BitmapFactory.decodeResource(GameActivity.instance.getResources()
                , R.drawable.gamescene);
        backgroundBitmap = Bitmap.createScaledBitmap(bmp,screenWidth,screenHeight,true);

        IActivatable target = new BackToMenu();
        GenericBtn menuBtn = new GenericBtn(target);
        menuBtn.onCreate(new Vector2((float) screenWidth /2, screenHeight * 0.80f), new Vector2(3.5f,3.5f),SpriteAnimationList.MainMenuBtn);
        menuBtn.setOrdinal(4);
        menuBtn.isActive = true;
    }

    @Override
    public void onEnter()
    {
        super.onEnter();
        scores = loadScoresJson();
        GenericBtn save = new GenericBtn(new SaveNewScore(this));
        save.onCreate(new Vector2((float) screenWidth/2, screenHeight * 0.80f), new Vector2(3.5f,3.5f),SpriteAnimationList.MainMenuBtn);
        save.setOrdinal(4);
        save.isActive = true;
        m_goList.add(save);
    }

    @Override
    public void onUpdate(float dt) {
        for(GameEntity obj : m_goList)
        {
            if(obj.isActive){
                obj.onUpdate(dt);
            }
        }
        HandleTouch();
    }

    private void HandleTouch() {
        MotionEvent event = GameActivity.instance.getMotionEvent();
        if (event == null) {
            return;
        }

        int action = event.getActionMasked();
        int index = event.getActionIndex();
        int pointerID = event.getPointerId(index);
        Vector2 touchPos = new Vector2(event.getX(index), event.getY(index));
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!isTouching) { // action down registers as hold on android studio emulator
                    for (GameEntity entity : m_goList) {
                        if (entity instanceof GenericBtn) {
                            GenericBtn btn = (GenericBtn) entity;
                            if (btn.checkIfPressed(touchPos)) {
                                IActivatable target = new SaveNewScore(this);
                                if (btn.getCallback().getClass() == target.getClass()) {
                                    btn.OnClick();
                                }
                                break;
                            }
                        }
                    }
                }
                break;
        }
    }

    public String UpdateScores()
    {
        StringBuilder sb = new StringBuilder();

        for (PlayerRecordEntry e : this.scores)
        {
            sb.append(e.score).append('\n');
        }

        String scoresText = sb.toString();
        return scoresText;
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
        String scores = UpdateScores();
        canvas.drawText(scores, screenWidth/2,120,null);
    }
}
