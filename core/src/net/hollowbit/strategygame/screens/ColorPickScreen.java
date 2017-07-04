package net.hollowbit.strategygame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.GameSettings;
import net.hollowbit.strategygame.tools.Screen;
import net.hollowbit.strategygame.ui.ColorPickerWindow;

public class ColorPickScreen extends Screen {
	
	ColorPickerWindow player1Picker;
	ColorPickerWindow player2Picker;
	TextButton backButton;
	TextButton nextButton;
	
	private Stage stage;
	
	public ColorPickScreen(final GameSettings settings) {
		stage = new Stage(StrategyGame.getGame().getUiCamera().getScreenViewport(), StrategyGame.getGame().getBatch());
		StrategyGame.getGame().getGameCamera().zoom(1);
		Gdx.input.setInputProcessor(stage);
		
		player1Picker = new ColorPickerWindow("" + settings.getPlayer1Name() + "'s Color:", settings.getPlayer1Color());
		player1Picker.setPosition(Gdx.graphics.getWidth() / 4 - player1Picker.getWidth() / 2, Gdx.graphics.getHeight() / 2 - player1Picker.getHeight() / 2);
		stage.addActor(player1Picker);
		
		player2Picker = new ColorPickerWindow("" + settings.getPlayer2Name() + "'s Color:", settings.getPlayer2Color());
		player2Picker.setPosition(Gdx.graphics.getWidth() / 4 * 3 - player1Picker.getWidth() / 2, Gdx.graphics.getHeight() / 2 - player2Picker.getHeight() / 2);
		stage.addActor(player2Picker);
		
		backButton = new TextButton("Back", StrategyGame.getGame().getSkin());
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				StrategyGame.getGame().getScreenManager().setScreen(new NamePickScreen(settings));
				super.clicked(event, x, y);
			}
		});
		backButton.setBounds(0, 0, 200, 50);
		backButton.setPosition(0, 0);
		stage.addActor(backButton);
		
		nextButton = new TextButton("Next", StrategyGame.getGame().getSkin());
		nextButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				StrategyGame.getGame().getScreenManager().setScreen(new MapPickerScreen(settings));
				super.clicked(event, x, y);
			}
		});
		nextButton.setBounds(0, 0, 200, 50);
		nextButton.setPosition(Gdx.graphics.getWidth() - nextButton.getWidth(), 0);
		stage.addActor(nextButton);
	}
	
	@Override
	public void renderUi(SpriteBatch batch, float width, float height) {
		batch.end();
		stage.draw();
		batch.begin();
		super.renderUi(batch, width, height);
	}
	
	@Override
	public void update(float deltaTime) {
		stage.act();
		super.update(deltaTime);
	}
	
	public void resize(int width, int height) {
		player1Picker.setPosition(Gdx.graphics.getWidth() / 4 - player1Picker.getWidth() / 2, Gdx.graphics.getHeight() / 2 - player1Picker.getHeight() / 2);
		player2Picker.setPosition(Gdx.graphics.getWidth() / 4 * 3 - player1Picker.getWidth() / 2, Gdx.graphics.getHeight() / 2 - player2Picker.getHeight() / 2);
		
		nextButton.setPosition(0, Gdx.graphics.getWidth() - nextButton.getWidth());
	};
	
}
