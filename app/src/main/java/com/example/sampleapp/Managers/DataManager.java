package com.example.sampleapp.Managers;

import android.content.Context;
import com.example.sampleapp.mgp2d.core.Singleton;
import java.util.HashMap;
import java.util.Map;

public class DataManager extends Singleton<DataManager> {
    public static DataManager getInstance() {
        return Singleton.getInstance(DataManager.class);
    }

    // Use HashMap instead of Dictionary (Dictionary is obsolete in Java)
    private Map<String, Object> data = new HashMap<>();

    /**
     * Loads the entire JSON file from storage and populates the memory cache.
     * Call this once at the start of the app.
     */
    public void loadAllData(Context ctx) {
        // We use the SaveManager to get the raw JSON string
        String jsonString = SaveManager.getInstance().getRawJsonString(ctx);

        if (jsonString == null || jsonString.isEmpty()) return;

        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject(jsonString);
            java.util.Iterator<String> keys = jsonObject.keys();

            while (keys.hasNext()) {
                String key = keys.next();
                Object value = jsonObject.get(key);

                // Store in the temporary HashMap cache
                data.put(key, value);
            }
            android.util.Log.d("DataManager", "All data loaded into temp cache: " + data.size() + " items.");
        } catch (Exception e) {
            android.util.Log.e("DataManager", "Failed to load all data: " + e.getMessage());
        }
    }

    /**
     * Set data into memory and immediately save it to the JSON file.
     */
    public void saveData(Context ctx, String key, Object value) {
        // Update local cache
        data.put(key, value);

        // Save to persistent storage using your SaveManager
        SaveManager.getInstance().saveValueJson(ctx, key, value);
    }

    /**
     * Retrieve data. Checks the memory cache first, then the disk.
     */
    public Object getData(Context ctx, String key) {
        // If it's already in memory, return it (Fast)
        if (data.containsKey(key)) {
            return data.get(key);
        }

        // If not in memory, try to load it from the file (Slow)
        Object value = SaveManager.getInstance().loadValueJson(ctx, key);

        // Cache it for next time
        if (value != null) {
            data.put(key, value);
        }

        return value;
    }

    // Helper method for Integers (like Currency)
    public int getInt(Context ctx, String key, int defaultValue) {
        Object val = getData(ctx, key);
        if (val instanceof Integer) {
            return (Integer) val;
        }
        return defaultValue;
    }

    // Helper method for Strings
    public String getString(Context ctx, String key, String defaultValue) {
        Object val = getData(ctx, key);
        if (val instanceof String) {
            return (String) val;
        }
        return defaultValue;
    }
}