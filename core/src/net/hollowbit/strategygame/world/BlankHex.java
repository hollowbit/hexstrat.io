package net.hollowbit.strategygame.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BlankHex extends Hex {

	public BlankHex(HexType type, int x, int y, Map map) {
		super(type, x, y, map);
	}
	
	@Override
	public void render(SpriteBatch batch, int x, int y) {
		//Do not render
	}
	
}
