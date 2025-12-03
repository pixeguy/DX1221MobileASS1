package com.example.sampleapp.Entity.Abilities;

import android.graphics.Canvas;

import com.example.sampleapp.UI.Buttons.GenericBtn;
import com.example.sampleapp.Entity.Player.PlayerObj;
import com.example.sampleapp.Enums.SpriteAnimationList;
import com.example.sampleapp.mgp2d.core.AnimatedSprite;
import com.example.sampleapp.mgp2d.core.Vector2;

public class MagicOrbAbi extends Ability{
    public MagicOrbAbi()
    {
        banner = SpriteAnimationList.MagicOrbBanner;
        icon = SpriteAnimationList.MagicOrbIcon;
        name = "TestAbility";
    }

    @Override
    public void execute() {
        onGetAbility();
    }

    @Override
    public void onGetAbility() {
        System.out.println("Got Ability");
        PlayerObj.getInstance().currAbility = this;
        gotten = true;
    }

    @Override
    public void onUpdate(float dt) {
        if(gotten){
            //System.out.println("Updating Ability");
        }
    }

    @Override
    public void onCreate(Vector2 pos, Vector2 scale) {
        super.onCreate(pos, scale);

        bannerAnim = new AnimatedSprite(banner.sprite,banner.rows, banner.columns,banner.fps);
        iconAnim = new AnimatedSprite(icon.sprite,icon.rows, icon.columns,icon.fps);

        selfBtn = new GenericBtn(this);
        selfBtn.onCreate(_position,_scale,banner);

    }

    @Override
    public void onRender(Canvas canvas) {
        if (!gotten){
            bannerAnim.render(canvas,(int)_position.x,(int)_position.y, _scale, paint);
        }
        else {
            iconAnim.render(canvas,(int)_position.x,(int)_position.y, _scale, paint);
        }
    }
}
