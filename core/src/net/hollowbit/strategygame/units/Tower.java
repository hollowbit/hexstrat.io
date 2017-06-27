package net.hollowbit.strategygame.units;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.Player;
import net.hollowbit.strategygame.world.Hex;
import net.hollowbit.strategygame.world.World;
import net.hollowbit.strategygame.gamecomponents.*;
import net.hollowbit.strategygame.gamecomponents.turntypes.*;

public class Tower extends Unit {
	
	private static final int HEALTH = 8;
	
	public Tower(World world, Player player, Hex hex) {
		super(world, player, hex, StrategyGame.getGame().getAssetManager().getTexture("tower"), StrategyGame.getGame().getAssetManager().getTexture("tower-overlay"), HEALTH);
		this.defaultTurnType = new AttackTurnType(this);
		this.turnTypes = new TurnType[]{new DoNothingTurnType(this)};
	}
	
	@Override
	public int getAttackRange() {
		return 2;
	}
	
	@Override
	public int getVisibilityRange() {
		return 3;
	}
	
	@Override
	public int getMoveSpeed() {
		return 0;
	}
	
	@Override
	public boolean isTower () {
		return true;
	}

}
