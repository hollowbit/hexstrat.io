package net.hollowbit.strategygame.units;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.Player;
import net.hollowbit.strategygame.gamecomponents.TurnType;
import net.hollowbit.strategygame.gamecomponents.turntypes.AttackTurnType;
import net.hollowbit.strategygame.gamecomponents.turntypes.BuildTurnType;
import net.hollowbit.strategygame.gamecomponents.turntypes.ChooseSpawnTurnType;
import net.hollowbit.strategygame.gamecomponents.turntypes.DoNothingTurnType;
import net.hollowbit.strategygame.screens.GameOverScreen;
import net.hollowbit.strategygame.screens.GameScreen;
import net.hollowbit.strategygame.world.Hex;
import net.hollowbit.strategygame.world.World;

public class Village extends Unit {
	
	private static final int MAX_HEALTH = 16;
	
	private int buildProdLeft = 1;
	private BuildType unitBeingBuilt;
	private Hex spawn;
	private AttackTurnType attackTurnType;
	
	private boolean firstUnitBuilt = false;//If false, make all unit purchases only cost 1
	
	public Village(World world, Player player, Hex hex) {
		super(world, player, hex, StrategyGame.getGame().getAssetManager().getTexture("village"), StrategyGame.getGame().getAssetManager().getTexture("village-overlay"), MAX_HEALTH);
		this.defaultTurnType = new BuildTurnType(this);
		this.attackTurnType = new AttackTurnType(this);
		this.turnTypes = new TurnType[]{attackTurnType, new ChooseSpawnTurnType(this), new DoNothingTurnType(this)};
		this.unitBeingBuilt = null;
		this.spawn = null;
	}
	
	@Override
	public void turnStart (GameScreen gameScreen) {
		super.turnStart(gameScreen);
		
		//Check if done building
		buildProdLeft -= player.getProduction();
		
		//If done building and a unit is being built, spawn unit
		if (isDoneBuilding() && unitBeingBuilt != null) {
			//Find a suitable place to spawn unit
			boolean spotFound = false;
			if (spawn != null && spawn.getUnitOnHex() == null) {
				spawnUnit(spawn);
				spotFound = true;
			} else {
				for (Hex hex : hex.getSurroundingHexesInRange(1)) {
					if (hex.getUnitOnHex() == null) {
						spawnUnit(hex);
						spotFound = true;
						break;
					}
				}
			}
			
			if (spotFound) {
				firstUnitBuilt = true;
			} else {//Give error message to player saying no space is available
				gameScreen.showErrorWindow("Could not spawn unit from village. All surrounding hexes are taken. Trying again next turn. Please make room.");
			}
		}
		
		//If village is building a unit, flag it as turn finished
		if (!isDoneBuilding())
			finishedTurn = true;
	}
	
	//Add new units to be built here!
	private void spawnUnit (Hex hex) {
		switch(unitBeingBuilt) {
		case HORSEMAN:
			world.addUnit(new Horseman(world, player, hex));
			break;
		case WORKER:
			world.addUnit(new Worker(world, player, hex));
			break;
		case ARCHER:
			world.addUnit(new Archer(world, player, hex));
			break;
		case SWORDSMAN:
			world.addUnit(new Swordsman(world, player, hex));
			break;
		case PHALANX:
			world.addUnit(new Phalanx(world, player, hex));
			break;
		case CATAPULT:
			world.addUnit(new Catapult(world, player, hex));
			break;
		case PRIEST:
			world.addUnit(new Priest(world, player, hex));
			break;
		case BEAST:
			world.addUnit(new Beast(world, player, hex));
			break;
		}
	}
	
	public void build (BuildType buildType) {
		unitBeingBuilt = buildType;
		if (isFirstBuild())
			buildProdLeft = 1;
		else
			buildProdLeft = buildType.prodNeeded;
	}
	
	@Override
	public boolean damage (int amount, Player damager, GameScreen gameScreen) {
		boolean dead = super.damage(amount, damager, gameScreen);
		
		//Since this is the village, if it is dead, then the game is over
		if (dead) {
			StrategyGame.getGame().getScreenManager().setScreen(new GameOverScreen(damager));
		}
		return dead;
	}
	
	public BuildType getUnitBeingBuilt() {
		return unitBeingBuilt;
	}
	
	@Override
	public int getAttackRange() {
		return 2;
	}
	
	@Override
	public boolean isTower () {
		return true;
	}
	
	@Override
	public int getVisibilityRange () {
		return 3;
	}
	
	@Override
	public int getMoveSpeed() {
		return 0;
	}
	
	public boolean isDoneBuilding () {
		return buildProdLeft <= 0;
	}
	
	public float getBuildTurnsLeft () {
		return buildProdLeft / player.getProduction() + (buildProdLeft % player.getProduction() > 0 ? 1:0);
	}
	
	public boolean isFirstBuild () {
		return !firstUnitBuilt;
	}
	
	public void setSpawn (Hex spawn) {
		this.spawn = spawn;
	}
	
	@Override
	public float getProduction() {
		return 1;
	}
	
	public enum BuildType {
		HORSEMAN("Horseman", "Unit that can move up to 3 hexes per turn. Effective against swordsman.", 5),
		WORKER("Worker", "Unit that can build towers and farms. Any attack kills it instantly.", 4),
		ARCHER("Archer", "Weaker unit that can attack at a range of 2 hexes, dealing 2 damage.", 4),
		SWORDSMAN("Swordsman", "Unit that has a special \"shield\" mode that protects it from attacks. Effective against phalanx.", 5),
		PHALANX("Phalanx", "Unit that has a \"berzerker\" mode that instantly kills units. Effective against horseman.", 5),
		CATAPULT("Catapult", "Low health unit that deals 4 damage to farms, villages and towers.", 4),
		PRIEST("Priest", "Support unit that can heal other units. Can also convert enemy units to your faction.", 6),
		BEAST("Beast", "Unit with lots of health and deals 4 damage to everything. Can also blitz every 3 turns. Expensive though and is slow.", 12);
		
		public static final BuildType[] FIRST_BUILD_VALUES = {HORSEMAN, WORKER, ARCHER, SWORDSMAN, PHALANX};
		
		public String name;
		public String desc;
		public int prodNeeded;
		
		private BuildType (String name, String desc, int prodNeeded) {
			this.name = name;
			this.desc = desc;
			this.prodNeeded = prodNeeded;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}

}
