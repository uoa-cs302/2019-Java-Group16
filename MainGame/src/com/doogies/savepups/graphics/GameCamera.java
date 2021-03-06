package com.doogies.savepups.graphics;

import com.doogies.savepups.Handler;
import com.doogies.savepups.entities.Entity;
import com.doogies.savepups.tiles.Tile;

public class GameCamera {

    private float xOffset;
    private float yOffset;
    private Handler handler;

    public GameCamera(Handler handler, float xOffset, float yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.handler = handler;
    }

    // This function fixes doesnt show blank space but could make maps look bad.
    public void checkBlankSpace(){
        if(xOffset < 0) {
            xOffset = 0;
        }
        else if(xOffset > handler.getRoom().getWidth() * Tile.TILEWIDTH - handler.getWidth()) {
            xOffset = handler.getRoom().getWidth() * Tile.TILEWIDTH - handler.getWidth();
        }

        if(yOffset < 0) {
            yOffset = 0;
        }
        else if(yOffset > handler.getRoom().getHeight() * Tile.TILEHEIGHT - handler.getHeight()) {
            yOffset = handler.getRoom().getHeight() * Tile.TILEHEIGHT - handler.getHeight();
        }
    }

    public void move(float xAmt, float yAmt) {
        xOffset += xAmt;
        yOffset += yAmt;
        //checkBlankSpace();
    }

    public void centerOnEntity(Entity e) {
        xOffset = e.getX() - handler.getWidth() / 2 + e.getWidth() / 2;
        yOffset = e.getY() - handler.getHeight() / 2 + e.getHeight() / 2;
        //checkBlankSpace();
    }

    public float getxOffset() {
        return xOffset;
    }

    public float getyOffset() {
        return yOffset;
    }

}
