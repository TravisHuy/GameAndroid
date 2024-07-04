package com.nhathuy.gameandroid;

import static com.nhathuy.gameandroid.MainActivity.GAME_HEIGHT;
import static com.nhathuy.gameandroid.MainActivity.GAME_WIDTH;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.nhathuy.gameandroid.entities.Character;
import com.nhathuy.gameandroid.entities.GameCharacters;
import com.nhathuy.gameandroid.entities.Player;
import com.nhathuy.gameandroid.entities.enemies.Skeleton;
import com.nhathuy.gameandroid.environments.GameMap;
import com.nhathuy.gameandroid.environments.MapManager;
import com.nhathuy.gameandroid.helpers.GameConstants;
import com.nhathuy.gameandroid.inputs.TouchEvents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

//Tạo bảng điều khiển trò chơi
//surfacerview: một lớp chuyên dụng cho phép vẽ trực tiếp lên màn hình
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {


    private Paint redPaint= new Paint();
    private SurfaceHolder holder;

    private float cameraX, cameraY;
    private PointF skeletonPos;
    //thêm luồng cho game
    private GameLoop gameLoop;
    private boolean movePlayer;
    //touch
    private TouchEvents touchEvents;
    private PointF lastTouchDiff;

    private Player player;

    private ArrayList<Skeleton> skeletons;


    private Character character;
    //Testing Map
    private MapManager mapManager;
    public GamePanel(Context context){
        super(context);
        holder=getHolder();
        holder.addCallback(this);
        redPaint.setColor(Color.GREEN);
        gameLoop=new GameLoop(this);
        touchEvents= new TouchEvents(this);
        mapManager=new MapManager();
        player=new Player();
        skeletons=new ArrayList<>();
        for (int i=0;i<50;i++){
            skeletons.add(new Skeleton(new Point(100,100)));
        }

    }

    public void render(){
        Canvas c=holder.lockCanvas();
        c.drawColor(Color.BLACK);

        mapManager.draw(c);
        touchEvents.draw(c);
        drawPlayer(c);
        for (Skeleton skeleton:skeletons){
            drawCharacter(c,skeleton);
        }
        holder.unlockCanvasAndPost(c);
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

    //update lại giao diện
    public void update(double delta){

        mapManager.setCameraValues(cameraX, cameraY);
        player.update(delta,movePlayer);
        for (Skeleton skeleton:skeletons){
            skeleton.update(delta);
        }
        updatePlayerMove(delta);

//        .updateAnimation();
    }

    private void updatePlayerMove(double delta) {
        if(!movePlayer)
            return;
        float baseSpeed=(float) delta*300;
        float ratio=Math.abs(lastTouchDiff.y)/Math.abs(lastTouchDiff.x);
        double angle= Math.atan(ratio);

        float xSpeed= (float) Math.cos(angle);
        float ySpeed= (float) Math.sin(angle);

//        System.out.println("Angle: "+Math.toDegrees(angle));
//        System.out.println("xSpeed: " + xSpeed+ "| ySpeed: "+ySpeed);

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



    // xử lý sự kiện chạm vào màn hình. kiểm tra hoạt động chậm , di chuyển, nhã  để thực hiện các hành động tưng ứng
    @Override
    public boolean onTouchEvent(MotionEvent event) {

       return touchEvents.touchEvent(event);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        gameLoop.startGameLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }
    public void setPlayerMoveTrue(PointF lastTouchDiff){
        movePlayer= true;
        this.lastTouchDiff=lastTouchDiff;
    }
    public void setPlayerMoveFalse(){
        movePlayer=false;
        player.resetAnimation();
    }


}
