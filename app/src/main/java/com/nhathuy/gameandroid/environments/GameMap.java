package com.nhathuy.gameandroid.environments;

import static com.nhathuy.gameandroid.MainActivity.GAME_HEIGHT;
import static com.nhathuy.gameandroid.MainActivity.GAME_WIDTH;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.nhathuy.gameandroid.helpers.GameConstants;

public class GameMap {
    private int[][] spriteIds;
    private int tileWidth, tileHeight;

    public GameMap(int[][] spriteIds) {
        this.spriteIds = spriteIds;
        calculateTileSize();
    }

    private void calculateTileSize() {
        tileWidth = GAME_WIDTH / spriteIds[0].length;
        tileHeight = GAME_HEIGHT / spriteIds.length;
    }

    public void draw(Canvas c){
        for (int j = 0; j < spriteIds.length; j++) {
            for (int i = 0; i < spriteIds[j].length; i++) {
                Rect dstRect = new Rect(
                        i * tileWidth,
                        j * tileHeight,
                        (i + 1) * tileWidth,
                        (j + 1) * tileHeight
                );
                c.drawBitmap(Floor.OUTSIDE.getSprite(spriteIds[j][i]), null, dstRect, null);
            }
        }
    }
}
