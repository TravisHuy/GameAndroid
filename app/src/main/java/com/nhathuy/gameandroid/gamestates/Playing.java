package com.nhathuy.gameandroid.gamestates;

import static com.nhathuy.gameandroid.helpers.GameConstants.Sprite.X_DRAW_OFFSET;
import static com.nhathuy.gameandroid.helpers.GameConstants.Sprite.Y_DRAW_OFFSET;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.nhathuy.gameandroid.entities.Character;
import com.nhathuy.gameandroid.entities.Player;
import com.nhathuy.gameandroid.entities.Weapons;
import com.nhathuy.gameandroid.entities.enemies.Skeleton;
import com.nhathuy.gameandroid.environments.MapManager;
import com.nhathuy.gameandroid.helpers.GameConstants;
import com.nhathuy.gameandroid.helpers.interfaces.GameStateInterface;
import com.nhathuy.gameandroid.main.Game;
import com.nhathuy.gameandroid.ui.PlayingUI;

import java.util.ArrayList;

public class Playing extends BaseState implements GameStateInterface {

    private final Paint redPaint;
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

    private boolean attacking,attackChecked;
    private RectF attackBox=null;


    public Playing(Game game){
        super(game);


        mapManager=new MapManager();
        player=new Player();
        skeletons=new ArrayList<>();

        playingUI=new PlayingUI(this);

        redPaint=new Paint();

        redPaint.setStrokeWidth(1);
        redPaint.setStyle(Paint.Style.STROKE);
        redPaint.setColor(Color.RED);


        for (int i = 0; i < 5; i++)
            spawnSkeleton();

        updateWepHitbox();
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
        updateWepHitbox();


        if(attacking){
            if(!attackChecked){
                checkAttack();
            }
        }


            for (Skeleton skeleton : skeletons) {
                if(skeleton.isActive()){
                    skeleton.update(delta);
                }


        }
        updatePlayerMove(delta);
        updateWepHitbox();
    }

    private void checkAttack() {
        RectF attackBoxWithoutCamera =new RectF(attackBox);
        attackBoxWithoutCamera.left-=cameraX;
        attackBoxWithoutCamera.top-=cameraY;
        attackBoxWithoutCamera.right-=cameraX;
        attackBoxWithoutCamera.bottom-=cameraY;

        for(Skeleton s:skeletons){
            if (attackBoxWithoutCamera.intersects(s.getHitBox().left, s.getHitBox().top, s.getHitBox().right, s.getHitBox().bottom)){
                s.setActive(false);
            }
        }
        attackChecked=true;
    }

    /*
    height>width
    width>height
     */
    private void updateWepHitbox() {
        PointF pos=getWepPos();

        float w=getWepWidth();
        float h=getWepHeight();

        attackBox = new RectF(pos.x,pos.y,pos.x+w,pos.y+h);
    }



    private float getWepWidth() {
        return switch (player.getFaceDir()){

            //height>width
            case GameConstants.Face_Dir.UP,GameConstants.Face_Dir.DOWN ->
                Weapons.BIG_SWORD.getWidth();
            case GameConstants.Face_Dir.LEFT  ,GameConstants.Face_Dir.RIGHT ->
                    Weapons.BIG_SWORD.getHeight();
                default -> throw new IllegalArgumentException("Unexpected Value: "+player.getFaceDir());
        };
    }
    private float getWepHeight() {
        return switch (player.getFaceDir()){

            //height>width
            case GameConstants.Face_Dir.UP,GameConstants.Face_Dir.DOWN ->
                    Weapons.BIG_SWORD.getHeight();
            case GameConstants.Face_Dir.LEFT  ,GameConstants.Face_Dir.RIGHT ->
                    Weapons.BIG_SWORD.getWidth();
            default -> throw new IllegalArgumentException("Unexpected Value: "+player.getFaceDir());
        };
    }
    //thêm ví trí của thanh kiếm
    private PointF getWepPos() {
        return switch (player.getFaceDir()){
            case GameConstants.Face_Dir.UP ->
                new PointF(player.getHitBox().left -0.5f *GameConstants.Sprite.SCALE_MULTIPLIER,
                        player.getHitBox().top-Weapons.BIG_SWORD.getHeight()-Y_DRAW_OFFSET);
            case GameConstants.Face_Dir.DOWN ->
                    new PointF(player.getHitBox().left+0.75f*GameConstants.Sprite.SCALE_MULTIPLIER,
                            player.getHitBox().bottom);
            case GameConstants.Face_Dir.LEFT->
                    new PointF(player.getHitBox().left-Weapons.BIG_SWORD.getHeight()-X_DRAW_OFFSET,
                            player.getHitBox().bottom-Weapons.BIG_SWORD.getWidth()-0.75f*GameConstants.Sprite.SCALE_MULTIPLIER );

            case GameConstants.Face_Dir.RIGHT->
                    new PointF(player.getHitBox().right+X_DRAW_OFFSET,
                            player.getHitBox().bottom-Weapons.BIG_SWORD.getWidth()-0.75f*GameConstants.Sprite.SCALE_MULTIPLIER );
            default -> throw new IllegalArgumentException("Unexpected Value: "+player.getFaceDir());
        };
    }

