package net.hollowbit.strategygame.units;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.Player;
import net.hollowbit.strategygame.world.Hex;
import net.hollowbit.strategygame.world.World;

public class Farm extends Unit {
	
	private static final int HEALTH = 4;
	
	public Farm (World world, Player player, Hex hex) {
		super(world, player, hex, StrategyGame.getGame().getAssetManager().getTexture("farm"), StrategyGame.getGame().getAssetManager().getTexture("farm-overlay"), HEALTH);
	}
	
	@Override
	public float getProduction () {
		return hex.getType().production;
	}
	
	@Override
	public boolean isTower () {
		return true;
	}
	
	@Override
	public boolean isFinishedTurn() {
		return true;
	}

}
