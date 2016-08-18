package net.hollowbit.strategygame.units;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.Player;
import net.hollowbit.strategygame.gamecomponents.TurnType;
import net.hollowbit.strategygame.gamecomponents.turntypes.BuildFarmTurnType;
import net.hollowbit.strategygame.gamecomponents.turntypes.BuildTowerTurnType;
import net.hollowbit.strategygame.gamecomponents.turntypes.MoveTurnType;
import net.hollowbit.strategygame.world.Hex;
import net.hollowbit.strategygame.world.World;

public class Worker extends Unit {

	private static final int HEALTH = 2;
	
	public Worker(World world, Player player, Hex hex) {
		super(world, player, hex, StrategyGame.getGame().getAssetManager().getTexture("worker"), StrategyGame.getGame().getAssetManager().getTexture("worker-overlay"), HEALTH);
		this.defaultTurnType = new MoveTurnType(this);
		this.turnTypes = new TurnType[]{new BuildTowerTurnType(this), new BuildFarmTurnType(this)};
	}

}
