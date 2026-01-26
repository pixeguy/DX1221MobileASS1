package com.example.sampleapp.Managers;

import android.content.Context;

import com.example.sampleapp.Entity.Player.PlayerRecordEntry;
import com.example.sampleapp.PostOffice.Message;
import com.example.sampleapp.PostOffice.ObjectBase;
import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.Singleton;

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
import java.util.List;

public class SaveManager extends Singleton<SaveManager> implements ObjectBase {
    private static final String FILEBESTSCORE = "best_score.txt";
    public List<PlayerRecordEntry> scores = new ArrayList<>();

    public static SaveManager getInstance() { return Singleton.getInstance(SaveManager.class);}

    public void saveScoresJson(Context ctx, List<PlayerRecordEntry> scores)
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

        try (OutputStream os = ctx.openFileOutput(FILEBESTSCORE, Context.MODE_PRIVATE))
        {
            os.write(arr.toString().getBytes(StandardCharsets.UTF_8));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void addScoreAndSave(Context ctx, PlayerRecordEntry entry)
    {
        scores.add(entry);
        saveScoresJson(ctx, scores);
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

    public List<PlayerRecordEntry> loadScoresJson(Context ctx)
    {
        List<PlayerRecordEntry> list = new ArrayList<>();

        try (InputStream input = ctx.openFileInput(FILEBESTSCORE))
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
    public boolean handle(Message message) {
        return false;
    }
}
