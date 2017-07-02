package net.hollowbit.strategygame.units;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.Player;
import net.hollowbit.strategygame.gamecomponents.TurnType;
import net.hollowbit.strategygame.gamecomponents.turntypes.ConvertTurnType;
import net.hollowbit.strategygame.gamecomponents.turntypes.DoNothingTurnType;
import net.hollowbit.strategygame.gamecomponents.turntypes.HealOtherTurnType;
import net.hollowbit.strategygame.gamecomponents.turntypes.MoveTurnType;
import net.hollowbit.strategygame.gamecomponents.turntypes.SurroundHealTurnType;
import net.hollowbit.strategygame.world.Hex;
import net.hollowbit.strategygame.world.World;

public class Priest extends Unit {
	
	private static final int HEALTH = 4;
	
	public Priest(World world, Player player, Hex hex) {
		super(world, player, hex, StrategyGame.getGame().getAssetManager().getTexture("priest"), StrategyGame.getGame().getAssetManager().getTexture("priest-overlay"), HEALTH);
		this.defaultTurnType = new MoveTurnType(this);
		this.turnTypes = new TurnType[] {new HealOtherTurnType(this), new SurroundHealTurnType(this), new ConvertTurnType(this), new DoNothingTurnType(this)};
	}
	
	@Override
	public int getAttackRange() {
		return 2;
	}

}
