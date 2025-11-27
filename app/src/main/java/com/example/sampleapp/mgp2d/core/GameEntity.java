package com.example.sampleapp.mgp2d.core;

import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

import com.example.sampleapp.Collision.Colliders.BoxCollider2D;
import com.example.sampleapp.Collision.Colliders.CircleCollider2D;
import com.example.sampleapp.Collision.Colliders.Collider2D;
import com.example.sampleapp.Enums.SpriteAnimationList;
import com.example.sampleapp.Statemchine.Statemachine;

import java.util.ArrayList;

public abstract class GameEntity {
    public Collider2D collider = null;
    private static int _totalEntitiesCreated = 0;
    public int _id = 0;
    public final Vector2 _position = new Vector2(0, 0);
    public float _rotationZ = 0.0f;
    public final Vector2 _scale = new Vector2(1,1);
    public Bitmap sprite;
    public AnimatedSprite animatedSprite;
    protected boolean _isCreated = false;
    public boolean isAlive = true;

    public Vector2 facingDirection = new Vector2(0, 0);
    public Statemachine sm = null;

    protected float currentHealth = 100.0f;
    protected float maxHealth = 100.0f;

    public GameEntity targetGO = null;


    public void onCreate(Vector2 pos, Vector2 scale) {
        _isCreated = true;
        _id = _totalEntitiesCreated++;
        _position.set(pos);
        _scale.set(scale);
    }

    public void onCreate(Vector2 pos, Vector2 scale, SpriteAnimationList spriteAnim) {
        onCreate(pos,scale);
        sprite = spriteAnim.sprite;
        animatedSprite = new AnimatedSprite(sprite, spriteAnim.rows, spriteAnim.columns,spriteAnim.fps);
    }

    public void SetAnimation(SpriteAnimationList nextAnim)
    {
        sprite = nextAnim.sprite;
        animatedSprite = new AnimatedSprite(sprite, nextAnim.rows, nextAnim.columns, nextAnim.fps, nextAnim.startFrame, nextAnim.endFrame);
        animatedSprite.setLooping(nextAnim.isLooping);
    }

    public Vector2 getPosition() { return _position.copy(); }
    public void setPosition(Vector2 position) { _position.set(position); }

    protected int _ordinal = 0;
    public int getOrdinal() { return _ordinal; }

    private boolean _isDone = false;
    public void destroy() { _isDone = true; }
    public boolean canDestroy() { return _isDone; }

    public void onUpdate(float dt) {
        if(canDestroy()) return;
        if(animatedSprite != null) {
            animatedSprite.update(dt); }
    }

    public void onRender(Canvas canvas){
        if(canDestroy()) return;
        if(animatedSprite != null) {
            canvas.save();
            canvas.rotate(_rotationZ, _position.x, _position.y);
            animatedSprite.render(canvas,(int)_position.x,(int)_position.y, _scale, paint);
            canvas.restore();
        }
    }

    private Paint paint = new Paint();
    private boolean tinted = false;
    private int tintColor = 0x77FF0000; // default: semi-transparent red

    public void setTint(int color) {
        tinted = true;
        tintColor = color;

        ColorFilter filter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        paint.setColorFilter(filter);
    }

    public void clearTint() {
        tinted = false;
        paint.setColorFilter(null);
    }

    public boolean CheckIfOutsideWorldBound(Vector2 direction, int offset) {
        if(collider == null) return false;

        int screenWidth = GameActivity.instance.getResources().getDisplayMetrics().widthPixels;
        int screenHeight = GameActivity.instance.getResources().getDisplayMetrics().heightPixels;

        if(collider.numVertices == 1) {
            CircleCollider2D circleCollider2D = (CircleCollider2D) collider;
            if(direction.equals(0, -1)) {
                return _position.y - circleCollider2D.radius - offset < 0;
            }
            else if(direction.equals(0, 1)) {
                return _position.y + circleCollider2D.radius + offset > screenHeight;

            }
            else if(direction.equals(-1, 0)) {
                return _position.x - circleCollider2D.radius - offset < 0;
            }
            else if(direction.equals(1, 0)) {
                return _position.x + circleCollider2D.radius + offset > screenWidth;
            }
        }
        else {
            BoxCollider2D boxCollider2D = (BoxCollider2D) collider;
            if(direction.equals(0, -1)) {
                return _position.y - boxCollider2D.height / 2.0f - offset < 0;
            }
            else if(direction.equals(0, 1)) {
                return _position.y + boxCollider2D.height / 2.0f + offset > screenHeight;
            }
            else if(direction.equals(-1, 0)) {
                return _position.x - boxCollider2D.width / 2.0f - offset < 0;
            }
            else if(direction.equals(1, 0)) {
                return _position.x + boxCollider2D.width / 2.0f + offset > screenWidth;
            }
        }

        return false;
    }

    public int[] CheckForUnvailableDirection() {
        int[] directions = new int[] {-1, -1, -1, -1};
        for(int i = 0; i < directions.length; i++) {
            Vector2 direction = new Vector2(0, 0);
            switch (i) {
                case 0:
                    direction.set(0, -1);
                    break;
                case 1:
                    direction.set(0, 1);
                    break;
                case 2:
                    direction.set(-1, 0);
                    break;
                case 3:
                    direction.set(1, 0);
                    break;
            }

            if(CheckIfOutsideWorldBound(direction, 50)) {
                directions[i] = i;
            }
        }

        return directions;
    }
}
