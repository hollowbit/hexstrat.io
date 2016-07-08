package net.hollowbit.strategygame.units;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.hollowbit.strategygame.gamecomponents.Player;
import net.hollowbit.strategygame.screens.GameScreen;
import net.hollowbit.strategygame.world.Hex;
import net.hollowbit.strategygame.world.World;

public abstract class Unit {
	
	public static final int INTERPOLATION_SPEED = 30;
	
	float x, y;
	int goalX, goalY;
	Hex hex;
	World world;
	Player player;
	int health;
	
	Texture image;
	Texture overlay;
	
	boolean finishedTurn;
	
	public Unit (World world, Player player, Hex hex, Texture image, Texture overlay, int health) {
		this.goalX = world.getXFromMapPositionX(hex.getX());
		this.goalY = world.getYFromMapPositionY(hex.getY(), hex.getX());
		this.x = goalX;
		this.y = goalY;
		this.hex = hex;
		this.player = player;
		this.world = world;
		this.image = image;
		this.overlay = overlay;
		this.health = health;
		
		player.addUnit(this);
	}
	
	public void turnStart () {
		damage(hex.getType().damage);
	}
	
	public void damage (int amount) {
		health += amount;
	}
	
	public void update (float deltaTime) {
		double xDif = goalX - x;
		double yDif = goalY - y;
		double angle = Math.atan2(yDif, xDif) * 180 / Math.PI;
		
		double dX = Math.cos(angle * Math.PI / 180) * INTERPOLATION_SPEED;
		double dY = Math.sin(angle * Math.PI / 180) * INTERPOLATION_SPEED;
		
		x = (float) (((x - goalX) * (x + dX - goalX) > 0 && x != goalX) ? x + dX : goalX);
		y = (float) (((x - goalY) * (y + dY - goalY) > 0 && y != goalY) ? y + dY : goalY);
	}
	
	public void render (SpriteBatch batch) {
		batch.draw(image, x, y);
		
		//Draw team color overlay
		batch.setColor(player.getColor());
		batch.draw(overlay, x, y);
		batch.setColor(Color.WHITE);
	}
	
	public float getX () {
		return x;
	}
	
	public float getY () {
		return y;
	}
	
	public void move (Hex hex) {
		//Remove unit from previous hex
		this.hex.setUnitOnHex(null);
		
		//Move it to new hex
		this.hex = hex;
		hex.setUnitOnHex(this);
		this.goalX = world.getXFromMapPositionX(hex.getX());
		this.goalY = world.getYFromMapPositionY(hex.getY(), hex.getX());
	}
	
	public Hex getHex () {
		return hex;
	}
	
	public Player getPlayer () {
		return player;
	}
	
	public boolean isMovable () {
		return getMoveSpeed() > 0;
	}
	
	public abstract void focusedOn (GameScreen gameScreen);
	public int getVisibilityRange () {return 2;}
	public boolean isTower () {return false;}
	public int getMoveSpeed () {return 1;}
	public float getHorseDamage () {return 2;}
	public float getSwordDamage () {return 2;}
	public float getSpearDamage () {return 2;}
	
}
