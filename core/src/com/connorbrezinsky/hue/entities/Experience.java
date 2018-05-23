package com.connorbrezinsky.hue.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Random;

/**
 * Created by connorbrezinsky on 2017-05-26.
 */

public class Experience extends Entity {

    int score;
    Color color;


    public Experience(){
        Random random = new Random();
        this.position = new Vector2(0,0);
        this.size = random.nextInt(48 - 16 + 1) + 16;
        this.rotation = 0;
        this.score = this.getSize()/8;
        this.setColor(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1));
        this.id = new Random().nextInt();
    }

    public Experience(int id){
        Random random = new Random();
        this.position = new Vector2(0,0);
        this.size = random.nextInt(48 - 16 + 1) + 16;
        this.rotation = 0;
        this.score = this.getSize()/8;
        this.setColor(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1));
        this.id = id;
    }

    // boolean is useless, it's to use area as first param because we already use an int param
    public Experience(int area, boolean bs){
        Random random = new Random();
        this.position = new Vector2(random.nextFloat() * (area - (-area)) + (-area),random.nextFloat() * (area - (-area)) + (-area));
        this.size = random.nextInt(48 - 16 + 1) + 16;
        this.rotation = 0;
        this.score = this.getSize()/8;
        this.setColor(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1));
        this.id = random.nextInt();
    }

    public Experience(int id, int area){
        Random random = new Random();
        this.position = new Vector2(random.nextFloat() * (area - (-area)) + (-area),random.nextFloat() * (area - (-area)) + (-area));
        this.size = random.nextInt(48 - 16 + 1) + 16;
        this.rotation = 0;
        this.score = this.getSize()/8;
        this.setColor(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1));
        this.id = id;
    }


    public void draw(ShapeRenderer shape){
        shape.setColor(color);
        shape.circle(this.position.x, this.position.y, getSize()/2);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setColor(Color color){
        this.color = color;
    }
}
