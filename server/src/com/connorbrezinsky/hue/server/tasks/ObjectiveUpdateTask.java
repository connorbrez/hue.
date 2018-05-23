package com.connorbrezinsky.hue.server.tasks;

import com.connorbrezinsky.hue.server.Main;
import com.connorbrezinsky.hue.server.Team;
import com.connorbrezinsky.hue.server.entities.Objective;
import com.connorbrezinsky.hue.server.entities.Player;
import com.connorbrezinsky.hue.server.events.Winner;
import com.connorbrezinsky.hue.server.events.update.UpdateObjectives;

import java.util.TimerTask;

public class ObjectiveUpdateTask extends TimerTask {

    UpdateObjectives updateObjectives = new UpdateObjectives();
    Winner winner = new Winner();


    @Override
    public void run() {
        boolean updated = false;
        int capturedRedObjectives = 0;
        int capturedBlueObjectives = 0;
        for(Objective objective : Main.objectives){

            if(objective.getTeam() == Team.RED)capturedRedObjectives++;
            if(objective.getTeam() == Team.BLUE)capturedBlueObjectives++;


            int blueCount = 0;
            int redCount = 0;

            for(Player player : Main.players){
                if(objective.getCaptureRect().intersects(player)){
                    objective.capturingPlayers.add(player);
                    redCount += player.getTeam() == Team.RED ? 1 : 0;
                    blueCount += player.getTeam() == Team.BLUE ? 1 : 0;
                }
            }

            if(blueCount>redCount){
                objective.setCapturingTeam(Team.BLUE);
                objective.setCaptureScore(objective.getCaptureScore() + (objective.getMultiplyer() * blueCount));
                if(objective.getCaptureScore()>=50){
                    objective.setCaptureScore(50);
                    objective.setTeam(Team.BLUE);
                }else{
                    objective.setTeam(Team.NEUTRAL);
                }
                updated = true;
            }else if(redCount>blueCount){
                objective.setCapturingTeam(Team.RED);
                objective.setCaptureScore(objective.getCaptureScore() - (objective.getMultiplyer() * redCount));
                if(objective.getCaptureScore()<=-50){
                    objective.setCaptureScore(-50);
                    objective.setTeam(Team.RED);
                }else{
                    objective.setTeam(Team.NEUTRAL);
                }
                updated = true;
            }


            if(!objective.capturingPlayers.isEmpty())objective.setCapturing(true);
            if(objective.capturingPlayers.isEmpty())objective.setCapturing(false);
            objective.capturingPlayers.clear();
        }
        updateObjectives.objectives = Main.objectives;
        if(updated)Main.server.sendToAllTCP(updateObjectives);

        if(capturedBlueObjectives >= 3){
            winner.team = Team.BLUE;
            Main.server.sendToAllTCP(winner);
            Main.timer.schedule(new GameOverTask(), 2500, 2500);
            return;
        }else if(capturedRedObjectives >= 3) {
            winner.team = Team.RED;
            Main.server.sendToAllTCP(winner);
            Main.timer.schedule(new GameOverTask(), 2500, 2500);
            return;
        }

    }
}
