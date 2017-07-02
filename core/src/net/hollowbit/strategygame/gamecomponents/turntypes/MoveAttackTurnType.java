package net.hollowbit.strategygame.gamecomponents.turntypes;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.TurnType;
import net.hollowbit.strategygame.screens.GameScreen;
import net.hollowbit.strategygame.tools.HexTouchListener;
import net.hollowbit.strategygame.units.Unit;
import net.hollowbit.strategygame.world.Hex;
import net.hollowbit.strategygame.world.Hex.OverlayColor;

public class MoveAttackTurnType extends TurnType implements HexTouchListener {
	
	private int movesLeft;
	private boolean attacked;
	
	public MoveAttackTurnType (Unit unit) {
		super(unit);
		this.movesLeft = unit.getMoveSpeed();
	}

	@Override
	public TextButton getTurnButton () {
		return new TextButton("Move/Attack", StrategyGame.getGame().getSkin());
	}

	@Override
	public void initiate (GameScreen gameScreen) {
		if (usable() && !initiated) {
			initiated = true;
			gameScreen.addHexTouchListener(this);
			initiateSecondTime(gameScreen);
		}
	}
	
	private void initiateSecondTime (GameScreen gameScreen) {
		//change overlay for movement hexes
		for (Hex hex : unit.getHex().getSurroundingHexesInRange(1)) {
			if (hex.getUnitOnHex() != null) {
				if (unit.getPlayer().doesUnitBelongToPlayer(hex.getUnitOnHex())) {//If unit is on same team, disallow moving there
					hex.setOverlayColor(OverlayColor.INVALID);
				}
			} else {
				if(hex.getType().collidable)
					hex.setOverlayColor(OverlayColor.INVALID);
				else
					hex.setOverlayColor(OverlayColor.VALID);
			}
		}
		
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
	public void dispose (GameScreen gameScreen) {
		initiated = false;
		gameScreen.removeHexTouchListener(this);
		gameScreen.resetFog();
	}

	@Override
	public boolean hexTouched (Hex hex, GameScreen gameScreen) {
		switch(hex.getOverlayColor()) {
		case ATTACK:
			//Do damage based on unit type
			Unit attackedUnit = hex.getUnitOnHex();
			boolean unitKilled = false;
			if (attackedUnit.isHorseman())
					unitKilled = attackedUnit.damage(-unit.getHorsemanDamage(), gameScreen.getCurrentPlayer(), gameScreen);
			else if (attackedUnit.isSpearman())
					unitKilled = attackedUnit.damage(-unit.getSpearmanDamage(), gameScreen.getCurrentPlayer(), gameScreen);
			else if (attackedUnit.isSwordsman())
					unitKilled = attackedUnit.damage(-unit.getSwordsmanDamage(), gameScreen.getCurrentPlayer(), gameScreen);
			else if (attackedUnit.isTower())
					unitKilled = attackedUnit.damage(-unit.getTowerDamage(), gameScreen.getCurrentPlayer(), gameScreen);
			else if (attackedUnit.isSwordsman())
					unitKilled = attackedUnit.damage(-unit.getSwordsmanDamage(), gameScreen.getCurrentPlayer(), gameScreen);
			else
					unitKilled = attackedUnit.damage(-unit.getNormalDamage(), gameScreen.getCurrentPlayer(), gameScreen);
			
			//If the enemy unit was killed and this unit is not a ranged unit, move to the hex
			if (unitKilled && unit.getAttackRange() <= 1)
				unit.move(hex);
			
			attacked = true;
			movesLeft -= 2;
			break;
		case VALID:
				unit.move(hex);
				movesLeft -= unit.getHex().getType().movesUsed;
			break;
		default://Do nothing if not one of the above
			return false;
		}
		unit.setTurnType(this);
		gameScreen.resetFog();
		unit.moveMade();
		if (usable()) {
			initiateSecondTime(gameScreen);
		} else {
			unit.setFinishedTurn(true);
			gameScreen.selectNextUnit();
		}
		gameScreen.resetUnitMoveButtons();
		return true;
	}

	@Override
	public void turnStart() {
		attacked = false;
		this.movesLeft = unit.getMoveSpeed();
	}

	@Override
	public boolean usable() {
		return movesLeft > 0 && !unit.isFinishedTurn() && (unit.getTurnTypeSet() == null || unit.getTurnTypeSet() == this);
	}

}
