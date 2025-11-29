package com.example.sampleapp.Entity.Buttons;

import android.graphics.Rect;

import com.example.sampleapp.mgp2d.core.AnimatedSprite;

public class GenericBtn extends ButtonObj{
    private IActivatable callback;

    public IActivatable getCallback() {
        return callback;
    }

    public GenericBtn(IActivatable target) { // Renamed the parameter for clarity
        this.callback = target;
    }

    @Override
    public void OnClick() {
        super.OnClick();
        // This line is now universal: it triggers whatever the 'target' needs to do.
        callback.execute();
    }
}
