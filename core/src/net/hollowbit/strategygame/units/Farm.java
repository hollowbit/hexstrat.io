package net.hollowbit.strategygame.units;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.Player;
import net.hollowbit.strategygame.gamecomponents.TurnType;
import net.hollowbit.strategygame.gamecomponents.turntypes.DoNothingTurnType;
import net.hollowbit.strategygame.gamecomponents.turntypes.SelfDestructTurnType;
import net.hollowbit.strategygame.world.Hex;
import net.hollowbit.strategygame.world.World;

public class Farm extends Unit {
	
	private static final int HEALTH = 4;
	
	public Farm (World world, Player player, Hex hex) {
		super(world, player, hex, StrategyGame.getGame().getAssetManager().getTexture("farm"), StrategyGame.getGame().getAssetManager().getTexture("farm-overlay"), HEALTH);
		this.defaultTurnType = new DoNothingTurnType(this);
		this.turnTypes = new TurnType[]{new SelfDestructTurnType(this)};
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
