package net.hollowbit.strategygame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.hollowbit.strategygame.StrategyGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.vSyncEnabled = true;
		config.foregroundFPS = 0;
		config.backgroundFPS = 0;
		config.width = 1280;
		config.height = 720;
		new LwjglApplication(new StrategyGame(), config);
	}
}
