package net.hollowbit.strategygame.screens;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.GameSettings;
import net.hollowbit.strategygame.tools.Screen;
import net.hollowbit.strategygame.ui.OpenSourceWindow;

public class MainMenuScreen extends Screen {
	
	private static final int PADDING = 10;
	
	private Stage stage;
	private TextButton playButton;
	private TextButton openSourceButton;
	private TextButton exitButton;
	private TextButton muteButton;
	private OpenSourceWindow openSourceWindow;
	
	private Texture logo;
	
	public MainMenuScreen() {
		stage = new Stage(StrategyGame.getGame().getUiCamera().getScreenViewport(), StrategyGame.getGame().getBatch());
		StrategyGame.getGame().getGameCamera().zoom(1);
		Gdx.input.setInputProcessor(stage);
		StrategyGame.getGame().getMusicManager().playMainMenuMusic();
		
		logo = new Texture("logo.png");
		
		playButton = new TextButton("Play", StrategyGame.getGame().getSkin());
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				StrategyGame.getGame().getScreenManager().setScreen(new NamePickScreen(new GameSettings()));
				super.clicked(event, x, y);
			}
		});
		playButton.setPosition(Gdx.graphics.getWidth() / 2 - playButton.getWidth() / 2, Gdx.graphics.getHeight() / 2 - playButton.getHeight());
		stage.addActor(playButton);
		
		openSourceButton = new TextButton("Open Source", StrategyGame.getGame().getSkin());
		openSourceButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				openSourceWindow = new OpenSourceWindow();
				openSourceWindow.setPosition(Gdx.graphics.getWidth() / 2 - openSourceWindow.getWidth() / 2, Gdx.graphics.getHeight() / 2 - openSourceWindow.getHeight() / 2);
				stage.addActor(openSourceWindow);
				super.clicked(event, x, y);
			}
		});
		openSourceButton.setPosition(Gdx.graphics.getWidth() / 2 - openSourceButton.getWidth() / 2, Gdx.graphics.getHeight() / 2 - openSourceButton.getHeight() - playButton.getHeight() - PADDING);
		stage.addActor(openSourceButton);

		
		exitButton = new TextButton("Exit", StrategyGame.getGame().getSkin());
		exitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
				super.clicked(event, x, y);
			}
		});
		exitButton.setPosition(Gdx.graphics.getWidth() / 2 - exitButton.getWidth() / 2, Gdx.graphics.getHeight() / 2 - exitButton.getHeight() - playButton.getHeight() - exitButton.getHeight() - PADDING * 2);
		if (Gdx.app.getType() != ApplicationType.WebGL)
			stage.addActor(exitButton);
		
		muteButton = new TextButton(StrategyGame.getGame().getMusicManager().isMute() ? "Unmute": "Mute", StrategyGame.getGame().getSkin());
		muteButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (StrategyGame.getGame().getMusicManager().toggleMute())
					muteButton.setText("Unmute");
				else
					muteButton.setText("Mute");
				super.clicked(event, x, y);
			}
		});
		muteButton.setWidth(200);
		muteButton.setPosition(Gdx.graphics.getWidth() - muteButton.getWidth() - 5, 5);
		stage.addActor(muteButton);
	}
	
	@Override
	public void resize(int width, int height) {
		playButton.setPosition(Gdx.graphics.getWidth() / 2 - playButton.getWidth() / 2, Gdx.graphics.getHeight() / 2 - playButton.getHeight());
		openSourceButton.setPosition(Gdx.graphics.getWidth() / 2 - openSourceButton.getWidth() / 2, Gdx.graphics.getHeight() / 2 - openSourceButton.getHeight() - playButton.getHeight() - PADDING);
		exitButton.setPosition(Gdx.graphics.getWidth() / 2 - exitButton.getWidth() / 2, Gdx.graphics.getHeight() / 2 - exitButton.getHeight() - playButton.getHeight() - exitButton.getHeight() - PADDING * 2);
		muteButton.setPosition(Gdx.graphics.getWidth() - muteButton.getWidth() - 5, 5);
		
		if (openSourceWindow != null)
			openSourceWindow.setPosition(width / 2 - openSourceWindow.getWidth() / 2, height / 2 - openSourceWindow.getHeight() / 2);
		
		super.resize(width, height);
	}
	
	@Override
	public void update(float deltaTime) {
		stage.act();
		super.update(deltaTime);
	}
	
	@Override
	public void renderUi(SpriteBatch batch, float width, float height) {
		batch.draw(logo, width / 2 - logo.getWidth() / 2, height - logo.getHeight() - 50);
		
		batch.end();
		stage.draw();
		batch.begin();
		super.renderUi(batch, width, height);
	}
	
}
