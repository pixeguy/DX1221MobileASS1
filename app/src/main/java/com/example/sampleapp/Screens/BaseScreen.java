package com.example.sampleapp.Screens;

import com.example.sampleapp.UI.Core.UIElement;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseScreen {
    protected boolean visible = true;

    protected List<UIElement> uiElementList = new ArrayList<>();

    public BaseScreen() {
        init();
    }

    // Initialize UI or setup logic
    protected abstract void init();

    public abstract void disposed();
}
