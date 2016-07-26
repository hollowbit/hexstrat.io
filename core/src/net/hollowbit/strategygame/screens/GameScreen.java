package net.hollowbit.strategygame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.Player;
import net.hollowbit.strategygame.tools.FontManager.Fonts;
import net.hollowbit.strategygame.tools.FontManager.Sizes;
import net.hollowbit.strategygame.tools.HexTouchChecker;
import net.hollowbit.strategygame.tools.Screen;
import net.hollowbit.strategygame.units.Unit;
import net.hollowbit.strategygame.units.Village;
import net.hollowbit.strategygame.world.Hex;
import net.hollowbit.strategygame.world.Hex.OverlayColor;
import net.hollowbit.strategygame.world.World;

public class GameScreen extends Screen implements InputProcessor {
	
	World world;
	Player currentPlayer;
	Player player1;
	Player player2;
	boolean unitChoiceMade;//Unit made a move
	
	Unit selectedUnit = null;//Null if no unit was selected
	
	HexTouchChecker hexTouchChecker;
	
	public GameScreen () {
		world = new World("grasslands");
		player1 = new Player("Player 1", Color.RED);
		player2 = new Player("Player 2", Color.BLUE);
		currentPlayer = player1;
		unitChoiceMade = false;
		
		//Input related
		InputMultiplexer inputMultiplexer = new InputMultiplexer(this);
		Gdx.input.setInputProcessor(inputMultiplexer);
		hexTouchChecker = new HexTouchChecker();
		
		//Give each player a village to start
		Unit player1Village = new Village(world, player1, world.getSpawn1());
		world.addUnit(player1Village);
		world.addUnit(new Village(world, player2, world.getSpawn2()));
		selectUnit(player1Village);
		
		resetFog();
	}
	
	@Override
	public void update(float deltaTime) {
		world.update(deltaTime);
	}
	
	private void selectUnit (Unit unit) {
		selectedUnit = unit;
		StrategyGame.getGame().getGameCamera().setGoal(new Vector2(selectedUnit.getX(), selectedUnit.getY()));
		selectedUnit.focusedOn(this);
		
		//If the unit belongs to the current player, allow doing a turn with it, otherwise, just display its stats
		if (currentPlayer.doesUnitBelongToPlayer(unit)) {
			
		}
	}
	
	@Override
	public void render(SpriteBatch batch, float width, float height) {
		world.render(batch);
		
		if (selectedUnit != null)
			batch.draw(StrategyGame.getGame().getAssetManager().getTexture("selected-hex-border"), selectedUnit.getX(), selectedUnit.getY());
	}
	
	@Override
	public void renderUi(SpriteBatch batch, float width, float height) {
		BitmapFont fontMedium = StrategyGame.getGame().getFontManager().getFont(Fonts.PIXELATED, Sizes.MEDIUM);
		BitmapFont fontSmall = StrategyGame.getGame().getFontManager().getFont(Fonts.PIXELATED, Sizes.SMALL);
		GlyphLayout turnLblLayout = new GlyphLayout(fontMedium, currentPlayer.getName() + "'s turn!");
		fontMedium.draw(batch, turnLblLayout, width / 2 - turnLblLayout.width / 2, height - turnLblLayout.height - 10);
		
		if (selectedUnit != null) {
			GlyphLayout healthStatLayout = new GlyphLayout(fontSmall, "Health: " + selectedUnit.getHealth() + "/" + selectedUnit.getMaxHealth());
			fontSmall.draw(batch, healthStatLayout, width - healthStatLayout.width - 10, height - 100);
			GlyphLayout ownerLayout = new GlyphLayout(fontSmall, "Owner: [#" + selectedUnit.getPlayer().getColor() + "]" + selectedUnit.getPlayer().getName());
			fontSmall.draw(batch, ownerLayout, width - ownerLayout.width - 10, height - 140);
		}
	}
	
	public void endTurn () {
		if (currentPlayer == player1)
			currentPlayer = player2;
		else
			currentPlayer = player1;
		unitChoiceMade = false;
		
		resetFog();
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

	@Override
	public boolean keyDown(int keycode) {return false;}

	@Override
	public boolean keyUp(int keycode) {return false;}

	@Override
	public boolean keyTyped(char character) {return false;}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector2 touchCoords = StrategyGame.getGame().getGameCamera().unproject(new Vector2(screenX, screenY));
		hexTouchChecker.touchDown((int) touchCoords.x, (int) touchCoords.y);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		//Check if player is selecting a unit
		if (!unitChoiceMade) {
			Vector2 touchCoords = StrategyGame.getGame().getGameCamera().unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
			Hex hexTouched = hexTouchChecker.getTouchedHex((int) touchCoords.x, (int) touchCoords.y, world.getMap());
			//If the touched hex has a unit, set it as selected
			if (hexTouched != null) {
				if (hexTouched.getUnitOnHex() != null)
					selectUnit(hexTouched.getUnitOnHex());
			}
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
