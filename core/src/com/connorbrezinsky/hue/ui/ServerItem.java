package com.connorbrezinsky.hue.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;


/**
 * Created by connorbrezinsky on 2017-05-17.
 */

public class ServerItem {

    String ip;
    String port;
    Texture background;
    FreeTypeFontGenerator fontGenerator;
    FreeTypeFontParameter fontParameter;
    BitmapFont font;


    float x, y;

    String s;

    public ServerItem(String ip, String port){
        this.ip = ip;
        this.port = port;
        background = new Texture(Gdx.files.internal("ui/ui_scribble_large.png"));
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("ui/Montserrat-Medium.ttf"));
        fontParameter = new FreeTypeFontParameter();
        fontParameter.size = 50;
        fontParameter.color = Color.BLACK;
        font = fontGenerator.generateFont(fontParameter);

        s = port.length() > 0 ? ip + ":" + port : ip;
    }



    public void draw(SpriteBatch batch){

        batch.draw(background, x, y);

        //System.out.println(s.length()*50);

        font.draw(batch, s, x + ((getTexture().getWidth()/2) - ((s.length()*23)/2)), y+ 100);


    }

    public void dispose(){
        background.dispose();
        fontGenerator.dispose();
        font.dispose();
    }

    public String getIp(){
        return this.ip;
    }

    public void setIp(String ip){
        this.ip = ip;
    }

    public String getPort(){
        return this.port;
    }

    public void setPort(String port){
        this.port = port;
    }

    public void setX(float x){
        this.x = x;
    }

    public float getX(){
        return this.y;
    }

    public void setY(float y){
        this.y = y;
    }

    public float getY(){
        return this.y;
    }

    public Texture getTexture(){
        return this.background;
    }


}
