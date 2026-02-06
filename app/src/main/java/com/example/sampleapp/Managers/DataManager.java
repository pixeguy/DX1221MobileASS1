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

    public void addDataAndSave(Context ctx, String key, Object value) {
        // Get the current value
        Object currentValue = getData(key);

        // If it's an integer, add the new value to it
        if (currentValue instanceof Integer) {
            int newValue = (Integer) currentValue + (Integer) value;
            saveData(ctx, key, newValue);
        }// If it's a string, concatenate the new value to it
        else if(currentValue instanceof String) {
            String newValue = (String) currentValue + (String) value;
            saveData(ctx, key, newValue);
        }// If it's a float, add the new value to it
        else if(currentValue instanceof Float) {
            float newValue = (Float) currentValue + (Float) value;
            saveData(ctx, key, newValue);
        }
        else {// Otherwise, just save the new value
            saveData(ctx, key, value);
        }

    }

    /**
     * Retrieve data. Checks the memory cache first, then the disk.
     */
    public Object getData(String key) {
        // If it's already in memory, return it (Fast)
        if (data.containsKey(key)) {
            return data.get(key);
        }
        return null;
    }

    // Helper method for Integers (like Currency)
    public int getInt(String key, int defaultValue) {
        Object val = getData(key);
        if (val instanceof Integer) {
            return (Integer) val;
        }
        return defaultValue;
    }

    // Helper method for Strings
    public String getString(String key, String defaultValue) {
        Object val = getData(key);
        if (val instanceof String) {
            return (String) val;
        }
        return defaultValue;
    }
}