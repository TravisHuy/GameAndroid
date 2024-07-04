package com.nhathuy.gameandroid.entities.enemies;

import static com.nhathuy.gameandroid.main.MainActivity.GAME_HEIGHT;
import static com.nhathuy.gameandroid.main.MainActivity.GAME_WIDTH;

import android.graphics.Point;

import com.nhathuy.gameandroid.entities.Character;
import com.nhathuy.gameandroid.entities.GameCharacters;
import com.nhathuy.gameandroid.helpers.GameConstants;

import java.util.Random;

public class Skeleton extends Character {
    private Random random=new Random();
    private long lastDirChange = System.currentTimeMillis();

    public Skeleton(Point pos) {
        super(pos, GameCharacters.SKELETON);
    }
    public void update(double delta){
        updateAnimation();
        updateMove(delta);
    }

    private void updateMove(double delta) {

        if (System.currentTimeMillis() - lastDirChange >= 3000) {
            faceDir = random.nextInt(4);
            lastDirChange = System.currentTimeMillis();
        }
        switch (faceDir){
            case GameConstants.Face_Dir.DOWN:
                hitBox.top+=delta*300;
                if(hitBox.top>=GAME_HEIGHT ){
                    faceDir =GameConstants.Face_Dir.UP;
                }
                break;
            case GameConstants.Face_Dir.UP:
                hitBox.top-=delta*300;
                if(hitBox.top<=0){
                    faceDir =GameConstants.Face_Dir.DOWN;
                }
                break;
            case GameConstants.Face_Dir.RIGHT:
                hitBox.left+=delta*300;
                if(hitBox.left>=GAME_WIDTH){
                    faceDir =GameConstants.Face_Dir.LEFT;
                }
                break;
            case GameConstants.Face_Dir.LEFT:
                hitBox.left-=delta*300;
                if(hitBox.left<=0){
                    faceDir =GameConstants.Face_Dir.RIGHT;
                }
                break;
        }
    }
}
