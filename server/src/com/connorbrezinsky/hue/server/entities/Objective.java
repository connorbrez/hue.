package com.connorbrezinsky.hue.server.entities;



import com.badlogic.gdx.math.Vector2;
import com.connorbrezinsky.hue.server.Team;

import java.util.ArrayList;

public class Objective extends Entity {


    Team team, capturingTeam;
    Entity captureRect;
    int captureScore = 0, redPlayers = 0, bluePlayers = 0, multiplyer = 1;
    boolean capturing = false;
    public ArrayList<Player> capturingPlayers;
    float captureTimer = 0;

    public Objective(float x, float y, int id){
        this.team = Team.NEUTRAL;
        this.capturingTeam = Team.NEUTRAL;
        this.position = new Vector2(x, y);
        this.size = 512;
        this.captureRect = new Entity(new Vector2(position.x - (this.size/1.5f), position.y - (this.size/1.5f)), this.size + (this.size/3));
        this.id = id;
        capturingPlayers = new ArrayList<>();
    }

    public Objective(){
        this.team = Team.NEUTRAL;
        this.capturingTeam = Team.NEUTRAL;
        this.position = new Vector2(0, 0);
        this.size = 512;
        this.captureRect = new Entity(new Vector2(position.x - (this.size/1.5f), position.y - (this.size/1.5f)), this.size + (this.size/3));
        id = 0;
        capturingPlayers = new ArrayList<>();
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Entity getCaptureRect() {
        return captureRect;
    }

    public void setCaptureRect(Entity captureRect) {
        this.captureRect = captureRect;
    }

    public int getCaptureScore() {
        return captureScore;
    }

    public void setCaptureScore(int captureScore) {
        this.captureScore = captureScore;
    }

    public int getMultiplyer() {
        return multiplyer;
    }

    public void setMultiplyer(int multiplyer) {
        this.multiplyer = multiplyer;
    }

    public boolean isCapturing() {
        return capturing;
    }

    public void setCapturing(boolean capturing) {
        this.capturing = capturing;


    }

    public Team getCapturingTeam() {
        return capturingTeam;
    }

    public void setCapturingTeam(Team capturingTeam) {
        this.capturingTeam = capturingTeam;
    }
}
