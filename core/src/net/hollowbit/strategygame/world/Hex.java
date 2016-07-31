package net.hollowbit.strategygame.world;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.units.Unit;

public class Hex {
	
	public static final int SIZE = 64;
	public static final int OVERLAP_SIZE = 49;
	
	HexType type;
	int x, y;
	Unit unitOnHex;
	OverlayColor overlay;
	Map map;
	
	public Hex (HexType type, int x, int y, Map map) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.map = map;
		overlay = OverlayColor.NONE;
	}
	
	public int getX () {
		return x;
	}
	
	public int getY () {
		return y;
	}
	
	public void render (SpriteBatch batch, int x, int y) {
		batch.draw(type.getTexture(overlay == OverlayColor.FOG), x, y);
		
		//Draw overlay
		if (overlay != OverlayColor.NONE) {
			batch.setColor(overlay.getColor());
			batch.draw(StrategyGame.getGame().getAssetManager().getTexture("blank-hex"), x, y);
			batch.setColor(1, 1, 1, 1);
		}
		
		//Draw grid
		batch.draw(StrategyGame.getGame().getAssetManager().getTexture("grid"), x, y);
	}
	
	public Unit getUnitOnHex () {
		return unitOnHex;
	}
	
	public void setUnitOnHex (Unit unit) {
		this.unitOnHex = unit;
	}
	
	public void setOverlayColor (OverlayColor overlay) {
		this.overlay = overlay;
	}
	
	public HexType getType () {
		return type;
	}
	
	public ArrayList<Hex> getSurroundingHexesInRange (int range) {
		ArrayList<Hex> hexes = new ArrayList<Hex>();
		//Get middle row
		for (int i = 1; i <= range; i++) {
			if (x >= 0 && x < map.getWidth() && y + i >= 0 && y + i < map.getHeight()) {
				hexes.add(map.getMap()[y + i][x]);
			}
			//Get from left
			if (x >= 0 && x < map.getWidth() && y - i >= 0 && y - i < map.getHeight()) {
				hexes.add(map.getMap()[y - i][x]);
			}
		}
		
		//Get rows after it
		int top = range;
		for (int col = 1; col <= range; col++) {
			if (col % 2 == (x % 2 == 0 ? 1:0))//If col is even, subtract y
				top--;
			for (int row = -top; row < range * 2 - col + 1 - top; row++) {
				//Get from right
				if (x + col >= 0 && x + col < map.getWidth() && y + row >= 0 && y + row < map.getHeight()) {
					hexes.add(map.getMap()[y + row][x + col]);
				}
				//Get from left
				if (x - col >= 0 && x - col < map.getWidth() && y + row >= 0 && y + row < map.getHeight()) {
					hexes.add(map.getMap()[y + row][x - col]);
				}
			}
		}
		return hexes;
	}
	
	public OverlayColor getOverlayColor () {
		return overlay;
	}
	
	public enum OverlayColor {
		NONE(1, 1, 1, 1),
		VALID(0, 0, 1, 0.7f),
		INVALID(1, 0, 0, 0.7f),
		FOG(0.2f, 0.2f, 0.2f, 0.5f),
		ATTACK(1, 0.5f, 0, 0.7f);
		
		private Color color;
		
		private OverlayColor (float r, float g, float b, float a) {
			this.color = new Color(r, g, b, a);
		}
		
		public Color getColor () {
			return color;
		}
	}
	
}
