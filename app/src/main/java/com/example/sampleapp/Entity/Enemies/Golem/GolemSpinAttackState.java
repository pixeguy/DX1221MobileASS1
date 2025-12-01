package com.example.sampleapp.Entity.Enemies.Golem;

import com.example.sampleapp.Collision.Colliders.CircleCollider2D;
import com.example.sampleapp.Collision.Detection.Collision;
import com.example.sampleapp.Entity.Player.PlayerObj;
import com.example.sampleapp.Enums.SpriteAnimationList;
import com.example.sampleapp.Statemchine.State;
import com.example.sampleapp.Utilities.Utilities;
import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

/** @noinspection FieldCanBeLocal*/
public class GolemSpinAttackState extends State {
    public enum AttackState{
        Enter,
        Perform,
        Done
    }
    private AttackState attackState = AttackState.Enter;
    private final float accelerationRate = 1000.0f;
    private float acceleration = 0.0f;
    private float angularVel =  0.0f;

    private final Vector2 velocity = new Vector2(0, 0);
    private final float impulseForce = 5000.0f;
    private final float bounciness = 0.9f;

    private final float damping = 0.975f;

    private GameEntity collidedPlayer = null;
    private boolean canDamage = true;

    private final Vector2 targetDir = new Vector2(0, 0);

    public GolemSpinAttackState(GameEntity go, String mStateID) {
        super(go, mStateID);
    }

    @Override
    public void OnEnter() {
        super.OnEnter();
        m_go.SetAnimation(SpriteAnimationList.GolemSpinAttackFront);
        attackState = AttackState.Enter;
        ((Golem)m_go).isAttacking = true;
    }

    @Override
    public void OnUpdate(float dt) {
        super.OnUpdate(dt);
        if(!m_go.animatedSprite.hasFinished()) return;
        switch(attackState) {
            case Enter:
                acceleration += accelerationRate * dt;
                angularVel += acceleration * dt;
                angularVel = Math.min(angularVel, 2500.0f);
                if(angularVel >= 2500.0f) {
                    acceleration = 0.0f;
                    attackState = AttackState.Perform;
                    Vector2 diff = m_go.getPosition().subtract(PlayerObj.getInstance().getPosition());
                    Vector2 direction = diff.normalize();
                    targetDir.set(direction.multiply(-1));
                    velocity.set(targetDir.multiply(impulseForce));
                }
                break;
            case Perform:
                ApplyPhysics(dt, GameActivity.instance.getResources().getDisplayMetrics().widthPixels,
                        GameActivity.instance.getResources().getDisplayMetrics().heightPixels);

                if(velocity.getMagnitude() <= 10.0f) {
                    acceleration = 0.0f;
                    attackState = AttackState.Done;
                }
                break;
            case Done:
                acceleration += accelerationRate * dt;
                angularVel -= acceleration * dt;
                angularVel = Math.max(angularVel, 0.0f);
                if(m_go._rotationZ != 0 && angularVel == 0.0f)
                {
                    m_go._rotationZ = Utilities.lerp(m_go._rotationZ, 360.0f, dt);
                    if(m_go._rotationZ > 358.0f) {
                        m_go._rotationZ = 0.0f;
                        m_go.sm.ChangeState("Idle");
                    }
                }

                break;
        }
        m_go._rotationZ += angularVel * dt;
        m_go._rotationZ %= 360.0f;

        collidedPlayer = null;
        boolean isCollided = Collision.CollisionDetection(m_go.collider, PlayerObj.getInstance().collider, new Vector2(0, 0));
        if(isCollided) {
            collidedPlayer = PlayerObj.getInstance();
            if(canDamage)
            {
                canDamage = false;
                float minDamage = 100.0f;
                float maxDamage = 120.0f;
                float damage = (float) (Math.random() * (maxDamage - minDamage) + minDamage);
                PlayerObj.getInstance().TakeDamage(damage);
            }
        }

        if(collidedPlayer == null)
            canDamage = true;
    }

    @Override
    public void OnExit() {
        ((Golem)m_go).SetSpinAttackCooldown();
        acceleration = 0.0f;
        angularVel = 0.0f;
        ((Golem)m_go).isAttacking = false;
    }

    private void ApplyPhysics(float dt, float worldWidth, float worldHeight) {

        velocity.x *= damping;
        velocity.y *= damping;

        m_go.setPosition(m_go.getPosition().add(velocity.multiply(dt)));

        CircleCollider2D collider = (CircleCollider2D) m_go.collider;
        if(m_go._position.x - collider.radius < 0) {
            velocity.x = -velocity.x * bounciness;
        }

        if(m_go._position.x + collider.radius > worldWidth) {
            velocity.x = -velocity.x * bounciness;
        }

        if(m_go._position.y - collider.radius < 0) {
            velocity.y = -velocity.y * bounciness;
        }

        if(m_go._position.y + collider.radius > worldHeight) {
            velocity.y = -velocity.y * bounciness;
        }

        if (Math.abs(velocity.x) < 0.01f) velocity.x = 0;
        if (Math.abs(velocity.y) < 0.01f) velocity.y = 0;
    }
}
