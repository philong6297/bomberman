package InGameObject.Character.Enemy;


import Client.Board;
import Client.Game;
import Graphics.Sprite;
import InGameObject.Character.Enemy.AI.AIHigh;

public class Minvo extends EnemyAbstract {

	public Minvo(int x, int y, Board board) {
		super(x, y, board, Sprite.minvo_dead, Game.getPlayerSpeed() * 0.5, 800);
		
		_sprite = Sprite.minvo_right1;
		_ai = new AIHigh(_board.getPlayer(), this, board.get_level().get_lineTiles());
		_direction  = _ai.calculateDirection();
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
					_sprite = Sprite.movingSprite(Sprite.minvo_right1, Sprite.minvo_right2, Sprite.minvo_right3, _animate, 60);
				else
					_sprite = Sprite.minvo_left1;
				break;
			case 2:
			case 3:
				if(_moving)
					_sprite = Sprite.movingSprite(Sprite.minvo_left1, Sprite.minvo_left2, Sprite.minvo_left3, _animate, 60);
				else
					_sprite = Sprite.minvo_left1;
				break;
		}
	}
}
