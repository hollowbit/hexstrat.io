package net.hollowbit.strategygame.tools;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Screen {

	public void create() {}
	public void update(float deltaTime) {}
	public void render(SpriteBatch batch, float width, float height) {}
	public void renderUi(SpriteBatch batch, float width, float height) {}
	public void resize(int width, int height) {}
	public void dispose() {}
	
}
