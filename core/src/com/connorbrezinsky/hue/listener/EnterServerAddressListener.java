package com.connorbrezinsky.hue.listener;

import com.badlogic.gdx.Input.TextInputListener;
import com.connorbrezinsky.hue.Constants;
import com.connorbrezinsky.hue.Main;
import com.connorbrezinsky.hue.Menu;
import com.connorbrezinsky.hue.server.ClientConnection;

import java.io.IOException;

/**
 * Created by connorbrezinsky on 2017-05-16.
 */

public class EnterServerAddressListener implements TextInputListener {

    ClientConnection connection;
    Main main;
    Menu menu;
    Thread thread;

    public EnterServerAddressListener(Main main, Menu menu){
        this.main = main;
        this.menu = menu;
    }

    @Override
    public void input (String text) {

        if(!isValid(text)){
            return;
        }

        menu.ip = text.contains(":") ? text.substring(0, text.lastIndexOf(":")) : text;
        menu.port = text.contains(":") ? Integer.parseInt(text.substring(text.lastIndexOf(":") + 1, text.length())) : Constants.PORT;

    }


    private boolean isValid (String value) {
        if (value == null) return false;
        value = value.trim();
        return value.length() != 0;
    }

    @Override
    public void canceled () {

    }

    public ClientConnection getConnection(){
        return this.connection;
    }


}
