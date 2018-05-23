package com.connorbrezinsky.hue.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Bullet extends Entity {

    Color color;
    float speed = 1;
    float bulletLife = 0;
    int power;
    Player fromPlayer;

    public Bullet(float x, float y, float rotation, float speed, Player fromPlayer){
        this.position = new Vector2(x, y);
        this.rotation = rotation;
        this.speed = speed;
        this.id = new Random().nextInt(Integer.MAX_VALUE);
        this.size = 32;
        this.fromPlayer = fromPlayer;
        this.power = 10;
        this.color = new Color(0.5f, 0.5f, 0.5f, 1);
    }

    public Bullet(){
        this.position = new Vector2(0, 0);
        this.rotation = 0;
        this.speed = 64;
        this.speed = 3;
        this.id = new Random().nextInt(Integer.MAX_VALUE);
        this.size = 32;
        this.fromPlayer = new Player();
        this.power = 10;
        this.color = new Color(0.5f, 0.5f, 0.5f, 1);
    }



    public void draw(ShapeRenderer shape){
        shape.setColor(color);
        shape.circle(this.position.x, this.position.y, getSize()/2);
    }

    public void update(){
        position.set(this.position.x - (float)Math.sin(this.getRotation() * Math.PI / 180) * this.speed, this.position.y + (float)Math.cos(this.getRotation() * Math.PI / 180) * this.speed);
        this.bulletLife += Gdx.graphics.getDeltaTime();
    }

    public float getBulletLife(){
        return this.bulletLife;
    }

    public Player getFromPlayer() {
        return this.fromPlayer;
    }

    public void setFromPlayer(Player player) {
        this.fromPlayer = player;
    }

    public void setPower(int power){
        this.power = power;
    }

    public int getPower(){
        return this.power;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
