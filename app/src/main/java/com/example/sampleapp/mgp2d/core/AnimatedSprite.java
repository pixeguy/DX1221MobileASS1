
package com.example.sampleapp.mgp2d.core;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class AnimatedSprite {
    private final int _col;
    public final int _width;
    public final int _height;
    private int _currentFrame = 0;
    private int _startFrame = 0;
    private int _endFrame;
    private final float _timePerFrame;
    private float _timeAccumulated = 0f;
    private final Bitmap _bmp;
    private boolean _isLooping = true;

    private final Rect _src;
    private final Rect _dst;

    public AnimatedSprite(Bitmap bitmap, int row, int col, int fps) {
        _bmp = bitmap;
        _col = col;
        _width = _bmp.getWidth() / _col;
        _height = _bmp.getHeight() / row;
        _timePerFrame = 1f / fps;
        _endFrame = _col * row;
        _src = new Rect();
        _dst = new Rect();
    }

    public AnimatedSprite(Bitmap bitmap, int row, int col, int fps, int startFrame, int endFrame) {
        this(bitmap, row, col, fps);
        _startFrame = startFrame;
        _currentFrame = startFrame;
        _endFrame = endFrame;
    }

    public void setLooping(boolean shouldLoop) { _isLooping = shouldLoop; }

    public boolean hasFinished() {
        if (_isLooping) return false;
        return _currentFrame == _endFrame;
    }

    public void update(float dt) {
        if (hasFinished()) return;
        _timeAccumulated += dt;
        if (_timeAccumulated > _timePerFrame) {
            _currentFrame++;
            if (_currentFrame > _endFrame && _isLooping)
                _currentFrame = _startFrame;
            _timeAccumulated = 0f;
        }
    }



    public void render(Canvas canvas, int x, int y, Vector2 scale, Paint paint) {
        int frameX = _currentFrame % _col;
        int frameY = _currentFrame / _col;
        int srcX = frameX * _width;
        int srcY = frameY * _height;

        int scaledWidth = (int) (_width * scale.x);
        int scaledHeight = (int) (_height * scale.y);

        // Center relative to x and y
        int left = x - scaledWidth / 2;
        int top = y - scaledHeight / 2;

        _src.set(srcX, srcY, srcX + _width, srcY + _height);
        _dst.set(left, top, left + scaledWidth, top + scaledHeight);

        canvas.drawBitmap(_bmp, _src, _dst, paint);
    }

    private Vector2 rotatePoint(float px, float py, float cx, float cy, float angleDeg)
    {
        double rad = Math.toRadians(angleDeg);

        float dx = px - cx;
        float dy = py - cy;

        float rx = (float)(dx * Math.cos(rad) - dy * Math.sin(rad)) + cx;
        float ry = (float)(dx * Math.sin(rad) + dy * Math.cos(rad)) + cy;

        return new Vector2(rx, ry);
    }

    public void render(Canvas canvas, int x, int y, Vector2 scale, float angleDeg, Paint paint)
    {
        // frame math
        int frameX = _currentFrame % _col;
        int frameY = _currentFrame / _col;
        int srcX = frameX * _width;
        int srcY = frameY * _height;

        int scaledWidth = (int)(_width * scale.x);
        int scaledHeight = (int)(_height * scale.y);

        // unrotated rect
        int left = x - scaledWidth / 2;
        int top = y - scaledHeight / 2;

        _src.set(srcX, srcY, srcX + _width, srcY + _height);

        // -----------------------------------------
        // ROTATE THE DST RECTANGLE COORDINATES HERE
        // -----------------------------------------

        // original rect corners
        float l = left;
        float r = left + scaledWidth;
        float t = top;
        float b = top + scaledHeight;

        // rotate all 4 corners around sprite center
        Vector2 p1 = rotatePoint(l, t, x, y, angleDeg);
        Vector2 p2 = rotatePoint(r, t, x, y, angleDeg);
        Vector2 p3 = rotatePoint(r, b, x, y, angleDeg);
        Vector2 p4 = rotatePoint(l, b, x, y, angleDeg);

        // compute rotated bounding box
        float minX = Math.min(Math.min(p1.x, p2.x), Math.min(p3.x, p4.x));
        float maxX = Math.max(Math.max(p1.x, p2.x), Math.max(p3.x, p4.x));
        float minY = Math.min(Math.min(p1.y, p2.y), Math.min(p3.y, p4.y));
        float maxY = Math.max(Math.max(p1.y, p2.y), Math.max(p3.y, p4.y));

        // update _dst to rotated bounding box
        _dst.set((int)minX, (int)minY, (int)maxX, (int)maxY);

        // draw using canvas rotation (matches shape)
        canvas.save();
        canvas.rotate(angleDeg, x, y);
        canvas.drawBitmap(_bmp, _src, _dst, paint);
        canvas.restore();
    }

    public Rect GetRect()
    {
        return _dst;
    }

    public Rect GetRect(Vector2 position, Vector2 scale) {
        int scaledWidth = (int) (_width * scale.x);
        int scaledHeight = (int) (_height * scale.y);

        // Center relative to x and y
        int left = (int) (position.x - (float) scaledWidth / 2);
        int top = (int) (position.y - (float) scaledHeight / 2);

        _dst.set(left, top, left + scaledWidth, top + scaledHeight);

        return _dst;
    }

    public int GetCurrentFrame() { return _currentFrame; }

    public int GetNumCol() { return _col; }

    public void Reset() { _currentFrame = _startFrame; }
}
