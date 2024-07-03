package com.nhathuy.gameandroid.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.nhathuy.gameandroid.MainActivity;
import com.nhathuy.gameandroid.R;

//lớp đại diện cho nhân vật
public enum GameCharacters {
    PLAYER(R.drawable.player_spritesheet),
    SKELETON(R.drawable.skeleton_spritesheet);
    private Bitmap spriteSheet;
    //cắt nhỏ nhân vật ra
    private Bitmap[][] sprites= new Bitmap[7][4];
    private BitmapFactory.Options options=new BitmapFactory.Options();
    GameCharacters(int resId) {
        options.inScaled=false;
        spriteSheet= BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(),resId,options);
        for (int j=0;j<sprites.length;j++){
            for(int i=0;i<sprites[j].length;i++){
                sprites[j][i]=getScaledBitmap(Bitmap.createBitmap(spriteSheet,16*i,16*j,16,16));
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
    private Bitmap getScaledBitmap(Bitmap bitmap){
        return Bitmap.createScaledBitmap(bitmap,bitmap.getWidth()*6,bitmap.getHeight()*6,false);
    }
}
