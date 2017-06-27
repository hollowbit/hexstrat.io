package net.hollowbit.strategygame.world;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Json;

public enum MapType {
	
	GRASSLANDS("grasslands", "Grasslands", new Color(0.3f, 0.5f, 0.95f, 1f)),
	HELL("hell", "Hell", new Color(0.1f, 0.02f, 0.02f, 1f));
	
	public String id;
	String name;
	MapData data;
	Color backgroundColor;
	
	private MapType (String id, String name, Color backgroundColor) {
		this.id = id;
		this.name = name;
		this.backgroundColor = backgroundColor;
		
		//Load map
		Json json = new Json();
		this.data = json.fromJson(MapData.class, Gdx.files.internal("maps/" + id + ".json").readString());
	}
	
	public MapData getData () {
		return data;
	}
	
	public Color getBackgroundColor() {
		return backgroundColor;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	private static HashMap<String, MapType> mapTypes;
	static {
		mapTypes = new HashMap<String, MapType>();
		
		//Load map types into hash map
		for (MapType mapType : MapType.values())
			mapTypes.put(mapType.id, mapType);
	}
	
	public static MapType getMapTypeById (String id) {
		return mapTypes.get(id);
	}
	
}
