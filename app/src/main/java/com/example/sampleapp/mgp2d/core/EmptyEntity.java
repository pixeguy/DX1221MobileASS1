package com.example.sampleapp.mgp2d.core;

import com.example.sampleapp.Enums.SpriteAnimationList;

public class EmptyEntity extends GameEntity{
    public void onCreate(Vector2 position, Vector2 scale)
    {
        super.onCreate(position,scale);
    }
    public void onCreate(Vector2 position, Vector2 scale, SpriteAnimationList sprite)
    {
        super.onCreate(position,scale, sprite);
    }
}
