package net.hollowbit.strategygame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.tools.Screen;
import net.hollowbit.strategygame.ui.MapPickerWindow;

public class MapPickerScreen extends Screen {
	
	//Ui
	Stage stage;
	MapPickerWindow window;
	
	public MapPickerScreen() {
		//Load ui
		stage = new Stage(StrategyGame.getGame().getUiCamera().getScreenViewport(), StrategyGame.getGame().getBatch());
		Gdx.input.setInputProcessor(stage);
		
		window = new MapPickerWindow();
		window.setPosition(Gdx.graphics.getWidth() / 2 - window.getWidth() / 2, Gdx.graphics.getHeight() / 2 - window.getHeight() / 2);
		stage.addActor(window);
	}
	
	@Override
	public void update(float deltaTime) {
		stage.act();
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
		window.setPosition(Gdx.graphics.getWidth() / 2 - window.getWidth() / 2, Gdx.graphics.getHeight() / 2 - window.getHeight() / 2);
	}
	
}
