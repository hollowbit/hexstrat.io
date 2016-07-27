package net.hollowbit.strategygame.gamecomponents;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import net.hollowbit.strategygame.screens.GameScreen;
import net.hollowbit.strategygame.units.Unit;

public abstract class TurnType {
	
	//Determine what to do on a player's turn
	protected Unit unit;
	
	public TurnType (Unit unit) {
		this.unit = unit;
	}
	
	public abstract TextButton getTurnButton();
	public abstract void initiate(GameScreen gameScreen);
	public abstract void dispose(GameScreen gameScreen);
	
}
