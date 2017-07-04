package net.hollowbit.strategygame.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.GameSettings;
import net.hollowbit.strategygame.screens.GameScreen;
import net.hollowbit.strategygame.world.MapType;

public class MapPickerWindow extends Window {

	TextButton playButton;
	Label descLabel;
	List<MapType> list;
	
	public MapPickerWindow(final GameSettings settings) {
		super("Choose Map:", StrategyGame.getGame().getSkin());
		
		this.setMovable(false);
		this.setResizable(false);
		this.setBounds(0, 0, 550, 400);
		
		
		list = new List<MapType>(StrategyGame.getGame().getSkin());
		list.setItems(MapType.values());
		list.setPosition(Gdx.graphics.getWidth() / 2 - list.getWidth() / 2, 350);
		list.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				descLabel.setText("" + MapType.values()[list.getSelectedIndex()].getData().desc);
			}
		});
		add(list).pad(5);
		row();
		
		descLabel = new Label("" + MapType.values()[list.getSelectedIndex()].getData().desc, getSkin());
		descLabel.setWrap(true);
		descLabel.setWidth(500);
		add(descLabel).width(500).height(150);
		row();
		
		playButton = new TextButton("Play", StrategyGame.getGame().getSkin());
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
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
