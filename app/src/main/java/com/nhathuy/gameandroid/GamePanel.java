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

import java.util.ArrayList;
import java.util.Random;

//Tạo bảng điều khiển trò chơi
//surfacerview: một lớp chuyên dụng cho phép vẽ trực tiếp lên màn hình
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {


    private Paint redPaint= new Paint();
    private SurfaceHolder holder;

    private Random random;
    private ArrayList<RndSquare> squarePos= new ArrayList<>();
    public GamePanel(Context context){
        super(context);
        holder=getHolder();
        holder.addCallback(this);
        redPaint.setColor(Color.GREEN);
        random=new Random();
    }

    private void render(){
        Canvas c=holder.lockCanvas();
        for (RndSquare pos: squarePos){
           pos.draw(c);
        }

        holder.unlockCanvasAndPost(c);
    }


    // xử lý sự kiện chạm vào màn hình. kiểm tra hoạt động chậm , di chuyển, nhã  để thực hiện các hành động tưng ứng
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction()==MotionEvent.ACTION_DOWN){
            PointF pos=new PointF(event.getX(),event.getY());

            int color= Color.rgb(random.nextInt(256),random.nextInt(256),random.nextInt(256));
            int size= 25+ random.nextInt(101);

            squarePos.add(new RndSquare(pos,color,size));
            render();
        }



        return  true;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        render();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }
    private class RndSquare{
        private PointF pos;
        private int size;
        private Paint paint;
        public RndSquare(PointF pos, int color, int size){
            this.pos=pos;
            this.size=size;
            paint=new Paint();
            paint.setColor(color);
        }


        public void draw(Canvas c){
            c.drawRect(pos.x,pos.y,pos.x+size,pos.y+size,paint);
        }
    }
}
