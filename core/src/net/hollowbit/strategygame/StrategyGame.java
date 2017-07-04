package net.hollowbit.strategygame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import net.hollowbit.strategygame.screens.MainMenuScreen;
import net.hollowbit.strategygame.tools.AssetManager;
import net.hollowbit.strategygame.tools.FontManager;
import net.hollowbit.strategygame.tools.GameCamera;
import net.hollowbit.strategygame.tools.MusicManager;
import net.hollowbit.strategygame.tools.Screen;
import net.hollowbit.strategygame.tools.ScreenManager;
import net.hollowbit.strategygame.tools.UiCamera;
import net.hollowbit.strategygame.world.HexType;

public class StrategyGame extends Game {
	
	private static StrategyGame game;
	public static float STATETIME = 0;
	
	private ScreenManager screenManager;
	private FontManager fontManager;
	private AssetManager assetManager;
	private MusicManager musicManager;
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
		
		skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
		skin.getFont("large-font").getData().markupEnabled = true;
		skin.getFont("default-font").getData().markupEnabled = true;
		
		//Load textures
		HexType.loadTextures();
		assetManager.putTexture("blank", new Texture("blank.png"));
		assetManager.putTexture("blank-hex", new Texture("blank_hex.png"));
		assetManager.putTexture("grid", new Texture("grid.png"));
		assetManager.putTexture("selected-hex-border", new Texture("selected_hex_border.png"));
		assetManager.putTextureMap("hearts", new Texture("hearts.png"), 15, 13);
		
		  //Unit textures
		assetManager.putTexture("village", new Texture("units/village.png"));
		assetManager.putTexture("village-overlay", new Texture("units/village_overlay.png"));
		assetManager.putTexture("horseman", new Texture("units/horseman.png"));
		assetManager.putTexture("horseman-overlay", new Texture("units/horseman_overlay.png"));
		assetManager.putTexture("archer", new Texture("units/archer.png"));
		assetManager.putTexture("archer-overlay", new Texture("units/archer_overlay.png"));
		assetManager.putTexture("worker", new Texture("units/worker.png"));
		assetManager.putTexture("worker-overlay", new Texture("units/worker_overlay.png"));
		assetManager.putTexture("tower", new Texture("units/tower.png"));
		assetManager.putTexture("tower-overlay", new Texture("units/tower_overlay.png"));
		assetManager.putTexture("farm", new Texture("units/farm.png"));
		assetManager.putTexture("farm-overlay", new Texture("units/farm_overlay.png"));
		assetManager.putTexture("swordsman", new Texture("units/swordsman.png"));
		assetManager.putTexture("swordsman-overlay", new Texture("units/swordsman_overlay.png"));
		assetManager.putTexture("spearman", new Texture("units/spearman.png"));
		assetManager.putTexture("spearman-overlay", new Texture("units/spearman_overlay.png"));
		assetManager.putTexture("catapult", new Texture("units/catapult.png"));
		assetManager.putTexture("catapult-overlay", new Texture("units/catapult_overlay.png"));
		assetManager.putTexture("beast", new Texture("units/beast.png"));
		assetManager.putTexture("beast-overlay", new Texture("units/beast_overlay.png"));
		assetManager.putTexture("priest", new Texture("units/priest.png"));
		assetManager.putTexture("priest-overlay", new Texture("units/priest_overlay.png"));
		
		musicManager = new MusicManager();
		
		screenManager = new ScreenManager(new MainMenuScreen());
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.1f, 0.6f, 0.95f, 1);
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
	
	public Screen getCurrentScreen() {
		return screenManager.getCurrentScreen();
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
	
	public MusicManager getMusicManager() {
		return musicManager;
	}
	
	public Skin getSkin () {
		return skin;
	}
	
	public SpriteBatch getBatch () {
		return batch;
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
