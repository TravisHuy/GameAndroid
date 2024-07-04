package com.nhathuy.gameandroid.entities;

import static com.nhathuy.gameandroid.helpers.GameConstants.Sprite.SIZE;

import android.graphics.Point;
import android.graphics.PointF;

import com.nhathuy.gameandroid.helpers.GameConstants;

public abstract class Character extends  Entity{

    protected int aniTick,aniIndex;
    protected int faceDir= GameConstants.Face_Dir.DOWN;
    protected final GameCharacters gameCharType;
    public Character(PointF pos, GameCharacters gameCharType) {
        super(pos, SIZE, SIZE);
        this.gameCharType=gameCharType;
    }

    // cập nhật lại chuyển động của nhân vật
    public  void updateAnimation(){
        aniTick++;
        if(aniTick>=GameConstants.Animation.SPEED){
            aniTick=0;
            aniIndex++;
            if(aniIndex>=GameConstants.Animation.AMOUNT){
                aniIndex=0;
            }
        }
    }
    public void resetAnimation() {
        aniTick=0;
       aniIndex=0;
    }
    public int getAniIndex() {
        return aniIndex;
    }

    public int getFaceDir() {
        return faceDir;
    }

    public void setFaceDir(int faceDir) {
        this.faceDir = faceDir;
    }

    public GameCharacters getGameCharType() {
        return gameCharType;
    }
}
