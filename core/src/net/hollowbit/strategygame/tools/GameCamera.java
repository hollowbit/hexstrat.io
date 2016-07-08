package net.hollowbit.strategygame.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import net.hollowbit.strategygame.units.Unit;
import net.hollowbit.strategygame.world.Hex;

public class GameCamera {
	
	public static final float UNITS_PER_PIXEL = 0.75f;
	
	private OrthographicCamera cam;
	private ScreenViewport viewport;
	private Vector2 goal;
	private Unit unitToFocusOn;
	
	public GameCamera () {
		cam = new OrthographicCamera();
		viewport = new ScreenViewport(cam);
		viewport.setUnitsPerPixel(UNITS_PER_PIXEL);
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		viewport.apply();
		cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0);
		cam.update();
		goal = null;
	}
	
	public void resize (int width, int height) {
		viewport.update(width, height);
	}
	
	public Matrix4 combined () {
		return cam.combined;
	}
	
	public void update (float deltatime) {
		if (goal == null) {
			if (unitToFocusOn != null)
				cam.position.set(unitToFocusOn.getX() + Hex.SIZE / 2, unitToFocusOn.getY() + Hex.SIZE / 2, 0);
		} else {
			cam.position.lerp(new Vector3(goal.x + Hex.SIZE / 2, goal.y + Hex.SIZE / 2, 0), deltatime);
			if (cam.position.epsilonEquals(goal.x + Hex.SIZE / 2, goal.y + Hex.SIZE / 2, 0, 1f)) {
				goal = null;
			}
		}
		
		//Draw camera
		if (Gdx.input.isTouched())
			cam.translate(-Gdx.input.getDeltaX(), Gdx.input.getDeltaY());
		
		/*if (ArchipeloClient.getGame().getWorld() != null && ArchipeloClient.getGame().getWorld().getMap() != null) {
			//If map width is more than screen width
			if (ArchipeloClient.getGame().getWorld().getMap().getWidth() > cam.viewportWidth / ArchipeloClient.TILE_SIZE) {
				if (cam.position.x < cam.viewportWidth / 2)
					cam.position.x =  cam.viewportWidth / 2;
				
				if (cam.position.x + cam.viewportWidth > ArchipeloClient.getGame().getWorld().getMap().getWidth() * ArchipeloClient.TILE_SIZE + cam.viewportWidth / 2)
					cam.position.x = ArchipeloClient.getGame().getWorld().getMap().getWidth() * ArchipeloClient.TILE_SIZE - cam.viewportWidth + cam.viewportWidth / 2;
			}
			
			//If map height is more than screen height
			if (ArchipeloClient.getGame().getWorld().getMap().getHeight() > cam.viewportHeight / ArchipeloClient.TILE_SIZE) {
				if (cam.position.y < cam.viewportHeight / 2 + ArchipeloClient.TILE_SIZE)
					cam.position.y =  cam.viewportHeight / 2 + ArchipeloClient.TILE_SIZE;
				
				if (cam.position.y + cam.viewportHeight > ArchipeloClient.getGame().getWorld().getMap().getHeight() * ArchipeloClient.TILE_SIZE + cam.viewportHeight / 2 + ArchipeloClient.TILE_SIZE)
					cam.position.y = ArchipeloClient.getGame().getWorld().getMap().getHeight() * ArchipeloClient.TILE_SIZE - cam.viewportHeight + cam.viewportHeight / 2 + ArchipeloClient.TILE_SIZE;
			}
		}*/
		
		cam.update();
	}
	
	public void setGoal (Vector2 goal) {
		this.goal = goal;
	}
	
	public void move (float x, float y, float z) {
		cam.position.set(x + cam.viewportWidth * cam.zoom / 2, y + cam.viewportHeight * cam.zoom / 2, z);
		cam.update();
	}
	
	public void zoom (float zoom) {
		cam.zoom = zoom;
		cam.update();
	}
	
	public void focusOnUnit (Unit unitToFocusOn) {
		goal = null;
		this.unitToFocusOn = unitToFocusOn;
	}
	
	/** Convert screen coords to world coords */
	public Vector2 unproject (Vector2 screenCoords) {
		Vector3 unprojected = cam.unproject(new Vector3(screenCoords.x, screenCoords.y, 0));
		return new Vector2(unprojected.x, unprojected.y);
	}
	
	/** Convert world coords to world screen */
	public Vector2 project (Vector2 worldCoords) {
		Vector3 projected = cam.project(new Vector3(worldCoords.x, worldCoords.y, 0));
		return new Vector2(projected.x, projected.y);
	}
	
	public float getWidth () {
		return cam.viewportWidth;
	}
	
	public float getHeight () {
		return cam.viewportHeight;
	}
	
	public float getX () {
		return cam.position.x;
	}
	
	public float getY () {
		return cam.position.y;
	}
	
	//TODO Perhaps implement this later if needed
	/*public CollisionRect getViewRect () {
		return new CollisionRect(cam.position.x - cam.viewportWidth * cam.zoom / 2, cam.position.y - cam.viewportHeight * cam.zoom / 2, 0, 0, cam.viewportWidth * cam.zoom, cam.viewportHeight * cam.zoom);
	}*/
	
}
