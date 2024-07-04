package com.nhathuy.gameandroid.gamestates;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.nhathuy.gameandroid.helpers.interfaces.GameStateInterface;
import com.nhathuy.gameandroid.main.Game;

public class Menu extends BaseState implements GameStateInterface {

    private Paint paint;

    public Menu(Game game) {
        super(game);
        paint=new Paint();
        paint.setTextSize(60);
        paint.setColor(Color.WHITE);
    }

    @Override
    public void update(double delta) {

    }

    @Override
    public void render(Canvas canvas) {
        canvas.drawText("MENU!",800,200,paint);
    }

    @Override
    public void touchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            game.setCurrentGameState(Game.GameState.PLAYING);
    }
}
