package com.nhathuy.gameandroid.environments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.nhathuy.gameandroid.main.MainActivity;
import com.nhathuy.gameandroid.R;
import com.nhathuy.gameandroid.helpers.GameConstants;
import com.nhathuy.gameandroid.helpers.interfaces.BitMapMethods;

//Bản đồ trong game
public enum Floor implements BitMapMethods {
    OUTSIDE(R.drawable.tileset_floor,22,26);
    private Bitmap[] sprites;
    Floor(int resId,int titlesWidth, int titleInHeight){
        options.inScaled=false;
        sprites=new Bitmap[titleInHeight*titlesWidth];
        Bitmap spriteSheet= BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(),resId,options);
        for (int j=0;j<titleInHeight;j++){
            for(int i=0;i<titlesWidth;i++) {
                int index = j * titlesWidth + i;
                sprites[index] = Bitmap.createBitmap(
                        spriteSheet,
                        GameConstants.Sprite.DEFAULT_SIZE * i,
                        GameConstants.Sprite.DEFAULT_SIZE * j,
                        GameConstants.Sprite.DEFAULT_SIZE,
                        GameConstants.Sprite.DEFAULT_SIZE
                );
            }
        }
    }

    public Bitmap  getSprite(int id){
        return sprites[id];
    }
}

