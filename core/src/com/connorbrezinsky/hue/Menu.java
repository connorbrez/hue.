package com.connorbrezinsky.hue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.connorbrezinsky.hue.listener.EnterServerAddressListener;
import com.connorbrezinsky.hue.server.ClientConnection;
import com.connorbrezinsky.hue.ui.Button;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by connorbrezinsky on 2017-04-12.
 */
public class Menu implements Screen, InputProcessor {

    private final Main main;
    Stage stage;


    private TextureAtlas atlas;
    private Texture title;

    private Button join;
    private Button exit;
    private Button rightArrow;
    private Button leftArrow;

    private EnterServerAddressListener ipListener;

    private Vector3 mouse = new Vector3();

    private int emojiSelected = 0;

    public String ip = "";
    public int port = 0;

    public Menu(final Main main){
        this.main = main;

        Table table = new Table();
        table.setFillParent(true);
        table.defaults().pad(20);


        stage = new Stage(new FitViewport(1920, 1080));
        Gdx.input.setInputProcessor(stage);
        stage.addActor(table);


        ipListener = new EnterServerAddressListener(main, this);

        atlas = new TextureAtlas(Gdx.files.internal("emoji/emoji.atlas"));


        title = new Texture(Gdx.files.internal("ui/string_title.png"));
        join = new Button("ui/button_join.png");
        join.setHoverButton(new Texture(Gdx.files.internal("ui/button_hover_join.png")));

        exit = new Button("ui/button_exit.png");
        exit.setHoverButton(new Texture(Gdx.files.internal("ui/button_hover_exit.png")));

        rightArrow = new Button("ui/button_arrow_right.png");
        rightArrow.setHoverButton(new Texture(Gdx.files.internal("ui/button_hover_arrow_right.png")));

        leftArrow = new Button("ui/button_arrow_left.png");
        leftArrow.setHoverButton(new Texture(Gdx.files.internal("ui/button_hover_arrow_left.png")));

        join.setX((main.camera.viewportWidth/2) - (join.getWidth()/2));
        join.setY((main.camera.viewportHeight/2) - (join.getHeight()/2));

        exit.setX((main.camera.viewportWidth/2) - (exit.getWidth()/2) - 5);
        exit.setY((main.camera.viewportHeight/2) - (exit.getHeight()/2) - 150);

        rightArrow.setX((main.camera.viewportWidth/2) - (rightArrow.getWidth()/2) + 125);
        rightArrow.setY(150);

        leftArrow.setX(((main.camera.viewportWidth/2) - (leftArrow.getWidth()/2)) - 125);
        leftArrow.setY(150);

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        main.batch.setProjectionMatrix(main.camera.combined);
        main.batch.begin();

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        main.batch.draw(title, (main.camera.viewportWidth/2)-(title.getWidth()/2), main.camera.viewportHeight - 300);

        join.draw(main.batch);
        exit.draw(main.batch);
        rightArrow.draw(main.batch);
        leftArrow.draw(main.batch);

        if(atlas.findRegion(String.valueOf(emojiSelected)) !=null) {
            main.batch.draw(atlas.findRegion(String.valueOf(emojiSelected)), (main.camera.viewportWidth / 2) - (128 / 2), 150, 128, 128);
        }
        main.batch.end();
        main.camera.update();

        if(!ip.isEmpty() && port > 0){
            main.setScreen(new Arena(main, new ClientConnection(ip, port)));
        }



    }




    @Override
    public void resize(int width, int height) {
        main.viewport.update(width, height, true);
        main.camera.update();


    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        title.dispose();
        join.dispose();
        exit.dispose();
        rightArrow.dispose();
        leftArrow.dispose();
        atlas.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
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
       // System.out.println("touch down");

        main.viewport.unproject(mouse.set(screenX, screenY, 0));

        if(join.clicked(mouse.x, mouse.y)){
            main.emoji = String.valueOf(emojiSelected);
            //TODO make custom(added ips) server list & public server list(hosted by us(if that even happens lol))
            Gdx.input.getTextInput(ipListener, "Enter Server Ip", "127.0.0.1", "127.0.0.1");
        }

        if(rightArrow.clicked(mouse.x, mouse.y)){
                emojiSelected++;
        }

        if(leftArrow.clicked(mouse.x, mouse.y)){
                emojiSelected--;

        }

        if(exit.clicked(mouse.x, mouse.y)){
            System.exit(0);
        }

        return true;
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

        main.viewport.unproject(mouse.set(screenX, screenY, 0));

        join.setHover(join.clicked(mouse.x, mouse.y));
        exit.setHover(exit.clicked(mouse.x, mouse.y));
        rightArrow.setHover(rightArrow.clicked(mouse.x, mouse.y));
        leftArrow.setHover(leftArrow.clicked(mouse.x, mouse.y));

        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
