package com.nhathuy.gameandroid.environments;

import static com.nhathuy.gameandroid.main.MainActivity.GAME_HEIGHT;
import static com.nhathuy.gameandroid.main.MainActivity.GAME_WIDTH;

import android.graphics.Canvas;
import android.graphics.Rect;

public class MapManager {

    private GameMap currentMap;
    private float cameraX, cameraY;
    private int tileWidth, tileHeight;

    public MapManager() {
        initTestMap();
        calculateTileSize();
    }

    private void calculateTileSize() {
        tileWidth = GAME_WIDTH / currentMap.getArrayWidth();
        tileHeight = GAME_HEIGHT / currentMap.getArrayHeight();
    }

    public void setCameraValues(float cameraX, float cameraY) {
        this.cameraX = cameraX;
        this.cameraY = cameraY;
    }

    public boolean canMoveHere(float x, float y) {
        if (x < 0 || y < 0)
            return false;

        if (x >= getMaxWidthCurrentMap() || y >= getMaxHeightCurrentMap())
            return false;

        return true;
    }

    public int getMaxWidthCurrentMap() {
        return currentMap.getArrayWidth() * tileWidth;
    }

    public int getMaxHeightCurrentMap() {
        return currentMap.getArrayHeight() * tileHeight;
    }

    public void draw(Canvas c) {
        for (int j = 0; j < currentMap.getArrayHeight(); j++) {
            for (int i = 0; i < currentMap.getArrayWidth(); i++) {
                Rect dstRect = new Rect(
                        (int)(i * tileWidth + cameraX),
                        (int)(j * tileHeight + cameraY),
                        (int)((i + 1) * tileWidth + cameraX),
                        (int)((j + 1) * tileHeight + cameraY)
                );
                c.drawBitmap(Floor.OUTSIDE.getSprite(currentMap.getSpriteID(i, j)), null, dstRect, null);
            }
        }
    }

    private void initTestMap() {

        int[][] spriteIds = {
                {454, 276, 275, 275, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 275},
                {454, 276, 275, 275, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 275},
                {454, 276, 275, 275, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 275},
                {454, 276, 275, 275, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 275},
                {454, 276, 275, 275, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 275},
                {454, 276, 275, 275, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 275},
                {454, 276, 275, 275, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 275},
                {454, 276, 275, 275, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 275},
                {454, 276, 275, 275, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 275},
                {454, 276, 275, 275, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 275},
                {454, 276, 275, 275, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 275},
                {454, 276, 275, 275, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 275},
                {454, 276, 275, 275, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 275},
                {454, 276, 275, 275, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 275},
                {454, 276, 275, 275, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 275},
                {454, 276, 275, 275, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 190, 275, 190, 275, 275, 279, 275}


        };

        currentMap = new GameMap(spriteIds);
    }
}
