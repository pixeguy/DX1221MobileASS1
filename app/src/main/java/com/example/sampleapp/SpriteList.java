package com.example.sampleapp;

import com.example.sampleapp.mgp2d.core.AnimatedSprite;

public enum SpriteList {
    PlayerIdle(R.drawable.player_heli_body,1,7,24);

    public final int spriteSheetID;
    public final int rows;
    public final int columns;
    public final int fps;
    public final int startFrame;
    public final int endFrame;

    SpriteList(int spriteSheetID, int rows, int columns, int fps) {
        this(spriteSheetID, rows, columns, fps, 0, rows * columns - 1); // Play full sheet by default
    }

    SpriteList(int spriteSheetID, int rows, int columns, int fps, int startFrame, int endFrame) {
        this.spriteSheetID = spriteSheetID;
        this.rows = rows;
        this.columns = columns;
        this.fps = fps;
        this.startFrame = startFrame;
        this.endFrame = endFrame;
    }
}
