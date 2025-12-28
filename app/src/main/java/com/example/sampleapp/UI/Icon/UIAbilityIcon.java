package com.example.sampleapp.UI.Icon;

import com.example.sampleapp.Enums.AbilityList;
import com.example.sampleapp.Managers.UIManager;
import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.Vector2;

public class UIAbilityIcon extends UIIcon {
    private float hoverTimer = 0.0f;
    private final UITooltip skillDescription;
    public UIAbilityIcon(float x, float y, float width, float height, AbilityList ability) {
        super(x, y, width, height);
        skillDescription = new UITooltip(
                ability.name,
                ability.description,
                new Vector2(x, y),
                400f, 300f
        );
        skillDescription.setOffset(new Vector2(0, height / 2.0f));
        skillDescription.setSlideMode(UITooltip.SlideMode.LEFT_TO_RIGHT);
        skillDescription.zIndex = 1;
        UIManager.getInstance().addElement(skillDescription);
    }

    public void setPosition(Vector2 pos, Vector2 offset) {
        position.set(pos);
        skillDescription.position.set(pos.x + bounds.width() + offset.x, pos.y + offset.y);
    }

    @Override
    public void onUpdate(float dt) {
        super.onUpdate(dt);
        if (hoverTimer > 0.2f) {
            skillDescription.show();
        }
    }

    @Override
    public void onTouchDown(float x, float y) {
        hoverTimer += GameActivity.instance.getDeltaTime();
    }

    @Override
    public void onTouchMove(float x, float y) {
        hoverTimer += GameActivity.instance.getDeltaTime();
    }

    @Override
    public void onTouchUp() {
        hoverTimer = 0.0f;
        skillDescription.hide();
    }
}
