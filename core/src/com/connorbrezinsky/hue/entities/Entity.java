package com.connorbrezinsky.hue.entities;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by connorbrezinsky on 2017-05-27.
 */

public class Entity {

    int id, size;
    float rotation, mass;
    Vector2 position;
    Vector2 velocity;

    public Entity(){

    }

    public Entity(Vector2 position, int size){
        this.position = position;
        this.size = size;
    }

    public boolean intersects(Entity e){
        return (this.position.x < e.getPosition().x + e.getSize() &&
                this.position.x + this.size > e.getPosition().x &&
                this.position.y < e.getPosition().y + e.getSize() &&
                this.position.y + this.getSize() > e.getPosition().y);

    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public void setRotation(float rotation){
        this.rotation = rotation;
    }

    public float getRotation(){
        return this.rotation;
    }

    public int getSize(){
        return this.size;
    }

    public void setSize(int size){
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entity entity = (Entity) o;

        if (id != entity.id) return false;
        return size == entity.size;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + size;
        return result;
    }
}
