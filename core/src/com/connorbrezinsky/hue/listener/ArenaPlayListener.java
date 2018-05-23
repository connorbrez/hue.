package com.connorbrezinsky.hue.listener;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.connorbrezinsky.hue.Arena;
import com.connorbrezinsky.hue.entities.Bullet;
import com.connorbrezinsky.hue.server.events.add.AddBullet;
import com.connorbrezinsky.hue.server.events.update.UpdatePlayer;

/**
 * Created by connorbrezinsky on 2017-05-20.
 */

public class ArenaPlayListener implements InputProcessor {

    Arena arena;
    UpdatePlayer updatePlayer;

    public ArenaPlayListener(Arena arena){
        this.arena = arena;
        this.updatePlayer = new UpdatePlayer();
    }

    boolean movingForward = false;
    boolean rotatingLeft = false;
    boolean rotatingRight = false;

    public void update() {

        if (!arena.connection.gameOver) {
            if (movingForward) {
                arena.connection.player.setPosition(
                        new Vector2(
                                arena.connection.player.getPosition().x - (float) Math.sin(arena.connection.player.getRotation() * Math.PI / 180) * arena.connection.player.getSpeed(),
                                arena.connection.player.getPosition().y + (float) Math.cos(arena.connection.player.getRotation() * Math.PI / 180) * arena.connection.player.getSpeed()
                        ));

                if (arena.connection.player.getVelocity().x > 10) {
                    arena.connection.player.getVelocity().x = 10;
                } else if (arena.connection.player.getVelocity().x < -10) {
                    arena.connection.player.getVelocity().x = -10;
                }

                if (arena.connection.player.getVelocity().y > 10) {
                    arena.connection.player.getVelocity().y = 10;
                } else if (arena.connection.player.getVelocity().y < -10) {
                    arena.connection.player.getVelocity().y = -10;
                }

                updatePlayer.player = arena.connection.player;
                arena.connection.getClient().sendTCP(updatePlayer);
            }

            if (rotatingLeft) {
                arena.connection.player.setRotation(arena.connection.player.getRotation() + arena.connection.player.getSpeed() / 2);
                updatePlayer.player = arena.connection.player;
                arena.connection.getClient().sendTCP(updatePlayer);


            } else if (rotatingRight) {
                arena.connection.player.setRotation(arena.connection.player.getRotation() - arena.connection.player.getSpeed() / 2);
                updatePlayer.player = arena.connection.player;
                arena.connection.getClient().sendTCP(updatePlayer);
            }
        }
    }


    @Override
    public boolean keyDown(int keycode) {

        if(keycode == Input.Keys.W){
            movingForward = true;
            return true;
        }

        switch (keycode){
            case Input.Keys.Q:
                    rotatingLeft = true;
                return true;
            case Input.Keys.E:
                    rotatingRight = true;
                return true;
            case Input.Keys.SPACE:
                AddBullet addBullet = new AddBullet();
                addBullet.bullet = new Bullet(arena.connection.player.getPosition().x, arena.connection.player.getPosition().y, arena.connection.player.getRotation(), 10, arena.connection.player);
                addBullet.bullet.setColor(arena.connection.player.getTeam().getColor());
                arena.connection.getClient().sendTCP(addBullet);
                return true;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        if(keycode == Input.Keys.W){
            movingForward = false;
            arena.connection.player.setVelocity(new Vector2(0, 0));
            return true;
        }

        switch (keycode){
            case Input.Keys.Q:
                rotatingLeft = false;
                return true;
            case Input.Keys.E:
                rotatingRight = false;
                return true;

        }

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
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
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
