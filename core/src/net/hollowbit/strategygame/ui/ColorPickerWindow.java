package net.hollowbit.strategygame.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import net.hollowbit.strategygame.StrategyGame;

public class ColorPickerWindow extends Window {

	public ColorPickerWindow(String title, final Color color) {
		super(title, StrategyGame.getGame().getSkin());
		
		this.setBounds(0, 0, 450, 400);
		this.setResizable(false);
		this.setMovable(false);
		
		ColorDisplay display = new ColorDisplay(color);
		this.add(display).width(150).height(150).center();
		this.row();
		
		Label labelR = new Label("R", getSkin());
		this.add(labelR);
		
		final Slider sliderR = new Slider(0, 1, 0.01f, false, getSkin());
		sliderR.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				color.r = sliderR.getValue();
			}
		});
		sliderR.setValue(color.r);
		this.add(sliderR);
		this.row();
		
		Label labelG = new Label("G", getSkin());
		this.add(labelG);
		
		final Slider sliderG = new Slider(0, 1, 0.01f, false, getSkin());
		sliderG.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				color.g = sliderG.getValue();
			}
		});
		sliderG.setValue(color.g);
		this.add(sliderG);
		this.row();
		
		Label labelB = new Label("B", getSkin());
		this.add(labelB);
		
		final Slider sliderB = new Slider(0, 1, 0.01f, false, getSkin());
		sliderB.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				color.b = sliderB.getValue();
			}
		});
		sliderB.setValue(color.b);
		this.add(sliderB);
		this.row();
	}

}
