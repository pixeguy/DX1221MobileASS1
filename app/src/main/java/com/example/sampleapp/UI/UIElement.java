package com.example.sampleapp.UI;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;

import com.example.sampleapp.Managers.UIManager;
import com.example.sampleapp.mgp2d.core.Vector2;

import java.util.ArrayList;
import java.util.List;

public abstract class UIElement {
    public enum ShapeType {Rect, Circle}

    protected ShapeType shape = ShapeType.Rect;
    public final Vector2 position = new Vector2(0, 0);
    public RectF bounds;
    public float radius = 0f; // Circle only
    public final Vector2 scale = new Vector2(1, 1);
    protected Vector2 pivot = new Vector2(0.5f, 0.5f); // center by default
    public void setPivot(float x, float y) {
        pivot.set(x, y);
    }

    public float rotation = 0f;
    public int zIndex = 0;
    public boolean visible = true;
    public boolean interactable = true;
    protected boolean world_space = false;
    protected Bitmap baseSprite = null;
    protected Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public UIElement parent = null;
    public final List<UIElement> children = new ArrayList<>();

    protected UIElement(float x, float y, float width, float height) {
        float halfWidth = width / 2f;
        float halfHeight = height / 2f;
        this.position.set(x, y);
        this.bounds = new RectF(-halfWidth, -halfHeight, halfWidth, halfHeight);
        this.shape = ShapeType.Rect;
    }

    protected UIElement(Vector2 position, float width, float height) {
        float halfWidth = width / 2f;
        float halfHeight = height / 2f;
        this.position.set(position);
        this.bounds = new RectF(-halfWidth, -halfHeight, halfWidth, halfHeight);
        this.shape = ShapeType.Rect;
    }

    protected UIElement(float x, float y, float radius) {
        this.position.set(x, y);
        this.bounds = new RectF(-radius, -radius, radius, radius);
        this.shape = ShapeType.Circle;
        this.radius = radius;
    }

    protected UIElement(Vector2 position, float radius) {
        this.position.set(position);
        this.bounds = new RectF(-radius, -radius, radius, radius);
        this.shape = ShapeType.Circle;
        this.radius = radius;
    }

    public void changeShape(ShapeType s) {
        this.shape = s;
        if (s == ShapeType.Circle) {
            this.radius = Math.min(bounds.width(), bounds.height()) / 2f;
        }
    }

    public void setColor(int color) {
        paint.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
    }

    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    public void setBaseSprite(Bitmap sprite) {
        this.baseSprite = sprite;
    }

    public RectF getTransformedBounds() {
        Vector2 pos = getGlobalPosition();
        Vector2 scl = getGlobalScale();
        return new RectF(
                pos.x + bounds.left * scl.x,
                pos.y + bounds.top * scl.y,
                pos.x + bounds.right * scl.x,
                pos.y + bounds.bottom * scl.y
        );
    }

    public Vector2 getGlobalPosition() {
        float offsetX = pivot.x * bounds.width();
        float offsetY = pivot.y * bounds.height();
        if (parent == null) return new Vector2(position.x + bounds.left + offsetX, position.y + bounds.top + offsetY);
        Vector2 p = parent.getGlobalPosition();
        Vector2 ps = parent.getGlobalScale();
        return new Vector2(p.x + position.x * ps.x, p.y + position.y * ps.y);
    }

    public Vector2 getGlobalScale() {
        if (parent == null) return new Vector2(scale.x, scale.y);
        Vector2 ps = parent.getGlobalScale();
        return new Vector2(scale.x * ps.x, scale.y * ps.y);
    }

    public boolean isPointInside(float px, float py) {
        if (shape == ShapeType.Rect) {
            RectF r = getTransformedBounds();
            return r.contains(px, py);
        } else if (shape == ShapeType.Circle) {
            Vector2 c = getGlobalPosition();
            Vector2 scl = getGlobalScale();
            float globalR = radius * ((scl.x + scl.y) / 2.0f);
            float dx = px - (c.x * scl.x);
            float dy = py - (c.y * scl.y);
            return (dx * dx + dy * dy) <= (globalR * globalR);
        }
        return false;
    }

    public void addParent(UIElement parent) {
        if(this.parent != null) parent.removeChild(this);
        this.parent = parent;
        if(this.parent != null) parent.addChild(this);
    }

    public void addChild(UIElement child) {
        child.parent = this;
        children.add(child);
    }

    public void removeChild(UIElement child) {
        children.remove(child);
    }

    public void onUpdate(float dt) {}
    public abstract void onRender(Canvas canvas);

    // Event hooks
    public void onTouchDown(float x, float y) {}
    public void onTouchMove(float x, float y) {}
    public void onTouchUp() {}

}
