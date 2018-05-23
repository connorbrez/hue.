package com.connorbrezinsky.hue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.connorbrezinsky.hue.entities.Bullet;
import com.connorbrezinsky.hue.entities.Experience;
import com.connorbrezinsky.hue.entities.Objective;
import com.connorbrezinsky.hue.entities.Player;
import com.connorbrezinsky.hue.listener.ArenaPlayListener;
import com.connorbrezinsky.hue.listener.ArenaUIListener;
import com.connorbrezinsky.hue.listener.EnterPlayerNameListener;
import com.connorbrezinsky.hue.server.ClientConnection;
import com.connorbrezinsky.hue.server.events.Damage;
import com.connorbrezinsky.hue.server.events.remove.RemoveExp;
import com.connorbrezinsky.hue.ui.Button;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by connorbrezinsky on 2017-04-12.
 */

public class Arena implements Screen {

    public final Main main;
    public final ClientConnection connection;

    Texture uiBackground;

    public ShapeRenderer shapeRenderer;
    Damage damageObject = new Damage();

    DecimalFormat noDec = new DecimalFormat("00");

    public int experienceToNextLevel = 25;
    public int upgradePoints = 1;
    private int p = 0;
    float damageTimer = 2;

    Color background = new Color(0.898f, 0.894f, 0.898f, 0.2f);

    InputMultiplexer input;
    ArenaUIListener uiListener;
    ArenaPlayListener playListener;
    public EnterPlayerNameListener enterNameListener;

    FreeTypeFontGenerator fontGenerator;
    FreeTypeFontParameter fontParameter;
    BitmapFont font75, font50Green, font50, font30, font25, font50Red;
    BitmapFont redTeamFont, blueTeamFont, redTeamWonFont, blueTeamWonFont;

    public Button enterName;
    public Button settings;
    public Button disconnect;

    public boolean hasEnteredName = false;
    public boolean menuOpen = false;
    public boolean canUpgrade = false;

    public Button uiHealth, uiRegen, uiSpeed, uiEat, uiSplit;
    Sound ouch, hitmarker;

    public Arena(final Main main, ClientConnection connection){

        ouch = Gdx.audio.newSound(Gdx.files.internal("sound/ouch.wav"));
        hitmarker = Gdx.audio.newSound(Gdx.files.internal("sound/hitmarker.wav"));


        this.main = main;
        this.connection = connection;

        main.camera.zoom = 1.1f;

        shapeRenderer = new ShapeRenderer();

        uiBackground = new Texture(Gdx.files.internal("ui/ui_background.png"));

        connection.player = new Player(main.emoji);
        connection.player.setHealth(connection.player.getMaxHealth());

        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("ui/comicBook.otf"));
        fontParameter = new FreeTypeFontParameter();

        fontParameter.size = 100;
        fontParameter.color = Color.FIREBRICK;
        font75 = fontGenerator.generateFont(fontParameter);

        fontParameter.size = 50;
        fontParameter.color = Color.FOREST;
        font50Green = fontGenerator.generateFont(fontParameter);

        fontParameter.size = 50;
        fontParameter.color = Color.RED;
        font50Red = fontGenerator.generateFont(fontParameter);

        fontParameter.size = 50;
        fontParameter.color = Color.BLACK;
        font50 = fontGenerator.generateFont(fontParameter);

        fontParameter.size = 30;
        font30 = fontGenerator.generateFont(fontParameter);

        fontParameter.color = Color.RED;
        redTeamFont = fontGenerator.generateFont(fontParameter);

        fontParameter.color = Color.BLUE;
        blueTeamFont = fontGenerator.generateFont(fontParameter);


        fontParameter.size = 150;

        fontParameter.color = Color.RED;
        redTeamWonFont = fontGenerator.generateFont(fontParameter);

        fontParameter.color = Color.BLUE;
        blueTeamWonFont = fontGenerator.generateFont(fontParameter);


        fontParameter.size = 25;
        font25 = fontGenerator.generateFont(fontParameter);

        input = new InputMultiplexer();
        uiListener = new ArenaUIListener(this);
        playListener = new ArenaPlayListener(this);

        enterNameListener = new EnterPlayerNameListener(this.main, this);

        uiHealth = new Button("ui/ui_health.png");
        uiRegen = new Button("ui/ui_regen.png");
        uiSpeed = new Button("ui/ui_speed.png");
        uiEat = new Button("ui/ui_eat.png");
        uiSplit = new Button("ui/ui_split.png");

        enterName = new Button("ui/button_enter_name.png");
        enterName.setHoverButton(new Texture(Gdx.files.internal("ui/button_hover_enter_name.png")));
        settings = new Button("ui/button_settings.png");
        settings.setHoverButton(new Texture(Gdx.files.internal("ui/button_hover_settings.png")));
        disconnect = new Button("ui/button_disconnect.png");
        disconnect.setHoverButton(new Texture(Gdx.files.internal("ui/button_hover_disconnect.png")));


