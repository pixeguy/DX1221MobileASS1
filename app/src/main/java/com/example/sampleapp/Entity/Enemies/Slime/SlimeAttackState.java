package com.example.sampleapp.Entity.Enemies.Slime;

import android.util.Log;

import com.example.sampleapp.Entity.Player.PlayerObj;
import com.example.sampleapp.Enums.SpriteAnimationList;
import com.example.sampleapp.Statemchine.State;
import com.example.sampleapp.Ultilies.Utilies;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

import java.util.Random;

public class SlimeAttackState extends State {

    private boolean isDamageDone = false;

    public SlimeAttackState(GameEntity go, String mStateID) {
        super(go, mStateID);
    }

    @Override
    public void OnEnter() {
        super.OnEnter();

        isDamageDone = false;
        Vector2 diff = m_go.getPosition().subtract(PlayerObj.getInstance().getPosition());
        float angle = Utilies.cal_angle(diff.x, diff.y);
        m_go.facingDirection = get4Direction(angle);

        if(m_go.facingDirection.equals(0, -1))
            m_go.SetAnimation(SpriteAnimationList.SlimeAttackBack);
        else if(m_go.facingDirection.equals(0, 1))
            m_go.SetAnimation(SpriteAnimationList.SlimeAttackFront);
        else if(m_go.facingDirection.equals(-1, 0))
            m_go.SetAnimation(SpriteAnimationList.SlimeAttackLeft);
        else
            m_go.SetAnimation(SpriteAnimationList.SlimeAttackRight);
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        if(!isDamageDone && ((Slime)m_go).CheckIfPlayerNear(((Slime)m_go).attackRange)) {
            Random rand = new Random();
            float minDamage = 20.0f;
            float maxDamage = 45.0f;
            float randDamage = rand.nextFloat() * (maxDamage - minDamage) + minDamage;
            PlayerObj.getInstance().TakeDamage(randDamage);
            isDamageDone = true;
        }

        if(m_go.animatedSprite.hasFinished()) {
            m_go.sm.ChangeState("SlimeIdle");
        }
    }

    @Override
    public void OnExit() {
        ((Slime) m_go).SetAttackCooldown();
    }

    public Vector2 get4Direction(float angle) {
        // Up, down left right
        if(angle >= 225 && angle < 315 ) {
            return new Vector2(0, 1);
        } else if(angle >= 315 || angle < 45 ) {
            return new Vector2(-1, 0);
        } else if(angle >= 45 && angle < 135 ) {
            return new Vector2(0, -1);
        } else if(angle >= 135 && angle < 225 ) {
            return new Vector2(1, 0);
        }
        return new Vector2(0, 0);
    }
}
