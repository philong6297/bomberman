package InGameObject.Tile.PowerUpTile;

import Client.Game;
import InGameObject.TileAbstract;
import InGameObject.Character.Player;
import Graphics.Sprite;

public class PowerUpFlames extends PowerUpAbstract {

	public PowerUpFlames(int x, int y, int level, Sprite sprite) {
		super(x, y, level, sprite);
	}
	
	@Override
	public boolean collide(TileAbstract e) {
		
		if(e instanceof Player) {
			((Player) e).addPowerup(this);
			remove();
			return true;
		}
		
		return false;
	}
	
	@Override
	public void setValues() {
		_active = true;
		Game.addBombRadius(1);
	}
	


}
