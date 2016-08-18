package net.hollowbit.strategygame.gamecomponents.turntypes;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.TurnType;
import net.hollowbit.strategygame.screens.GameScreen;
import net.hollowbit.strategygame.tools.HexTouchListener;
import net.hollowbit.strategygame.units.Unit;
import net.hollowbit.strategygame.world.Hex;
import net.hollowbit.strategygame.world.Hex.OverlayColor;

public class MoveTurnType extends TurnType implements HexTouchListener {
	
	private int movesLeft;
	
	public MoveTurnType (Unit unit) {
		super(unit);
		this.movesLeft = unit.getMoveSpeed();
	}

	@Override
	public TextButton getTurnButton () {
		return new TextButton("Move", StrategyGame.getGame().getSkin());
	}

	@Override
	public void turnStart () {
		this.movesLeft = unit.getMoveSpeed();
	}
	
	private void initiateSecondTime (GameScreen gameScreen) {
		//change overlay for movement hexes
		for (Hex hex : unit.getHex().getSurroundingHexesInRange(1)) {
			if (hex.getUnitOnHex() != null) {
				hex.setOverlayColor(OverlayColor.INVALID);
			} else {
				if(hex.getType().collidable)
					hex.setOverlayColor(OverlayColor.INVALID);
				else
					hex.setOverlayColor(OverlayColor.VALID);
			}
		}
	}

	@Override
	public void initiate (GameScreen gameScreen) {
		if (usable() && !initiated) {
			initiated = true;
			gameScreen.addHexTouchListener(this);
			initiateSecondTime(gameScreen);
		}
	}

	@Override
	public void dispose (GameScreen gameScreen) {
		initiated = false;
		gameScreen.removeHexTouchListener(this);
		gameScreen.resetFog();
	}

	@Override
	public boolean usable () {
		return movesLeft > 0 && !unit.isFinishedTurn() && (unit.getTurnTypeSet() == null || unit.getTurnTypeSet() == this);
	}

	@Override
	public boolean hexTouched(Hex hex, GameScreen gameScreen) {
		switch(hex.getOverlayColor()) {
		case VALID:
			unit.move(hex);
			break;
		default://Do nothing if not one of the above
			return false;
		}
		unit.setTurnType(this);
		gameScreen.resetUnitMoveButtons();
		movesLeft -= unit.getHex().getType().movesUsed;
		unit.moveMade();
		
		gameScreen.resetFog();
		if (usable()) {
			initiateSecondTime(gameScreen);
		} else {
			unit.setFinishedTurn(true);
			gameScreen.selectNextUnit();
		}
		return true;
	}

}
