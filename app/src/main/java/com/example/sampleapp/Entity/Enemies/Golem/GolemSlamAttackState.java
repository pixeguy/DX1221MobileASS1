package com.example.sampleapp.Entity.Enemies.Golem;

import com.example.sampleapp.Entity.Enemies.Enemy;
import com.example.sampleapp.Entity.Player.PlayerObj;
import com.example.sampleapp.Enums.SpriteAnimationList;
import com.example.sampleapp.Statemchine.State;
import com.example.sampleapp.Utilities.Utilities;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

public class GolemSlamAttackState extends State {

    private int currentNumSlam = 0;
    private final Vector2 prevFacingDirection = new Vector2(0, 0);

    private boolean canDamage = true;

    public GolemSlamAttackState(GameEntity go, String mStateID) {
        super(go, mStateID);
    }

    @Override
    public void OnEnter() {
        super.OnEnter();
        currentNumSlam = 0;
        prevFacingDirection.set(0, 0);
        ((Golem)m_go).isAttacking = true;
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);

        Vector2 diff = m_go.getPosition().subtract(PlayerObj.getInstance().getPosition());
        float angle = Utilities.cal_angle(diff.x, diff.y);
        Vector2 currentFacingDirection = Utilities.get4Direction(angle);

        if(!currentFacingDirection.equals(prevFacingDirection))
        {
            if(currentFacingDirection.equals(0, -1))
                m_go.SetAnimation(SpriteAnimationList.GolemSlamAttackBack);
            else if(currentFacingDirection.equals(0, 1))
                m_go.SetAnimation(SpriteAnimationList.GolemSlamAttackFront);
            else if(currentFacingDirection.equals(-1, 0))
                m_go.SetAnimation(SpriteAnimationList.GolemSlamAttackLeft);
            else
                m_go.SetAnimation(SpriteAnimationList.GolemSlamAttackRight);

            m_go.facingDirection.set(currentFacingDirection);
        }

        prevFacingDirection.set(currentFacingDirection);

        int currentFrame = m_go.animatedSprite.GetCurrentFrame() % m_go.animatedSprite.GetNumCol();
        if(currentFrame >= 5 && currentFrame < 7)
        {
            if(canDamage && ((Enemy)m_go).CheckIfPlayerNear(Golem.ATTACK_RANGE))
            {
                float minDamage = 50.0f;
                float maxDamage = 100.0f;
                float damage = (float) (Math.random() * (maxDamage - minDamage) + minDamage);
                PlayerObj.getInstance().TakeDamage(damage);
                canDamage = false;
            }
        }

        if(m_go.animatedSprite.hasFinished())
        {
            currentNumSlam++;
            m_go.animatedSprite.Reset();
            canDamage = true;
        }

        if(currentNumSlam == Golem.MAX_NUM_SLAM)
        {
            m_go.sm.ChangeState("Idle");
        }
    }

    @Override
    public void OnExit() {
        ((Golem)m_go).SetAttackCooldown();
        ((Golem)m_go).isAttacking = false;
    }
}
