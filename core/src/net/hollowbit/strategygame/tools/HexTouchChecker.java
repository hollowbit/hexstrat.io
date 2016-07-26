package net.hollowbit.strategygame.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;

import net.hollowbit.strategygame.world.Hex;
import net.hollowbit.strategygame.world.Map;

public class HexTouchChecker {
	
	public static final int BLACK = Color.argb8888(0, 0, 0, 1);
	public static final int WHITE = Color.argb8888(1, 1, 1, 1);
	
	public static final int ERROR = 15;
	
	Pixmap hexMap;
	int touchX = 0, touchY = 0;
	
	public HexTouchChecker () {
		hexMap = new Pixmap(Gdx.files.internal("hexmap.png"));
	}
	
	public void touchDown (int worldX, int worldY) {
		this.touchX = worldX;
		this.touchY = worldY;
	}
	
	public Hex getTouchedHex (int worldX, int worldY, Map map) {
		if (new Vector2(worldX, worldY).dst(touchX, touchY) < 15) {
			
			//Find array position of tile being interacted with
			int x = (int) (worldX / Hex.OVERLAP_SIZE);
			int y = (int) ((worldY + (x % 2 == 1 ? Hex.SIZE / 2:0)) / Hex.SIZE);
			
			//Find coordinate being clicked within that position
			int hexMapX = (int) (worldX % Hex.OVERLAP_SIZE);
			int hexMapY = (int) ((worldY - (x % 2 == 1 ? Hex.SIZE / 2:0)) % Hex.SIZE);
			
			//Depending on the color, change index
			int color = hexMap.getPixel(hexMapX, hexMapY);
			if (color == WHITE) {
				x--;
				y -= (x % 2 == 0 ? 1:0);
			} else if (color == BLACK) {
				x--;
				y += (x % 2 == 1 ? 1:0);
			}
			
			//Make sure it isn't out of bounds
			if (x < 0 || x >= map.getMap()[0].length || y < 0 || y >= map.getMap().length)
				return null;
			
			//If this point is reached, then a valid hex was clicked
			return map.getMap()[y][x];
		}
		return null;
	}
	
}
