package com.connorbrezinsky.hue;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Main extends Game {

	SpriteBatch batch;
	BitmapFont font;

   public BitmapFont menuFont;


	public OrthographicCamera camera;
	public Viewport viewport;
	public static Skin skin;

	public String emoji = "";

	@Override
	public void create () {
		batch = new SpriteBatch();

//
//        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/montserrat-regular.ttf"));
//        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
//
//        fontParameter.color = Color.BLACK;
//        fontParameter.size = 50;
//        menuFont = fontGenerator.generateFont(fontParameter);

        font = new BitmapFont();
        font.setColor(Color.WHITE);
		camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);

        viewport = new FitViewport(1920,1080,camera);
		skin = new Skin();

		Gdx.graphics.setTitle("hue.");


        this.setScreen(new Menu(this));
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		batch.dispose();
        font.dispose();
		super.dispose();
	}


}
