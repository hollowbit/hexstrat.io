package net.hollowbit.strategygame.units;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.Player;
import net.hollowbit.strategygame.gamecomponents.turntypes.BuildTurnType;
import net.hollowbit.strategygame.world.Hex;
import net.hollowbit.strategygame.world.World;

public class Village extends Unit {
	
	private static final int MAX_HEALTH = 16;
	
	private int buildProdLeft = 1;
	private BuildType unitBeingBuilt;
	
	private boolean firstUnitBuilt = false;//If false, make all unit purchases only cost 1
	
	public Village(World world, Player player, Hex hex) {
		super(world, player, hex, StrategyGame.getGame().getAssetManager().getTexture("village"), StrategyGame.getGame().getAssetManager().getTexture("village-overlay"), MAX_HEALTH);
		this.defaultTurnType = new BuildTurnType(this);
		this.unitBeingBuilt = null;
	}
	
	@Override
	public void turnStart () {
		super.turnStart();
		//Check if done building
		buildProdLeft -= player.getProduction();
		
		//If done building and a unit is being built, spawn unit
		if (isDoneBuilding() && unitBeingBuilt != null) {
			//Find a suitable place to spawn unit
			boolean spotFound = false;
			for (Hex hex : hex.getSurroundingHexesInRange(1)) {
				if (hex.getUnitOnHex() == null) {
					spawnUnit(hex);
					spotFound = true;
					break;
				}
			}
			
			if (spotFound) {
				firstUnitBuilt = true;
			} else {//Give error message to player saying no space is available
				
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
	
	public int getBuildTurnsLeft () {
		return buildProdLeft / player.getProduction() + (buildProdLeft % player.getProduction() > 0 ? 1:0);
	}
	
	public boolean isFirstBuild () {
		return !firstUnitBuilt;
	}
	
	@Override
	public int getProduction() {
		return 1;
	}
	
	public enum BuildType {
		HORSEMAN("Horseman", "Unit that can move 2 hexes per turn. Also effective against swordsman.", 5);
		
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
