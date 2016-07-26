package net.hollowbit.strategygame.units;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.Player;
import net.hollowbit.strategygame.screens.GameScreen;
import net.hollowbit.strategygame.world.Hex;
import net.hollowbit.strategygame.world.World;

public class Village extends Unit {
	
	private static final int MAX_HEALTH = 16;
	
	public Village(World world, Player player, Hex hex) {
		super(world, player, hex, StrategyGame.getGame().getAssetManager().getTexture("village"), StrategyGame.getGame().getAssetManager().getTexture("village-overlay"), MAX_HEALTH);
	}

	@Override
	public void focusedOn (GameScreen gameScreen) {
		
	}
	
	@Override
	public boolean isTower () {
		return true;
	}
	
	@Override
	public int getVisibilityRange () {
		return 3;
	}

}
