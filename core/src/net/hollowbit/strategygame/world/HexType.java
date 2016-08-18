package net.hollowbit.strategygame.world;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.hollowbit.strategygame.StrategyGame;

public enum HexType {
	
	GRASS(0, 0, 0, false, 0, 0.5f, 1),
	WATER(1, 1, 0, true, 0, 0, 0, 2, 0.8f),
	FOREST(2, 3, 0, false, 0, 0.25f, 2),
	HILLS(3, 4, 0, false, 0, 0.25f, 2),
	LAVA(4, 5, 0, true, 0, 0, 0, 2, 0.8f),
	POISON(5, 7, 0, false, -1, 0, 1),
	SPRING(6, 8, 0, false, 1, 0.75f, 1),
	FARMLAND(7, 9, 0, false, 0, 1, 1),
	HELL(8, 10, 0, false, 0, 0.5f, 1),
	HELL_POISON(9, 11, 0, false, -1, 0, 1),
	HELL_HEALING(10, 12, 0, false, 1, 1, 1),
	HELL_CEMETARY(11, 13, 0, false, 0, 1, 2),
	HELL_RUINS(12, 14, 0, false, 0, 0.25f, 2);
	
	TextureRegion texture;
	Animation animation;
	public int id;
	private int spriteSheetX, spriteSheetY;
	public boolean collidable;
	public int damage;
	public float production;
	public int movesUsed;
	int animationFrames;
	float animationTime;
	
	private HexType (int id, int spriteSheetX, int spriteSheetY, boolean collidable, int damage, float production, int movesUsed) {
		this(id, spriteSheetX, spriteSheetY, collidable, damage, production, movesUsed, 0, 0);
	}
	
	private HexType (int id, int spriteSheetX, int spriteSheetY, boolean collidable, int damage, float production, int movesUsed, int animationFrames, float animationTime) {
		this.id = id;
		this.spriteSheetX = spriteSheetX;
		this.spriteSheetY = spriteSheetY;
		this.collidable = collidable;
		this.damage = damage;
		this.production = production;
		this.movesUsed = movesUsed;
		this.animationFrames = animationFrames;
		this.animationTime = animationTime;
	}
	
	private void loadTexture (TextureRegion[][] spriteSheet) {
		if (isAnimated()) {
			TextureRegion[] frames = new TextureRegion[animationFrames];
			for (int i = 0; i < animationFrames; i++) {
				frames[i] = spriteSheet[spriteSheetY][spriteSheetX + i];
			}
			animation = new Animation(animationTime, frames);
		} else
			this.texture = spriteSheet[spriteSheetY][spriteSheetX];
	}
	
	public TextureRegion getTexture (boolean forceNoAnimation) {
		if (isAnimated())
			return animation.getKeyFrame(forceNoAnimation ? 0:StrategyGame.STATETIME, true);
		else
			return texture;
	}
	
	public boolean isAnimated () {
		return animationFrames > 1;
	}
	
	private static HashMap<Integer, HexType> hexTypes;
	static {
		hexTypes = new HashMap<Integer, HexType>();
		
		//Load each hex into the hash map
		for (HexType hexType : HexType.values())
			hexTypes.put(hexType.id, hexType);
	}
	
	public static void loadTextures () {
		TextureRegion[][] spriteSheet = TextureRegion.split(new Texture("hexes.png"), Hex.SIZE, Hex.SIZE);
		for (HexType hexType : HexType.values())
			hexType.loadTexture(spriteSheet);
	}
	
	public static HexType getHexTypeById (int id) {
		return hexTypes.get(id);
	}
	
}
