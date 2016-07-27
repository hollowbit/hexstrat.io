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
		gameScreen.addHexTouchListener(this);
		initiateSecondTime(gameScreen);
	}
	
	private void initiateSecondTime (GameScreen gameScreen) {
		for (Hex hex : unit.getHex().getSurroundingHexesInRange(1)) {
			if (hex.getUnitOnHex() != null) {
				if (unit.getPlayer().doesUnitBelongToPlayer(hex.getUnitOnHex())) {//If unit is on same team, disallow moving there
					hex.setOverlayColor(OverlayColor.INVALID);
				} else {
					hex.setOverlayColor(OverlayColor.ATTACK);
				}
			} else {
				hex.setOverlayColor(OverlayColor.VALID);
			}
		}
	}

	@Override
	public void dispose (GameScreen gameScreen) {
		gameScreen.removeHexTouchListener(this);
	}

	@Override
	public void hexTouched (Hex hex, GameScreen gameScreen) {
		switch(hex.getOverlayColor()) {
		case ATTACK:
			
			break;
		case VALID:
			unit.move(hex);
			break;
		default://Do nothing if not one of the above
			break;
		}
		movesLeft--;
		if (movesLeft <= 0)
			gameScreen.flagTurnAsDone();
		else {
			gameScreen.resetFog();
			initiateSecondTime(gameScreen);
		}
	}

}
