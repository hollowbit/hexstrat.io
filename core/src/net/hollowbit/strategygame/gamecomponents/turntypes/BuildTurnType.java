package net.hollowbit.strategygame.gamecomponents.turntypes;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.TurnType;
import net.hollowbit.strategygame.screens.GameScreen;
import net.hollowbit.strategygame.units.Village;

public class BuildTurnType extends TurnType {

	Village village;//Keep track of the village that is generating units
	
	public BuildTurnType (Village village) {
		super(village);
		this.village = village;
	}

	@Override
	public TextButton getTurnButton() {
		return new TextButton("Build", StrategyGame.getGame().getSkin());
	}

	@Override
	public void initiate (GameScreen gameScreen) {
		if (usable() && !initiated) {
			initiated = true;
			gameScreen.openBuildWindow(village);
		}
	}

	@Override
	public void dispose(GameScreen gameScreen) {
		initiated = false;
	}

	@Override
	public void turnStart() {
		
	}

	@Override
	public boolean usable () {
		return village.isDoneBuilding();
	}

}
