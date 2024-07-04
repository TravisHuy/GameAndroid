package com.nhathuy.gameandroid.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.nhathuy.gameandroid.R;
import com.nhathuy.gameandroid.helpers.interfaces.BitMapMethods;
import com.nhathuy.gameandroid.main.MainActivity;

public enum Weapons implements BitMapMethods {

    BIG_SWORD(R.drawable.big_sword),
    SHADOW(R.drawable.shadow);

    Bitmap weaponImg;
    Weapons(int resId){
        weaponImg= getScaledBitmap(BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(),resId,options));
    }

    public Bitmap getWeaponImg() {
        return weaponImg;
    }
    public int getWidth(){
        return weaponImg.getWidth();
    }
    public int getHeight(){
        return weaponImg.getHeight();
    }


}
