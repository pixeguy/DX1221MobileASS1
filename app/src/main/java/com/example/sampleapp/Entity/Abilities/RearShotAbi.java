package com.example.sampleapp.Entity.Abilities;

import android.graphics.Canvas;

import com.example.sampleapp.Enums.AbilityList;
import com.example.sampleapp.Managers.UIManager;
import com.example.sampleapp.UI.Buttons.GenericBtn;
import com.example.sampleapp.Entity.Player.PlayerObj;
import com.example.sampleapp.Enums.SpriteAnimationList;
import com.example.sampleapp.UI.Icon.UIAbilityIcon;
import com.example.sampleapp.mgp2d.core.AnimatedSprite;
import com.example.sampleapp.mgp2d.core.Vector2;

public class RearShotAbi extends Ability {
    public RearShotAbi()
    {
        banner = SpriteAnimationList.RearShotBanner;
        icon = SpriteAnimationList.RearShotIcon;
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
        iconUI.setPosition(new Vector2(50, 1000), new Vector2(200, 0));
        iconUI.setPivot(1, 1);
        iconUI.setAlpha(125);
        iconUI.interactable = true;
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

        iconUI = new UIAbilityIcon(pos.x, pos.y, 150, 150, AbilityList.RearArrow);
        iconUI.setBaseSprite(icon.sprite);
        iconUI.zIndex = 1;
        iconUI.interactable = false;
        UIManager.getInstance().addElement(iconUI);

        selfBtn = new GenericBtn(this);
        selfBtn.onCreate(_position,_scale,banner);

    }

    @Override
    public void onRender(Canvas canvas) {
        if (!gotten){
            bannerAnim.render(canvas,(int)_position.x,(int)_position.y, _scale, paint);
        }
    }
}
