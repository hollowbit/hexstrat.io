package net.hollowbit.strategygame.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.GameSettings;
import net.hollowbit.strategygame.screens.ColorPickScreen;
import net.hollowbit.strategygame.screens.MainMenuScreen;

public class NamePickerWindow extends Window {

	public NamePickerWindow(final GameSettings settings) {
		super("Pick your names:", StrategyGame.getGame().getSkin());
		
		this.setBounds(0, 0, 500, 200);
		this.setResizable(false);
		this.setMovable(false);
		
		final TextField player1Field = new TextField("Player 1", getSkin());
		player1Field.setMessageText("Player 1's Name");
		this.add(player1Field).padBottom(5).width(400);
		this.row();
		
		final TextField player2Field = new TextField("Player 2", getSkin());
		player2Field.setMessageText("Player 2's Name");
		this.add(player2Field).padBottom(5).width(400);
		
		this.row();
		
		TextButton backButton = new TextButton("Back", getSkin());
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				StrategyGame.getGame().getScreenManager().setScreen(new MainMenuScreen());
				super.clicked(event, x, y);
			}
		});
		this.add(backButton);
		
		TextButton nextButton = new TextButton("Next", getSkin());
		nextButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				settings.setPlayer1Name(player1Field.getText());
				settings.setPlayer2Name(player2Field.getText());
				StrategyGame.getGame().getScreenManager().setScreen(new ColorPickScreen(settings));
				super.clicked(event, x, y);
			}
		});
		this.add(nextButton);
	}

}
