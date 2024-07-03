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

import com.nhathuy.gameandroid.entities.GameCharacters;
import com.nhathuy.gameandroid.environments.GameMap;
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
    private  float x,y;
    private Random random;

//    private ArrayList<PointF> skeletons= new ArrayList<>();
    private PointF skeletonPos;
    //thêm luồng cho game
    private GameLoop gameLoop;
    private boolean movePlayer;
    //touch
    private TouchEvents touchEvents;
    private PointF lastTouchDiff;
    private int playerAniIndexY,playerFaceDir= GameConstants.Face_Dir.RIGHT;
    private int aniTick;
    private int aniSpeed=10;
    private long lastDirChange = System.currentTimeMillis();
    private int skeletonFaceDir=GameConstants.Face_Dir.DOWN;


    //Testing Map
    private GameMap testMap;
    public GamePanel(Context context){
        super(context);
        holder=getHolder();
        holder.addCallback(this);
        redPaint.setColor(Color.GREEN);
        random=new Random();
        gameLoop=new GameLoop(this);
        touchEvents= new TouchEvents(this);



        skeletonPos =new PointF(random.nextInt(GAME_WIDTH ),random.nextInt(GAME_HEIGHT));

        int[][] spriteIds = {
                {454, 276, 275, 275, 190, 275, 275, 279, 275, 275, 275, 297, 110, 0, 1, 1, 1, 2, 110, 132},
                {454, 275, 169, 232, 238, 275, 275, 275, 276, 275, 275, 297, 110, 22, 89, 23, 23, 24, 110, 132},
                {454, 275, 190, 276, 275, 275, 279, 275, 275, 275, 279, 297, 110, 22, 23, 23, 23, 24, 110, 132},
                {454, 275, 190, 279, 275, 275, 169, 233, 275, 275, 275, 297, 110, 22, 23, 23, 23, 24, 110, 132},
                {454, 275, 190, 276, 277, 275, 190, 279, 279, 279, 275, 297, 110, 22, 23, 88, 23, 24, 110, 132},
                {454, 275, 235, 232, 232, 232, 260, 279, 276, 279, 275, 297, 110, 22, 23, 89, 23, 24, 110, 132},
                {454, 275, 275, 275, 275, 275, 190, 279, 279, 279, 275, 297, 110, 22, 23, 23, 23, 24, 110, 132},
                {454, 277, 275, 275, 279, 275, 257, 232, 232, 232, 238, 297, 110, 22, 88, 23, 23, 24, 110, 132},
                {454, 275, 275, 275, 275, 275, 190, 279, 275, 275, 275, 297, 110, 22, 23, 23, 88, 24, 110, 132},
                {454, 275, 275, 275, 275, 275, 190, 279, 279, 279, 279, 297, 110, 22, 23, 23, 23, 24, 110, 132},
                {454, 169, 232, 232, 232, 232, 239, 232, 232, 232, 172, 297, 110, 22, 23, 89, 23, 24, 110, 132},
                {454, 190, 279, 275, 275, 275, 275, 275, 275, 275, 190, 297, 110, 44, 45, 45, 45, 46, 110, 132}
        };

        testMap = new GameMap(spriteIds);
    }

    public void render(){
        Canvas c=holder.lockCanvas();
        c.drawColor(Color.BLACK);

        testMap.draw(c);
        touchEvents.draw(c);

        c.drawBitmap(GameCharacters.PLAYER.getSprites(playerAniIndexY,playerFaceDir),x,y,null);
        c.drawBitmap(GameCharacters.SKELETON.getSprites(playerAniIndexY,skeletonFaceDir),skeletonPos.x,skeletonPos.y,null);


        holder.unlockCanvasAndPost(c);
    }

    //update lại giao diện
    public void update(double delta){
        if (System.currentTimeMillis() - lastDirChange >= 3000) {
            skeletonFaceDir = random.nextInt(4);
            lastDirChange = System.currentTimeMillis();
        }
        switch (skeletonFaceDir){
            case GameConstants.Face_Dir.DOWN:
                skeletonPos.y+=delta*300;
                if(skeletonPos.y>=GAME_HEIGHT ){
                    skeletonFaceDir =GameConstants.Face_Dir.UP;
                }
                break;
            case GameConstants.Face_Dir.UP:
                skeletonPos.y-=delta*300;
                if(skeletonPos.y<=0){
                    skeletonFaceDir =GameConstants.Face_Dir.DOWN;
                }
                break;
            case GameConstants.Face_Dir.RIGHT:
                skeletonPos.x+=delta*300;
                if(skeletonPos.x>=GAME_WIDTH){
                    skeletonFaceDir =GameConstants.Face_Dir.LEFT;
                }
                break;
            case GameConstants.Face_Dir.LEFT:
                skeletonPos.x-=delta*300;
                if(skeletonPos.x<=0){
                    skeletonFaceDir =GameConstants.Face_Dir.RIGHT;
                }
                break;
        }
        updatePlayerMove(delta);

        updateAnimation();
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
                playerFaceDir=GameConstants.Face_Dir.RIGHT;
            }
            else playerFaceDir=GameConstants.Face_Dir.LEFT;
        }
        else{
            if(lastTouchDiff.y>0){
                playerFaceDir=GameConstants.Face_Dir.DOWN;
            }
            else playerFaceDir=GameConstants.Face_Dir.UP;
        }

        if(lastTouchDiff.x<0){
            xSpeed*=-1;
        }
        if(lastTouchDiff.y<0){
            ySpeed*=-1;
        }
        x+=xSpeed*baseSpeed;
        y+=ySpeed*baseSpeed;
    }

    // cập nhật lại chuyển động của nhân vật
    private void updateAnimation(){
        if(!movePlayer){
            return;
        }
        aniTick++;
        if(aniTick>=aniSpeed){
            aniTick=0;
            playerAniIndexY++;
            if(playerAniIndexY>=4){
                playerAniIndexY=0;
            }
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
        resetAnimation();
    }

    private void resetAnimation() {
        aniTick=0;
        playerAniIndexY=0;
    }
}
