package com.example.sampleapp.Entity.Enemies.Toxito;

import com.example.sampleapp.Entity.Player.PlayerObj;
import com.example.sampleapp.Enums.SpriteAnimationList;
import com.example.sampleapp.PostOffice.MessageSpawnProjectile;
import com.example.sampleapp.PostOffice.PostOffice;
import com.example.sampleapp.Statemchine.State;
import com.example.sampleapp.Ultilies.Utilies;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

public class ToxitoAttackState extends State {

    private boolean isAttackDone = false;

    public ToxitoAttackState(GameEntity go, String mStateID) {
        super(go, mStateID);
    }

    @Override
    public void OnEnter() {
        super.OnEnter();
        m_go.SetAnimation(SpriteAnimationList.ToxitoAttackFront);

        Vector2 diff = PlayerObj.getInstance().getPosition().subtract(m_go.getPosition());
        m_go._rotationZ = Utilies.cal_angle(diff.x, diff.y) - 90.0f;
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);

        if(!isAttackDone && m_go.animatedSprite.GetCurrentFrame() == 6) {
            MessageSpawnProjectile message = new MessageSpawnProjectile(m_go,
                    MessageSpawnProjectile.PROJECTILE_TYPE.ENEMY_TOXIC_MISSILE, 2000.0f,
                    m_go._position, m_go.facingDirection);
            PostOffice.getInstance().send("Scene", message);
            isAttackDone = true;
        }

        if(m_go.animatedSprite.hasFinished()) {
            m_go.sm.ChangeState("Idle");
        }
    }

    @Override
    public void OnExit() {
        ((Toxito)m_go).SetAttackCooldown();
        m_go._rotationZ = 0.0f;
        isAttackDone = false;
    }
}
