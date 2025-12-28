package com.example.sampleapp.UI.Buttons;

import android.graphics.Canvas;

import com.example.sampleapp.Enums.SpriteList;
import com.example.sampleapp.UI.Core.UIElement;
import com.example.sampleapp.UI.Icon.UIIcon;
import com.example.sampleapp.UI.Text.UIText;
import com.example.sampleapp.mgp2d.core.Vector2;

public class UIAbilityBtn extends UIElement {

    private UIIcon abilityIcon = null;
    private UIText abilityLabel = null;
    private UIText abilityDesc = null;

    public Runnable onClick;
    public Runnable onHover;
    public Runnable onRelease;

    protected UIAbilityBtn(float x, float y, float width, float height, SpriteList abilitySprite) {
        super(x, y, width, height);
        this.abilityIcon = new UIIcon(0, 0, width * 0.8f, height * 0.8f);
        this.abilityIcon.setBaseSprite(abilitySprite.sprite);
        this.abilityIcon.addParent(this);

        this.abilityLabel = new UIText("Ability Name", 30, new Vector2(0.0f, 0.0f), width, width);
        this.abilityLabel.addParent(this);
        this.abilityLabel.zIndex = 1;

        this.abilityDesc = new UIText("Ability Description", 25, new Vector2(0.0f, 0.0f), width, width);
        this.abilityDesc.addParent(this);
        this.abilityDesc.zIndex = 1;
    }

    @Override
    public void onRender(Canvas canvas) {
        if(!visible) return;
        for(UIElement child : children) {
            child.onRender(canvas);
        }
    }

    @Override
    public void onTouchDown(float x, float y)
    {
        if(onClick != null) onClick.run();
    }
}
