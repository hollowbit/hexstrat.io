package net.hollowbit.strategygame.gamecomponents;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import net.hollowbit.strategygame.screens.GameScreen;
import net.hollowbit.strategygame.units.Unit;

public abstract class TurnType {
	
	public abstract TextButton getTurnButton();
	public abstract void buttonPressed(GameScreen gameScreen, Unit unit);
	
}
