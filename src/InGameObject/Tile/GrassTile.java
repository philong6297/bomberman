package InGameObject.Tile;


import Graphics.Sprite;

public class GrassTile extends InGameObject.Tile.TileAbstract {

	public GrassTile(int x, int y, Sprite sprite) {
		super(x, y, sprite);
	}
	
	@Override
	public boolean collide(InGameObject.TileAbstract e) {
		return true;
	}
}
