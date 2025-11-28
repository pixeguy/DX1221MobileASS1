package com.example.sampleapp.Entity.Abilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.sampleapp.Entity.Buttons.GenericBtn;
import com.example.sampleapp.Entity.Player.PlayerObj;
import com.example.sampleapp.Enums.SpriteList;
import com.example.sampleapp.mgp2d.core.AnimatedSprite;
import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

public class TestAbility extends Ability{

    public TestAbility()
    {
        banner = SpriteList.TestAbility;
        icon = SpriteList.TestIcon;
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
    }

    @Override
    public void onUpdate() {
        System.out.println("Updating Ability");
    }

    @Override
    public void onCreate(Vector2 pos, Vector2 scale) {
        super.onCreate(pos, scale);

        Bitmap bmp = BitmapFactory.decodeResource(GameActivity.instance.getResources(), banner.spriteSheetID);
        bannerSprite = Bitmap.createScaledBitmap(bmp,bmp.getWidth(),bmp.getHeight(),true);
        bannerAnim = new AnimatedSprite(bannerSprite,banner.rows, banner.columns,banner.fps);
        Bitmap bmp2 = BitmapFactory.decodeResource(GameActivity.instance.getResources(), icon.spriteSheetID);
        iconSprite = Bitmap.createScaledBitmap(bmp2,bmp2.getWidth(),bmp2.getHeight(),true);
        iconAnim = new AnimatedSprite(iconSprite,icon.rows, icon.columns,icon.fps);

        selfBtn = new GenericBtn(this);
        selfBtn.onCreate(_position,_scale,banner);

        isActive = true;
    }

    @Override
    public void onRender(Canvas canvas) {
        bannerAnim.render(canvas,(int)_position.x,(int)_position.y, _scale, paint);
    }

}