    @Override
    public void render(Canvas c) {
        mapManager.draw(c);
        playingUI.draw(c);
        drawPlayer(c);

            for (Skeleton skeleton : skeletons)
                if(skeleton.isActive())
                    drawCharacter(c, skeleton);


    }



    private void drawPlayer(Canvas c) {
        //vẽ bóng
        c.drawBitmap(Weapons.SHADOW.getWeaponImg(),player.getHitBox().left,
                player.getHitBox().bottom-5*GameConstants.Sprite.SCALE_MULTIPLIER,null);

        c.drawBitmap(player.getGameCharType().getSprites(getAniIndex(),player.getFaceDir()),
                player.getHitBox().left - GameConstants.Sprite.X_DRAW_OFFSET,
                player.getHitBox().top - Y_DRAW_OFFSET,
                null);

        c.drawRect(player.getHitBox(),redPaint);

        if(attacking){
            drawWeapon(c);
        }
    }
    private int getAniIndex(){
        if(attacking) return 4;
        return player.getAniIndex();
    }
    private void drawWeapon(Canvas c) {
        c.rotate(getWepRot(),attackBox.left,attackBox.top);
        c.drawBitmap(Weapons.BIG_SWORD.getWeaponImg(),attackBox.left+wepRotAdjustLeft(),
                attackBox.top+wepRotAdjustTop(),null);
        c.rotate(getWepRot()*-1,attackBox.left,attackBox.top);
        c.drawRect(attackBox,redPaint);
    }

    private float wepRotAdjustTop() {
        return switch (player.getFaceDir()){
            case GameConstants.Face_Dir.LEFT   , GameConstants.Face_Dir.UP ->
                    - Weapons.BIG_SWORD.getHeight();
            default -> 0;
        };
    }

    private float wepRotAdjustLeft() {

        return switch (player.getFaceDir()){
            case GameConstants.Face_Dir.UP  , GameConstants.Face_Dir.RIGHT ->
                - Weapons.BIG_SWORD.getWidth();
            default -> 0;
        };
    }

    private float getWepRot() {
        return switch (player.getFaceDir()){
            case GameConstants.Face_Dir.LEFT -> 90;
            case GameConstants.Face_Dir.UP -> 180;
            case GameConstants.Face_Dir.RIGHT -> 270;
            default -> 0;
        };
    }


    public void drawCharacter(Canvas canvas, Character c){

        canvas.drawBitmap(Weapons.SHADOW.getWeaponImg(),c.getHitBox().left+cameraX,
                c.getHitBox().bottom-5*GameConstants.Sprite.SCALE_MULTIPLIER+cameraY,null);

        canvas.drawBitmap(c.getGameCharType().getSprites(c.getAniIndex(),c.getFaceDir()),
                c.getHitBox().left+cameraX-GameConstants.Sprite.X_DRAW_OFFSET
                ,c.getHitBox().top+cameraY- Y_DRAW_OFFSET,null);

        canvas.drawRect(c.getHitBox().left + cameraX, c.getHitBox().top + cameraY, c.getHitBox().right + cameraX,
                c.getHitBox().bottom + cameraY, redPaint);
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

    public void setAttacking(boolean attacking) {
        this.attacking=attacking;
        if(!attacking){
            attackChecked=false;
        }
    }
}
