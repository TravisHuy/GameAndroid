package com.nhathuy.gameandroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.nhathuy.gameandroid.entities.GameCharacters;
import com.nhathuy.gameandroid.helpers.GameConstants;

import java.util.ArrayList;
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

    private int playerAniIndexY,playerFaceDir= GameConstants.Face_Dir.RIGHT;
    private int aniTick;
    private int aniSpeed=10;
    private long lastDirChange = System.currentTimeMillis();
    private int skeletonFaceDir=GameConstants.Face_Dir.DOWN;

    public GamePanel(Context context){
        super(context);
        holder=getHolder();
        holder.addCallback(this);
        redPaint.setColor(Color.GREEN);
        random=new Random();
        gameLoop=new GameLoop(this);

        skeletonPos =new PointF(random.nextInt(1080),random.nextInt(1920));

    }

    public void render(){
        Canvas c=holder.lockCanvas();
        c.drawColor(Color.BLACK);


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
                if(skeletonPos.y>=1920){
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
                if(skeletonPos.x>=1080){
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

        updateAnimation();
    }

    // cập nhật lại chuyển động của nhân vật
    private void updateAnimation(){
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

        if(event.getAction()==MotionEvent.ACTION_DOWN){

            float newX=event.getX();
            float newY=event.getY();

            float xDiff= Math.abs(newX-x);
            float yDiff= Math.abs(newY-y);

            if(xDiff>yDiff){
                if(newX>x){
                    playerFaceDir=GameConstants.Face_Dir.RIGHT;
                }
                else playerFaceDir=GameConstants.Face_Dir.LEFT;
            }
            else{
                if(newY>y){
                    playerFaceDir=GameConstants.Face_Dir.DOWN;
                }
                else playerFaceDir=GameConstants.Face_Dir.UP;
            }



            x=event.getX();
            y=event.getY();
        }

        return  true;
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

}
