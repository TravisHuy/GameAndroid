package com.nhathuy.gameandroid.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.nhathuy.gameandroid.R;
import com.nhathuy.gameandroid.helpers.interfaces.BitMapMethods;
import com.nhathuy.gameandroid.main.MainActivity;

public enum GameImages implements BitMapMethods {

    MAINMENU_MENUBG(R.drawable.mainmenu_menubackground);
    private final Bitmap image;

    GameImages(int resId) {
        options.inScaled=false;
        image= BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(),resId,options);
    }

    public Bitmap getImage() {
        return image;
    }
}
