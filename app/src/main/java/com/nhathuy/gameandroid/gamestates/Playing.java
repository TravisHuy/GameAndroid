package com.nhathuy.gameandroid.gamestates;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.nhathuy.gameandroid.entities.Character;
import com.nhathuy.gameandroid.entities.Player;
import com.nhathuy.gameandroid.entities.enemies.Skeleton;
import com.nhathuy.gameandroid.environments.MapManager;
import com.nhathuy.gameandroid.helpers.GameConstants;
import com.nhathuy.gameandroid.helpers.interfaces.GameStateInterface;
import com.nhathuy.gameandroid.main.Game;
import com.nhathuy.gameandroid.ui.PlayingUI;

import java.util.ArrayList;

public class Playing extends BaseState implements GameStateInterface {

    private Paint redPaint= new Paint();
    private float cameraX, cameraY;
    private PointF skeletonPos;
    //thêm luồng cho game
    private boolean movePlayer;
    private PointF lastTouchDiff;

    private Player player;

    private ArrayList<Skeleton> skeletons;


    private Character character;
    //Testing Map
    private MapManager mapManager;

    private PlayingUI playingUI;


    public Playing(Game game){
        super(game);


        mapManager=new MapManager();
        player=new Player();
        skeletons=new ArrayList<>();

        playingUI=new PlayingUI(this);
    }

    public void spawnSkeleton(){
        synchronized (skeletons){
            skeletons.add(new Skeleton(new PointF(player.getHitBox().left-cameraX,player.getHitBox().top-cameraY)));
        }
    }
    @Override
    public void update(double delta) {
        mapManager.setCameraValues(cameraX, cameraY);
        player.update(delta,movePlayer);
        synchronized (skeletons) {
            for (Skeleton skeleton : skeletons) {
                skeleton.update(delta);
            }
        }
        updatePlayerMove(delta);

    }

    @Override
    public void render(Canvas c) {
        mapManager.draw(c);
        playingUI.draw(c);
        drawPlayer(c);
        synchronized (skeletons) {
            for (Skeleton skeleton : skeletons) {
                drawCharacter(c, skeleton);
            }
        }
    }



    private void drawPlayer(Canvas c) {
        c.drawBitmap(player.getGameCharType().getSprites(player.getAniIndex(),player.getFaceDir()),
                player.getHitBox().left,
                player.getHitBox().top,
                null);
    }
    public void drawCharacter(Canvas canvas, Character c){
        canvas.drawBitmap(c.getGameCharType().getSprites(c.getAniIndex(),c.getFaceDir()),c.getHitBox().left+cameraX,c.getHitBox().top+cameraY,null);
    }
    private void updatePlayerMove(double delta) {
        if(!movePlayer)
            return;
        float baseSpeed=(float) delta*300;
        float ratio=Math.abs(lastTouchDiff.y)/Math.abs(lastTouchDiff.x);
        double angle= Math.atan(ratio);

        float xSpeed= (float) Math.cos(angle);
        float ySpeed= (float) Math.sin(angle);


        if(xSpeed>ySpeed){
            if(lastTouchDiff.x>0){
                player.setFaceDir(GameConstants.Face_Dir.RIGHT);
            }
            else player.setFaceDir(GameConstants.Face_Dir.LEFT);
        }
        else{
            if(lastTouchDiff.y>0){
                player.setFaceDir(GameConstants.Face_Dir.DOWN);
            }
            else player.setFaceDir(GameConstants.Face_Dir.UP);
        }

        if(lastTouchDiff.x<0){
            xSpeed*=-1;
        }
        if(lastTouchDiff.y<0){
            ySpeed*=-1;
        }

        int pWidth = GameConstants.Sprite.SIZE;
        int pHeight = GameConstants.Sprite.SIZE;

        if (xSpeed <= 0)
            pWidth = 0;
        if (ySpeed <= 0)
            pHeight = 0;


        float deltaX = xSpeed * baseSpeed * -1;
        float deltaY = ySpeed * baseSpeed * -1;

        if (mapManager.canMoveHere(player.getHitBox().left+ cameraX * -1 + deltaX * -1 + pWidth, player.getHitBox().top + cameraY * -1 + deltaY * -1 + pHeight)) {
            cameraX += deltaX;
            cameraY += deltaY;
        }
    }
    public void setPlayerMoveTrue(PointF lastTouchDiff){
        movePlayer= true;
        this.lastTouchDiff=lastTouchDiff;
    }
    public void setPlayerMoveFalse(){
        movePlayer=false;
        player.resetAnimation();
    }
    public void setGameStateToMenu(){
        game.setCurrentGameState(Game.GameState.MENU);
    }
    @Override
    public void touchEvent(MotionEvent event) {
        playingUI.touchEvents(event);
    }

}
