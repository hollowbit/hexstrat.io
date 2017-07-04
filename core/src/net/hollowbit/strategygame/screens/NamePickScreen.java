package net.hollowbit.strategygame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.GameSettings;
import net.hollowbit.strategygame.tools.Screen;
import net.hollowbit.strategygame.ui.NamePickerWindow;

public class NamePickScreen extends Screen {

	private Stage stage;
	private NamePickerWindow window;
	
	public NamePickScreen(GameSettings settings) {
		stage = new Stage(StrategyGame.getGame().getUiCamera().getScreenViewport(), StrategyGame.getGame().getBatch());
		StrategyGame.getGame().getGameCamera().zoom(1);
		Gdx.input.setInputProcessor(stage);
		
		window = new NamePickerWindow(settings);
		window.setPosition(Gdx.graphics.getWidth() / 2 - window.getWidth() /2, Gdx.graphics.getHeight() / 2 - window.getHeight() / 2);
		stage.addActor(window);
		
	}
	
	@Override
	public void update(float deltaTime) {
		stage.act();
		super.update(deltaTime);
	}
	
	@Override
	public void renderUi(SpriteBatch batch, float width, float height) {
		batch.end();
		stage.draw();
		batch.begin();
		super.renderUi(batch, width, height);
	}
	
	@Override
	public void resize(int width, int height) {
		window.setPosition(Gdx.graphics.getWidth() / 2 - window.getWidth() /2, Gdx.graphics.getHeight() / 2 - window.getHeight() / 2);
		
		super.resize(width, height);
	}
	
}
