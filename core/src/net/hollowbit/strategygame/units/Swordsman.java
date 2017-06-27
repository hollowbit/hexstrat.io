package net.hollowbit.strategygame.units;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.Player;
import net.hollowbit.strategygame.world.Hex;
import net.hollowbit.strategygame.world.World;
import net.hollowbit.strategygame.screens.*;
import net.hollowbit.strategygame.gamecomponents.turntypes.*;
import net.hollowbit.strategygame.gamecomponents.*;

public class Swordsman extends Unit {
	
	private static final int HEALTH = 6;
	
	boolean shielded = false;
	
	public Swordsman(World world, Player player, Hex hex) {
		super(world, player, hex, StrategyGame.getGame().getAssetManager().getTexture("swordsman"), StrategyGame.getGame().getAssetManager().getTexture("swordsman-overlay"), HEALTH);
		this.defaultTurnType = new MoveAttackTurnType(this);
		this.turnTypes = new TurnType[]{new ShieldTurnType(this), new HealTurnType(this), new DoNothingTurnType(this)};
	}
	
	@Override
	public void turnStart (GameScreen gameScreen) {
		shielded = false;
		super.turnStart(gameScreen);
	}
	
	@Override
	public boolean damage (int amount, Player damager, GameScreen gameScreen) {
		if (shielded)
			amount = 0;
		return super.damage(amount, damager, gameScreen);
	}
	
	@Override
	public boolean isSwordsman() {
		return true;
	}
	
	@Override
	public int getSpearmanDamage() {
		return 3;
	}
	
	public void setShielded (boolean shielded) {
		this.shielded = shielded;
	}
	
	public boolean isShielded () {
		return shielded;
	}
	
}
