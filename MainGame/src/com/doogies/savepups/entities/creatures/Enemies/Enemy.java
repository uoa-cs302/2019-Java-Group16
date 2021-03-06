package com.doogies.savepups.entities.creatures.Enemies;

import com.doogies.savepups.Handler;
import com.doogies.savepups.audio.AudioManager;
import com.doogies.savepups.audio.AudioPlayer;
import com.doogies.savepups.entities.Entity;
import com.doogies.savepups.entities.creatures.Creature;
import com.doogies.savepups.entities.creatures.Player;
import com.doogies.savepups.graphics.assets.FurnitureAssets;
import com.doogies.savepups.house.AStarNode;
import com.doogies.savepups.items.Item;
import com.doogies.savepups.tiles.Tile;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public abstract class Enemy extends Creature {

    int diameter;
    float dx, dy;
    int count;

    Player player;
    protected int direction = 0;
    protected AStarNode moveTo;

    protected Rectangle attackRectangle;

    // Attack timer
    protected long lastAttackTimer, attackCooldown = 2500, attackTimer = attackCooldown;

    // Game Timer
    protected boolean timerSet = false;
    protected int timeTakenMinutes, timeTakenSeconds = 0;
    protected long initalTime;
    protected boolean playerActive = false;
    protected boolean attacking = false;

    protected boolean attackUp, attackDown, attackLeft, attackRight;

    public Enemy(Handler handler, float x, float y, int width, int height) {
        super(handler, x, y, width, height);
        player = handler.getPlayer();
        diameter = 200;
        moveTo = new AStarNode((int) x, (int) y, Tile.tiles[0].getTexture(), false, handler);

    }

    protected void setupAttack() {
        attackRectangle = new Rectangle();
        attackRectangle.width = bounds.width;
        attackRectangle.height = bounds.height;
    }

    protected  void basicEnemyMoveTick() {

        glitchCollisionRespawn();

        if(colCircleBox(handler.getPlayer()) && !(player.getCurrentAnimationFrame() == FurnitureAssets.bed)) {
            diameter = 600;
            moveToPlayer(Tile.TILEHEIGHT/2);
            move();
            checkAttacks();
        } else {
            count++;
            if(count > 30) {
                autoMoveDecider();
            }
            move();
            diameter = 200;
        }
        timeTracker();
    }

    protected void glitchCollisionRespawn() {
        if(collisionWithTile((int) (x + bounds.x)/Tile.TILEHEIGHT,
                             (int) (y + bounds.y)/Tile.TILEHEIGHT) ||
                checkEntityCollision(0, 0)) {

            x = (new Random().nextInt(handler.getRoom().getWidth() - 2) + 1) * Tile.TILEWIDTH;
            y = (new Random().nextInt(handler.getRoom().getHeight() - 2) + 1) * Tile.TILEHEIGHT;
        }
    }

    protected void moveToPlayer(int range) {

        AStarNode goalNode = handler.getRoom().getPathFinder().getNode(
                (int) ((handler.getPlayer().getX() + handler.getPlayer().getBounds().x) / Tile.TILEWIDTH),
                (int) ((handler.getPlayer().getY() + handler.getPlayer().getBounds().y) / Tile.TILEHEIGHT)
        );

        AStarNode startNode = handler.getRoom().getPathFinder().getNode(
                (int) ((x + bounds.x) / Tile.TILEWIDTH),
                (int) ((y + bounds.y) / Tile.TILEHEIGHT)
        );

        AStarNode node = handler.getRoom().getPathFinder().pathFind(startNode, goalNode);

        if(y > (node.y * Tile.TILEHEIGHT) + Tile.TILEHEIGHT / 4) {
            yMove = -speed;
            direction = 1;
        }

        if(y < (node.y * Tile.TILEHEIGHT) - Tile.TILEHEIGHT / 4) {
            yMove = speed;
            direction = 0;
        }

        if(x > (node.x * Tile.TILEWIDTH) + Tile.TILEWIDTH / 4) {
            xMove = -speed;
            direction = 2;
        }

        if(x < (node.x * Tile.TILEWIDTH) - Tile.TILEWIDTH / 4) {
            xMove = speed;
            direction = 3;
        }

        if(getDistanceToPlayer() <  range) {
            dontMove();
        }
    }

    protected void autoMoveDecider() {

        count = 0;

        if(new Random().nextInt(5) == 0){
            xMove = speed;
        }
        if(new Random().nextInt(5) == 1){
            xMove = -speed;
        }
        if(new Random().nextInt(5) == 2){
            yMove = speed;
        }
        if(new Random().nextInt(5) == 3){
            yMove = -speed;
        }
        if(new Random().nextInt(5) == 4) {
            dontMove();
        }

    }

    protected void dontMove() {
        yMove = 0;
        xMove = 0;
    }

    @Override
    protected boolean collisionWithTile(int x, int y) {
        return handler.getRoom().getPathFinder().getNode(x, y).isEntry() || handler.getRoom().getPathFinder().getNode(x, y).isSolid;
    }

    protected boolean colCircleBox(Player player) {

        float dx = (x + width/2 - handler.getGameCamera().getxOffset()) - (player.getX() - handler.getGameCamera().getxOffset() + player.getWidth() / 4);

        float dy = (y + height/2 - handler.getGameCamera().getyOffset()) - (player.getY() + player.getBounds().y - handler.getGameCamera().getyOffset() + player.getHeight() / 4);

        if(Math.sqrt(dx * dx + dy * dy) < (diameter /2 + player.getWidth() /2)) {
            return true;
        }

        return false;
    }

    protected void checkAttacks() {
        attackTimer += System.currentTimeMillis() - lastAttackTimer;
        lastAttackTimer = System.currentTimeMillis();

        if (attackTimer < attackCooldown) {
            attacking = false;
            return;
        }

        Rectangle enemyBounds = getCollisionBounds(0, 0);
        Rectangle playerBounds = player.getCollisionBounds(0, 0);
        Boolean shouldAttack = getDistanceToPlayer() < Tile.TILEWIDTH;

        if (shouldAttack) {
            setupAttackRectangle(enemyBounds, playerBounds);
            playerActive = true;
        } else {
            return;
        }

        attacking = true;

        attackTimer = 0;

        for (Entity e : handler.getRoom().getEntityManager().getEntities()) {
            if (!(e instanceof  Enemy) && e.getCollisionBounds(0,0).intersects(attackRectangle)) {
                e.damage(1);
            }
        }
    }

    protected void setupAttackRectangle(Rectangle enemyBounds, Rectangle playerBounds) {

        if(enemyBounds.x > playerBounds.x) {
            attackRectangle.x = enemyBounds.x - enemyBounds.width;
        } else if (enemyBounds.x < playerBounds.x){
            attackRectangle.x = enemyBounds.x + enemyBounds.width;
        } else {
            attackRectangle.x = playerBounds.x;
        }

        if(enemyBounds.y > playerBounds.y) {
            attackRectangle.y = enemyBounds.y - enemyBounds.height;
        } else if(enemyBounds.y < playerBounds.y){
            attackRectangle.y = enemyBounds.y + enemyBounds.height;
        } else {
            attackRectangle.y = playerBounds.y;
        }
    }

    protected void timeTracker(){

        if(playerActive && !timerSet){
            initalTime = System.currentTimeMillis();
            timerSet = true;
        }

        if(!playerActive) {
            timeTakenMinutes = 0;
            timeTakenSeconds = 0;
        }
        else {
            timeTakenMinutes = (int) (System.currentTimeMillis() - initalTime) / 1000 / 60;
            timeTakenSeconds = (int) ((System.currentTimeMillis() - initalTime) / 1000) % 60;
        }
    }

    protected float getDistanceToPlayer() {
        this.dx = (x + width/2 - handler.getGameCamera().getxOffset()) - (player.getX() - handler.getGameCamera().getxOffset() + player.getWidth() / 2);
        this.dy = (y + height/2 - handler.getGameCamera().getyOffset()) - (player.getY() - handler.getGameCamera().getyOffset() + player.getHeight() / 2);

        return (float) Math.sqrt(this.dx*this.dx + this.dy * this.dy);
    }

    protected void basicEnemyDeath() {
        if(getEnemyEntities() == 1) {
            handler.getRoom().getItemManager().addItem(Item.dog.createNew((int) x, (int) y));
            AudioManager.barking1.play();
        } else {

            int spawnDecider = new Random().nextInt(1);

            if(spawnDecider == 0) {
                handler.getRoom().getItemManager().addItem(Item.coinGold.createNew((int) x, (int) y));
                AudioManager.goldCoinDrop.play();
            }
        }
        handler.getRoom().getItemManager().addItem(Item.life.createNew((int) x + 20, (int) y));
    }

    protected int getEnemyEntities() {
        int count = 0;

        for(Entity e: handler.getRoom().getEntityManager().getEntities()) {
            if(e instanceof Enemy) {
                count++;
            }
        }

        return count;
    }

}
