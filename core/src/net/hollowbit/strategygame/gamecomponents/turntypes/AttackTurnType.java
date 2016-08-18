package net.hollowbit.strategygame.gamecomponents.turntypes;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.TurnType;
import net.hollowbit.strategygame.screens.GameScreen;
import net.hollowbit.strategygame.tools.HexTouchListener;
import net.hollowbit.strategygame.units.Unit;
import net.hollowbit.strategygame.world.Hex;
import net.hollowbit.strategygame.world.Hex.OverlayColor;

public class AttackTurnType extends TurnType implements HexTouchListener {

	private boolean attacked;
	
	public AttackTurnType (Unit unit) {
		super(unit);
	}

	@Override
	public TextButton getTurnButton() {
		return new TextButton("Attack", StrategyGame.getGame().getSkin());
	}

	@Override
	public void turnStart() {
		attacked = false;
		if (!isUnitInRange()) {//If no unit is in range, than this unit has no need to take a turn
			unit.setFinishedTurn(true);
			attacked = true;
		}
	}

	@Override
	public void initiate(GameScreen gameScreen) {
		if (usable() && !initiated) {
			if (!isUnitInRange()) {//If no unit is in range, than this unit has no need to take a turn
				unit.setFinishedTurn(true);
				attacked = true;
				return;
			}
			initiated = true;
			gameScreen.addHexTouchListener(this);
			initiateSecondTime(gameScreen);
		}
	}
	
	private void initiateSecondTime (GameScreen gameScreen) {
		//Change overlay for attack hexes
		for (Hex hex : unit.getHex().getSurroundingHexesInRange(unit.getAttackRange())) {
			if (hex.getUnitOnHex() != null && !unit.getPlayer().doesUnitBelongToPlayer(hex.getUnitOnHex())) {
					if (attacked)//If this unit already attacked, then don't let it attack again
						hex.setOverlayColor(OverlayColor.INVALID);
					else
						hex.setOverlayColor(OverlayColor.ATTACK);
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
	public boolean hexTouched(Hex hex, GameScreen gameScreen) {
		switch(hex.getOverlayColor()) {
		case ATTACK:
			//Do damage based on unit type
			Unit attackedUnit = hex.getUnitOnHex();
			if (attackedUnit.isHorseman())
				attackedUnit.damage(-unit.getHorsemanDamage(), gameScreen.getCurrentPlayer(), gameScreen);
			else if (attackedUnit.isSpearman())
				attackedUnit.damage(-unit.getSpearmanDamage(), gameScreen.getCurrentPlayer(), gameScreen);
			else if (attackedUnit.isSwordsman())
				attackedUnit.damage(-unit.getSwordsmanDamage(), gameScreen.getCurrentPlayer(), gameScreen);
			else if (attackedUnit.isTower())
				attackedUnit.damage(-unit.getTowerDamage(), gameScreen.getCurrentPlayer(), gameScreen);
			else if (attackedUnit.isSwordsman())
				attackedUnit.damage(-unit.getSwordsmanDamage(), gameScreen.getCurrentPlayer(), gameScreen);
			else
				attackedUnit.damage(-unit.getNormalDamage(), gameScreen.getCurrentPlayer(), gameScreen);
			
			attacked = true;
			break;
		default://Do nothing if not one of the above
			return false;
		}
		gameScreen.resetFog();
		unit.setTurnType(this);
		gameScreen.resetUnitMoveButtons();
		unit.moveMade();
		if (usable()) {
			initiateSecondTime(gameScreen);
		} else {
			unit.setFinishedTurn(true);
			gameScreen.selectNextUnit();
		}
		return true;
	}

	@Override
	public boolean usable() {
		return !attacked && !unit.isFinishedTurn() && (unit.getTurnTypeSet() == null || unit.getTurnTypeSet() == this);
	}
	
	private boolean isUnitInRange () {
		for (Hex hex : unit.getHex().getSurroundingHexesInRange(unit.getAttackRange())) {
			if (hex.getUnitOnHex() != null && !unit.getPlayer().doesUnitBelongToPlayer(hex.getUnitOnHex())) {
					if (attacked)//If this unit already attacked, then don't let it attack again
						hex.setOverlayColor(OverlayColor.INVALID);
					else
						return true;
			}
		}
		return false;
	}

}
