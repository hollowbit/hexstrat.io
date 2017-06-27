package net.hollowbit.strategygame.units;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.Player;
import net.hollowbit.strategygame.world.Hex;
import net.hollowbit.strategygame.world.World;
import net.hollowbit.strategygame.gamecomponents.*;
import net.hollowbit.strategygame.gamecomponents.turntypes.*;

public class Phalanx extends Unit {
	
	private static final int HEALTH = 6;
	
	public Phalanx (World world, Player player, Hex hex) {
		super(world, player, hex, StrategyGame.getGame().getAssetManager().getTexture("spearman"), StrategyGame.getGame().getAssetManager().getTexture("spearman-overlay"), HEALTH);
		this.defaultTurnType = new MoveAttackTurnType(this);
		this.turnTypes = new TurnType[]{new HealTurnType(this), new BerzerkTurnType(this), new DoNothingTurnType(this)};
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
