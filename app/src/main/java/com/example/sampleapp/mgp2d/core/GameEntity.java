package com.example.sampleapp.mgp2d.core;

import static com.example.sampleapp.Scenes.GameLevel.GameLevelScene.screenHeight;

import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.util.Log;

import com.example.sampleapp.Collision.Colliders.BoxCollider2D;
import com.example.sampleapp.Collision.Colliders.CircleCollider2D;
import com.example.sampleapp.Collision.Colliders.Collider2D;
import com.example.sampleapp.Entity.Enemies.Enemy;
import com.example.sampleapp.Enums.SpriteAnimationList;
import com.example.sampleapp.Scenes.GameLevel.GameLevelScene;
import com.example.sampleapp.Statemchine.Statemachine;

public abstract class GameEntity {
    public Collider2D collider = null;
    private static int _totalEntitiesCreated = 0;
    public int _id = 0;
    public Vector2 _position = new Vector2(0, 0);
    public float _rotationZ = 0.0f;
    public Vector2 _scale = new Vector2(1,1);
    public Bitmap sprite;
    public AnimatedSprite animatedSprite;
    protected boolean _isCreated = false;
    public boolean isAlive = true;

    public Vector2 facingDirection = new Vector2(0, 0);
    public Statemachine sm = null;

    public float mass = 1.0f;

    public GameEntity targetGO = null;
    public boolean isActive = false;

    public void onCreate(Vector2 pos, Vector2 scale) {
        _isCreated = true;
        _id = _totalEntitiesCreated++;
        _position.set(pos);
        _scale.set(scale);
    }

    public void onCreate(Vector2 pos, Vector2 scale, SpriteAnimationList spriteAnim) {
        onCreate(pos,scale);
        SetAnimation(spriteAnim);
    }

    public void SetAnimation(SpriteAnimationList nextAnim) {
        if(nextAnim == SpriteAnimationList.ExamplePause){
            Log.d("Button", "Btn created");
        }
        sprite = nextAnim.sprite;
        animatedSprite = new AnimatedSprite(sprite, nextAnim.rows, nextAnim.columns, nextAnim.fps, nextAnim.startFrame, nextAnim.endFrame);
        animatedSprite.setLooping(nextAnim.isLooping);
    }

    public Vector2 getPosition() { return _position.copy(); }
    public void setPosition(Vector2 position) { _position.set(position); }

    protected int _ordinal = 0;
    public int getOrdinal() { return _ordinal; }
    public void setOrdinal(int ord) {_ordinal = ord;}

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

    public Paint paint = new Paint();
    private boolean tinted = false;

    public void setTint(int color) {
        tinted = true;

        ColorFilter filter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        paint.setColorFilter(filter);
    }

    public void clearTint() {
        tinted = false;
        paint.setColorFilter(null);
    }

    public boolean CheckIfOutsideWorldBound(Vector2 direction, int offset) {
        if(collider == null) return false;

        RectF wordBound = GameLevelScene.world_bounds;
        int minX = (int) wordBound.left;
        int minY = (int) wordBound.top;
        int maxX = (int) wordBound.right;
        int maxY = (int) wordBound.bottom;

        if(collider.numVertices == 1) {
            CircleCollider2D circleCollider2D = (CircleCollider2D) collider;
            if(direction.equals(0, -1)) {
                return _position.y - circleCollider2D.radius - offset < minY;
            }
            else if(direction.equals(0, 1)) {
                return _position.y + circleCollider2D.radius + offset > maxY;

            }
            else if(direction.equals(-1, 0)) {
                return _position.x - circleCollider2D.radius - offset < minX;
            }
            else if(direction.equals(1, 0)) {
                return _position.x + circleCollider2D.radius + offset > maxX;
            }
        }
        else {
            BoxCollider2D boxCollider2D = (BoxCollider2D) collider;
            if(direction.equals(0, -1)) {
                return _position.y - boxCollider2D.height / 2.0f - offset < minY;
            }
            else if(direction.equals(0, 1)) {
                return _position.y + boxCollider2D.height / 2.0f + offset > maxY;
            }
            else if(direction.equals(-1, 0)) {
                return _position.x - boxCollider2D.width / 2.0f - offset < minX;
            }
            else if(direction.equals(1, 0)) {
                return _position.x + boxCollider2D.width / 2.0f + offset > maxX;
            }
        }

        return false;
    }

    public int[] CheckForUnavailableDirection() {
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
