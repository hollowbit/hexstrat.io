package net.hollowbit.strategygame.gamecomponents.turntypes;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.TurnType;
import net.hollowbit.strategygame.screens.GameScreen;
import net.hollowbit.strategygame.units.Unit;

public class MoveAttackTurn extends TurnType {

	@Override
	public TextButton getTurnButton() {
		return new TextButton("Move/Attack", StrategyGame.getGame().getSkin());
	}

	@Override
	public void buttonPressed (GameScreen gameScreen, Unit unit) {
		
	}

}
