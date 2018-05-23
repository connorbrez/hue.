package com.connorbrezinsky.hue.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.NumberUtils;
import com.connorbrezinsky.hue.server.entities.Bullet;
import com.connorbrezinsky.hue.server.entities.Entity;
import com.connorbrezinsky.hue.server.entities.Experience;
import com.connorbrezinsky.hue.server.entities.Objective;
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
import com.connorbrezinsky.hue.server.listener.ServerListener;
import com.connorbrezinsky.hue.server.entities.Player;
import com.connorbrezinsky.hue.server.objects.DamageDisplay;
import com.connorbrezinsky.hue.server.tasks.GenerateExp;
import com.connorbrezinsky.hue.server.tasks.ObjectiveUpdateTask;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

public class Main {


    public static int PORT;
    public static int MAX_PLAYERS;
    public static int MAX_EXP;
    public static int AREA;
    public static int EXP_GEN;

    public static ArrayList<Player> players = new ArrayList<>();
    public static ArrayList<Experience> exp = new ArrayList<>();
    public static ArrayList<Bullet> bullets = new ArrayList<>();
    public static ArrayList<Objective> objectives = new ArrayList<>();

    public static Server server;
    static Kryo kryo;
    static ServerListener serverListener;
    public static Timer timer = new Timer();
    public static boolean running = false;
    public static int expID = 0;

    public static void main(String[] args){
        Log.set(Log.LEVEL_DEBUG);

        server = new Server(65536, 65536);
        kryo = server.getKryo();
        serverListener = new ServerListener(){
            protected Connection newConnection(){
                return new PlayerConnection();
            }
        };

        PORT = Config.getProperty("PORT") != null ? Integer.parseInt(Config.getProperty("PORT")) : Constants.DEFAULT_PORT;
        MAX_PLAYERS = Config.getProperty("MAX_PLAYERS") != null ? Integer.parseInt(Config.getProperty("MAX_PLAYERS")) : Constants.DEFAULT_MAX_PLAYERS;
        MAX_EXP = Config.getProperty("MAX_EXP") != null ? Integer.parseInt(Config.getProperty("MAX_EXP")) : Constants.DEFAULT_MAX_EXP;
        AREA = Config.getProperty("AREA") != null ? Integer.parseInt(Config.getProperty("AREA")) : Constants.DEFAULT_AREA;
        EXP_GEN = Config.getProperty("EXP_GEN") != null ? Integer.parseInt(Config.getProperty("EXP_GEN")) : Constants.DEFAULT_EXP_GEN;

        System.out.println("starting hue server with current settings");
        System.out.println("=========================================");
        System.out.println("MAX_PLAYERS - " + MAX_PLAYERS + " : " + Constants.DEFAULT_MAX_PLAYERS);
        System.out.println("MAX_EXP - " + MAX_EXP + " : " + Constants.DEFAULT_MAX_EXP);
        System.out.println("EXP_GEN - " + EXP_GEN + " : " + Constants.DEFAULT_EXP_GEN);
        System.out.println("PORT - " + PORT + " : " + Constants.DEFAULT_PORT);
        System.out.println("ARENA_SIZE - " + AREA + " : " + Constants.DEFAULT_AREA);

        //register classes with kryo
        register();

        //create objectives

        System.out.println("Generating Objectives...");

        Objective obj1 = new Objective(0, 0, 1);
        //obj1.setCaptureScore(49);
        Objective obj2 = new Objective(-(AREA/2), 0, 2);
        //obj2.setCaptureScore(49);
        Objective obj3 = new Objective(AREA/2, 0 , 3);
        //obj3.setCaptureScore(49);

        objectives.add(obj1);
        objectives.add(obj2);
        objectives.add(obj3);

        System.out.println("Objectives Generated!");


        //initialize experience
        for(int i = 0; i < MAX_EXP; i++){
            exp.add(new Experience(expID, AREA));
            expID++;
        }

        //start generating exp
        timer.schedule(new GenerateExp(), 0, EXP_GEN);
        timer.schedule(new ObjectiveUpdateTask(), 0, 1000);

        server.addListener(serverListener);

        try {
            server.bind(PORT, PORT+1);
            server.start();
            System.out.println("PORT HAS BEEN BOUND");
            running = true;
        }catch (IOException e){
            e.printStackTrace();
            System.exit(0);
        }

        if(running){
            Scanner input = new Scanner(System.in);
            String i = "";
            while(running){
                i = input.nextLine();
                if(i.equalsIgnoreCase("stop") || i.equals("quit")){
                    server.close();
                    server.removeListener(serverListener);
                    System.exit(0);
                }
            }
        }

    }

    public static void register(){
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
    }
}
