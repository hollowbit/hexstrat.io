package net.hollowbit.strategygame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.GameSettings;
import net.hollowbit.strategygame.gamecomponents.Player;
import net.hollowbit.strategygame.tools.FontManager.Fonts;
import net.hollowbit.strategygame.tools.FontManager.Sizes;
import net.hollowbit.strategygame.tools.Screen;

public class GameOverScreen extends Screen {
	
	//Ui
	Stage stage;
	TextButton playAgainButton;
	TextButton mainMenuButton;
	
	Player playerWhoWon;
	
	public GameOverScreen (Player playerWhoWon, final GameSettings settings) {
		this.playerWhoWon = playerWhoWon;
		
		//Load ui
		stage = new Stage(StrategyGame.getGame().getUiCamera().getScreenViewport(), StrategyGame.getGame().getBatch());
		Gdx.input.setInputProcessor(stage);
		
		playAgainButton = new TextButton("Rematch", StrategyGame.getGame().getSkin());
		playAgainButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				StrategyGame.getGame().getScreenManager().setScreen(new GameScreen(settings));//When play again is clicked, open a new game screen
				super.clicked(event, x, y);
			}
		});
		playAgainButton.setPosition(Gdx.graphics.getWidth() / 2 - playAgainButton.getWidth() / 2, 300);
		stage.addActor(playAgainButton);
		
		mainMenuButton = new TextButton("Main Menu", StrategyGame.getGame().getSkin());
		mainMenuButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				StrategyGame.getGame().getScreenManager().setScreen(new MainMenuScreen());
				super.clicked(event, x, y);
			}
		});
		mainMenuButton.setPosition(Gdx.graphics.getWidth() / 2 - mainMenuButton.getWidth() / 2, 250);
		stage.addActor(mainMenuButton);
	}
	
	@Override
	public void update (float deltaTime) {
		stage.act();
	}
	
	@Override
	public void renderUi (SpriteBatch batch, float width, float height) {
		BitmapFont fontLarge = StrategyGame.getGame().getFontManager().getFont(Fonts.PIXELATED, Sizes.LARGE);
		BitmapFont fontMedium = StrategyGame.getGame().getFontManager().getFont(Fonts.PIXELATED, Sizes.MEDIUM);
		
		//Draw gameover label
		GlyphLayout gameOverLayout = new GlyphLayout(fontLarge, "Game Over");
		fontLarge.draw(batch, gameOverLayout, width / 2 - gameOverLayout.width / 2, height - gameOverLayout.height - 50);
		
		//Draw winning player
		GlyphLayout winnerLayout = new GlyphLayout(fontMedium, "Winner: [#" + playerWhoWon.getColor() + "]"+ playerWhoWon.getName());
		fontMedium.draw(batch, winnerLayout, width / 2 - winnerLayout.width / 2, height - winnerLayout.height - 150);
		
		batch.end();
		stage.draw();
		batch.begin();
	}
	
	@Override
	public void resize(int width, int height) {
		playAgainButton.setPosition(width / 2 - playAgainButton.getWidth() / 2, 300);
		mainMenuButton.setPosition(Gdx.graphics.getWidth() / 2 - mainMenuButton.getWidth() / 2, 250);
	}
	
}
