package com.doogies.savepups.entities.creatures.Enemies;

import com.doogies.savepups.Handler;
import com.doogies.savepups.entities.Entity;
import com.doogies.savepups.entities.creatures.Creature;
import com.doogies.savepups.graphics.Animation;
import com.doogies.savepups.graphics.Assets;
import com.doogies.savepups.utils.GameTimer;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Screamer extends Enemy {

    // Animations
    private Animation animationDown, animationUp, animationLeft, animationRight;


    // Player Direction
    // 0 = down, 1 = up, 2 = left, 3 = right


    public Screamer(Handler handler, float x, float y) {
        super(handler, x, y, Creature.DEFAULT_CREATURE_WIDTH, Creature.DEFAULT_CREATURE_HEIGHT);


        bounds.x = 16;
        bounds.y = 20;
        bounds.width = 32;
        bounds.height = 43;
        setupAttack();

        loadSprites();
        setSpeed(1f);
        setHealth(2);
    }

    private void loadSprites() {
        //Animations
        animationDown = new Animation(64, Assets.screamer_left);
        animationUp = new Animation(64, Assets.screamer_right);
        animationLeft = new Animation(64, Assets.screamer_left);
        animationRight = new Animation(64, Assets.screamer_right);
    }


    @Override
    public void tick() {
        //Animations
        animationDown.tick();
        animationUp.tick();
        animationLeft.tick();
        animationRight.tick();

        //Movement
        if(colCircleBox(handler.getPlayer())) {
            diameter = 600;
            moveToPlayer();
            move();
            count = 31;
        } else {
            count++;
            if(count > 40) {
                dontMove();
                count = 0;
            }
            move();

            diameter = 200;
        }

        checkAttacks();
        timeTracker();
    }

    @Override
    public void die(){
        System.out.println("Screamer has been slain");
    }

    @Override
    public void render(Graphics g) {

        g.drawImage(getCurrentAnimationFrame(),
                (int) (x - handler.getGameCamera().getxOffset()),
                (int) (y - handler.getGameCamera().getyOffset()),
                width, height, null);

        //DOesnt work
        // Red rectangle to represent players collision box
        g.setColor(Color.red);
        g.drawRect((int)(x + bounds.x - handler.getGameCamera().getxOffset()),
                (int)(y + bounds.y - handler.getGameCamera().getyOffset()),
                bounds.width, bounds.height);

        // Oval around enemy
        g.setColor(Color.blue);
        g.drawOval((int)(x + width/2 - handler.getGameCamera().getxOffset() - diameter / 2),
                (int)(y + height/2 - handler.getGameCamera().getyOffset() - diameter / 2), diameter, diameter);

        g.drawRect((int)(x + width/2 - handler.getGameCamera().getxOffset() - diameter / 2),
                (int)(y + height/2 - handler.getGameCamera().getyOffset() - diameter / 2), diameter, diameter);

        // Rect around player
        g.setColor(Color.red);
        g.drawRect((int) (handler.getPlayer().getX() + player.getBounds().x - handler.getGameCamera().getxOffset()),
                (int) (handler.getPlayer().getY() + player.getBounds().y - handler.getGameCamera().getyOffset()),
                handler.getPlayer().getBounds().width, handler.getPlayer().getBounds().height);

        g.setColor(Color.red);
        g.drawRect((int) (attackRectangle.x - handler.getGameCamera().getxOffset()),
                (int) (attackRectangle.y - handler.getGameCamera().getyOffset()),
                attackRectangle.width,
                attackRectangle.height);
    }
//
//
//    public void postRender(Graphics g){
//
//        //Attack animations
//
//        if(attackUp) {
//            g.drawImage(Assets.attack,
//                    (int) (x - handler.getGameCamera().getxOffset()),
//                    (int) (y - handler.getGameCamera().getyOffset() - (height / 2 + 20)),
//                    width, height, null);
//            //return Assets.playerIdleDown;
//        }
//        else if(attackDown) {
//            g.drawImage(Assets.attack,
//                    (int) (x - handler.getGameCamera().getxOffset()),
//                    (int) (y - handler.getGameCamera().getyOffset() + (height / 2 + 20)),
//                    width, height, null);
//            //return Assets.playerIdleUp;
//        }
//        else if(attackLeft) {
//            g.drawImage(Assets.attack,
//                    (int) (x - handler.getGameCamera().getxOffset() - (width / 2 + 20)),
//                    (int) (y - handler.getGameCamera().getyOffset()),
//                    width, height, null);
//            //return Assets.playerIdleLeft;
//        }
//        else if(attackRight) {
//            g.drawImage(Assets.attack,
//                    (int) (x - handler.getGameCamera().getxOffset() + (width / 2 + 20)),
//                    (int) (y - handler.getGameCamera().getyOffset()),
//                    width, height, null);
//            //return Assets.playerIdleRight;
//        }
//    }

    // Getters and setters

    private BufferedImage getCurrentAnimationFrame(){
        if(xMove <0){
            return animationLeft.getCurrentFrame();
        }
        else if(xMove > 0){
            return animationRight.getCurrentFrame();
        }
        else if(yMove < 0) {
            return animationUp.getCurrentFrame();
        }
        else if(yMove > 0){
            return animationDown.getCurrentFrame();
        }
        else{
            // 0 = down, 1 = up, 2 = left, 3 = right
            if(direction == 0) {
                return Assets.screamerIdleLeft;
            }
            else if(direction == 1) {
                return Assets.screamerIdleRight;
            }
            else if(direction == 2) {
                return Assets.screamerIdleRight;
            }
            else if(direction == 3) {
                return Assets.screamerIdleLeft;
            }
        }
        return Assets.enemyIdleDown;
    }


    public boolean isAttackUp() {
        return attackUp;
    }

    public void setAttackUp(boolean attackUp) {
        this.attackUp = attackUp;
    }

    public boolean isAttackDown() {
        return attackDown;
    }

    public void setAttackDown(boolean attackDown) {
        this.attackDown = attackDown;
    }

    public boolean isAttackLeft() {
        return attackLeft;
    }

    public void setAttackLeft(boolean attackLeft) {
        this.attackLeft = attackLeft;
    }

    public boolean isAttackRight() {
        return attackRight;
    }

    public void setAttackRight(boolean attackRight) {
        this.attackRight = attackRight;
    }

    public long getAttackCooldown() {
        return attackCooldown;
    }

    public void setAttackCooldown(long attackCooldown) {
        this.attackCooldown = attackCooldown;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
