package com.example.sampleapp.Entity.Enemies.Slime;

import android.util.Log;

import com.example.sampleapp.Entity.Player.PlayerObj;
import com.example.sampleapp.Enums.SpriteAnimationList;
import com.example.sampleapp.PostOffice.MessageSpawnProjectile;
import com.example.sampleapp.PostOffice.PostOffice;
import com.example.sampleapp.Statemchine.State;
import com.example.sampleapp.mgp2d.core.GameEntity;

import java.util.Random;

public class SlimeRunState extends State {

    private float runDuration = 0.0f;
    /** @noinspection FieldCanBeLocal*/
    private final float runSpeed = 100.0f;

    private boolean isShoot = false;

    public SlimeRunState(GameEntity go, String mStateID) {
        super(go, mStateID);
    }

    @Override
    public void OnEnter() {
        super.OnEnter();
        if(m_go.facingDirection.equals(0, -1))
            m_go.SetAnimation(SpriteAnimationList.SlimeRunBack);
        else if(m_go.facingDirection.equals(0, 1))
            m_go.SetAnimation(SpriteAnimationList.SlimeRunFront);
        else if(m_go.facingDirection.equals(-1, 0))
            m_go.SetAnimation(SpriteAnimationList.SlimeRunLeft);
        else
            m_go.SetAnimation(SpriteAnimationList.SlimeRunRight);

        Random random = new Random();
        float minRunTime = 1.5f;
        float maxRunTime = 2.5f;
        runDuration = random.nextFloat() * (maxRunTime - minRunTime) + minRunTime;
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        if(stateTimer >= runDuration || m_go.CheckIfOutsideWorldBound(m_go.facingDirection, 50)) {
            m_go.sm.ChangeState("SlimeIdle");
            return;
        }

        m_go.setPosition(m_go._position.add(m_go.facingDirection.multiply(runSpeed * dt)));
        int currentFrame = m_go.animatedSprite.GetCurrentFrame() % m_go.animatedSprite.GetNumCol();
        if(currentFrame == 5 && !isShoot) {
            MessageSpawnProjectile message = new MessageSpawnProjectile(
                    m_go, MessageSpawnProjectile.PROJECTILE_TYPE.ENEMY_FIRE_MISSILE,
                    1000.0f, m_go.getPosition(), m_go.facingDirection);
            PostOffice.getInstance().send("Scene", message);
            isShoot = true;
        }
        else if(currentFrame != 5) isShoot = false;
    }

    @Override
    public void OnExit() {

    }
}
