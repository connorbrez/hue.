package com.connorbrezinsky.hue.server.listener;

import com.badlogic.gdx.math.Vector2;
import com.connorbrezinsky.hue.server.Main;
import com.connorbrezinsky.hue.server.Team;
import com.connorbrezinsky.hue.server.entities.Bullet;
import com.connorbrezinsky.hue.server.entities.Experience;
import com.connorbrezinsky.hue.server.entities.Objective;
import com.connorbrezinsky.hue.server.entities.Player;
import com.connorbrezinsky.hue.server.events.add.AddBullet;
import com.connorbrezinsky.hue.server.events.AllPlayers;
import com.connorbrezinsky.hue.server.events.Damage;
import com.connorbrezinsky.hue.server.events.Join;
import com.connorbrezinsky.hue.server.events.remove.RemoveBullet;
import com.connorbrezinsky.hue.server.events.remove.RemoveExp;
import com.connorbrezinsky.hue.server.events.remove.RemovePlayer;
import com.connorbrezinsky.hue.server.events.update.UpdateArena;
import com.connorbrezinsky.hue.server.events.update.UpdateExp;
import com.connorbrezinsky.hue.server.events.update.UpdateObjective;
import com.connorbrezinsky.hue.server.events.update.UpdateObjectives;
import com.connorbrezinsky.hue.server.events.update.UpdatePlayer;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.util.Iterator;

public class ServerListener extends Listener {

	Main main;

	int blueCount = 0, redCount = 0;

	public ServerListener(){



	}

	public void connected(Connection c){
        UpdateArena arena = new UpdateArena();
        arena.size = Main.AREA;
        arena.objectives = Main.objectives;

        UpdateExp exp = new UpdateExp();
        exp.exp = Main.exp;

        c.sendTCP(arena);
        c.sendTCP(exp);
    }


	@Override
	public void received(Connection c, Object o){

        if(o instanceof UpdatePlayer){
            Player player = ((UpdatePlayer) o).player;
            Main.players.forEach((p) -> {
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
                    Main.server.sendToAllTCP(o);
                }
            });

        }

        if(o instanceof UpdateObjective){
            Main.server.sendToAllTCP(o);
            Objective objective = ((UpdateObjective)o).objective;
            Main.objectives.set(objective.getId(), objective);
        }

        if(o instanceof Join){
            Player player = ((Join) o).player;

            if(redCount==blueCount || blueCount>redCount){
                player.setTeam(Team.RED);
                redCount++;
            }else if(redCount>blueCount){
                player.setTeam(Team.BLUE);
                blueCount++;
            }

            player.setPosition( (player.getTeam() == Team.BLUE) ? new Vector2(0, Main.AREA/2) : new Vector2(0, -(Main.AREA/2)));
            player.setRotation( (player.getTeam() == Team.BLUE) ? 180f : 0f);

            System.out.println(player.getName() + " has joined");

            if(Main.players.size()>0){
                AllPlayers allPlayers = new AllPlayers();
                allPlayers.players = Main.players;
                c.sendTCP(allPlayers);
            }

            Main.players.add(player);

            Join join = new Join();
            join.player = player;



            Main.server.sendToAllTCP(join);
        }
        
        if(o instanceof AddBullet){
            Main.server.sendToAllTCP(o);
            Bullet b = ((AddBullet)o).bullet;
            main.bullets.add(b);
        }

        if(o instanceof RemoveBullet){
            Bullet bullet = ((RemoveBullet) o).bullet;
            for (Iterator<Bullet> i = Main.bullets.iterator(); i.hasNext();) {
                Bullet element = i.next();
                if (bullet.getId() == element.getId()) {
                    RemoveBullet removeBullet = new RemoveBullet();
                    removeBullet.bullet = element;
                    Main.server.sendToAllTCP(removeBullet);
                    i.remove();
                }
            }
        }

        if(o instanceof RemoveExp){
            Experience orb = ((RemoveExp) o).exp;
            for (Iterator<Experience> i = Main.exp.iterator(); i.hasNext();) {
                Experience element = i.next();
                if (orb.getId() == element.getId()) {
                    RemoveExp removeExp = new RemoveExp();
                    removeExp.exp = element;
                    Main.server.sendToAllTCP(removeExp);
                    i.remove();
                }
            }
        }

        if(o instanceof Damage){
            Main.server.sendToAllTCP(o);
        }

	}

	public void disconnected (Connection c) {
        System.out.println("Player disconnected");
        for (Iterator<Player> i = Main.players.iterator(); i.hasNext();) {
            Player element = i.next();
            if (c.getID() == element.getId()) {
                RemovePlayer removePlayer = new RemovePlayer();
                removePlayer.player = element;
                if(removePlayer.player.getTeam() == Team.RED) {
                    redCount--;
                }else if(removePlayer.player.getTeam() == Team.BLUE){
                    blueCount--;
                }
                Main.server.sendToAllTCP(removePlayer);
                i.remove();
            }
        }
	}

	private boolean isValid (String value) {
		if (value == null) return false;
		value = value.trim();
		return value.length() != 0;
	}
	
}
