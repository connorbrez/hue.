package com.connorbrezinsky.hue.server;

import com.badlogic.gdx.graphics.Color;

public enum Team {

    RED(new Color(250/255, 50/255, 50/255, 1)), BLUE(new Color(57/255, 60/255, 229/255, 1)), NEUTRAL(new Color(150/255, 150/255, 150/255, 1));

    Color color;

    Team(Color color){
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
