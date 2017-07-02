package net.hollowbit.strategygame.gamecomponents;

import com.badlogic.gdx.graphics.Color;

public class GameSettings {
	
	private String player1Name;
	private String player2Name;
	private Color player1Color;
	private Color player2Color;
	private String mapId;
	
	public GameSettings() {
		player1Color = new Color(Color.RED);
		player2Color = new Color(Color.BLUE);
		player1Name = "Player 1";
		player2Name = "Player 2";
	}

	public String getPlayer1Name() {
		return player1Name;
	}

	public void setPlayer1Name(String player1Name) {
		this.player1Name = player1Name;
	}

	public String getPlayer2Name() {
		return player2Name;
	}

	public void setPlayer2Name(String player2Name) {
		this.player2Name = player2Name;
	}

	public Color getPlayer1Color() {
		return player1Color;
	}

	public void setPlayer1Color(Color player1Color) {
		this.player1Color = player1Color;
	}

	public Color getPlayer2Color() {
		return player2Color;
	}

	public void setPlayer2Color(Color player2Color) {
		this.player2Color = player2Color;
	}

	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapName) {
		this.mapId = mapName;
	}
	
}
