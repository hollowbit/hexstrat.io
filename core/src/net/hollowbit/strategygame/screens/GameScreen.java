package net.hollowbit.strategygame.screens;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.GameSettings;
import net.hollowbit.strategygame.gamecomponents.Player;
import net.hollowbit.strategygame.gamecomponents.TurnType;
import net.hollowbit.strategygame.tools.FontManager.Fonts;
import net.hollowbit.strategygame.tools.FontManager.Sizes;
import net.hollowbit.strategygame.tools.HexMessageManager;
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
	private static final float MIN_ZOOM = 0.3f;
	private static final float MAX_ZOOM = 2.1f;
	
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
	TextButton zoomInButton;
	TextButton zoomOutButton;
	TextButton exitButton;
	
	BuildWindow buildWindow = null;
	
	boolean windowOpen = false;
	
	TurnType selectedTurnType = null;
	
	HexMessageManager hexMessageManager;
	boolean canMoveUnit = false;
	
	boolean privacyMode;//enabled to prevent players from seeing the other player's game between hotseat turns
	GameSettings settings;
	
	public GameScreen (GameSettings settings) {
		this.settings = settings;
		stage = new Stage(StrategyGame.getGame().getUiCamera().getScreenViewport(), StrategyGame.getGame().getBatch());
		world = new World(settings.getMapId());
		hexMessageManager = new HexMessageManager();
		StrategyGame.getGame().getMusicManager().playNextTrack();
		
		StrategyGame.getGame().getGameCamera().zoom(1);
		
		turnTypeButtons = new ArrayList<TextButton>();
		hexTouchListeners = new ArrayList<HexTouchListener>();
		
		//Input related
		InputMultiplexer inputMultiplexer = new InputMultiplexer(this, stage);
		Gdx.input.setInputProcessor(inputMultiplexer);
		hexTouchChecker = new HexTouchChecker();

		player1 = new Player(settings.getPlayer1Name(), settings.getPlayer1Color());
		player2 = new Player(settings.getPlayer2Name(), settings.getPlayer2Color());
		currentPlayer = player1;
		privacyMode = true;
		
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
				if (!windowOpen) {
					if (canMoveUnit)
						selectNextUnit();
					else
						endTurn();
				}
				super.clicked(event, x, y);
			}
		});
		stage.addActor(endTurnButton);
		
		exitButton = new TextButton("End Game", StrategyGame.getGame().getSkin());
		exitButton.setPosition(Gdx.graphics.getWidth() - exitButton.getWidth() - END_TURN_BUTTON_PADDING, Gdx.graphics.getHeight() - END_TURN_BUTTON_PADDING - exitButton.getHeight());
		exitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showConfirmExitWindow();
				super.clicked(event, x, y);
			}
		});
		stage.addActor(exitButton);
		
		zoomInButton = new TextButton("+", StrategyGame.getGame().getSkin(), "large");
		zoomOutButton = new TextButton("-", StrategyGame.getGame().getSkin(), "large");
		zoomInButton.setBounds(0, 0, 50, 22);
		zoomInButton.setPosition(Gdx.graphics.getWidth() - endTurnButton.getWidth() - END_TURN_BUTTON_PADDING * 2 - zoomInButton.getWidth(), END_TURN_BUTTON_PADDING + 22 + 6);
		zoomInButton.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				float zoom = StrategyGame.getGame().getGameCamera().getZoom();
				if (zoom > MIN_ZOOM) {
					zoom -= 0.3f;
				}

				if (zoom <= MIN_ZOOM) {
					zoomInButton.setDisabled(true);
					zoom = MIN_ZOOM;
				}
				
				StrategyGame.getGame().getGameCamera().zoom(zoom);
				
				zoomOutButton.setDisabled(false);
				super.clicked(event, x, y);
			}
			
		});
		stage.addActor(zoomInButton);
		
		zoomOutButton.setBounds(0, 0, 50, 22);
		zoomOutButton.setPosition(Gdx.graphics.getWidth() - endTurnButton.getWidth() - END_TURN_BUTTON_PADDING * 2 - zoomOutButton.getWidth(), END_TURN_BUTTON_PADDING);
		zoomOutButton.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				float zoom = StrategyGame.getGame().getGameCamera().getZoom();
				if (zoom < MAX_ZOOM) {
					zoom += 0.3f;
				}

				if (zoom >= MAX_ZOOM) {
					zoomOutButton.setDisabled(true);
					zoom = MAX_ZOOM;
				}
				
				StrategyGame.getGame().getGameCamera().zoom(zoom);
				
				zoomInButton.setDisabled(false);
				super.clicked(event, x, y);
			}
			
		});
		stage.addActor(zoomOutButton);
		
		resetFog();
		startTurn();
	}
	
	@Override
	public void update(float deltaTime) {
		if (endTurnFlag)
			endTurn();
		
		stage.act();
		world.update(deltaTime);
		hexMessageManager.update(deltaTime);
	}
	
	/**
	 * Returns whether there was a next unit to select
	 * @return
	 */
	public boolean selectNextUnit () {
		//Loop through player's units to find an unused one
		boolean foundUnit = false;
		for (Unit unit : currentPlayer.getUnits()) {
			if (!unit.isFinishedTurn() && unit != selectedUnit) {
				selectUnit(unit);
				foundUnit = true;
			}
		}
		
		if (!foundUnit) {
			endTurnButton.setText("End Turn");
			canMoveUnit = false;
		}
		return foundUnit;
	}
	
	public void selectUnit (Unit unit) {
		boolean foundUnit = false;
		for (Unit testUnit : currentPlayer.getUnits()) {
			if (!testUnit.isFinishedTurn() && testUnit != unit)
				foundUnit = true;
		}
		
		if (!foundUnit) {
			endTurnButton.setText("End Turn");
			canMoveUnit = false;
		}
		
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
		
		//if (selectedUnit.isFinishedTurn())
			//return;
		
		ArrayList<TurnType> turnTypes = new ArrayList<TurnType>();
		if (selectedUnit.getDefaultTurnType() != null) {
			turnTypes.add(selectedUnit.getDefaultTurnType());//Add default turn type button
			selectedTurnType = selectedUnit.getDefaultTurnType();
		}
		
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
					if (!windowOpen) {
						if (selectedTurnType != null)
							selectedTurnType.dispose(gameScreen);
						selectedTurnType = turnType;
						turnType.initiate(gameScreen);
					}
					super.clicked(event, x, y);
				}
			});
			turnTypeButton.setPosition(15, 300 - indexOfButtons * 40);
			turnTypeButtons.add(turnTypeButton);
			stage.addActor(turnTypeButton);
			indexOfButtons++;
		}
	}
	
	public void setSelectedTurnType (TurnType turnType) {
		this.selectedTurnType = turnType;
	}
	
	@Override
	public void resize(int width, int height) {
		endTurnButton.setPosition(width - endTurnButton.getWidth() - END_TURN_BUTTON_PADDING, END_TURN_BUTTON_PADDING);
		zoomOutButton.setPosition(Gdx.graphics.getWidth() - endTurnButton.getWidth() - END_TURN_BUTTON_PADDING * 2 - zoomOutButton.getWidth(), END_TURN_BUTTON_PADDING);
		zoomInButton.setPosition(Gdx.graphics.getWidth() - endTurnButton.getWidth() - END_TURN_BUTTON_PADDING * 2 - zoomInButton.getWidth(), END_TURN_BUTTON_PADDING + 22 + 6);
		exitButton.setPosition(Gdx.graphics.getWidth() - exitButton.getWidth() - END_TURN_BUTTON_PADDING, Gdx.graphics.getHeight() - END_TURN_BUTTON_PADDING - exitButton.getHeight());
		
		if (buildWindow != null)
			buildWindow.setPosition(width / 2 - buildWindow.getWidth() / 2, height / 2 - buildWindow.getHeight() / 2);
		super.resize(width, height);
	}
	
	@Override
	public void render(SpriteBatch batch, float width, float height) {
		Gdx.gl.glClearColor(world.getMap().getType().getBackgroundColor().r, world.getMap().getType().getBackgroundColor().g, world.getMap().getType().getBackgroundColor().b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if (!privacyMode) {
			world.renderHexes(batch);
		
			if (selectedUnit != null)
				batch.draw(StrategyGame.getGame().getAssetManager().getTexture("selected-hex-border"), selectedUnit.getX(), selectedUnit.getY());
			
			world.renderUnits(batch);
			
			hexMessageManager.render(batch);
		}
	}
	
	@Override
	public void renderUi(SpriteBatch batch, float width, float height) {
		BitmapFont fontMedium = StrategyGame.getGame().getFontManager().getFont(Fonts.PIXELATED, Sizes.MEDIUM);
		BitmapFont fontSmall = StrategyGame.getGame().getFontManager().getFont(Fonts.PIXELATED, Sizes.SMALL);
		if (privacyMode) {
			GlyphLayout turnLblLayout = new GlyphLayout(fontMedium, "[#" + currentPlayer.getColor() + "]" + currentPlayer.getName() + "'s [WHITE]turn!");
			fontMedium.draw(batch, turnLblLayout, width / 2 - turnLblLayout.width / 2, height - turnLblLayout.height - 10);
			GlyphLayout tapToStartTurnLayout = new GlyphLayout(fontSmall, "Tap to start turn!");
			fontSmall.draw(batch, tapToStartTurnLayout, width / 2 - tapToStartTurnLayout.width / 2, height / 2 - tapToStartTurnLayout.height / 2);
		} else {
			GlyphLayout productionLayout = new GlyphLayout(fontSmall, "Food: +" + currentPlayer.getProduction());
			fontSmall.draw(batch, productionLayout, 10, height - productionLayout.height - 10);
			
			//Draw labels for info on selected unit, friendly or not
			if (selectedUnit != null) {
				GlyphLayout healthStatLayout = new GlyphLayout(fontSmall, "Health: " + selectedUnit.getHealth() + "/" + selectedUnit.getMaxHealth());
				fontSmall.draw(batch, healthStatLayout, width - healthStatLayout.width - 10, height - 100);
				GlyphLayout ownerLayout = new GlyphLayout(fontSmall, "Owner: [#" + selectedUnit.getPlayer().getColor() + "]" + selectedUnit.getPlayer().getName());
				fontSmall.draw(batch, ownerLayout, width - ownerLayout.width - 10, height - 140);
				
				if (currentPlayer.doesUnitBelongToPlayer(selectedUnit)) {
					GlyphLayout actionsLayout = new GlyphLayout(fontSmall, "Actions:");
					fontSmall.draw(batch, actionsLayout, 15, 350);
					
					//If no actions for this unit, show message
					if (turnTypeButtons.size() == 0) {
						GlyphLayout actionsNoneLayout = new GlyphLayout(fontSmall, "None Left.");
						fontSmall.draw(batch, actionsNoneLayout, 15, 320);
					}
				}
			}
		
			batch.end();
			stage.draw();
			batch.begin();
		}
	}
	
	public void endTurn () {
		endTurnFlag = false;
		disposeOfSelectedUnit();
		if (currentPlayer == player1)
			currentPlayer = player2;
		else
			currentPlayer = player1;

		resetFog();
		privacyMode = true;
		
		//Start turn
		startTurn();
	}
	
	public void startTurn () {
		hexMessageManager.startTurn();
		currentPlayer.turnStart();//Start turn for player
		//Loop through all player's units and run startTurn
		ArrayList<Unit> currentUnits = new ArrayList<Unit>();
		currentUnits.addAll(currentPlayer.getUnits());
		for (Unit unit : currentUnits)
			unit.turnStart(this);
		
		endTurnButton.setText("Next Unit");
		canMoveUnit = true;
		
		//Depending on whether the village is ready to build something new, zoom to it, or the last used unit
		if (!currentPlayer.getVillage().isDoneBuilding()) {
			//If no unit was last used, zoom to village, but don't select it
			if (currentPlayer.getLastMovedUnit() != null && !currentPlayer.getLastMovedUnit().isDead() && !currentPlayer.getLastMovedUnit().isFinishedTurn())
				selectUnit(currentPlayer.getLastMovedUnit());
			else if (currentPlayer.getLastMovedUnit().isDead()) {
				if (!selectNextUnit())
					StrategyGame.getGame().getGameCamera().setGoal(new Vector2(currentPlayer.getVillage().getX(), currentPlayer.getVillage().getY()));
			} else
				StrategyGame.getGame().getGameCamera().setGoal(new Vector2(currentPlayer.getVillage().getX(), currentPlayer.getVillage().getY()));
		} else {
			selectUnit(currentPlayer.getVillage());
		}
	}
	
	public void flagTurnAsDone () {
		endTurnFlag = true;
	}
	
	public void disposeOfSelectedUnit () {
		if (selectedUnit != null) {
			if (selectedUnit.getDefaultTurnType() != null)
				selectedUnit.getDefaultTurnType().dispose(this);
			if (selectedUnit.getTurnTypes() != null) {
				for (TurnType turnType : selectedUnit.getTurnTypes())
					turnType.dispose(this);
			}
			for (TextButton turnTypeButton : turnTypeButtons)
				turnTypeButton.remove();
			turnTypeButtons.clear();
			if (selectedUnit != null && currentPlayer.doesUnitBelongToPlayer(selectedUnit))//Make sure the unit is not null and actually belongs to player
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
	
	public void showErrorWindow (String error) {
		Dialog dialog = new Dialog("Error", StrategyGame.getGame().getSkin(), "dialog") {
		    public void result(Object obj) {
		        remove();
		    }
		};
		Label label = new Label(error, StrategyGame.getGame().getSkin());
		label.setWrap(true);
		dialog.add(label).width(400);
		dialog.button("Close", true);
		dialog.key(Keys.ENTER, true);
		dialog.key(Keys.ESCAPE, true);
		dialog.show(stage);
	}
	
	public void showConfirmExitWindow() {
		Dialog dialog = new Dialog("Are you sure?", StrategyGame.getGame().getSkin(), "dialog") {
		    public void result(Object obj) {
		    	if (((Boolean) obj).booleanValue() == true)
		    		StrategyGame.getGame().getScreenManager().setScreen(new GameOverScreen((currentPlayer == player1 ? player2 :  player1), settings));
		    }
		};
		Label label = new Label("Are you sure you want to surrender to your enemy?", StrategyGame.getGame().getSkin());
		label.setWrap(true);
		dialog.add(label).width(400);
		dialog.row();
		dialog.button("Yes", true);
		dialog.button("No", false);
		dialog.key(Keys.ENTER, true);
		dialog.key(Keys.ESCAPE, false);
		dialog.show(stage);
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
	
	public World getWorld () {
		return world;
	}
	
	public Player getCurrentPlayer () {
		return currentPlayer;
	}
	
	public HexMessageManager getHexMessageManager () {
		return hexMessageManager;
	}
	
	public GameSettings getSettings() {
		return settings;
	}
	
	public void openMessageWindow(String title, String text) {
		Dialog dialog = new Dialog(title, StrategyGame.getGame().getSkin(), "dialog") {
		    public void result(Object obj) {
		    	this.remove();
		    }
		};
		dialog.text(text);
		dialog.button("Close", null);
		dialog.key(Keys.ENTER, null);
		dialog.key(Keys.ESCAPE, null);
		dialog.show(stage);
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
		if (privacyMode) {
			privacyMode = false;
		} else {
			if (windowOpen)
				return false;
			
			Vector2 touchCoords = StrategyGame.getGame().getGameCamera().unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
			Hex hexTouched = hexTouchChecker.getTouchedHex((int) touchCoords.x, (int) touchCoords.y, world.getMap());
			//If the touched hex has a unit, set it as selected
			if (hexTouched != null) {
				
				boolean hexHandled = false;
				//Loop through each listener and loop
				ArrayList<HexTouchListener> currentListeners = new ArrayList<HexTouchListener>();//This is to avoid a concurrent modification error
				currentListeners.addAll(hexTouchListeners);
				for (HexTouchListener hexTouchListener : currentListeners) {
					if (hexTouchListener.hexTouched(hexTouched, this))
						hexHandled = true;
				}
				
				if (!hexHandled/* || (hexTouched.getUnitOnHex() != null && currentPlayer.doesUnitBelongToPlayer(hexTouched.getUnitOnHex()))*/) {
					if (hexTouched.getUnitOnHex() != null && hexTouched.getUnitOnHex() != selectedUnit)
						selectUnit(hexTouched.getUnitOnHex());
				}
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
