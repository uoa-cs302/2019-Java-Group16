package com.doogies.savepups;

import com.doogies.savepups.entities.Entity;
import com.doogies.savepups.entities.EntityManager;
import com.doogies.savepups.entities.creatures.Player;
import com.doogies.savepups.graphics.GameCamera;
import com.doogies.savepups.house.Room;
import com.doogies.savepups.input.KeyManager;
import com.doogies.savepups.input.MouseManager;

public class Handler {

    private Game game;
    private Room room;
    public EntityManager entityManager;
    public Player player;

    public Handler(Game game) {
        this.game = game;
        player = new Player(this, 64, 64);
        entityManager = new EntityManager(this, player);
    }

    public GameCamera getGameCamera() {
        return game.getGameCamera();
    }

    public KeyManager getKeyManager() {
        return game.getKeyManager();
    }

    public MouseManager getMouseManager() { return  game.getMouseManager(); }

    public int getWidth() {
        return game.getWidth();
    }

    public int getHeight() { return game.getHeight(); }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
        this.room.getEntityManager().getPlayer().setX(room.getSpawnX());
        this.room.getEntityManager().getPlayer().setY(room.getSpawnY());
        this.room.loadFurniture();
    }
}
