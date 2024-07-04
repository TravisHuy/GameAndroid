package com.nhathuy.gameandroid.entities;

import android.graphics.Point;
import android.graphics.RectF;

public abstract class Entity {
    protected RectF hitBox;

    public Entity(Point pos, float width, float height){
        this.hitBox=new RectF(pos.x,pos.y,width,height);
    }

    public RectF getHitBox() {
        return hitBox;
    }
}
