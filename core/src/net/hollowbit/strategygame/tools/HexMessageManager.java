package net.hollowbit.strategygame.tools;
import java.util.*;
import com.badlogic.gdx.graphics.g2d.*;

public class HexMessageManager
{
	
	ArrayList<HexMessage> hexMessages;
	ArrayList<HexMessage> hexMessagesForNextTurn;
	
	public HexMessageManager () {
		hexMessages = new ArrayList<HexMessage>();
		hexMessagesForNextTurn = new ArrayList<HexMessage>();
	}
	
	public void update (float deltaTime) {
		ArrayList<HexMessage> hexMessagesToRemove = new ArrayList<HexMessage>();
		for (HexMessage hexMessage : hexMessages) {
			hexMessage.update(deltaTime);
			
			if (hexMessage.remove)
				hexMessagesToRemove.add(hexMessage);
		}
		hexMessages.removeAll(hexMessagesToRemove);
	}
	
	public void render (SpriteBatch batch) {
		for (HexMessage hexMessage : hexMessages)
			hexMessage.render(batch);
	}
	
	public void startTurn () {
		hexMessages.clear();
		hexMessages.addAll(hexMessagesForNextTurn);
		hexMessagesForNextTurn.clear();
	}
	
	public void addHexMessage (HexMessage hexMessage, boolean useOnNextTurn) {
		hexMessages.add(hexMessage);
		
		if (useOnNextTurn)
			hexMessagesForNextTurn.add(hexMessage);
	}
	
}
