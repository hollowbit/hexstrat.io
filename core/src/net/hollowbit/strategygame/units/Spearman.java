package net.hollowbit.strategygame.units;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.Player;
import net.hollowbit.strategygame.gamecomponents.turntypes.MoveAttackTurnType;
import net.hollowbit.strategygame.world.Hex;
import net.hollowbit.strategygame.world.World;

public class Spearman extends Unit {
	
	private static final int HEALTH = 6;
	
	public Spearman (World world, Player player, Hex hex) {
		super(world, player, hex, StrategyGame.getGame().getAssetManager().getTexture("spearman"), StrategyGame.getGame().getAssetManager().getTexture("spearman-overlay"), HEALTH);
		this.defaultTurnType = new MoveAttackTurnType(this);
	}
	
	@Override
	public boolean isSpearman() {
		return true;
	}
	
	@Override
	public int getHorsemanDamage () {
		return 3;
	}

}
