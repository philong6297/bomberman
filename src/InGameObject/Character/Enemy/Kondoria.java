package InGameObject.Character.Enemy;


import Client.Board;
import Client.Game;
import Graphics.Sprite;
import InGameObject.Character.Enemy.AI.AIMedium;

public class Kondoria extends EnemyAbstract {
	
	
	public Kondoria(int x, int y, Board board) {
		super(x, y, board, Sprite.kondoria_dead, Game.getPlayerSpeed() / 4, 1000);
		
		_sprite = Sprite.kondoria_right1;
		
		_ai = new AIMedium(_board.getPlayer(), this); //TODO: implement AIHigh 
        //_direction  = _ai.calculateDirection();
    }
	/*
	|--------------------------------------------------------------------------
	| CharacterAbstract Sprite
	|--------------------------------------------------------------------------
	 */
	@Override
	protected void chooseSprite() {
		switch(_direction) {
			case 0:
			case 1:
				if(_moving)
					_sprite = Sprite.movingSprite(Sprite.kondoria_right1, Sprite.kondoria_right2, Sprite.kondoria_right3, _animate, 60);
				else
					_sprite = Sprite.kondoria_left1;
				break;
			case 2:
			case 3:
				if(_moving)
					_sprite = Sprite.movingSprite(Sprite.kondoria_left1, Sprite.kondoria_left2, Sprite.kondoria_left3, _animate, 60);
				else
					_sprite = Sprite.kondoria_left1;
				break;
		}
	}
}
