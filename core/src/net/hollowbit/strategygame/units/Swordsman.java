package net.hollowbit.strategygame.units;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.Player;
import net.hollowbit.strategygame.gamecomponents.turntypes.MoveAttackTurnType;
import net.hollowbit.strategygame.world.Hex;
import net.hollowbit.strategygame.world.World;

public class Swordsman extends Unit {
	
	private static final int HEALTH = 6;
	
	public Swordsman(World world, Player player, Hex hex) {
		super(world, player, hex, StrategyGame.getGame().getAssetManager().getTexture("swordsman"), StrategyGame.getGame().getAssetManager().getTexture("swordsman-overlay"), HEALTH);
		this.defaultTurnType = new MoveAttackTurnType(this);
	}
	
	@Override
	public boolean isSwordsman() {
		return true;
	}
	
	@Override
	public int getSpearmanDamage() {
		return 3;
	}

}
