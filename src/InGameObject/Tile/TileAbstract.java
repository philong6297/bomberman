package InGameObject.Tile;

import Graphics.Screen;
import Graphics.Sprite;
import Level.Coordinates;

public abstract class TileAbstract extends InGameObject.TileAbstract {
	
	
	public TileAbstract(int x, int y, Sprite sprite) {
		_x = x;
		_y = y;
		_sprite = sprite;
	}
	
	@Override
	public boolean collide(InGameObject.TileAbstract e) {
		return false;
	}
	
	@Override
	public void render(Screen screen) {
		screen.renderEntity( Coordinates.tileToPixel(_x), Coordinates.tileToPixel(_y), this);
	}
	
	@Override
	public void update() {}
}
