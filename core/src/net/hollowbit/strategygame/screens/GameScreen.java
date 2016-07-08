package net.hollowbit.strategygame.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.Player;
import net.hollowbit.strategygame.tools.FontManager.Fonts;
import net.hollowbit.strategygame.tools.FontManager.Sizes;
import net.hollowbit.strategygame.tools.Screen;
import net.hollowbit.strategygame.units.Unit;
import net.hollowbit.strategygame.units.Village;
import net.hollowbit.strategygame.world.Hex;
import net.hollowbit.strategygame.world.Hex.OverlayColor;
import net.hollowbit.strategygame.world.World;

public class GameScreen extends Screen {
	
	World world;
	Player currentPlayer;
	Player player1;
	Player player2;
	boolean unitChoiceMade;
	
	public GameScreen () {
		world = new World("grasslands");
		player1 = new Player("Player 1", Color.RED);
		player2 = new Player("Player 2", Color.BLUE);
		currentPlayer = player1;
		unitChoiceMade = false;
		
		//Give each player a village to start
		Unit player1Village = new Village(world, player1, world.getSpawn1());
		world.addUnit(new Village(world, player1, world.getSpawn1()));
		world.addUnit(new Village(world, player2, world.getSpawn2()));
		StrategyGame.getGame().getGameCamera().setGoal(new Vector2(player1Village.getX(), player1Village.getY()));
		
		resetFog();
	}
	
	@Override
	public void update(float deltaTime) {
		world.update(deltaTime);
	}
	
	@Override
	public void render(SpriteBatch batch, float width, float height) {
		world.render(batch);
	}
	
	@Override
	public void renderUi(SpriteBatch batch, float width, float height) {
		BitmapFont font = StrategyGame.getGame().getFontManager().getFont(Fonts.PIXELATED, Sizes.MEDIUM);
		GlyphLayout turnLblLayout = new GlyphLayout(font, currentPlayer.getName() + "'s turn!");
		font.draw(batch, turnLblLayout, width / 2 - turnLblLayout.width / 2, height - turnLblLayout.height - 10);
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
	
}
