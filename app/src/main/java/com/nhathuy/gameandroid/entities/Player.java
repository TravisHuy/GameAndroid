package com.nhathuy.gameandroid.entities;

import static com.nhathuy.gameandroid.MainActivity.GAME_HEIGHT;
import static com.nhathuy.gameandroid.MainActivity.GAME_WIDTH;

import android.graphics.Point;

import com.nhathuy.gameandroid.MainActivity;

public class Player extends Character{
    public Player() {
        super(new Point(GAME_WIDTH/2,GAME_HEIGHT/2), GameCharacters.PLAYER);
    }
    public void update(double delta, boolean movePlayer){
        if(movePlayer)
            updateAnimation();
    }
}
