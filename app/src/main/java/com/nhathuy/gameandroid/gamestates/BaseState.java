package com.nhathuy.gameandroid.gamestates;

import com.nhathuy.gameandroid.main.Game;

public abstract  class BaseState {
    protected Game game;

    public BaseState(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