        input.addProcessor(uiListener);
        input.addProcessor(playListener);

        Gdx.input.setInputProcessor(input);
    }


    @Override
    public void show() {

    }

    public float translateX(float x){
        return (main.camera.position.x - (main.camera.viewportWidth/2)) + x;
    }

    public float translateY(float y){
        return (main.camera.position.y - (main.camera.viewportHeight/2)) + y;
    }

    public void drawMenu(){

        if(menuOpen) {
            settings.draw(main.batch);
            disconnect.draw(main.batch);
        }

    }



    @Override
    public void render(float delta) {
        main.batch.setProjectionMatrix(main.camera.combined);
        shapeRenderer.setProjectionMatrix(main.camera.combined);

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(background);
        shapeRenderer.rect(-connection.arenaSize, -connection.arenaSize, connection.arenaSize*2, connection.arenaSize*2);

        shapeRenderer.setColor(new Color(0, 0, 1f, 0.3f));
        shapeRenderer.rect( 0 - 500, (connection.arenaSize/2) - 500,  1000, 1000);
        shapeRenderer.setColor(new Color(1, 0, 0, 0.3f));
        shapeRenderer.rect( 0 - 500, (-(connection.arenaSize/2)) - 500,  1000, 1000);

       for(Iterator<Map.Entry<Integer, Experience>> it = connection.experience.entrySet().iterator(); it.hasNext();) {
            it.next().getValue().draw(shapeRenderer);
        }

        for(Iterator<Map.Entry<Integer, Bullet>> bulletItr = connection.bullets.entrySet().iterator(); bulletItr.hasNext();) {
            bulletItr.next().getValue().draw(shapeRenderer);
        }

        for(Iterator<Map.Entry<Integer, Objective>> objectiveItr = connection.objectives.entrySet().iterator(); objectiveItr.hasNext();) {
            objectiveItr.next().getValue().draw(shapeRenderer);
        }

        shapeRenderer.end();

        main.batch.begin();

        main.batch.draw(uiBackground, translateX(25), translateY(735), 300, 338);
        if(connection.player!=null)font75.draw(main.batch, String.valueOf(connection.player.getHealth()), translateX(375), translateY(1000));
        font50.draw(main.batch, "x: " + noDec.format(connection.player.getPosition().x), translateX(75), translateY(995));
        font50.draw(main.batch, "y: " + noDec.format(connection.player.getPosition().y), translateX(75), translateY(950));
        font50.draw(main.batch, "xp: " + noDec.format(connection.player.getExperience()), translateX(75), translateY(905));
        font50.draw(main.batch, "Level " + connection.player.levels[0], translateX(75), translateY(860));
        font50.draw(main.batch, "Upgrades: " + upgradePoints, translateX(10), translateY(600));

        uiHealth.setX(translateX(0));
        uiHealth.setY(translateY(425));

        uiRegen.setX(translateX(0));
        uiRegen.setY(translateY(325));

        uiSpeed.setX(translateX(0));
        uiSpeed.setY(translateY(225));

        uiSplit.setX(translateX(0));
        uiSplit.setY(translateY(125));

        uiEat.setX(translateX(0));
        uiEat.setY(translateY(25));

        uiHealth.draw(main.batch);
        uiRegen.draw(main.batch);
        uiSpeed.draw(main.batch);
        uiSplit.draw(main.batch);
        uiEat.draw(main.batch);

        if(canUpgrade) {
            font50Green.draw(main.batch, String.valueOf(connection.player.levels[1]), translateX(250), translateY(500));
            font50Green.draw(main.batch, String.valueOf(connection.player.levels[2]), translateX(250), translateY(400));
            font50Green.draw(main.batch, String.valueOf(connection.player.levels[3]), translateX(250), translateY(300));
            font50Green.draw(main.batch, String.valueOf(connection.player.levels[4]), translateX(235), translateY(185));
            font50Green.draw(main.batch, String.valueOf(connection.player.levels[5]), translateX(200), translateY(85));
        }else{
            font50.draw(main.batch, String.valueOf(connection.player.levels[1]), translateX(250), translateY(500));
            font50.draw(main.batch, String.valueOf(connection.player.levels[2]), translateX(250), translateY(400));
            font50.draw(main.batch, String.valueOf(connection.player.levels[3]), translateX(250), translateY(300));
            font50.draw(main.batch, String.valueOf(connection.player.levels[4]), translateX(235), translateY(185));
            font50.draw(main.batch, String.valueOf(connection.player.levels[5]), translateX(200), translateY(85));
        }


        connection.player.draw(main.batch, blueTeamFont, redTeamFont, font50Red);

        for(Player p : connection.players.values()){
            p.draw(main.batch, blueTeamFont, redTeamFont, font50Red);
        }

        drawMenu();

        if (!hasEnteredName) {
            enterName.draw(main.batch);
        }



        if(connection.gameOver && connection.teamWon != null){
            if(connection.teamWon == Team.RED){
                redTeamWonFont.draw(main.batch,"RED TEAM WINS", translateX(300), translateY(900));
            }else if(connection.teamWon == Team.BLUE){
                blueTeamWonFont.draw(main.batch,"BLUE TEAM WINS", translateX(300), translateY(900));
            }
        }

        main.batch.end();
        main.camera.update();
        checkCameraBounds();
        playListener.update();


        if(!connection.getClient().isConnected()){
            main.setScreen(new Menu(main));
        }

        for(Iterator<Map.Entry<Integer, Experience>> it = connection.experience.entrySet().iterator(); it.hasNext();) {
            Experience e = it.next().getValue();
            if(connection.player.intersects(e)){
                connection.player.setExperience(connection.player.getExperience() + e.getScore());
                RemoveExp exp = new RemoveExp();
                exp.exp = e;
                connection.getClient().sendTCP(exp);
                it.remove();
                break;
            }
        }

        if(connection.player.getExperience() >= this.experienceToNextLevel){
            connection.player.setExperience(0);
            upgradePoints++;
            connection.player.levels[0]++;
            updateLevelUpExperience();
        }

        if(this.upgradePoints > 0){
            canUpgrade = true;
        }else{
            canUpgrade = false;
        }



       if(damageTimer<0.5)damageTimer+=Gdx.graphics.getDeltaTime();

        for(Iterator<Map.Entry<Integer, Bullet>> bulletItr = connection.bullets.entrySet().iterator(); bulletItr.hasNext();) {
            Bullet bullet = bulletItr.next().getValue();
            bullet.update();

            if(bullet.intersects(connection.player) && !connection.player.equals(bullet.getFromPlayer())){
                connection.player.damageDisplay.add(bullet.getPower());
                hitmarker.play();
               // connection.player.setHealth(connection.player.getHealth()-bullet.getPower());
                damageTimer = 0;
                damageObject.value = bullet.getPower();
                damageObject.exp = 0;
                damageObject.player = connection.player;
                connection.getClient().sendTCP(damageObject);
                bulletItr.remove();
                return;
            }

            for(Player p : connection.players.values()){
                if(bullet.intersects(p) && !p.equals(bullet.getFromPlayer())){
                    bulletItr.remove();
                    return;
                }
            }

            if(bullet.getBulletLife() >= 5){
                bulletItr.remove();
            }
        }

//        for(Player p : connection.players.values()){
//            if(connection.player.intersects(p) && !connection.player.getTeam().equals(p.getTeam()) && !connection.player.equals(p)){
//                if(damageTimer>=0.5){
//                    ouch.play();
//                    damageTimer = 0;
//                    damageObject.value = connection.player.getEatPower();
//                    damageObject.exp = 15;
//                    damageObject.player = p;
//                    connection.getClient().sendTCP(damageObject);
//                }
//            }
//        }

        if(connection.player.getHealth() <= 0){
            connection.getClient().close();
        }

        if (!hasEnteredName) {
            enterName.setX(translateX((main.camera.viewportWidth / 2) - (enterName.getWidth() / 2)));
            enterName.setY(translateY((main.camera.viewportHeight / 2) - (enterName.getHeight() / 2)));
        }

        if(menuOpen){
            settings.setX(translateX(main.camera.viewportWidth/2)-(settings.getWidth()/2));
            settings.setY(translateY(main.camera.viewportHeight/2)-(settings.getHeight()/2) + 150);
            disconnect.setX(translateX(main.camera.viewportWidth/2)-(disconnect.getWidth()/2));
            disconnect.setY(translateY(main.camera.viewportHeight/2)-(disconnect.getHeight()/2) - 150);
        }

    }

    public void updateLevelUpExperience(){
        switch (experienceToNextLevel){
            case 10:
                upgradePoints++;
                experienceToNextLevel = 15;
                break;
            case 15:
                experienceToNextLevel = 25;
                break;
            case 25:
                experienceToNextLevel = 50;
                break;
            default:
                experienceToNextLevel += 50;
                if(p==0)upgradePoints++;
                p = p == 0 ? 1 : 0;
                break;
        }
    }

    public void checkCameraBounds(){
        float playerX = connection.player.getPosition().x;
        float playerY = connection.player.getPosition().y;

        if(playerX < connection.arenaSize && playerX > -connection.arenaSize) {
            main.camera.position.x = playerX + (connection.player.getSize() / 2);
        }

        if(playerY < connection.arenaSize && playerY > -connection.arenaSize){
            main.camera.position.y = playerY + (connection.player.getSize() / 2);
        }
    }

    @Override
    public void resize(int width, int height) {
        main.viewport.update(width, height);
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
        enterName.dispose();
        settings.dispose();
        disconnect.dispose();
        fontGenerator.dispose();
        font75.dispose();
        font50.dispose();
        font30.dispose();
        font25.dispose();
        uiHealth.dispose();
        uiRegen.dispose();
        uiSpeed.dispose();
        uiEat.dispose();
        uiSplit.dispose();
    }
}


