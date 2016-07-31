package net.hollowbit.strategygame.units;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.Player;
import net.hollowbit.strategygame.gamecomponents.turntypes.MoveAttackTurnType;
import net.hollowbit.strategygame.world.Hex;
import net.hollowbit.strategygame.world.World;

public class Beast extends Unit {
	
	private static final int HEALTH = 10;
	
	public Beast(World world, Player player, Hex hex) {
		super(world, player, hex, StrategyGame.getGame().getAssetManager().getTexture("beast"), StrategyGame.getGame().getAssetManager().getTexture("beast-overlay"), HEALTH);
		this.defaultTurnType = new MoveAttackTurnType(this);
	}
	
	@Override
	public int getNormalDamage() {
		return 4;
	}
	
	@Override
	public int getHorsemanDamage() {
		return 4;
	}
	
	@Override
	public int getSwordsmanDamage() {
		return 4;
	}
	
	@Override
	public int getSpearmanDamage() {
		return 4;
	}
	
	@Override
	public int getTowerDamage() {
		return 4;
	}
	
}
