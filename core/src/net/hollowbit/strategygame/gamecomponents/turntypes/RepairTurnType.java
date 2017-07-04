package net.hollowbit.strategygame.gamecomponents.turntypes;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.TurnType;
import net.hollowbit.strategygame.screens.GameScreen;
import net.hollowbit.strategygame.tools.HexTouchListener;
import net.hollowbit.strategygame.units.Unit;
import net.hollowbit.strategygame.world.Hex;
import net.hollowbit.strategygame.world.Hex.OverlayColor;

public class RepairTurnType extends TurnType implements HexTouchListener {

	public RepairTurnType(Unit unit) {
		super(unit);
	}

	@Override
	public TextButton getTurnButton() {
		return new TextButton("Repair", StrategyGame.getGame().getSkin());
	}

	@Override
	public void turnStart() {}

	@Override
	public void initiate(GameScreen gameScreen) {
		if (usable() && !initiated) {
			initiated = true;
			gameScreen.addHexTouchListener(this);
			initiateSecondTime(gameScreen);
		}
	}
	
	private void initiateSecondTime (GameScreen gameScreen) {
		//Change overlay for attack hexes
		for (Hex hex : unit.getHex().getSurroundingHexesInRange(unit.getAttackRange())) {
			if (hex.getUnitOnHex() != null && hex.getUnitOnHex().isTower() && unit.getPlayer().doesUnitBelongToPlayer(hex.getUnitOnHex())) {
				hex.setOverlayColor(OverlayColor.VALID);
			}
		}
	}

	@Override
	public void dispose(GameScreen gameScreen) {
		initiated = false;
		gameScreen.removeHexTouchListener(this);
		gameScreen.resetFog();
	}

	@Override
	public boolean usable() {
		return isUnitInRange() && !unit.isFinishedTurn();
	}

	@Override
	public boolean hexTouched(Hex hex, GameScreen gameScreen) {
		switch(hex.getOverlayColor()) {
		case VALID:
			Unit unitToHeal = hex.getUnitOnHex();
			unitToHeal.damage(3, unit.getPlayer(), gameScreen);
			break;
		default:
			return false;
		}
		gameScreen.resetFog();
		unit.setFinishedTurn(true);
		gameScreen.resetUnitMoveButtons();
		gameScreen.selectNextUnit();
		return true;
	}

	public boolean isUnitInRange () {
		for (Hex hex : unit.getHex().getSurroundingHexesInRange(unit.getAttackRange())) {
			if (hex.getUnitOnHex() != null && hex.getUnitOnHex().isTower() && unit.getPlayer().doesUnitBelongToPlayer(hex.getUnitOnHex())) {
				return true;
			}
		}
		return false;
	}

}
