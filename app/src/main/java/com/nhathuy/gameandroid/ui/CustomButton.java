package com.nhathuy.gameandroid.ui;

import android.graphics.RectF;

public class CustomButton {
    private RectF hitBox;
    private boolean pushed;
    private int pointerId=-1;
    public CustomButton(float x, float y ,float width, float height){
        hitBox=new RectF(x,y,x+width,y+height);
    }

    public RectF getHitBox() {
        return hitBox;
    }

    public boolean isPushed(int pointerId) {
        if(this.pointerId!=pointerId){
            return false;
        }
        return pushed;
    }

    public void unPush(int pointerId){
        if(this.pointerId!=pointerId){
            return ;
        }
        this.pushed=false;
        this.pointerId=-1;
    }
    public void setPushed(boolean pushed,int pointerId) {
        if(this.pushed)
            return;
        this.pushed = pushed;
        this.pointerId=pointerId;
    }
    public void setPushed(boolean pushed) {
        this.pushed = pushed;
    }
    public boolean isPushed() {
        return pushed;
    }

    public int getPointerId() {
        return pointerId;
    }
}
