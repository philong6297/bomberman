package InGameObject.Tile.PowerUpTile;

import Client.Game;
import InGameObject.TileAbstract;
import InGameObject.Character.Player;
import Graphics.Sprite;

public class PowerUpSpeed extends PowerUpAbstract {

	public PowerUpSpeed(int x, int y, int level, Sprite sprite) {
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
		Game.addPlayerSpeed(0.1);
	}
	


}
