package net.hollowbit.strategygame.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.GameSettings;
import net.hollowbit.strategygame.screens.GameScreen;
import net.hollowbit.strategygame.world.MapType;

public class MapPickerWindow extends Window {

	TextButton playButton;
	List<MapType> list;
	
	public MapPickerWindow() {
		super("Choose Map:", StrategyGame.getGame().getSkin());
		
		this.setMovable(false);
		this.setResizable(false);
		this.setBounds(0, 0, 400, 300);
		
		list = new List<MapType>(StrategyGame.getGame().getSkin());
		list.setItems(MapType.values());
		list.setPosition(Gdx.graphics.getWidth() / 2 - list.getWidth() / 2, 350);
		add(list).pad(5);
		row();
		
		playButton = new TextButton("Play", StrategyGame.getGame().getSkin());
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				GameSettings settings = new GameSettings();
				settings.setMapId(list.getSelected().id);
				StrategyGame.getGame().getScreenManager().setScreen(new GameScreen(settings));//When play again is clicked, open a new game screen
				super.clicked(event, x, y);
			}
		});
		playButton.setBounds(0, 0, 250, 70);
		playButton.setPosition(Gdx.graphics.getWidth() / 2 - playButton.getWidth() / 2, 200);
		add(playButton);
	}

}
