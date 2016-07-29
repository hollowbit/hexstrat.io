package net.hollowbit.strategygame.gamecomponents.turntypes;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.TurnType;
import net.hollowbit.strategygame.screens.GameScreen;
import net.hollowbit.strategygame.tools.HexTouchListener;
import net.hollowbit.strategygame.units.Village;
import net.hollowbit.strategygame.world.Hex;
import net.hollowbit.strategygame.world.Hex.OverlayColor;

public class ChooseSpawnTurnType extends TurnType implements HexTouchListener {
	
	Village village;
	
	public ChooseSpawnTurnType(Village unit) {
		super(unit);
		this.village = unit;
	}

	@Override
	public TextButton getTurnButton() {
		return new TextButton("Choose Spawn", StrategyGame.getGame().getSkin());
	}

	@Override
	public void turnStart() {
	}

	@Override
	public void initiate (GameScreen gameScreen) {
		if (!initiated) {
			initiated = true;
			gameScreen.addHexTouchListener(this);
			for (Hex hex : village.getHex().getSurroundingHexesInRange(1)) {
				if (hex.getType().collidable)
					hex.setOverlayColor(OverlayColor.INVALID);
				else
					hex.setOverlayColor(OverlayColor.VALID);
			}
		}
	}

	@Override
	public void dispose (GameScreen gameScreen) {
		initiated = false;
		gameScreen.removeHexTouchListener(this);
		gameScreen.resetFog();
	}

	@Override
	public boolean usable() {
		return true;
	}

	@Override
	public boolean hexTouched(Hex hex, GameScreen gameScreen) {
		if (hex.getOverlayColor() == OverlayColor.VALID) {
			village.setSpawn(hex);
			gameScreen.resetFog();
		}
		return true;
	}

}
