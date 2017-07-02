package net.hollowbit.strategygame.gamecomponents.turntypes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.TurnType;
import net.hollowbit.strategygame.screens.GameScreen;
import net.hollowbit.strategygame.tools.HexMessage;
import net.hollowbit.strategygame.tools.HexTouchListener;
import net.hollowbit.strategygame.tools.StaticTools;
import net.hollowbit.strategygame.units.Farm;
import net.hollowbit.strategygame.units.Tower;
import net.hollowbit.strategygame.units.Unit;
import net.hollowbit.strategygame.units.Village;
import net.hollowbit.strategygame.world.Hex;
import net.hollowbit.strategygame.world.Hex.OverlayColor;

public class ConvertTurnType extends TurnType implements HexTouchListener {
	
	public ConvertTurnType(Unit unit) {
		super(unit);
	}

	@Override
	public TextButton getTurnButton() {
		return new TextButton("Convert (20% Chance)", StrategyGame.getGame().getSkin());
	}

	@Override
	public void turnStart() {}

	@Override
	public void initiate(GameScreen gameScreen) {
		if (usable() && !initiated) {
			initiated = true;
			gameScreen.addHexTouchListener(this);
			initiateSecondTime(gameScreen);
		}
	}
	
	private void initiateSecondTime (GameScreen gameScreen) {
		//Change overlay for attack hexes
		for (Hex hex : unit.getHex().getSurroundingHexesInRange(unit.getAttackRange())) {
			if (hex.getUnitOnHex() != null && !(unit instanceof Village) && !(unit instanceof Tower) && !(unit instanceof Farm) && !unit.getPlayer().doesUnitBelongToPlayer(hex.getUnitOnHex())) {
				hex.setOverlayColor(OverlayColor.VALID);
			}
		}
	}

	@Override
	public void dispose(GameScreen gameScreen) {
		initiated = false;
		gameScreen.removeHexTouchListener(this);
		gameScreen.resetFog();
	}

	@Override
	public boolean usable() {
		return !unit.isFinishedTurn();
	}

	@Override
	public boolean hexTouched(Hex hex, GameScreen gameScreen) {
		switch(hex.getOverlayColor()) {
		case VALID:
			int die = StaticTools.getRandom().nextInt(5);
			if (die == 0) {
				hex.getUnitOnHex().convert(unit.getPlayer());
				hex.getUnitOnHex().setFinishedTurn(true);
				gameScreen.getHexMessageManager().addHexMessage(new HexMessage("Converted!", hex, gameScreen.getWorld(), Color.GREEN), true);
			} else {
				gameScreen.getHexMessageManager().addHexMessage(new HexMessage("Convert Failed", hex, gameScreen.getWorld(), Color.RED), true);
			}
			break;
		default:
			return false;
		}
		gameScreen.resetFog();
		unit.setFinishedTurn(true);
		gameScreen.resetUnitMoveButtons();
		gameScreen.selectNextUnit();
		return true;
	}

}
