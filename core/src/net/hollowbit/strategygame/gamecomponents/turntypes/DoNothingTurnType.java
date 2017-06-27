package net.hollowbit.strategygame.gamecomponents.turntypes;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.TurnType;
import net.hollowbit.strategygame.screens.GameScreen;
import net.hollowbit.strategygame.units.Unit;

public class DoNothingTurnType extends TurnType {

	public DoNothingTurnType(Unit unit) {
		super(unit);
	}

	@Override
	public TextButton getTurnButton() {
		return new TextButton("Do Nothing", StrategyGame.getGame().getSkin());
	}

	@Override
	public void turnStart() {}

	@Override
	public void initiate(GameScreen gameScreen) {
		if (usable()) {
			unit.setFinishedTurn(true);
			gameScreen.resetUnitMoveButtons();
			unit.disposeTurnTypes(gameScreen);
			gameScreen.selectNextUnit();
			unit.moveMade();
		}
	}

	@Override
	public void dispose(GameScreen gameScreen) {}

	@Override
	public boolean usable() {
		return true;
	}

}
