package net.hollowbit.strategygame.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.hollowbit.strategygame.units.Unit;
import net.hollowbit.strategygame.units.Village;
import net.hollowbit.strategygame.world.Hex.OverlayColor;

public class Map {
	
	Hex[][] map;
	Hex spawn1;
	Hex spawn2;
	MapType type;
	
	public Map (String mapId) {
		this.type = MapType.getMapTypeById(mapId);
		MapData data = type.data;
		//Load hexes
		map = new Hex[data.data.length][data.data[0].length];
		for (int r = 0; r < data.data.length; r++) {
			for (int c = 0; c < data.data[0].length; c++) {
				if (data.data[r][c] == 'z') {
					map[map.length - r - 1][c] = new BlankHex(HexType.getHexTypeById(data.data[r][c]), c, map.length - r - 1, this);
				} else {
					map[map.length - r - 1][c] = new Hex(HexType.getHexTypeById(data.data[r][c]), c, map.length - r - 1, this);
				}
				if (map.length - r - 1 == data.spawnY1 && c == data.spawnX1)
					spawn1 = map[map.length - r - 1][c];
				if (map.length - r - 1 == data.spawnY2 && c == data.spawnX2)
					spawn2 = map[map.length - r - 1][c];
			}
		}
	}
	
	public void render (SpriteBatch batch) {
		//Draw map
		for (int x = 0; x < map[0].length; x++) {
			for (int y = 0; y < map.length; y++)
				map[map.length - y - 1][x].render(batch, getXFromMapPositionX(x), getYFromMapPositionY(map.length - y - 1, x));
		}
	}
	
	public void renderUnits (SpriteBatch batch) {
		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[0].length; x++) {
				if (x % 2 == 0) {
					Unit unit = map[map.length - y - 1][x].getUnitOnHex();
					if (unit != null && unit.getHex().getOverlayColor() != OverlayColor.FOG || unit instanceof Village)//Only draw enemies in fog if they are villages
						unit.render(batch);
				}
			}

			for (int x = 0; x < map[0].length; x++) {
				if (x % 2 == 1) {
					Unit unit = map[map.length - y - 1][x].getUnitOnHex();
					if (unit != null && unit.getHex().getOverlayColor() != OverlayColor.FOG || unit instanceof Village)//Only draw enemies in fog if they are villages
						unit.render(batch);
				}
			}
		}
	}
	
	public MapType getType() {
		return type;
	}
	
	public Hex[][] getMap () {
		return map;
	}
	
	public int getXFromMapPositionX (int x) {
		return x * Hex.OVERLAP_SIZE;
	}
	
	public int getYFromMapPositionY (int y, int x) {
		return y * Hex.SIZE - (x % 2 == 1 ? Hex.SIZE / 2:0);
	}
	
	public Hex getSpawn1 () {
		return spawn1;
	}
	
	public Hex getSpawn2 () {
		return spawn2;
	}
	
	public float getRenderWidth() {
		return getWidth() * Hex.OVERLAP_SIZE;
	}
	
	public float getRenderHeight() {
		return getHeight() * Hex.OVERLAP_SIZE;
	}
	
	public int getWidth () {
		return map[0].length;
	}
	
	public int getHeight () {
		return map.length;
	}
	
}
