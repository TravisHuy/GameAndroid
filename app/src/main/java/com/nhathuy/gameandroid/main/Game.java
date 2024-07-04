package com.nhathuy.gameandroid.main;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.nhathuy.gameandroid.gamestates.Menu;
import com.nhathuy.gameandroid.gamestates.Playing;

public class Game {
    private SurfaceHolder holder ;
    private Menu menu;
    private Playing playing;
    private GameLoop gameLoop;
    private GameState currentGameState=GameState.MENU;
    public Game(SurfaceHolder holder) {
        this.holder = holder;
        gameLoop = new GameLoop(this);
        initGameState();
    }

    public  void render(){
        Canvas c=holder.lockCanvas();
        c.drawColor(Color.BLACK);


        //Draw the game
        switch (currentGameState){
            case MENU -> menu.render(c);
            case PLAYING -> playing.render(c);
        }
        holder.unlockCanvasAndPost(c);
    }
    public void update(double delta){
        switch (currentGameState){
            case MENU -> menu.update(delta);
            case PLAYING -> playing.update(delta);
        }
    }

    private void initGameState() {
        menu=new Menu(this);
        playing=new Playing(this);
    }

    public void startGameLoop() {
        gameLoop.startGameLoop();
    }

    public boolean touchEvent(MotionEvent event) {
        switch (currentGameState){
            case MENU -> menu.touchEvent(event);
            case PLAYING -> playing.touchEvent(event);
        }
        return true;
    }

    public enum GameState{
        MENU,PLAYING;
    }

    public GameState getCurrentGameState() {
        return currentGameState;
    }

    public void setCurrentGameState(GameState currentGameState) {
        this.currentGameState = currentGameState;
    }
}
