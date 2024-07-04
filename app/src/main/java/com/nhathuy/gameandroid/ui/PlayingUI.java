package com.nhathuy.gameandroid.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.nhathuy.gameandroid.gamestates.Playing;
import com.nhathuy.gameandroid.main.Game;

public class PlayingUI {

    private final PointF joystickCenterPos= new PointF(350,1050);
    private final PointF attackBtnCenterPos= new PointF(2500,1050);

    private  final Playing playing;
    private final Paint circlePaint;
    //for Ui
    private int radius=150;


//    private float xTouch,yTouch;
    //For multitouch
    private int joystickPointerId=-1;
    private boolean touchDown;

    private CustomButton btnMenu;


    public PlayingUI(Playing playing){
        this.playing=playing;
        circlePaint = new Paint();
        circlePaint.setColor(Color.RED);
        circlePaint.setStrokeWidth(5);
        circlePaint.setStyle(Paint.Style.STROKE);

        btnMenu=new CustomButton(5,5, ButtonImages.PLAYING_MENU.getWidth(),ButtonImages.PLAYING_MENU.getHeight());
    }
    public void draw(Canvas c){
        drawUI(c);
    }
    private void drawUI(Canvas c) {
        c.drawCircle(joystickCenterPos.x,joystickCenterPos.y,radius,circlePaint);
        c.drawCircle(attackBtnCenterPos.x,attackBtnCenterPos.y,radius,circlePaint);
        c.drawBitmap(ButtonImages.PLAYING_MENU.getBtnImg(btnMenu.isPushed(btnMenu.getPointerId())),
                btnMenu.getHitBox().left,
                btnMenu.getHitBox().top
                ,null);
    }

    private void spawnSkeleton(){
        playing.spawnSkeleton();
    }
    //kiểm tra nút di chuyển có còn nằm trong vùng di chuyển không
    private boolean checkInsideJoyStick(PointF eventPos, int pointerId){

        if(isInsideRadius(eventPos,joystickCenterPos)){
            touchDown=true;
            joystickPointerId=pointerId;
            return true;
        }

        return false;
    }
    //kiểm tra nút tấn công
    private boolean checkInsideAttackBtn(PointF eventPos){
        return  isInsideRadius(eventPos,attackBtnCenterPos);
    }
    private boolean isInsideRadius(PointF eventPos,PointF circle){
        float a=Math.abs(eventPos.x-circle.x);
        float b=Math.abs(eventPos.y-circle.y);
        float c=(float) Math.hypot(a,b);

        return c<=radius;
    }
    public void touchEvents(MotionEvent event){

       final int action =event.getActionMasked();
       final int actionIndex=event.getActionIndex();
       final int pointerId=event.getPointerId(actionIndex);

       final PointF eventPos=new PointF(event.getX(actionIndex),event.getY(actionIndex));

        switch (action){
            case MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {

                if(checkInsideJoyStick(eventPos,pointerId)){
                    touchDown=true;
                } else if (checkInsideAttackBtn(eventPos)) {
                    spawnSkeleton();
                } else{
                    if(isIn(eventPos,btnMenu)){
                        btnMenu.setPushed(true,pointerId);
                    }
                }

            }
            case MotionEvent.ACTION_MOVE-> {

                if(touchDown){
                    for (int i = 0; i <event.getPointerCount() ; i++) {
                        if (event.getPointerId(i)==joystickPointerId){
                            float xDiff=event.getX(i) -joystickCenterPos.x;
                            float yDiff=event.getY(i) -joystickCenterPos.y;
                            playing.setPlayerMoveTrue(new PointF(xDiff,yDiff));
                        }
                    }
                }


//                if(touchDown){
//
//                    float xDiff=event.getX()-xCenter;
//                    float yDiff=event.getY()-yCenter;
//
//                    playing.setPlayerMoveTrue(new PointF(xDiff,yDiff));
//                }

            }
            case MotionEvent.ACTION_UP,MotionEvent.ACTION_POINTER_UP-> {

                if(joystickPointerId==pointerId){
                    resetJoystick();
                }
                else{
                    if(isIn(eventPos,btnMenu)){
                        if(btnMenu.isPushed(pointerId)){
                            resetJoystick();
                            playing.setGameStateToMenu();
                        }
                    }
                    btnMenu.unPush(pointerId);
                }
            }
        }
    }
    private void resetJoystick(){
        touchDown=false;
        playing.setPlayerMoveFalse();
        joystickPointerId=-1;
    }
    private boolean isIn(PointF eventPos, CustomButton b) {
        return b.getHitBox().contains(eventPos.x,eventPos.y);
    }
}
