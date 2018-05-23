package com.connorbrezinsky.hue.listener;

import com.badlogic.gdx.Input.TextInputListener;
import com.connorbrezinsky.hue.Arena;
import com.connorbrezinsky.hue.Main;
import com.connorbrezinsky.hue.server.events.Join;

/**
 * Created by connorbrezinsky on 2017-05-16.
 */

public class EnterPlayerNameListener implements TextInputListener {

    Main main;
    Arena arena;

    public EnterPlayerNameListener(Main main, Arena arena){
        this.main = main;
        this.arena = arena;
    }

    @Override
    public void input (String text) {
        if(!text.isEmpty()) {
            arena.connection.player.setId(arena.connection.getClient().getID());
            arena.connection.player.setName(text);
            arena.hasEnteredName = true;

            Join join = new Join();
            join.player = arena.connection.player;
            arena.connection.getClient().sendTCP(join);

        }else{
            arena.hasEnteredName = false;
        }
    }

    private boolean isValid (String value) {
        if (value == null) return false;
        value = value.trim();
        return value.length() != 0;
    }

    @Override
    public void canceled () {
        arena.hasEnteredName = false;
    }



}
