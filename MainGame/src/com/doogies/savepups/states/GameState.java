package com.doogies.savepups.states;

import com.doogies.savepups.Handler;
import com.doogies.savepups.audio.AudioPlayer;
import com.doogies.savepups.entities.creatures.Player;
import com.doogies.savepups.house.AStarNode;
import com.doogies.savepups.house.HouseGraph;
import com.doogies.savepups.house.Room;
import com.doogies.savepups.items.Item;
import com.doogies.savepups.tiles.Tile;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class GameState extends State {

    private HouseGraph house;

    private boolean worldChanged = false;

    public GameState(Handler handler){
        super(handler);
        house = new HouseGraph(handler);
        handler.setRoom(house.getRoom(1), 5 * Tile.TILEWIDTH, 5*Tile.TILEHEIGHT);
    }

    @Override
    public void tick() {
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_ESCAPE) || handler.getKeyManager().keyJustPressed(KeyEvent.VK_P)){
            State.setState(handler.getGame().pause);
        }
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_H)){
            State.setState(handler.getGame().help);
        }
        checkForRoomChange();
        handler.getRoom().tick();
        //handler.highScoreManager.tick();
    }

    private void checkForRoomChange() {
        Player player = handler.getPlayer();
        Room room = house.getRoom(player.getTileWorldID());
        boolean canSwitch = player.getInventory().getItem("Dog") == handler.getRoom().getID() - 1 ||
                room.hasBeenVisited;

        if(player.inEntry() &&  canSwitch) {
            ArrayList<AStarNode> houseGraph = HouseGraph.house.get(room.ID);
            AStarNode entrance = houseGraph.get(handler.getRoom().ID);
            handler.setRoom(room, entrance.roomSpawnX, entrance.roomSpawnY);
            worldChanged = true;
        }
    }

    @Override
    public void render(Graphics g) {
        if(worldChanged) {
            loadScreen(g);
        }
        handler.getRoom().render(g);
    }

    @Override
    public void startMusic() {

    }

    @Override
    public void stopMusic() {

    }

    private void loadScreen(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0 ,0, handler.getWidth(), handler.getHeight());

        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        worldChanged = false;
    }
}
