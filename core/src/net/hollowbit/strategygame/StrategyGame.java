package net.hollowbit.strategygame;

import java.util.HashMap;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class StrategyGame extends ApplicationAdapter implements InputProcessor {
	
	public static final int BLACK = Color.argb8888(0, 0, 0, 1);
	public static final int WHITE = Color.argb8888(1, 1, 1, 1);
	
	SpriteBatch batch;
	
	HashMap<Integer, TextureRegion> tileMap;
	
	TextureRegion grid;
	Pixmap hexMap;
	Texture hexMapImage;
	
	OrthographicCamera cam;
	ScreenViewport viewport;
	
	//0 is grass, 1 is water
	int[][] map = new int[][]{
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		{0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1},
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		{0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0},
		{0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0},
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		{1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0},
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		};
	
	int touchX, touchY;
	
	BitmapFont font;
		
	@Override
	public void create () {
		batch = new SpriteBatch();
		
		cam = new OrthographicCamera();
		viewport = new ScreenViewport(cam);
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		viewport.apply();
		cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0);
		cam.update();
		
		//Load textures
		grid = new TextureRegion(new Texture("hexgrid.png"));
		fixBleeding(grid);
		hexMap = new Pixmap(Gdx.files.internal("hexmap.png"));
		hexMapImage = new Texture("hexmap.png");
		
		//Load tiles
		tileMap = new HashMap<Integer, TextureRegion>();
		TextureRegion[][] hexTileMap = TextureRegion.split(new Texture("hextilemap.png"), 64, 65);
		for (int y = 0; y < hexTileMap.length; y++) {
			for (int x = 0; x < hexTileMap[0].length; x++) {
				fixBleeding(hexTileMap[y][x]);
			}
		}
		tileMap.put(0, hexTileMap[0][0]);
		tileMap.put(1, hexTileMap[0][1]);
		
		font = new BitmapFont();
		
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if (Gdx.input.isTouched()) {
			cam.translate(-Gdx.input.getDeltaX() * cam.zoom, Gdx.input.getDeltaY() * cam.zoom);
			cam.update();
		}

		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		
		//Draw map
		for (int y = 0; y < 11; y++) {
			for (int x = 0; x < 14; x++) {
				batch.draw(tileMap.get(map[11 - y - 1][x]), x * 49, y * 64 - (x % 2 == 1 ? 32:0));
				if (Gdx.input.isKeyPressed(Keys.SPACE))
					batch.draw(hexMapImage, x * 49, y * 64 - (x % 2 == 1 ? 32:0));
			}
		}
		
		//Draw grid overlay
		batch.draw(grid, 0, 0);
		
		//Draw fps
		Vector3 fpsPosition = cam.unproject(new Vector3(10, 10, 0));
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), fpsPosition.x, fpsPosition.y);
		
		batch.end();
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		viewport.update(width, height);
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		touchX = screenX;
		touchY = screenY;
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (new Vector2(screenX, screenY).dst(touchX, touchY) < 15) {
			Vector3 position = cam.unproject(new Vector3(screenX, screenY, 0));
			
			//Find array position of tile being interacted with
			int x = (int) (position.x / 49);
			int y = 11 - (int) ((position.y - (x % 2 == 1 ? 32:0)) / 64) - 1 - (x % 2 == 1 ? 1:0);
			
			//Find coordinate being clicked within that position
			int hexMapX = (int) (position.x % 49);
			int hexMapY = (int) ((position.y - (x % 2 == 1 ? 32:0)) % 64);
			
			//Depending on the color, change index
			int color = hexMap.getPixel(hexMapX, hexMapY);
			if (color == WHITE) {
				x--;
				y += (x % 2 == 0 ? 1:0);
				System.out.println("white");
			} else if (color == BLACK) {
				x--;
				y -= (x % 2 == 1 ? 1:0);
				System.out.println("black");
			}
			
			//Make sure it isn't out of bounds
			if (x < 0 || x >= map[0].length || y < 0 || y >= map.length)
				return false;
			
			//Toggle hex position
			map[y][x] = (map[y][x] == 1 ? 0 : 1);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean scrolled (int amount) {
		cam.zoom += amount * 0.1;
		if (cam.zoom > 2) {
			cam.zoom = 2;
			return true;
		}
		
		if (cam.zoom < 0.1) {
			cam.zoom = 0.1f;
			return true;
		}
		
		cam.position.lerp(cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)), 0.1f *  1 / cam.zoom);
		cam.update();
		return true;
	}

	@Override
	public boolean keyDown(int keycode) {return false;}

	@Override
	public boolean keyUp(int keycode) {return false;}

	@Override
	public boolean keyTyped(char character) {return false;}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {return false;}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {return false;}
	
	private void fixBleeding (TextureRegion region) {
	    float fix = 0.01f;

	    float x = region.getRegionX();
	    float y = region.getRegionY();
	    float width = region.getRegionWidth();
	    float height = region.getRegionHeight();
	    float invTexWidth = 1f / region.getTexture().getWidth();
	    float invTexHeight = 1f / region.getTexture().getHeight();
	    region.setRegion((x + fix) * invTexWidth, (y + fix) * invTexHeight, (x + width - fix) * invTexWidth, (y + height - fix) * invTexHeight);
	}
	
}
