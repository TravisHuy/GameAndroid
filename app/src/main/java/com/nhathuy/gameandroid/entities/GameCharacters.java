package com.nhathuy.gameandroid.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.nhathuy.gameandroid.MainActivity;
import com.nhathuy.gameandroid.R;
import com.nhathuy.gameandroid.helpers.GameConstants;
import com.nhathuy.gameandroid.helpers.interfaces.BitMapMethods;

//lớp đại diện cho nhân vật
public enum GameCharacters implements BitMapMethods {
    PLAYER(R.drawable.player_spritesheet),
    SKELETON(R.drawable.skeleton_spritesheet);
    private Bitmap spriteSheet;
    //cắt nhỏ nhân vật ra
    private Bitmap[][] sprites= new Bitmap[7][4];

    GameCharacters(int resId) {
        options.inScaled=false;
        spriteSheet= BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(),resId,options);
        for (int j=0;j<sprites.length;j++){
            for(int i=0;i<sprites[j].length;i++){
                sprites[j][i]=getScaledBitmap(Bitmap.createBitmap(spriteSheet, GameConstants.Sprite.DEFAULT_SIZE*i,GameConstants.Sprite.DEFAULT_SIZE*j,GameConstants.Sprite.DEFAULT_SIZE,GameConstants.Sprite.DEFAULT_SIZE));
            }
        }
    }

    public Bitmap getSpriteSheet() {
        return spriteSheet;
    }

    public Bitmap getSprites(int yPos,int xPos) {
        return sprites[yPos][xPos];
    }

    //thanh đổi kích thước gốc nó từ 16*16 pixel lên kích thước lớn hơn

}
