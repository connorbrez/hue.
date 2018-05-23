package com.connorbrezinsky.hue.server;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.NumberUtils;
import com.connorbrezinsky.hue.Team;
import com.connorbrezinsky.hue.entities.Bullet;
import com.connorbrezinsky.hue.entities.Entity;
import com.connorbrezinsky.hue.entities.Experience;
import com.connorbrezinsky.hue.entities.Objective;
import com.connorbrezinsky.hue.entities.Player;
import com.connorbrezinsky.hue.server.events.Winner;
import com.connorbrezinsky.hue.server.events.add.AddBullet;
import com.connorbrezinsky.hue.server.events.add.AddExp;
import com.connorbrezinsky.hue.server.events.AllPlayers;
import com.connorbrezinsky.hue.server.events.Damage;
import com.connorbrezinsky.hue.server.events.Join;
import com.connorbrezinsky.hue.server.events.add.AddObjective;
import com.connorbrezinsky.hue.server.events.remove.RemoveBullet;
import com.connorbrezinsky.hue.server.events.remove.RemoveExp;
import com.connorbrezinsky.hue.server.events.remove.RemoveObjective;
import com.connorbrezinsky.hue.server.events.remove.RemovePlayer;
import com.connorbrezinsky.hue.server.events.update.UpdateArena;
import com.connorbrezinsky.hue.server.events.update.UpdateBullets;
import com.connorbrezinsky.hue.server.events.update.UpdateExp;
import com.connorbrezinsky.hue.server.events.update.UpdateObjective;
import com.connorbrezinsky.hue.server.events.update.UpdateObjectives;
import com.connorbrezinsky.hue.server.events.update.UpdatePlayer;
import com.connorbrezinsky.hue.ui.DamageDisplay;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Connection;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by connorbrezinsky on 2017-05-18.
 */

public class ClientConnection {

    Client client;
    Kryo kryo;
    final int TIMEOUT = 5000;
    public Player player;
    public ConcurrentHashMap<Integer, Player> players;
    public ConcurrentHashMap<Integer, Experience> experience;
    public ConcurrentHashMap<Integer, Bullet> bullets;
    public ConcurrentHashMap<Integer, Objective> objectives;
    public float arenaSize = 0;
    public boolean gameOver = false;
    public Team teamWon;

