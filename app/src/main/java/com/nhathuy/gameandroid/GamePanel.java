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

import java.util.ArrayList;
import java.util.Random;

//Tạo bảng điều khiển trò chơi
//surfacerview: một lớp chuyên dụng cho phép vẽ trực tiếp lên màn hình
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {


    private Paint redPaint= new Paint();
    private SurfaceHolder holder;
    private  float x,y;
    private Random random;

    private ArrayList<PointF> skeletons= new ArrayList<>();

    //thêm luồng cho game
    private GameLoop gameLoop;
    public GamePanel(Context context){
        super(context);
        holder=getHolder();
        holder.addCallback(this);
        redPaint.setColor(Color.GREEN);
        random=new Random();
        gameLoop=new GameLoop(this);

        for (int i=0;i<50;i++){
            skeletons.add(new PointF(random.nextInt(1080),random.nextInt(1920)));
        }
    }

    public void render(){
        Canvas c=holder.lockCanvas();
        c.drawColor(Color.BLACK);

        c.drawBitmap(GameCharacters.PLAYER.getSpriteSheet(),500,500,null);
        c.drawBitmap(GameCharacters.PLAYER.getSprites(6,3),x,y,null);

        for(PointF pos:skeletons)
            c.drawBitmap(GameCharacters.SKELETON.getSprites(0,0),pos.x,pos.y,null);

        holder.unlockCanvasAndPost(c);
    }

    //update lại giao diện
    public void update(double delta){
        for(PointF pos:skeletons){
            pos.y+=delta*300;
            if(pos.y>=1920){
                pos.y=0;
            }
        }
    }

    // xử lý sự kiện chạm vào màn hình. kiểm tra hoạt động chậm , di chuyển, nhã  để thực hiện các hành động tưng ứng
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction()==MotionEvent.ACTION_DOWN){

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
