package com.nhathuy.gameandroid.helpers.interfaces;

import android.graphics.Canvas;
import android.view.MotionEvent;

public interface GameStateInterface {
    void update(double delta);
    void render(Canvas canvas);
    void touchEvent(MotionEvent event);
}
