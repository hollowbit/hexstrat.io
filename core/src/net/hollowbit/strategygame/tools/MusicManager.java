package net.hollowbit.strategygame.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;

public class MusicManager {
	
	private static final int NUMBER_OF_TRACKS = 11;
	
	private Music[] tracks;
	private Music currentTrack;
	
	public MusicManager() {
		tracks = new Music[NUMBER_OF_TRACKS];
		for (int i = 0; i < NUMBER_OF_TRACKS; i++)
			tracks[i] = Gdx.audio.newMusic(Gdx.files.internal("music/track" + i + ".ogg"));
		
		playNextTrack();
	}
	
	public void playNextTrack() {
		Music nextTrack = null;
		
		//Prevents playing the same track twice
		do {
			nextTrack = tracks[StaticTools.getRandom().nextInt(NUMBER_OF_TRACKS)];
		} while (nextTrack == currentTrack);
		
		nextTrack.play();
		nextTrack.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(Music music) {
				playNextTrack();
			}
		});
		currentTrack = nextTrack;
	}
	
}
