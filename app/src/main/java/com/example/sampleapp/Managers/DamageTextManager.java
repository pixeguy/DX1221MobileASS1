package com.example.sampleapp.Managers;

import com.example.sampleapp.UI.Text.UIDamageText;
import com.example.sampleapp.mgp2d.core.Singleton;

import java.util.ArrayList;
import java.util.List;

public class DamageTextManager extends Singleton<DamageTextManager> {
    public static DamageTextManager getInstance() {
        return getInstance(DamageTextManager.class);
    }

    private final List<UIDamageText> texts = new ArrayList<>();

    public void spawnText(String text, float x, float y, int color) {
        UIDamageText dmgText = new UIDamageText(text, x, y);
        dmgText.setColor(color);
        texts.add(dmgText);
        UIManager.getInstance().addElement(dmgText);
    }

    public void onUpdate() {
        for (int i = texts.size() - 1; i >= 0; i--) {
            UIDamageText t = texts.get(i);
            if (t.isExpired()) {
                texts.remove(i);
                UIManager.getInstance().removeElement(t);
            }
        }
    }
}
