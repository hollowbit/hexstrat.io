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
import net.hollowbit.strategygame.gamecomponents.Player;
import net.hollowbit.strategygame.tools.FontManager.Fonts;
import net.hollowbit.strategygame.tools.FontManager.Sizes;
import net.hollowbit.strategygame.tools.Screen;

public class GameOverScreen extends Screen {
	
	//Ui
	Stage stage;
	TextButton playAgainButton;
	
	Player playerWhoWon;
	
	public GameOverScreen (Player playerWhoWon) {
		this.playerWhoWon = playerWhoWon;
		
		//Load ui
		stage = new Stage(StrategyGame.getGame().getUiCamera().getScreenViewport(), StrategyGame.getGame().getBatch());
		Gdx.input.setInputProcessor(stage);
		
		playAgainButton = new TextButton("Play Again", StrategyGame.getGame().getSkin());
		playAgainButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				StrategyGame.getGame().getScreenManager().setScreen(new MapPickerScreen());//When play again is clicked, open a new game screen
				super.clicked(event, x, y);
			}
		});
		playAgainButton.setPosition(Gdx.graphics.getWidth() / 2 - playAgainButton.getWidth() / 2, 300);
		stage.addActor(playAgainButton);
		
		
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
	}
	
}