    public ClientConnection(String ip, int port){
        player = new Player();
        client = new Client(65536, 65536);
        new Thread(client).start();

        kryo = client.getKryo();
        //kryo.setRegistrationRequired(false);

        kryo.register(PlayerConnection.class);
        kryo.register(Player.class);
        kryo.register(UpdatePlayer.class);
        kryo.register(AllPlayers.class);
        kryo.register(Join.class);
        kryo.register(RemovePlayer.class);
        kryo.register(Experience.class);
        kryo.register(UpdateExp.class);
        kryo.register(RemoveExp.class);
        kryo.register(AddExp.class);
        kryo.register(HashSet.class);
        kryo.register(ArrayList.class);
        kryo.register(int[].class);
        kryo.register(UpdateArena.class);
        kryo.register(Bullet.class);
        kryo.register(AddBullet.class);
        kryo.register(UpdateBullets.class);
        kryo.register(Damage.class);
        kryo.register(RemoveBullet.class);
        kryo.register(Random.class);
        kryo.register(Vector2.class);
        kryo.register(NumberUtils.class);
        kryo.register(DamageDisplay.class);
        kryo.register(Team.class);
        kryo.register(Objective.class);
        kryo.register(Circle.class);
        kryo.register(UpdateObjectives.class);
        kryo.register(UpdateObjective.class);
        kryo.register(AddObjective.class);
        kryo.register(RemoveObjective.class);
        kryo.register(Entity.class);
        kryo.register(Winner.class);

        kryo.register(Array.class, new Serializer<Array>() {
            {
                setAcceptsNull(true);
            }

            private Class genericType;

            public void setGenerics (Kryo kryo, Class[] generics) {
                if (generics != null && kryo.isFinal(generics[0])) genericType = generics[0];
                else genericType = null;
            }

            public void write (Kryo kryo, Output output, Array array) {
                int length = array.size;
                output.writeInt(length, true);
                if (length == 0) {
                    genericType = null;
                    return;
                }
                if (genericType != null) {
                    Serializer serializer = kryo.getSerializer(genericType);
                    genericType = null;
                    for (Object element : array)
                        kryo.writeObjectOrNull(output, element, serializer);
                } else {
                    for (Object element : array)
                        kryo.writeClassAndObject(output, element);
                }
            }

            public Array read (Kryo kryo, Input input, Class<Array> type) {
                Array array = new Array();
                kryo.reference(array);
                int length = input.readInt(true);
                array.ensureCapacity(length);
                if (genericType != null) {
                    Class elementClass = genericType;
                    Serializer serializer = kryo.getSerializer(genericType);
                    genericType = null;
                    for (int i = 0; i < length; i++)
                        array.add(kryo.readObjectOrNull(input, elementClass, serializer));
                } else {
                    for (int i = 0; i < length; i++)
                        array.add(kryo.readClassAndObject(input));
                }
                return array;
            }
        });

        kryo.register(IntArray.class, new Serializer<IntArray>() {
            {
                setAcceptsNull(true);
            }

            public void write (Kryo kryo, Output output, IntArray array) {
                int length = array.size;
                output.writeInt(length, true);
                if (length == 0) return;
                for (int i = 0, n = array.size; i < n; i++)
                    output.writeInt(array.get(i), true);
            }

            public IntArray read (Kryo kryo, Input input, Class<IntArray> type) {
                IntArray array = new IntArray();
                kryo.reference(array);
                int length = input.readInt(true);
                array.ensureCapacity(length);
                for (int i = 0; i < length; i++)
                    array.add(input.readInt(true));
                return array;
            }
        });

        kryo.register(FloatArray.class, new Serializer<FloatArray>() {
            {
                setAcceptsNull(true);
            }

            public void write (Kryo kryo, Output output, FloatArray array) {
                int length = array.size;
                output.writeInt(length, true);
                if (length == 0) return;
                for (int i = 0, n = array.size; i < n; i++)
                    output.writeFloat(array.get(i));
            }

            public FloatArray read (Kryo kryo, Input input, Class<FloatArray> type) {
                FloatArray array = new FloatArray();
                kryo.reference(array);
                int length = input.readInt(true);
                array.ensureCapacity(length);
                for (int i = 0; i < length; i++)
                    array.add(input.readFloat());
                return array;
            }
        });

        kryo.register(Color.class, new Serializer<Color>() {
            public Color read (Kryo kryo, Input input, Class<Color> type) {
                Color color = new Color();
                Color.rgba8888ToColor(color, input.readInt());
                return color;
            }

            public void write (Kryo kryo, Output output, Color color) {
                output.writeInt(Color.rgba8888(color));
            }
        });

        players = new ConcurrentHashMap<>();
        experience = new ConcurrentHashMap<>();
        bullets = new ConcurrentHashMap<>();
        objectives = new ConcurrentHashMap<>();

        client.addListener(new Listener.ThreadedListener(new Listener(){

            public void connected (Connection connection) {
                System.out.println("Connected with id " + connection.getID());
            }

            public void received (Connection connection, Object object) {

                if(object instanceof Damage){
                    Damage damageObject = (Damage)object;
                    if(damageObject.player.equals(player)){
                        player.setHealth(player.getHealth()-damageObject.value);
                        player.setExperience(player.getExperience()-damageObject.exp);
                    }

                    UpdatePlayer updatePlayer = new UpdatePlayer();
                    updatePlayer.player = player;
                    getClient().sendTCP(updatePlayer);
                }

                if(object instanceof AddBullet){
                    Bullet bullet = ((AddBullet)object).bullet;
                    bullets.putIfAbsent(bullet.getId(), bullet);
                }

                if(object instanceof RemoveBullet){
                    Bullet bullet = ((RemoveBullet) object).bullet;
                    for(Iterator<Map.Entry<Integer, Bullet>> it = bullets.entrySet().iterator(); it.hasNext(); ) {
                        Map.Entry<Integer, Bullet> entry = it.next();
                        if(entry.getKey() == bullet.getId()) {
                            it.remove();
                        }
                    }
                }

                if(object instanceof UpdateArena){
                    arenaSize = ((UpdateArena) object).size;
                    ArrayList<Objective> serverObjectives = ((UpdateArena)object).objectives;

                    objectives.clear();
                    for( Objective obj : serverObjectives){
                        objectives.putIfAbsent(obj.getId(), obj);
                    }
                }

                if(object instanceof UpdateObjectives){
                    ArrayList<Objective> serverObjectives = ((UpdateObjectives)object).objectives;

                    objectives.clear();
                    for( Objective obj : serverObjectives){
                        objectives.putIfAbsent(obj.getId(), obj);
                    }
                }

                if(object instanceof UpdateObjective){
                    Objective objective = ((UpdateObjective)object).objective;
                    for(Iterator<Map.Entry<Integer, Objective>> it = objectives.entrySet().iterator(); it.hasNext(); ) {
                        Objective o = it.next().getValue();
                        if(o.getId() == objective.getId()){
                            it.remove();
                            objectives.putIfAbsent(objective.getId(), objective);
                            return;
                        }
                    }
                }

                if(object instanceof Join){
                    Player p = ((Join) object).player;

                    if(!p.equals(player)){
                        System.out.println(p.getName() + " has joined");
                        players.put(connection.getID(), p);
                    }else{
                        player = p;
                        System.out.println("Successfully joined");
                    }

                }

                if(object instanceof AllPlayers){
                    players.clear();
                    ArrayList<Player> ps = ((AllPlayers) object).players;
                    for(Player p : ps){
                        System.out.println("adding " + p.getName() + " as " + p.getId());
                        players.putIfAbsent(p.getId(), p);
                    }
                }

                if(object instanceof Winner){
                    gameOver = true;
                    teamWon = ((Winner)object).team;
                }

                if(object instanceof UpdatePlayer){
                    Player player = ((UpdatePlayer) object).player;
                    for (Player p : players.values()){
                        if(player.getId() == p.getId()){
                            p.levels = player.levels;
                            p.setHealth(player.getHealth());
                            p.setPosition(player.getPosition());
                            p.setRotation(player.getRotation());
                            p.damageDisplay = player.damageDisplay;
                            p.setSpeed(player.getSpeed());
                            p.setMaxHealth(player.getMaxHealth());
                            p.setExperience(player.getExperience());
                            p.setRegenSpeed(player.getRegenSpeed());
                            p.setEatPower(player.getEatPower());
                            p.setSplitPower(player.getSplitPower());
                            p.setTeam(player.getTeam());
                        }
                    }
                }

                if(object instanceof RemovePlayer){
                    Player p = ((RemovePlayer) object).player;
                    for(Iterator<Map.Entry<Integer, Player>> it = players.entrySet().iterator(); it.hasNext(); ) {
                        Map.Entry<Integer, Player> entry = it.next();
                        if(entry.getValue().equals(p)) {
                            it.remove();
                           return;
                        }
                    }
                }

                if(object instanceof UpdateExp){
                    experience.clear();
                    ArrayList<Experience> e = ((UpdateExp) object).exp;
                    for(Experience exp : e){
                        experience.putIfAbsent(exp.getId(), exp);
                    }
                }

                if(object instanceof UpdateBullets){
                    bullets.clear();
                    ArrayList<Bullet> b = ((UpdateBullets) object).bullets;
                    for(Bullet bullet : b){
                        bullets.putIfAbsent(bullet.getId(), bullet);
                    }
                }

                if(object instanceof AddExp){
                    Experience exp = ((AddExp) object).exp;
                    experience.putIfAbsent(exp.getId(), exp);
                }

                if(object instanceof RemoveExp){
                    Experience exp = ((RemoveExp) object).exp;
                    for(Iterator<Map.Entry<Integer, Experience>> it = experience.entrySet().iterator(); it.hasNext(); ) {
                        Map.Entry<Integer, Experience> entry = it.next();
                        if(entry.getKey() == exp.getId()) {
                            it.remove();
                        }
                    }
                }

            }

            public void disconnected(Connection c){
                System.out.println("disconnected");
            }
        }));

            try {
                client.connect(TIMEOUT, ip, port, port+1);
            }catch (IOException e){
                e.printStackTrace();
            }
    }


    public Client getClient(){
        return this.client;
    }

    public Kryo getKryo(){
        return this.kryo;
    }



}

