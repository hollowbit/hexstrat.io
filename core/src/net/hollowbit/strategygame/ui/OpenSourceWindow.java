package net.hollowbit.strategygame.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import net.hollowbit.strategygame.StrategyGame;

public class OpenSourceWindow extends Window {

	public OpenSourceWindow() {
		super("Open Source", StrategyGame.getGame().getSkin());
		
		this.setBounds(0, 0, 550, 400);
		this.setResizable(false);
		this.setMovable(false);
		
		Label infoLabel = new Label("This game is completely open source and is under the MIT License. This means that you can do anything you want with the assets as long as the license is included in it and credit is given where due. Github link is available below.", getSkin());
		infoLabel.setWrap(true);
		infoLabel.setWidth(400);
		infoLabel.setAlignment(Align.center);
		this.add(infoLabel).width(400).height(300).center().padBottom(10);
		this.row();
		
		TextButton githubButton = new TextButton("Github", getSkin());
		githubButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.net.openURI("https://github.com/vedi0boy/hexstrat.io");
				super.clicked(event, x, y);
			}
		});
		this.add(githubButton);
		
		TextButton closeButton = new TextButton("Close", getSkin());
		closeButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				remove();
				super.clicked(event, x, y);
			}
		});
		this.add(closeButton);
	}

}
