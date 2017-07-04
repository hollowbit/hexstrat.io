package net.hollowbit.strategygame.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;

public class MusicManager {
	
	private static final int NUMBER_OF_TRACKS = 9;
	
	private Music[] tracks;
	private Music currentTrack;
	
	private Music mainMenu;
	
	private boolean mute = false;
	
	public MusicManager() {
		tracks = new Music[NUMBER_OF_TRACKS];
		for (int i = 0; i < NUMBER_OF_TRACKS; i++)
			tracks[i] = Gdx.audio.newMusic(Gdx.files.internal("music/track" + i + ".ogg"));
		
		mainMenu = Gdx.audio.newMusic(Gdx.files.internal("music/menu.ogg"));
		//playNextTrack();
	}
	
	public void playNextTrack() {
		if (currentTrack != null)
			currentTrack.stop();
		
		Music nextTrack = null;
		
		//Prevents playing the same track twice
		do {
			nextTrack = tracks[StaticTools.getRandom().nextInt(NUMBER_OF_TRACKS)];
		} while (nextTrack == currentTrack);
		
		if (mute)
			nextTrack.setVolume(0);
		else
			nextTrack.setVolume(1);
		nextTrack.play();
		nextTrack.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(Music music) {
				playNextTrack();
			}
		});
		currentTrack = nextTrack;
	}
	
	public void playMainMenuMusic() {
		if (currentTrack != null && currentTrack != mainMenu)
			currentTrack.stop();
		
		currentTrack = mainMenu;
		currentTrack.play();
	}
	
	public void pause() {
		if (currentTrack != null)
			currentTrack.pause();
	}
	
	public void resume() {
		if (currentTrack != null)
			currentTrack.play();
	}
	
	public boolean toggleMute() {
		mute = !mute;

		if (currentTrack != null)
			currentTrack.setVolume(mute ? 0 : 1);
		return mute;
	}
	
	public boolean isMute() {
		return mute;
	}
	
}
