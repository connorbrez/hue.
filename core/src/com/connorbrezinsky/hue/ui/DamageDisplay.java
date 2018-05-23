package com.connorbrezinsky.hue.ui;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;

public class DamageDisplay {


    public ArrayList<Integer> damageValues;
    float timer = 0;

    public DamageDisplay(){
        damageValues = new ArrayList<>();
    }

    public void add(int value){
        damageValues.clear();
        damageValues.add(value);
        timer = 0;
    }

    public void update(){
        timer+= Gdx.graphics.getDeltaTime();

        if(timer>=5){
            damageValues.clear();
        }

    }


}
