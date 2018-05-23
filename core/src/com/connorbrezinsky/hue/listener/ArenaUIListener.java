package com.connorbrezinsky.hue.listener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.connorbrezinsky.hue.Arena;

/**
 * Created by connorbrezinsky on 2017-05-20.
 */

public class ArenaUIListener implements InputProcessor {

    Arena arena;
    Vector3 mouse;

    public ArenaUIListener(Arena arena){
        this.arena = arena;
        mouse = new Vector3();
    }



    @Override
    public boolean keyDown(int keycode) {
        switch (keycode){
            case Input.Keys.ESCAPE:
                arena.menuOpen = !arena.menuOpen;
                return true;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        arena.main.viewport.unproject(mouse.set(screenX, screenY, 0));

        if(!arena.hasEnteredName) {
            if (arena.enterName.clicked(mouse.x, mouse.y)) {
                Gdx.input.getTextInput(arena.enterNameListener, "Enter Name", "", "name");
                return true;
            }
        }

        if (arena.menuOpen) {
            if(arena.settings.clicked(mouse.x, mouse.y)){
                return true;
            }

            if(arena.disconnect.clicked(mouse.x, mouse.y)){
                arena.connection.getClient().close();
                return true;
            }
        }

        if(arena.canUpgrade){
            if(arena.uiHealth.clicked(mouse.x, mouse.y)){
                arena.connection.player.levels[1]++;
                arena.upgradePoints--;
                return true;
            }

            if(arena.uiRegen.clicked(mouse.x, mouse.y)){
                arena.connection.player.levels[2]++;
                arena.upgradePoints--;
                return true;
            }

            if(arena.uiSpeed.clicked(mouse.x, mouse.y)){
                arena.connection.player.levels[3]++;
                arena.upgradePoints--;
                return true;
            }

            if(arena.uiSplit.clicked(mouse.x, mouse.y)){
                arena.connection.player.levels[4]++;
                arena.upgradePoints--;
                return true;
            }

            if(arena.uiEat.clicked(mouse.x, mouse.y)){
                arena.connection.player.levels[5]++;
                arena.upgradePoints--;
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        arena.main.viewport.unproject(mouse.set(screenX, screenY, 0));

        if(!arena.hasEnteredName) {
            arena.enterName.setHover(arena.enterName.clicked(mouse.x, mouse.y));
        }

        if(arena.menuOpen){
            arena.settings.setHover(arena.settings.clicked(mouse.x, mouse.y));
            arena.disconnect.setHover(arena.disconnect.clicked(mouse.x, mouse.y));
        }

        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
