package com.example.sampleapp.Entity.Abilities;

import android.graphics.Bitmap;

import com.example.sampleapp.Entity.Buttons.GenericBtn;
import com.example.sampleapp.Entity.Buttons.IActivatable;
import com.example.sampleapp.Enums.SpriteList;
import com.example.sampleapp.mgp2d.core.AnimatedSprite;
import com.example.sampleapp.mgp2d.core.GameEntity;

public abstract class Ability extends GameEntity implements IActivatable {
    public String name;
    public SpriteList banner;
    public SpriteList icon;
    protected Bitmap bannerSprite;
    protected Bitmap iconSprite;
    protected AnimatedSprite bannerAnim;
    protected AnimatedSprite iconAnim;

    public GenericBtn selfBtn;

    public abstract void onGetAbility();
    public abstract void onUpdate();
}
