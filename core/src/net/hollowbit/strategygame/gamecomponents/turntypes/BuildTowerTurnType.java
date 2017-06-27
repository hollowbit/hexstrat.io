package net.hollowbit.strategygame.gamecomponents.turntypes;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.TurnType;
import net.hollowbit.strategygame.screens.GameScreen;
import net.hollowbit.strategygame.units.Tower;
import net.hollowbit.strategygame.units.Unit;

public class BuildTowerTurnType extends TurnType {

	public BuildTowerTurnType (Unit unit) {
		super(unit);
	}

	@Override
	public TextButton getTurnButton () {
		return new TextButton("Build Tower", StrategyGame.getGame().getSkin());
	}

	@Override
	public void turnStart () {}

	@Override
	public void initiate (GameScreen gameScreen) {
		//Remove worker
		unit.getPlayer().removeUnit(unit);
		gameScreen.getWorld().removeUnit(unit);
		
		//Add tower
		Tower tower = new Tower(gameScreen.getWorld(), unit.getPlayer(), unit.getHex());
		gameScreen.getWorld().addUnit(tower);
		gameScreen.selectUnit(tower);
	}

	@Override
	public void dispose(GameScreen gameScreen) {}

	@Override
	public boolean usable () {
		return !unit.isFinishedTurn();
	}

}
