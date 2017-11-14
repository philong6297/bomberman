package InGameObject.Tile.PowerUpTile;

import Client.Game;
import Graphics.Sprite;
import InGameObject.Character.Player;
import InGameObject.TileAbstract;

public class PowerUpBombs extends PowerUpAbstract {

	public PowerUpBombs(int x, int y, int level, Sprite sprite) {
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
		Game.addPlayerBombRate(1);
	}
	


}
