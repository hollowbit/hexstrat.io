package net.hollowbit.strategygame.world;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Json;

public enum MapType {
	
	GRASSLANDS("grasslands"),
	HELL("hell"),
	SKYLANDS("skylands"),
	CHOKEPOINTISLAND("chokepointisland"),
	LAVALAKE("lavalake");
	
	public String id;
	MapData data;
	Color backgroundColor;
	
	private MapType (String id) {
		this.id = id;
		
		//Load map
		Json json = new Json();
		this.data = json.fromJson(MapData.class, Gdx.files.internal("maps/" + id + ".json").readString());
		this.backgroundColor = new Color(data.r / 255f, data.g / 255f, data.b / 255f, 1);
	}
	
	public MapData getData () {
		return data;
	}
	
	public Color getBackgroundColor() {
		return backgroundColor;
	}
	
	@Override
	public String toString() {
		return this.data.name;
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
