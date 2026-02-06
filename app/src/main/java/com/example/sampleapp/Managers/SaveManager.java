package com.example.sampleapp.Managers;

import android.content.Context;

import com.example.sampleapp.Entity.Player.PlayerRecordEntry;
import com.example.sampleapp.PostOffice.Message;
import com.example.sampleapp.PostOffice.ObjectBase;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SaveManager extends Singleton<SaveManager> implements ObjectBase {
    private static final String FILEBESTSCORE = "best_score.txt";
    private static final String FILEGLOBAL =  "persistent.txt";
    public List<PlayerRecordEntry> scores = new ArrayList<>();

    public static SaveManager getInstance() { return Singleton.getInstance(SaveManager.class);}

    public void saveValueJson(Context ctx, String key, Object value) {
        JSONObject jsonObject;// 1. Try to load existing data first so we don't overwrite other saved keys
        try (InputStream input = ctx.openFileInput(FILEGLOBAL)) {
            String existingJson = readAllText(input);
            jsonObject = new JSONObject(existingJson);
        } catch (FileNotFoundException e) {
            jsonObject = new JSONObject(); // File doesn't exist yet
        } catch (Exception e) {
            jsonObject = new JSONObject();
        }

        // 2. Add/Update the new value
        try {
            jsonObject.put(key, value);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        // 3. Save the updated JSON back to the file
        try (OutputStream os = ctx.openFileOutput(FILEGLOBAL, Context.MODE_PRIVATE)) {
            os.write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object loadValueJson(Context ctx, String key) {
        Object val = null;

        try (InputStream input = ctx.openFileInput(FILEGLOBAL))
        {
            String json = readAllText(input);

            JSONObject o = new JSONObject(json);
            val = (int) o.optDouble("value", 0.0);
        }
        catch (FileNotFoundException ignored) { }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        return val;
    }

    public int LoadLocalValueJson(Context ctx) {
        int val = 0;

        try (InputStream input = ctx.openFileInput(FILEGLOBAL))
        {
            String json = readAllText(input);

            JSONObject o = new JSONObject(json);
            val = (int) o.optDouble("value", 0.0);
        }
        catch (FileNotFoundException ignored) { }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        return val;
    }

    public void addToLocalValueAndSave(Context ctx, int add) {
        int curr = (int) loadValueJson(ctx, "value");
        int newVal = curr + add;
        saveValueJson(ctx, "value", newVal);
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
    public void saveScoresJson(Context ctx, List<PlayerRecordEntry> scores) {
        JSONArray arr = new JSONArray();

        for (PlayerRecordEntry e : scores)
        {
            JSONObject o = new JSONObject();
            try {
                o.put("score", e.score);

                // NEW
                o.put("lootValue", e.lootValue);
                o.put("name", e.name == null ? "" : e.name);

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

    public void addScoreAndSave(Context ctx, PlayerRecordEntry entry) {
        scores.add(entry);
        saveScoresJson(ctx, scores);
    }

    public List<PlayerRecordEntry> loadScoresJson(Context ctx) {
        List<PlayerRecordEntry> list = new ArrayList<>();

        try (InputStream input = ctx.openFileInput(FILEBESTSCORE))
        {
            String json = readAllText(input);
            JSONArray arr = new JSONArray(json);

            for (int i = 0; i < arr.length(); i++)
            {
                JSONObject o = arr.getJSONObject(i);

                PlayerRecordEntry e = new PlayerRecordEntry();

                e.score = o.optInt("score", 0);

                // NEW
                e.lootValue = o.optInt("lootValue", 0);
                e.name = o.optString("name", "");

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

    public void sortBestTimeToSlowest(List<PlayerRecordEntry> list) {
        if (list == null) return;

        // Fastest time first (smallest value first)
        Collections.sort(list, new Comparator<PlayerRecordEntry>() {
            @Override
            public int compare(PlayerRecordEntry a, PlayerRecordEntry b) {
                // If score is int time-in-seconds:
                return Float.compare(a.score, b.score);

                // If score is float time-in-seconds instead, use:
                // return Float.compare(a.score, b.score);
            }
        });
    }

    public String getRawJsonString(Context ctx) {
        try (java.io.InputStream input = ctx.openFileInput(FILEGLOBAL)) {
            return readAllText(input); // This uses your existing readAllText helper
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public boolean handle(Message message) {
        return false;
    }
}
