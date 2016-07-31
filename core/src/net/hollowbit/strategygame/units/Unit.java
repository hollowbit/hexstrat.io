package net.hollowbit.strategygame.units;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import net.hollowbit.strategygame.gamecomponents.Player;
import net.hollowbit.strategygame.gamecomponents.TurnType;
import net.hollowbit.strategygame.screens.GameScreen;
import net.hollowbit.strategygame.world.Hex;
import net.hollowbit.strategygame.world.World;

public abstract class Unit {
	
	public static final int INTERPOLATION_SPEED = 8;
	
	float x, y;
	int goalX, goalY;
	Hex hex;
	World world;
	Player player;
	int health;
	int maxHealth;
	TurnType defaultTurnType;
	TurnType[] turnTypes;
	
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
		this.maxHealth = health;
		
		hex.setUnitOnHex(this);
		
		player.addUnit(this);
	}
	
	public void turnStart (GameScreen gameScreen) {
		finishedTurn = false;
		
		damage(hex.getType().damage, null);//Apply damage from hex
		
		if (defaultTurnType != null)
			defaultTurnType.turnStart();

		if (turnTypes != null) {
			for (TurnType turnType : turnTypes)
				turnType.turnStart();
		}
	}
	
	public boolean damage (int amount, Player damager) {
		health += amount;
		if (health <= 0) {
			player.removeUnit(this);
			world.removeUnit(this);
			return true;
		} else 
			return false;
	}
	
	public void update (float deltaTime) {
		Vector2 pos = new Vector2(x, y);
		pos.lerp(new Vector2(goalX, goalY), deltaTime * INTERPOLATION_SPEED);
		x = pos.x;
		y = pos.y;
		/*double xDif = goalX - x;
		double yDif = goalY - y;
		double angle = Math.atan2(yDif, xDif) * 180 / Math.PI;
		
		double dX = Math.cos(angle * Math.PI / 180) * INTERPOLATION_SPEED * deltaTime;
		double dY = Math.sin(angle * Math.PI / 180) * INTERPOLATION_SPEED * deltaTime;
		
		x = (float) (((x - goalX) * (x + dX - goalX) > 0 && x != goalX) ? x + dX : goalX);
		y = (float) (((x - goalY) * (y + dY - goalY) > 0 && y != goalY) ? y + dY : goalY);*/
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
	
	public int getHealth () {
		return health;
	}
	
	public int getMaxHealth () {
		return maxHealth;
	}
	
	public Player getPlayer () {
		return player;
	}
	
	public TurnType getDefaultTurnType () {
		return defaultTurnType;
	}
	
	public TurnType[] getTurnTypes () {
		return turnTypes;
	}
	
	public void focusedOn (GameScreen gameScreen) {
		if (defaultTurnType != null)
			defaultTurnType.initiate(gameScreen);
	}
	
	public void setFinishedTurn (boolean finishedTurn) {
		this.finishedTurn = finishedTurn;
	}
	
	public boolean isFinishedTurn () {
		return finishedTurn;
	}
	
	public int getProduction () {return 0;}
	public int getVisibilityRange () {return 2;}
	public boolean isTower () {return false;}//If true, it is expected/assumed that this unit doesn't move
	public int getMoveSpeed () {return 1;}
	public int getAttackRange () {return 1;}
	public int getHorsemanDamage () {return 2;}
	public int getSwordsmanDamage () {return 2;}
	public int getSpearmanDamage () {return 2;}
	public int getNormalDamage () {return 2;}
	public int getTowerDamage () {return 2;}
	public boolean isHorseman () {return false;}
	public boolean isSwordsman () {return false;}
	public boolean isSpearman () {return false;}
	
}
