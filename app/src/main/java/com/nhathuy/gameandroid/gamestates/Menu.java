package com.nhathuy.gameandroid.gamestates;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.nhathuy.gameandroid.helpers.interfaces.GameStateInterface;
import com.nhathuy.gameandroid.main.Game;
import com.nhathuy.gameandroid.main.MainActivity;
import com.nhathuy.gameandroid.ui.ButtonImages;
import com.nhathuy.gameandroid.ui.CustomButton;
import com.nhathuy.gameandroid.ui.GameImages;

public class Menu extends BaseState implements GameStateInterface {

    private CustomButton btnStart;

    private int menuX= MainActivity.GAME_WIDTH/6;
    private int menuY=200;
    private int btnStartX= menuX+ GameImages.MAINMENU_MENUBG.getImage().getWidth()/2- ButtonImages.MENU_START.getWidth()/2;
    private int btnStartY= menuY+100;
    public Menu(Game game) {
        super(game);
        btnStart=new CustomButton(btnStartX,btnStartY, ButtonImages.MENU_START.getWidth(), ButtonImages.MENU_START.getHeight());
    }

    @Override
    public void update(double delta) {

    }

    @Override
    public void render(Canvas canvas) {
        canvas.drawBitmap(GameImages.MAINMENU_MENUBG.getImage(),
                menuX,
                menuY,
                null);

        canvas.drawBitmap(ButtonImages.MENU_START.getBtnImg(btnStart.isPushed()),
                btnStart.getHitBox().left,
                btnStart.getHitBox().top,
                null);

    }

    @Override
    public void touchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (isIn(event, btnStart))
                btnStart.setPushed(true);
        }
        else if(event.getAction()==MotionEvent.ACTION_UP){
            if (isIn(event, btnStart))
                if(btnStart.isPushed())
                    game.setCurrentGameState(Game.GameState.PLAYING);
            btnStart.setPushed(false);
        }
    }

    private boolean isIn(MotionEvent e, CustomButton b) {
        return b.getHitBox().contains(e.getX(),e.getY());
    }
}
