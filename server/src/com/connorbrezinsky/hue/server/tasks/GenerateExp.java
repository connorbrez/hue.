package com.connorbrezinsky.hue.server.tasks;

import com.connorbrezinsky.hue.server.Main;
import com.connorbrezinsky.hue.server.entities.Experience;
import com.connorbrezinsky.hue.server.events.add.AddExp;

import java.util.TimerTask;

/**
 * Created by connorbrezinsky on 2017-06-04.
 */

public class GenerateExp extends TimerTask {

    AddExp exp = new AddExp();

    @Override
    public void run() {
        if(Main.exp.size() < Main.MAX_EXP ){
            exp.exp = new Experience(Main.expID, Main.AREA);
            Main.exp.add(exp.exp);
            Main.expID++;
            Main.server.sendToAllTCP(exp);
        }
    }
}
