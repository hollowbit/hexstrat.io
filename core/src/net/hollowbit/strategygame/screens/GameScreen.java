package net.hollowbit.strategygame.screens;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.Player;
import net.hollowbit.strategygame.gamecomponents.TurnType;
import net.hollowbit.strategygame.tools.FontManager.Fonts;
import net.hollowbit.strategygame.tools.FontManager.Sizes;
import net.hollowbit.strategygame.tools.HexTouchChecker;
import net.hollowbit.strategygame.tools.HexTouchListener;
import net.hollowbit.strategygame.tools.Screen;
import net.hollowbit.strategygame.ui.BuildWindow;
import net.hollowbit.strategygame.units.Unit;
import net.hollowbit.strategygame.units.Village;
import net.hollowbit.strategygame.world.Hex;
import net.hollowbit.strategygame.world.Hex.OverlayColor;
import net.hollowbit.strategygame.world.World;

public class GameScreen extends Screen implements InputProcessor {
	
	private static final int END_TURN_BUTTON_PADDING = 10;
	
	Stage stage;
	World world;
	Player currentPlayer;
	Player player1;
	Player player2;
	
	Unit selectedUnit = null;//Null if no unit was selected
	
	HexTouchChecker hexTouchChecker;
	
	ArrayList<HexTouchListener> hexTouchListeners;
	
	//Action buttons
	ArrayList<TextButton> turnTypeButtons;
	
	boolean endTurnFlag = false;;
	
	TextButton endTurnButton;
	BuildWindow buildWindow = null;
	
	boolean windowOpen = false;
	
