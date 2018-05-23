package com.connorbrezinsky.hue.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.connorbrezinsky.hue.Team;
import com.connorbrezinsky.hue.server.ClientConnection;
import com.connorbrezinsky.hue.server.events.update.UpdateObjective;

import java.util.ArrayList;
import java.util.Iterator;

public class Objective extends Entity {


    Team team, capturingTeam;
    Entity captureRect;
    int captureScore = 0, redPlayers = 0, bluePlayers = 0, multiplyer = 1;
    boolean capturing = false;
    public ArrayList<Player> capturingPlayers;
    float captureTimer = 0;

    transient float updateTimer = 0;


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

    public void draw(ShapeRenderer shapeRenderer){
        shapeRenderer.setColor(Color.PINK);
        shapeRenderer.rect(captureRect.position.x, captureRect.position.y, captureRect.size, captureRect.size);

        if(team != null && team.getColor()!= null) shapeRenderer.setColor(team.getColor());
        shapeRenderer.circle(this.position.x, this.position.y, getSize()/2);

        if(isCapturing()){
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.rect((this.position.x - this.size/2) + this.size/8, this.position.y, this.size - ((this.size/8)*2), 50);
            shapeRenderer.setColor(this.capturingTeam.getColor());
            shapeRenderer.rect(this.position.x, this.position.y + 12.5f, this.captureScore*3.5f, 25);
        }
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
}
