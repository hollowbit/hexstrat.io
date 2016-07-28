package net.hollowbit.strategygame.gamecomponents;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;

import net.hollowbit.strategygame.units.Unit;
import net.hollowbit.strategygame.units.Village;

public class Player {

	private String name;
	private ArrayList<Unit> units;
	private Color color;
	private Unit lastMovedUnit;
	
	public Player (String name, Color color) {
		this.name = name;
		this.color = color;
		this.units = new ArrayList<Unit>();
	}
	
	//Check if a particular unit belongs to this player
	public boolean doesUnitBelongToPlayer (Unit unit) {
		for (Unit unitInList : units) {
			if (unitInList == unit)
				return true;
		}
		return false;
	}
	
	//Checks if unit is an enemy
	public boolean isUnitAnEnemy (Unit unit) {
		return !doesUnitBelongToPlayer(unit);
	}
	
	public String getName () {
		return name;
	}
	
	public Color getColor () {
		return color;
	}
	
	public void addUnit (Unit unit) {
		units.add(unit);
	}
	
	public void removeUnit (Unit unit) {
		units.remove(unit);
	}
	
	public void setLastMovedUnit (Unit unit) {
		this.lastMovedUnit = unit;
	}
	
	public Unit getLastMovedUnit () {
		return lastMovedUnit;
	}
	
	public ArrayList<Unit> getUnits () {
		return units;
	}
	
	public Village getVillage () {
		return (Village) units.get(0);//First unit index should be a village
	}
	
	public int getProduction () {
		int production = 0;
		for (Unit unit : units)
			production += unit.getProduction();
		return production;
	}
	
}
