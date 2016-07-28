package net.hollowbit.strategygame.tools;

import net.hollowbit.strategygame.screens.GameScreen;
import net.hollowbit.strategygame.world.Hex;

public interface HexTouchListener {
	
	public abstract boolean hexTouched (Hex hex, GameScreen gameScreen);
	
}
