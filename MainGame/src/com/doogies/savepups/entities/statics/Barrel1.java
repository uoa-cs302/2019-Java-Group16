package com.doogies.savepups.entities.statics;

import com.doogies.savepups.Handler;
import com.doogies.savepups.entities.Entity;
import com.doogies.savepups.graphics.Assets;
import com.doogies.savepups.items.Item;
import com.doogies.savepups.tiles.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Barrel1 extends Entity {

    private BufferedImage barrel1Texture = Assets.barrel1;

    public Barrel1(Handler handler, float x, float y) {
        super(handler, x, y, Tile.TILEWIDTH / 2, Tile.TILEHEIGHT / 2);

        bounds.x = 0;
        bounds.y = (int) (height - height/ 1.5f);
        bounds.width = width + 5;
        bounds.height = (int) (height - height / 2.0f);

        setHealth(3);
    }

    @Override
    public void tick() {
    }

    @Override
    public void die() {
        System.out.println("???");
        handler.getRoom().getItemManager().addItem(Item.bedItem.createNew((int) x, (int) y));
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(barrel1Texture,
                (int) (x - handler.getGameCamera().getxOffset()),
                (int) (y - handler.getGameCamera().getyOffset()),
                width, height, null);

        // Red rectangle to represent players collision box
//        g.setColor(Color.red);
//        g.fillRect((int)(x + bounds.x - handler.getGameCamera().getxOffset()),
//                (int)(y + bounds.y - handler.getGameCamera().getyOffset()),
//               bounds.width, bounds.height);
    }


}
