package net.hollowbit.strategygame.gamecomponents.turntypes;
import net.hollowbit.strategygame.gamecomponents.*;
import net.hollowbit.strategygame.screens.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import net.hollowbit.strategygame.*;
import net.hollowbit.strategygame.units.*;
import net.hollowbit.strategygame.world.*;
import net.hollowbit.strategygame.tools.*;

public class BerzerkTurnType extends TurnType implements HexTouchListener
{
	
	Phalanx phalanx;
	boolean charged = false;
	boolean attacked = false;
	
	public BerzerkTurnType (Phalanx unit) {
		super(unit);
		this.phalanx = unit;
	}

	@Override
	public TextButton getTurnButton()
	{
		if (charged)
			return new TextButton("Berzerk Attack", StrategyGame.getGame().getSkin());
		else
			return new TextButton("Charge Berzerk", StrategyGame.getGame().getSkin());
	}

	@Override
	public void turnStart () {
		attacked = false;
		initiated = false;
	}
	
	@Override
	public void moveMade() {
		charged = false;
	}

	@Override
	public void initiate(GameScreen gameScreen)
	{
		if (charged) {
			if (usable() && !initiated) {
				initiated = true;
				gameScreen.addHexTouchListener(this);
				initiateSecondTime(gameScreen);
			}	
		} else {
			unit.setFinishedTurn(true);
			gameScreen.resetUnitMoveButtons();
			unit.disposeTurnTypes(gameScreen);
			gameScreen.selectNextUnit();
			unit.moveMade();
			charged = true;
		}
	}
	
	private void initiateSecondTime (GameScreen gameScreen) {
		//Change overlay for attack hexes
		for (Hex hex : unit.getHex().getSurroundingHexesInRange(unit.getAttackRange())) {
			if (hex.getUnitOnHex() != null && !unit.getPlayer().doesUnitBelongToPlayer(hex.getUnitOnHex())) {
				hex.setOverlayColor(Hex.OverlayColor.ATTACK);
			}
		}
	}
	
	@Override
	public boolean hexTouched(Hex hex, GameScreen gameScreen) {
		switch(hex.getOverlayColor()) {
			case ATTACK:
				//Do damage based on unit type
				Unit attackedUnit = hex.getUnitOnHex();
				boolean unitKilled = false;
				if (attackedUnit.isTower())//If it is a tower, do 4 damage, otherwise, kill the unit
					unitKilled = attackedUnit.damage(-4, gameScreen.getCurrentPlayer(), gameScreen);
				else
					unitKilled = attackedUnit.damage(-attackedUnit.getMaxHealth(), gameScreen.getCurrentPlayer(), gameScreen);
				System.out.println("POOOOOOOP!!!!!!");

				//If the enemy unit was killed and this unit is not a ranged unit, move to the hex
				if (unitKilled && unit.getAttackRange() <= 1)
					unit.move(hex);
				
				attacked = true;
				break;
			default://Do nothing if not one of the above
				return false;
		}
		charged = false;
		gameScreen.resetFog();
		unit.setTurnType(this);
		gameScreen.resetUnitMoveButtons();
		unit.moveMade();
		if (usable()) {
			initiateSecondTime(gameScreen);
		} else {
			unit.setFinishedTurn(true);
			gameScreen.selectNextUnit();
		}
		return true;
	}

	@Override
	public void dispose(GameScreen gameScreen)
	{
		initiated = false;
		gameScreen.removeHexTouchListener(this);
		gameScreen.resetFog();
	}

	@Override
	public boolean usable()
	{
		return !attacked && !unit.isFinishedTurn() && (unit.getTurnTypeSet() == null || unit.getTurnTypeSet() == this);
	}

	
	
	
}
