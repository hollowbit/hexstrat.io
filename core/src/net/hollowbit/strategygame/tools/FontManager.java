package net.hollowbit.strategygame.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class FontManager {
	
	public static final int LOADED_FONTS = 1;
	
	public enum Fonts { PIXELATED }
	public enum Sizes { SMALL, MEDIUM, LARGE }
	
	private BitmapFont[][] fonts;
	
	public FontManager () {
		fonts = new BitmapFont[Fonts.values().length][Sizes.values().length];
		load();
	}
	
	//Load all fonts here
	private void load () {
		fonts[Fonts.PIXELATED.ordinal()][Sizes.SMALL.ordinal()] = new BitmapFont(Gdx.files.internal("fonts/pixelated_small.fnt"));
		fonts[Fonts.PIXELATED.ordinal()][Sizes.SMALL.ordinal()].getData().markupEnabled = true;
		fonts[Fonts.PIXELATED.ordinal()][Sizes.MEDIUM.ordinal()] = new BitmapFont(Gdx.files.internal("fonts/pixelated_medium.fnt"));
		fonts[Fonts.PIXELATED.ordinal()][Sizes.MEDIUM.ordinal()].getData().markupEnabled = true;
		fonts[Fonts.PIXELATED.ordinal()][Sizes.LARGE.ordinal()] = new BitmapFont(Gdx.files.internal("fonts/pixelated_large.fnt"));
		fonts[Fonts.PIXELATED.ordinal()][Sizes.LARGE.ordinal()].getData().markupEnabled = true;
	}
	
	public BitmapFont getFont (Fonts font, Sizes size) {
		return fonts[font.ordinal()][size.ordinal()];
	}
	
}
