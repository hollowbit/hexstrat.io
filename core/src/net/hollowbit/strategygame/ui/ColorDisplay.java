package net.hollowbit.strategygame.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

import net.hollowbit.strategygame.StrategyGame;

public class ColorDisplay extends Widget {
	
	private Color color;
	
	public ColorDisplay(Color color) {
		this.color = color;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(color.r, color.g, color.b, parentAlpha);
		batch.draw(StrategyGame.getGame().getAssetManager().getTexture("blank"), this.getX(), this.getY(), this.getWidth(), this.getHeight());
		batch.setColor(Color.WHITE);
		super.draw(batch, parentAlpha);
	}
	
}
