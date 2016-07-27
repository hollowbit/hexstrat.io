package net.hollowbit.strategygame.gamecomponents.turntypes;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.TurnType;
import net.hollowbit.strategygame.screens.GameScreen;
import net.hollowbit.strategygame.units.Unit;

public class BuildTurnType extends TurnType {

	public BuildTurnType (Unit unit) {
		super(unit);
	}

	@Override
	public TextButton getTurnButton() {
		return new TextButton("Build", StrategyGame.getGame().getSkin());
	}

	@Override
	public void initiate(GameScreen gameScreen) {
		// TODO Auto-generated method stub
		System.out.println("BuildTurnType.java test~!");
		
	}

	@Override
	public void dispose(GameScreen gameScreen) {
		// TODO Auto-generated method stub
		
	}

}
