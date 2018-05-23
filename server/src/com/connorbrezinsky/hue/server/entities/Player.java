package com.connorbrezinsky.hue.server.entities;

import com.badlogic.gdx.math.Vector2;
import com.connorbrezinsky.hue.server.Team;
import com.connorbrezinsky.hue.server.objects.DamageDisplay;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by connorbrezinsky on 2017-05-18.
 */

public class Player extends Entity{

    public int[] levels;
    int health, maxHealth, experience, regenSpeed, eatPower, splitPower;
    float  speed;
    String name, emoji;
    public DamageDisplay damageDisplay;
    Team team;

    public Player(String emoji){
        this.id = new Random().nextInt(999999);
        this.emoji = emoji;
        this.position = new Vector2(0,0);
        this.size = 90;
        this.rotation = 0;
        this.name = "i";
        this.speed = 5;
        this.health = 100;
        this.maxHealth = 100;
        this.regenSpeed = 1;
        this.eatPower = 1;
        this.splitPower = 1;
        levels = new int[6];
        for(int i = 0; i<6;i++){
            levels[i] = 1;
        }
        this.velocity = new Vector2(0, 0);
        this.damageDisplay = new DamageDisplay();
        this.team = Team.NEUTRAL;
    }

    public Player(int id){
        this.emoji = "0";
        this.position = new Vector2(0,0);
        this.size = 90;
        this.rotation = 0;
        this.name = "i";
        this.id = id;
        this.speed = 5;
        this.health = 100;
        this.maxHealth = 100;
        this.regenSpeed = 1;
        this.eatPower = 1;
        this.splitPower = 1;
        levels = new int[6];
        for(int i = 0; i<6;i++){
            levels[i] = 1;
        }
        this.velocity = new Vector2(0, 0);
        this.damageDisplay = new DamageDisplay();
        this.team = Team.NEUTRAL;
    }

    public Player(String emoji, Team team){
        this.id = new Random().nextInt(999999);
        this.emoji = emoji;
        this.position = new Vector2(0,0);
        this.size = 90;
        this.rotation = 0;
        this.name = "i";
        this.speed = 5;
        this.health = 100;
        this.maxHealth = 100;
        this.regenSpeed = 1;
        this.eatPower = 1;
        this.splitPower = 1;
        levels = new int[6];
        for(int i = 0; i<6;i++){
            levels[i] = 1;
        }
        this.velocity = new Vector2(0, 0);
        this.damageDisplay = new DamageDisplay();
        this.team = team;
    }

    public Player(int id, Team team){
        this.emoji = "0";
        this.position = new Vector2(0,0);
        this.size = 90;
        this.rotation = 0;
        this.name = "i";
        this.id = id;
        this.speed = 5;
        this.health = 100;
        this.maxHealth = 100;
        this.regenSpeed = 1;
        this.eatPower = 1;
        this.splitPower = 1;
        levels = new int[6];
        for(int i = 0; i<6;i++){
            levels[i] = 1;
        }
        this.velocity = new Vector2(0, 0);
        this.damageDisplay = new DamageDisplay();
        this.team = team;
    }

    public Player(){
        this.emoji = "0";
        this.position = new Vector2(0,0);
        this.size = 90;
        this.rotation = 0;
        this.name = "i";
        this.id = new Random().nextInt(999999);
        this.speed = 5;
        this.health = 100;
        this.maxHealth = 100;
        this.regenSpeed = 1;
        this.eatPower = 1;
        this.splitPower = 1;
        levels = new int[6];
        for(int i = 0; i<6;i++){
            levels[i] = 1;
        }
        this.velocity = new Vector2(0, 0);
        this.damageDisplay = new DamageDisplay();
    }

    public void draw(){


    }

    void update(){

    }




    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getRegenSpeed() {
        return regenSpeed;
    }

    public void setRegenSpeed(int regenSpeed) {
        this.regenSpeed = regenSpeed;
    }

    public int getEatPower() {
        return eatPower;
    }

    public void setEatPower(int eatPower) {
        this.eatPower = eatPower;
    }

    public int getSplitPower() {
        return splitPower;
    }

    public void setSplitPower(int splitPower) {
        this.splitPower = splitPower;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Player player = (Player) o;

        if (health != player.health) return false;
        if (maxHealth != player.maxHealth) return false;
        if (experience != player.experience) return false;
        if (regenSpeed != player.regenSpeed) return false;
        if (eatPower != player.eatPower) return false;
        if (splitPower != player.splitPower) return false;
        if (Float.compare(player.speed, speed) != 0) return false;
        if (!Arrays.equals(levels, player.levels)) return false;
        if (!name.equals(player.name)) return false;
        return emoji.equals(player.emoji);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(levels);
        result = 31 * result + health;
        result = 31 * result + maxHealth;
        result = 31 * result + experience;
        result = 31 * result + regenSpeed;
        result = 31 * result + eatPower;
        result = 31 * result + splitPower;
        result = 31 * result + (speed != +0.0f ? Float.floatToIntBits(speed) : 0);
        result = 31 * result + name.hashCode();
        result = 31 * result + emoji.hashCode();
        return result;
    }
}
