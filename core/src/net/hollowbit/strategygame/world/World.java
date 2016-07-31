package net.hollowbit.strategygame.world;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.hollowbit.strategygame.gamecomponents.Player;
import net.hollowbit.strategygame.units.Unit;
import net.hollowbit.strategygame.units.Village;
import net.hollowbit.strategygame.world.Hex.OverlayColor;

public class World {
	
	Map map;
	ArrayList<Unit> units;
	
	public World (String mapId) {
		map = new Map(mapId);
		units = new ArrayList<Unit>();
	}
	
	public void update (float deltaTime) {
		for (Unit unit : units)
			unit.update(deltaTime);
	}
	
	public void render (SpriteBatch batch, Player currentPlayer) {
		map.render(batch);
		for (Unit unit : units) {
			if (unit.getHex().getOverlayColor() != OverlayColor.FOG || unit instanceof Village)//Only draw enemies in fog if they are villages
				unit.render(batch);
		}
	}
	
	public void addUnit (Unit unit) {
		units.add(unit);
	}
	
	public void removeUnit (Unit unit) {
		unit.getPlayer().removeUnit(unit);
		units.remove(unit);
		unit.getHex().setUnitOnHex(null);
	}
	
	public Map getMap () {
		return map;
	}
	
	public Hex getSpawn1 () {
		return map.getSpawn1();
	}
	
	public Hex getSpawn2 () {
		return map.getSpawn2();
	}
	
	public int getXFromMapPositionX (int x) {
		return map.getXFromMapPositionX(x);
	}
	
	public int getYFromMapPositionY (int y, int x) {
		return map.getYFromMapPositionY(y, x);
	}
	
}
