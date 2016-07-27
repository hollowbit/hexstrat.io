package net.hollowbit.strategygame.units;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.Player;
import net.hollowbit.strategygame.gamecomponents.turntypes.BuildTurnType;
import net.hollowbit.strategygame.world.Hex;
import net.hollowbit.strategygame.world.World;

public class Village extends Unit {
	
	private static final int MAX_HEALTH = 16;
	
	public Village(World world, Player player, Hex hex) {
		super(world, player, hex, StrategyGame.getGame().getAssetManager().getTexture("village"), StrategyGame.getGame().getAssetManager().getTexture("village-overlay"), MAX_HEALTH);
		this.defaultTurnType = new BuildTurnType(this);
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

}
