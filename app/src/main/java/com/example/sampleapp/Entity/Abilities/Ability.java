package com.example.sampleapp.Entity.Abilities;

import android.graphics.Bitmap;

import com.example.sampleapp.Entity.Buttons.GenericBtn;
import com.example.sampleapp.Entity.Buttons.IActivatable;
import com.example.sampleapp.Enums.SpriteAnimationList;
import com.example.sampleapp.Enums.SpriteList;
import com.example.sampleapp.mgp2d.core.AnimatedSprite;
import com.example.sampleapp.mgp2d.core.GameEntity;

public abstract class Ability extends GameEntity implements IActivatable {
    public String name;
    public SpriteAnimationList banner;
    public SpriteAnimationList icon;
    public AnimatedSprite bannerAnim;
    public AnimatedSprite iconAnim;
    public boolean gotten = false;

    public GenericBtn selfBtn;

    public abstract void onGetAbility();
}
