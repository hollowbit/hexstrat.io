package net.hollowbit.strategygame.gamecomponents.turntypes;
import net.hollowbit.strategygame.gamecomponents.*;
import net.hollowbit.strategygame.screens.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import net.hollowbit.strategygame.units.*;
import net.hollowbit.strategygame.*;

public class ShieldTurnType extends TurnType
{
	
	Swordsman swordsman;
	int uses = 3;
	
	public ShieldTurnType (Swordsman unit) {
		super(unit);
		this.swordsman = unit;
	}
	
	@Override
	public TextButton getTurnButton()
	{
		return new TextButton("Shield (Uses left: " + uses + ")", StrategyGame.getGame().getSkin());
	}

	@Override
	public void turnStart()
	{
		swordsman.setShielded(false);
	}

	@Override
	public void initiate(GameScreen gameScreen)
	{
		if (usable()) {
			uses--;
			swordsman.setShielded(true);
			//unit.setFinishedTurn(true);
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
		return uses > 0 && !swordsman.isShielded() && !unit.isFinishedTurn() && (unit.getTurnTypeSet() == null || unit.getTurnTypeSet() == this);
	}

}
