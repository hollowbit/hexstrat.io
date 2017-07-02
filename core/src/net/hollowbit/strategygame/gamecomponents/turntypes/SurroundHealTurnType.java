package net.hollowbit.strategygame.gamecomponents.turntypes;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import net.hollowbit.strategygame.StrategyGame;
import net.hollowbit.strategygame.gamecomponents.TurnType;
import net.hollowbit.strategygame.screens.GameScreen;
import net.hollowbit.strategygame.units.Unit;
import net.hollowbit.strategygame.world.Hex;

public class SurroundHealTurnType extends TurnType {

	public SurroundHealTurnType(Unit unit) {
		super(unit);
	}

	@Override
	public TextButton getTurnButton() {
		return new TextButton("Surround Heal", StrategyGame.getGame().getSkin());
	}

	@Override
	public void turnStart() {}

	@Override
	public void initiate(GameScreen gameScreen) {
		if (usable()) {
			for (Hex hex : unit.getHex().getSurroundingHexesInRange(1)) {
				if (hex.getUnitOnHex() != null && unit.getPlayer().doesUnitBelongToPlayer(hex.getUnitOnHex()))
					hex.getUnitOnHex().damage(1, unit.getPlayer(), gameScreen);
			}
			unit.setFinishedTurn(true);
			gameScreen.selectNextUnit();
			gameScreen.resetUnitMoveButtons();
			unit.disposeTurnTypes(gameScreen);
			unit.moveMade();
		}
	}

	@Override
	public void dispose(GameScreen gameScreen) {}

	@Override
	public boolean usable() {
		return !unit.isFinishedTurn();
	}

}
