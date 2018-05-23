package com.connorbrezinsky.hue;

import com.badlogic.gdx.graphics.Color;

public enum Team {

    RED(new Color(1f, 0.1f, 0.1f, 1)), BLUE(new Color(0.22f, 0.23f, 0.89f, 1)), NEUTRAL(new Color(0.5f, 0.5f, 0.5f, 1));

    Color color;

    Team(Color color){
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
