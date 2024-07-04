package com.nhathuy.gameandroid.entities;

import android.graphics.Point;

import com.nhathuy.gameandroid.helpers.GameConstants;

public class Character extends  Entity{

    protected int aniTick,aniIndex;
    protected int faceDir= GameConstants.Face_Dir.DOWN;
    protected final GameCharacters gameCharType;
    public Character(Point pos, GameCharacters gameCharType) {
        super(pos, 1, 1);
        this.gameCharType=gameCharType;
    }
}
