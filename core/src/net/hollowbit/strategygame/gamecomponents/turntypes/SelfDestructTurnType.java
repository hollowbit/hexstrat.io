package net.hollowbit.strategygame.gamecomponents.turntypes;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.TurnType;
import net.hollowbit.strategygame.screens.GameScreen;
import net.hollowbit.strategygame.units.Unit;

public class SelfDestructTurnType extends TurnType {

	public SelfDestructTurnType(Unit unit) {
		super(unit);
	}

	@Override
	public TextButton getTurnButton() {
		return new TextButton("Destroy", StrategyGame.getGame().getSkin());
	}

	@Override
	public void turnStart() {

	}

	@Override
	public void initiate(GameScreen gameScreen) {
		unit.getPlayer().removeUnit(unit);
		gameScreen.getWorld().removeUnit(unit);
	}

	@Override
	public void dispose(GameScreen gameScreen) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean usable() {
		// TODO Auto-generated method stub
		return true;
	}

}
