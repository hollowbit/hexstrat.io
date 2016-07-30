package net.hollowbit.strategygame.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.screens.GameScreen;
import net.hollowbit.strategygame.units.Village;
import net.hollowbit.strategygame.units.Village.BuildType;

public class BuildWindow extends Window {
	
	List<BuildType> unitList;
	Label costLabel;
	Label descLabel;
	TextButton buildButton;
	
	Village village;
	
	public BuildWindow (final GameScreen gameScreen, final Village village) {
		super("Build Units", StrategyGame.getGame().getSkin());
		
		this.village = village;
		
		this.setMovable(false);
		this.padLeft(20);
		this.padRight(20);
		this.padBottom(20);
		this.setWidth(400);
		
		//Add ui elements
		unitList = new List<BuildType>(getSkin());
		unitList.setItems(Village.BuildType.values());
		unitList.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				costLabel.setText("Cost: " + (village.isFirstBuild() ? 1:Village.BuildType.values()[unitList.getSelectedIndex()].prodNeeded));
				descLabel.setText("" + Village.BuildType.values()[unitList.getSelectedIndex()].desc);
			}
		});
		add(unitList);
		row();
		
		costLabel = new Label("Cost: " + (village.isFirstBuild() ? 1:Village.BuildType.values()[unitList.getSelectedIndex()].prodNeeded), getSkin(), "large");
		add(costLabel);
		row();
				
		descLabel = new Label("" + Village.BuildType.values()[unitList.getSelectedIndex()].desc, getSkin());
		descLabel.setWrap(true);
		descLabel.setWidth(400);
		add(descLabel).width(400);
		row();
		
		final BuildWindow window = this;
		
		buildButton = new TextButton("Build", getSkin());
		buildButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				village.build(Village.BuildType.values()[unitList.getSelectedIndex()]);
				gameScreen.closeWindow();
				window.remove();
				village.setFinishedTurn(true);
				gameScreen.selectNextUnit();
				gameScreen.resetUnitMoveButtons();
				super.clicked(event, x, y);
			}
		});
		add(buildButton);
		
		pack();
	}

}