	public GameScreen () {
		stage = new Stage(StrategyGame.getGame().getUiCamera().getScreenViewport(), StrategyGame.getGame().getBatch());
		world = new World("grasslands");
		
		turnTypeButtons = new ArrayList<TextButton>();
		hexTouchListeners = new ArrayList<HexTouchListener>();
		
		//Input related
		InputMultiplexer inputMultiplexer = new InputMultiplexer(this, stage);
		Gdx.input.setInputProcessor(inputMultiplexer);
		hexTouchChecker = new HexTouchChecker();

		player1 = new Player("Player 1", Color.RED);
		player2 = new Player("Player 2", Color.BLUE);
		currentPlayer = player1;
		
		//Give each player a village to start
		world.addUnit(new Village(world, player1, world.getSpawn1()));
		world.addUnit(new Village(world, player2, world.getSpawn2()));
		
		//Add ui
		endTurnButton = new TextButton("End Turn", StrategyGame.getGame().getSkin(), "large");
		endTurnButton.setBounds(0, 0, 300, 50);
		endTurnButton.setPosition(Gdx.graphics.getWidth() - endTurnButton.getWidth() - END_TURN_BUTTON_PADDING, END_TURN_BUTTON_PADDING);
		endTurnButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				endTurn();
				super.clicked(event, x, y);
			}
		});
		stage.addActor(endTurnButton);
		
		resetFog();
		startTurn();
	}
	
	@Override
	public void update(float deltaTime) {
		if (endTurnFlag)
			endTurn();
		
		if (selectedUnit.isFinishedTurn()) {
			for (Unit unit : currentPlayer.getUnits()) {
				if (!unit.isFinishedTurn()) {
					selectUnit(unit);
				}
			}
		}
		
		stage.act();
		world.update(deltaTime);
	}
	
	private void selectUnit (Unit unit) {
		//Dispose of current unit turn types before setting a new one
		disposeOfSelectedUnit();
		selectedUnit = unit;
		StrategyGame.getGame().getGameCamera().setGoal(new Vector2(selectedUnit.getX(), selectedUnit.getY()));
		
		//If the unit belongs to the current player, allow doing a turn with it, otherwise, just display its stats
		if (currentPlayer.doesUnitBelongToPlayer(unit)) {
			selectedUnit.focusedOn(this);
			resetUnitMoveButtons();
		}
	}
	
	//Reset buttons if some are now incapacitated
	public void resetUnitMoveButtons () {
		for (TextButton turnButton : turnTypeButtons)
			turnButton.remove();
		turnTypeButtons.clear();
		
		ArrayList<TurnType> turnTypes = new ArrayList<TurnType>();
		turnTypes.add(selectedUnit.getDefaultTurnType());//Add default turn type button
		
		if (selectedUnit.getTurnTypes() != null)
			turnTypes.addAll(Arrays.asList(selectedUnit.getTurnTypes()));
		
		//Add buttons for each turn type
		int indexOfButtons = 0;
		for (final TurnType turnType : turnTypes) {
			if (!turnType.usable())//If turn type button is not usable, skip it
				continue;
			
			TextButton turnTypeButton = turnType.getTurnButton();
			final GameScreen gameScreen = this;
			turnTypeButton.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if (!windowOpen)
						turnType.initiate(gameScreen);
					super.clicked(event, x, y);
				}
			});
			turnTypeButton.setPosition(15, 500 - indexOfButtons * 75);
			turnTypeButtons.add(turnTypeButton);
			stage.addActor(turnTypeButton);
			indexOfButtons++;
		}
	}
	
	@Override
	public void resize(int width, int height) {
		endTurnButton.setPosition(width - endTurnButton.getWidth() - END_TURN_BUTTON_PADDING, END_TURN_BUTTON_PADDING);
		if (buildWindow != null)
			buildWindow.setPosition(width / 2 - buildWindow.getWidth() / 2, height / 2 - buildWindow.getHeight() / 2);
		super.resize(width, height);
	}
	
	@Override
	public void render(SpriteBatch batch, float width, float height) {
		world.render(batch, currentPlayer);
		
		if (selectedUnit != null)
			batch.draw(StrategyGame.getGame().getAssetManager().getTexture("selected-hex-border"), selectedUnit.getX(), selectedUnit.getY());
	}
	
	@Override
	public void renderUi(SpriteBatch batch, float width, float height) {
		BitmapFont fontMedium = StrategyGame.getGame().getFontManager().getFont(Fonts.PIXELATED, Sizes.MEDIUM);
		BitmapFont fontSmall = StrategyGame.getGame().getFontManager().getFont(Fonts.PIXELATED, Sizes.SMALL);
		GlyphLayout turnLblLayout = new GlyphLayout(fontMedium, currentPlayer.getName() + "'s turn!");
		fontMedium.draw(batch, turnLblLayout, width / 2 - turnLblLayout.width / 2, height - turnLblLayout.height - 10);
		
		//Draw labels for info on selected unit, friendly or not
		if (selectedUnit != null) {
			GlyphLayout healthStatLayout = new GlyphLayout(fontSmall, "Health: " + selectedUnit.getHealth() + "/" + selectedUnit.getMaxHealth());
			fontSmall.draw(batch, healthStatLayout, width - healthStatLayout.width - 10, height - 100);
			GlyphLayout ownerLayout = new GlyphLayout(fontSmall, "Owner: [#" + selectedUnit.getPlayer().getColor() + "]" + selectedUnit.getPlayer().getName());
			fontSmall.draw(batch, ownerLayout, width - ownerLayout.width - 10, height - 140);
			
			if (currentPlayer.doesUnitBelongToPlayer(selectedUnit)) {
				GlyphLayout actionsLayout = new GlyphLayout(fontSmall, "Actions:");
				fontSmall.draw(batch, actionsLayout, 15, 550);
				
				//If no actions for this unit, show message
				if (turnTypeButtons.size() == 0) {
					GlyphLayout actionsNoneLayout = new GlyphLayout(fontSmall, "None Left.");
					fontSmall.draw(batch, actionsNoneLayout, 15, 520);
				}
			}
		}
		
		batch.end();
		stage.draw();
		batch.begin();
	}
	
	public void endTurn () {
		endTurnFlag = false;
		disposeOfSelectedUnit();
		if (currentPlayer == player1)
			currentPlayer = player2;
		else
			currentPlayer = player1;

		resetFog();
		
		//Start turn
		startTurn();
	}
	
	public void startTurn () {
		//Loop through all player's units and run startTurn
		ArrayList<Unit> currentUnits = new ArrayList<Unit>();
		currentUnits.addAll(currentPlayer.getUnits());
		for (Unit unit : currentUnits)
			unit.turnStart();
		
		//Depending on whether the village is ready to build something new, zoom to it, or the last used unit
		if (!currentPlayer.getVillage().isDoneBuilding())
			//If no unit was last used, zoom to village, but don't select it
			if (player1.getLastMovedUnit() != null)
				selectUnit(currentPlayer.getLastMovedUnit());
			else
				StrategyGame.getGame().getGameCamera().setGoal(new Vector2(currentPlayer.getVillage().getX(), currentPlayer.getVillage().getY()));
		else {
			selectUnit(currentPlayer.getVillage());
		}
	}
	
	public void flagTurnAsDone () {
		endTurnFlag = true;
	}
	
	public void disposeOfSelectedUnit () {
		if (selectedUnit != null) {
			selectedUnit.getDefaultTurnType().dispose(this);
			if (selectedUnit.getTurnTypes() != null) {
				for (TurnType turnType : selectedUnit.getTurnTypes())
					turnType.dispose(this);
			}
			for (TextButton turnTypeButton : turnTypeButtons)
				turnTypeButton.remove();
			turnTypeButtons.clear();
			currentPlayer.setLastMovedUnit(selectedUnit);
			selectedUnit = null;
		}
	}
	
	public void resetFog () {
		//Make the map foggy
		for (int row = 0; row < world.getMap().getHeight(); row++) {
			for (int col = 0; col < world.getMap().getWidth(); col++) {
				world.getMap().getMap()[row][col].setOverlayColor(OverlayColor.FOG);
			}
		}
		
		//Remove fog based on player's units
		for (Unit unit : currentPlayer.getUnits()) {
			Hex unitHex = unit.getHex();
			unitHex.setOverlayColor(OverlayColor.NONE);
			for (Hex hex : unitHex.getSurroundingHexesInRange(unit.getVisibilityRange())) 
				hex.setOverlayColor(OverlayColor.NONE);
		}
	}
	
	public void addHexTouchListener (HexTouchListener hexTouchListener) {
		hexTouchListeners.add(hexTouchListener);
	}
	
	public void removeHexTouchListener (HexTouchListener hexTouchListener) {
		hexTouchListeners.remove(hexTouchListener);
	}
	
	public void openBuildWindow (Village village) {
		windowOpen = true;
		buildWindow = new BuildWindow(this, village);
		buildWindow.setPosition(Gdx.graphics.getWidth() / 2 - buildWindow.getWidth() / 2, Gdx.graphics.getHeight() / 2 - buildWindow.getHeight() / 2);
		stage.addActor(buildWindow);
	}
	
	public void closeWindow () {
		windowOpen = false;
	}

	@Override
	public boolean keyDown(int keycode) {return false;}

	@Override
	public boolean keyUp(int keycode) {return false;}

	@Override
	public boolean keyTyped(char character) {return false;}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (windowOpen)
			return false;
		
		Vector2 touchCoords = StrategyGame.getGame().getGameCamera().unproject(new Vector2(screenX, screenY));
		hexTouchChecker.touchDown((int) touchCoords.x, (int) touchCoords.y);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (windowOpen)
			return false;
		
		Vector2 touchCoords = StrategyGame.getGame().getGameCamera().unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
		Hex hexTouched = hexTouchChecker.getTouchedHex((int) touchCoords.x, (int) touchCoords.y, world.getMap());
		//If the touched hex has a unit, set it as selected
		if (hexTouched != null) {
			
			boolean hexHandled = false;
			//Loop through each listener and loop
			for (HexTouchListener hexTouchListener : hexTouchListeners) {
				if (hexTouchListener.hexTouched(hexTouched, this))
					hexHandled = true;
			}
			
			if (!hexHandled) {
				if (hexTouched.getUnitOnHex() != null && hexTouched.getUnitOnHex() != selectedUnit)
					selectUnit(hexTouched.getUnitOnHex());
			}
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {return false;}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {return false;}

	@Override
	public boolean scrolled(int amount) {return false;}
	
}
