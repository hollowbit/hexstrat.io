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
import net.hollowbit.strategygame.*;
import net.hollowbit.strategygame.tools.*;

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
	TurnType turnTypeSet;
	
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
	
	public void moveMade () {
		if (defaultTurnType != null)
			defaultTurnType.moveMade();

		if (turnTypes != null) {
			for (TurnType turnType : turnTypes) 
				turnType.moveMade();
		}
	}
	
	public void disposeTurnTypes (GameScreen gameScreen) {
		if (defaultTurnType != null)
			defaultTurnType.dispose(gameScreen);
		
		if (turnTypes != null) {
			for (TurnType turnType : turnTypes) 
				turnType.dispose(gameScreen);
		}
	}
	
	public void setTurnType (TurnType turnType) {
		turnTypeSet = turnType;
	}
	
	public TurnType getTurnTypeSet () {
		return turnTypeSet;
	}
	
	public void turnStart (GameScreen gameScreen) {
		finishedTurn = false;
		turnTypeSet = null;
		
		if (hex.getType().damage != 0)
			damage(hex.getType().damage, null, gameScreen);//Apply damage from hex
		
		if (defaultTurnType != null) {
			defaultTurnType.turnStart();
			gameScreen.setSelectedTurnType(defaultTurnType);
		}

		if (turnTypes != null) {
			for (TurnType turnType : turnTypes)
				turnType.turnStart();
		}
		
		if (getProduction() != 0)
			gameScreen.getHexMessageManager().addHexMessage(new HexMessage("+" + getProduction() + "F", hex, world, Color.YELLOW), false);
	}
	
	public boolean damage (int amount, Player damager, GameScreen gameScreen) {
		//if health is full, dont add any more
		if (health == maxHealth && amount > 0)
			return false;
			
		health += amount;
		if (health > maxHealth)
			health = maxHealth;
		
		if (amount > 0)
			gameScreen.getHexMessageManager().addHexMessage(new HexMessage("+" + amount, hex, world, Color.GREEN), true);
		else if (amount < 0)
			gameScreen.getHexMessageManager().addHexMessage(new HexMessage("" + amount, hex, world, Color.RED), true);
		else
			gameScreen.getHexMessageManager().addHexMessage(new HexMessage("SHIELD", hex, world, Color.ORANGE), true);
		
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
		
		//Draw health bar
		drawHealthBar(batch);
	}
	
	private void drawHealthBar (SpriteBatch batch) {
		if (maxHealth > 6) {
			int healthBarLength = Hex.SIZE  - 4;
			float healthMultiplier = ((float) health / maxHealth);
			batch.setColor(Color.BLACK);
			batch.draw(StrategyGame.getGame().getAssetManager().getTexture("blank"), x + 1, y + image.getHeight() + 4, healthBarLength + 2, 6);
			batch.setColor(Color.GREEN);
			batch.draw(StrategyGame.getGame().getAssetManager().getTexture("blank"), x + 2, y + image.getHeight() + 5, healthBarLength * healthMultiplier, 4);
			batch.setColor(1, 1, 1, 1);
		} else {
			int padding = 18;
			float startX = x + Hex.SIZE / 2 - (maxHealth * padding / 4);
			int heartsToDraw = health / 2;
			boolean drawHalfHeart = health % 2 == 1;
			for (int i = 0; i < heartsToDraw; i++)
				batch.draw(StrategyGame.getGame().getAssetManager().getTextureMap("hearts")[0][0], startX + i * padding, y + image.getHeight() + 4);
			
			if (drawHalfHeart)
				batch.draw(StrategyGame.getGame().getAssetManager().getTextureMap("hearts")[0][1], startX + heartsToDraw * padding, y + image.getHeight() + 4);
		}
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
	
	/**
	 * Converts this unit to another team
	 * @param newPlayer
	 */
	public void convert (Player newPlayer) {
		player.removeUnit(this);
		newPlayer.addUnit(this);
		this.player = newPlayer;
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
	
	public boolean isDead() {
		return health <= 0;
	}
	
	public float getProduction () {return 0;}
	public int getVisibilityRange () {return 2;}
	
	/**
	 * Returns whether this unit is a building (village, tower, farm, etc)
	 * If true, it is expected/assumed that this unit doesn't move
	 * @return
	 */
	public boolean isTower () {return false;}
	public int getMoveSpeed () {return 2;}
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
