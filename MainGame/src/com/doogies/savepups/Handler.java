package com.doogies.savepups;

import com.doogies.savepups.entities.creatures.Player;
import com.doogies.savepups.graphics.GameCamera;
import com.doogies.savepups.house.Room;
import com.doogies.savepups.input.KeyManager;
import com.doogies.savepups.input.MouseManager;
import com.doogies.savepups.utils.HighScoreManager;

public class Handler {

    private Game game;
    private Room room;
    public Player player;

    public HighScoreManager highScoreManager;

    public Handler(Game game) {
        this.game = game;
        player = new Player(this, 64, 64);
        highScoreManager = new HighScoreManager(this);
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

    public Player getPlayer() { return player; }

    public void setRoom(Room room) {
        this.room = room;
        player.setX(room.getSpawnX());
        player.setY(room.getSpawnY());
    }

    public void newPlayer() {
        player = new Player(this, 64, 64);
    }

    public HighScoreManager getHighScoreManager() {
        return highScoreManager;
    }

    public void setHighScoreManager(HighScoreManager highScoreManager) {
        this.highScoreManager = highScoreManager;
    }
}
