package net.hollowbit.strategygame.gamecomponents.turntypes;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.TurnType;
import net.hollowbit.strategygame.screens.GameScreen;
import net.hollowbit.strategygame.units.Farm;
import net.hollowbit.strategygame.units.Unit;

public class BuildFarmTurnType extends TurnType {

	public BuildFarmTurnType (Unit unit) {
		super(unit);
	}

	@Override
	public TextButton getTurnButton() {
		return new TextButton("Build Farm", StrategyGame.getGame().getSkin());
	}

	@Override
	public void turnStart() {}

	@Override
	public void initiate(GameScreen gameScreen) {
		//Remove worker
		unit.getPlayer().removeUnit(unit);
		gameScreen.getWorld().removeUnit(unit);
				
		//Add farm
		Farm farm = new Farm(gameScreen.getWorld(), unit.getPlayer(), unit.getHex());
		gameScreen.getWorld().addUnit(farm);
		gameScreen.selectUnit(farm);
	}

	@Override
	public void dispose (GameScreen gameScreen) {}

	@Override
	public boolean usable () {
		return true;
	}

}
