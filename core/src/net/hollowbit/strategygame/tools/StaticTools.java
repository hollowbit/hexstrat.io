package net.hollowbit.strategygame.tools;

import java.util.Random;

public class StaticTools {
	
	private static Random random;
	
	static {
		random = new Random();
	}
	
	public static Random getRandom() {
		return random;
	}
	
}
