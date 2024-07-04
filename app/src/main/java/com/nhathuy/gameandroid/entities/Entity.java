package com.nhathuy.gameandroid.entities;

import android.graphics.PointF;
import android.graphics.RectF;

public abstract class Entity {
    protected RectF hitBox;
    protected boolean active= true;
    public Entity(PointF pos, float width, float height){
        this.hitBox=new RectF(pos.x,pos.y,pos.x+width,pos.y+height);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public RectF getHitBox() {
        return hitBox;
    }
}
