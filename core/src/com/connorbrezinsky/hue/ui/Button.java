package com.connorbrezinsky.hue.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by connorbrezinsky on 2017-05-07.
 */

public class Button {

    Texture texture;
    Texture hoverButton;
    float x, y, width, height;
    boolean hover = false;

    public Button(String path){
        texture = new Texture(Gdx.files.internal(path));
        this.width = texture.getWidth();
        this.height = texture.getHeight();
    }



    public Texture getTexture(){
        return this.texture;
    }

    public void draw(SpriteBatch batch){
        if(hover){
            batch.draw(hoverButton, getX(), getY(), getWidth(), getHeight());
        }else {
            batch.draw(texture, getX(), getY(), getWidth(), getHeight());
        }
    }

    public boolean clicked(float mX, float mY){
        return mX >= getX() && mX <= getX() + getWidth() && mY >= getY() && mY <= getY() + getHeight();
    }

    public void setHoverButton(Texture hover){
        this.hoverButton = hover;
    }

    public Texture getHoverButton(){
        return this.hoverButton;
    }

    public void setHover(boolean b){
        hover = b;
    }

    public boolean isHovering(){
        return this.hover;
    }

    public boolean getHover(){
        return this.hover;
    }

    public void setX(float x){
        this.x = x;
    }

    public void setY(float y){
        this.y = y;
    }

    public float getX(){
        return this.x;
    }

    public float getY(){
        return this.y;
    }

    public float getWidth(){
        return this.width;
    }

    public float getHeight(){
        return this.height;
    }

    public void setSize(float width, float height){
        this.width = width;
        this.height = height;
    }

    public void dispose(){
        texture.dispose();
        hoverButton.dispose();
    }


}
