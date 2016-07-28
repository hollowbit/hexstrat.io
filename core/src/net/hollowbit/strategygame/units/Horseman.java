package net.hollowbit.strategygame.units;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.Player;
import net.hollowbit.strategygame.gamecomponents.turntypes.MoveAttackTurnType;
import net.hollowbit.strategygame.world.Hex;
import net.hollowbit.strategygame.world.World;

public class Horseman extends Unit {

	public Horseman(World world, Player player, Hex hex) {
		super(world, player, hex, StrategyGame.getGame().getAssetManager().getTexture("horseman"), StrategyGame.getGame().getAssetManager().getTexture("horseman-overlay"), 6);
		this.defaultTurnType = new MoveAttackTurnType(this);
	}
	
	@Override
	public int getMoveSpeed() {
		return 2;
	}
	
	@Override
	public int getSwordsmanDamage() {
		return 3;
	}
	
	

}
