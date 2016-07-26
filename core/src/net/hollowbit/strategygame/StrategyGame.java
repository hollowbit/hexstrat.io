package net.hollowbit.strategygame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import net.hollowbit.strategygame.screens.GameScreen;
import net.hollowbit.strategygame.tools.AssetManager;
import net.hollowbit.strategygame.tools.FontManager;
import net.hollowbit.strategygame.tools.GameCamera;
import net.hollowbit.strategygame.tools.ScreenManager;
import net.hollowbit.strategygame.tools.UiCamera;
import net.hollowbit.strategygame.world.HexType;

public class StrategyGame extends Game {
	
	private static StrategyGame game;
	public static float STATETIME = 0;
	
	private ScreenManager screenManager;
	private FontManager fontManager;
	private AssetManager assetManager;
	private GameCamera gameCamera;
	private UiCamera uiCamera;
	private SpriteBatch batch;
	
	private Skin skin;
		
	@Override
	public void create () {
		game = this;
		fontManager = new FontManager();
		assetManager = new AssetManager();
		gameCamera = new GameCamera();
		uiCamera = new UiCamera();
		batch = new SpriteBatch();
		
		skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
		
		//Load textures
		HexType.loadTextures();
		assetManager.putTexture("blank", new Texture("blank.png"));
		assetManager.putTexture("blank-hex", new Texture("blank_hex.png"));
		assetManager.putTexture("selected-hex-border", new Texture("selected_hex_border.png"));
		
		  //Unit textures
		assetManager.putTexture("village", new Texture("units/village.png"));
		assetManager.putTexture("village-overlay", new Texture("units/village_overlay.png"));
		
		screenManager = new ScreenManager(new GameScreen());
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		float deltaTime = Gdx.graphics.getDeltaTime();
		STATETIME += deltaTime;
		screenManager.update(deltaTime);
		
		//Render game world
		gameCamera.update(deltaTime);
		batch.setProjectionMatrix(gameCamera.combined());
		batch.begin();
		screenManager.render(batch, gameCamera.getWidth(), gameCamera.getHeight());
		batch.end();
		
		//Render Ui
		batch.setProjectionMatrix(uiCamera.combined());
		batch.begin();
		screenManager.renderUi(batch, uiCamera.getWidth(), uiCamera.getHeight());
		batch.end();
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		gameCamera.resize(width, height);
		uiCamera.resize(width, height);
		screenManager.resize(width, height);
	}
	
	public GameCamera getGameCamera () {
		return gameCamera;
	}
	
	public UiCamera getUiCamera () {
		return uiCamera;
	}
	
	public ScreenManager getScreenManager () {
		return screenManager;
	}
	
	public FontManager getFontManager () {
		return fontManager;
	}
	
	public AssetManager getAssetManager () {
		return assetManager;
	}
	
	public Skin getSkin () {
		return skin;
	}
	
	public void fixBleeding (TextureRegion region) {
	    float fix = 0.01f;

	    float x = region.getRegionX();
	    float y = region.getRegionY();
	    float width = region.getRegionWidth();
	    float height = region.getRegionHeight();
	    float invTexWidth = 1f / region.getTexture().getWidth();
	    float invTexHeight = 1f / region.getTexture().getHeight();
	    region.setRegion((x + fix) * invTexWidth, (y + fix) * invTexHeight, (x + width - fix) * invTexWidth, (y + height - fix) * invTexHeight);
	}
	
	public static StrategyGame getGame () {
		return game;
	}
	
}
