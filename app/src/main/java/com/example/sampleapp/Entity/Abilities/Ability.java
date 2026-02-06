package com.example.sampleapp.Entity.Abilities;

import com.example.sampleapp.UI.Buttons.GenericBtn;
import com.example.sampleapp.UI.Buttons.IActivatable;
import com.example.sampleapp.Enums.SpriteAnimationList;
import com.example.sampleapp.UI.Icon.UIAbilityIcon;
import com.example.sampleapp.mgp2d.core.AnimatedSprite;
import com.example.sampleapp.mgp2d.core.GameEntity;

public abstract class Ability extends GameEntity implements IActivatable {
    protected float yPosition = 400.0f;

    public String name;
    public SpriteAnimationList banner;
    public SpriteAnimationList icon;
    public AnimatedSprite bannerAnim;

    public UIAbilityIcon iconUI = null;

    public boolean gotten = false;

    public GenericBtn selfBtn;

    public abstract void onGetAbility();
}
