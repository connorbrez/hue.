package com.connorbrezinsky.hue.server.tasks;

import java.util.TimerTask;

public class GameOverTask extends TimerTask {

    @Override
    public void run() {
        System.exit(0);
    }
}
