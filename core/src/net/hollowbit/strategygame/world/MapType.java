package net.hollowbit.strategygame.world;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

public enum MapType {
	
	GRASSLANDS("grasslands");
	
	public String id;
	MapData data;
	
	private MapType (String id) {
		this.id = id;
		
		//Load map
		Json json = new Json();
		this.data = json.fromJson(MapData.class, Gdx.files.internal("maps/" + id + ".json").readString());
	}
	
	public MapData getData () {
		return data;
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
