package net.hollowbit.strategygame.units;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.Player;
import net.hollowbit.strategygame.world.Hex;
import net.hollowbit.strategygame.world.World;
import net.hollowbit.strategygame.gamecomponents.*;
import net.hollowbit.strategygame.gamecomponents.turntypes.*;

public class Horseman extends Unit {

	public Horseman(World world, Player player, Hex hex) {
		super(world, player, hex, StrategyGame.getGame().getAssetManager().getTexture("horseman"), StrategyGame.getGame().getAssetManager().getTexture("horseman-overlay"), 6);
		this.defaultTurnType = new MoveAttackTurnType(this);
		this.turnTypes = new TurnType[]{new HealTurnType(this), new DoNothingTurnType(this)};
	}
	
	@Override
	public int getMoveSpeed() {
		return 3;
	}
	
	@Override
	public int getSwordsmanDamage() {
		return 3;
	}
	
	@Override
	public boolean isHorseman() {
		return true;
	}

}
