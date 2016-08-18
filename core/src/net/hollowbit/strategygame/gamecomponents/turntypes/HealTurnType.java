package net.hollowbit.strategygame.gamecomponents.turntypes;
import net.hollowbit.strategygame.gamecomponents.*;
import net.hollowbit.strategygame.screens.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import net.hollowbit.strategygame.units.*;
import net.hollowbit.strategygame.*;

public class HealTurnType extends TurnType
{
	
	public HealTurnType (Unit unit) {
		super(unit);
	}
	
	@Override
	public TextButton getTurnButton()
	{
		return new TextButton("Heal", StrategyGame.getGame().getSkin());
	}

	@Override
	public void turnStart() {}

	@Override
	public void initiate(GameScreen gameScreen)
	{
		if (usable()) {
			unit.damage(1, null, gameScreen);
			unit.setFinishedTurn(true);
			gameScreen.resetUnitMoveButtons();
			unit.disposeTurnTypes(gameScreen);
			gameScreen.selectNextUnit();
			unit.moveMade();
		}
	}

	@Override
	public void dispose(GameScreen gameScreen) {}

	@Override
	public boolean usable()
	{
		return unit.getHealth() < unit.getMaxHealth() && !unit.isFinishedTurn() && (unit.getTurnTypeSet() == null || unit.getTurnTypeSet() == this);
	}

}
