package net.hollowbit.strategygame.tools;
import net.hollowbit.strategygame.world.*;
import com.badlogic.gdx.graphics.g2d.*;
import net.hollowbit.strategygame.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.*;

public class HexMessage
{
	
	float x, y;
	GlyphLayout messageLayout;
	float showTimer = 4;
	boolean show = false;
	public boolean remove = false;
	
	public HexMessage (String message, Hex hex, World world, Color color) {
		this.messageLayout = new GlyphLayout(StrategyGame.getGame().getFontManager().getFont(FontManager.Fonts.POPUP, FontManager.Sizes.SMALL), message, color, 0, Align.left, false);
		this.x = world.getXFromMapPositionX(hex.getX()) + Hex.SIZE / 2 - messageLayout.width / 2;
		this.y = world.getYFromMapPositionY(hex.getY(), hex.getX()) + Hex.SIZE;
	}
	
	public void update (float deltaTime) {
		if (isInCameraView())
			show = true;
			
		if (show)
			showTimer -= deltaTime;
			
		if (showTimer <= 0)
			remove = true;
	}
	
	public void render (SpriteBatch batch) {
		if (show)
			StrategyGame.getGame().getFontManager().getFont(FontManager.Fonts.POPUP, FontManager.Sizes.SMALL).draw(batch, messageLayout, x, y);
	}
	
	private boolean isInCameraView () {
		return StrategyGame.getGame().getGameCamera().isRectInView(x, y, messageLayout.width, messageLayout.height);
	}
	
}
