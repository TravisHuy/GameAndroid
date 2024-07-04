package com.nhathuy.gameandroid.ui;

import android.graphics.RectF;

public class CustomButton {
    private RectF hitBox;
    private boolean pushed;
    public CustomButton(float x, float y ,float width, float height){
        hitBox=new RectF(x,y,x+width,y+height);
    }

    public RectF getHitBox() {
        return hitBox;
    }

    public boolean isPushed() {
        return pushed;
    }
    public void setPushed(boolean pushed) {
        this.pushed = pushed;
    }
}
