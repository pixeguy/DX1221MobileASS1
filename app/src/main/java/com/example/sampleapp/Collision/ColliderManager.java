package com.example.sampleapp.Collision;

import com.example.sampleapp.Collision.Colliders.Collider2D;
import com.example.sampleapp.mgp2d.core.GameEntity;
import com.example.sampleapp.mgp2d.core.Vector2;

import java.util.Vector;

public class ColliderManager {
    private static ColliderManager instance;

    public Vector<Collider2D> m_colliders;

    public ColliderManager() {
        m_colliders = new Vector<Collider2D>();
    }

    public static ColliderManager GetInstance() {
        if (instance == null) {
            instance = new ColliderManager();
        }
        return instance;

    }

    public void ClearColliders()
    {
        m_colliders.clear();
    }
}
