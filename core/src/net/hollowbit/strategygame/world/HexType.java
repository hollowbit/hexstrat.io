package net.hollowbit.strategygame.world;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.hollowbit.strategygame.StrategyGame;

public enum HexType {
	
	GRASS(0, 0, 0, false, 0),
	WATER(1, 1, 0, true, 0, 2, 0.8f)/*,
	MOUNTAIN(2, 2, 0, true, 0)*/;
	
	TextureRegion texture;
	Animation animation;
	public int id;
	private int spriteSheetX, spriteSheetY;
	public boolean collidable;
	public int damage;
	int animationFrames;
	float animationTime;
	
	private HexType (int id, int spriteSheetX, int spriteSheetY, boolean collidable, int damage) {
		this(id, spriteSheetX, spriteSheetY, collidable, damage, 0, 0);
	}
	
	private HexType (int id, int spriteSheetX, int spriteSheetY, boolean collidable, int damage, int animationFrames, float animationTime) {
		this.id = id;
		this.spriteSheetX = spriteSheetX;
		this.spriteSheetY = spriteSheetY;
		this.collidable = collidable;
		this.damage = damage;
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
