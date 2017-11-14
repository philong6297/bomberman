package InGameObject.Character;

import Client.Board;
import Client.Game;
import Graphics.Screen;
import Graphics.Sprite;
import InGameObject.Bomb.AroundBombExplosion;
import InGameObject.Bomb.Bomb;
import InGameObject.Character.Enemy.EnemyAbstract;
import InGameObject.Message;
import InGameObject.Tile.PowerUpTile.PowerUpAbstract;
import InGameObject.TileAbstract;
import Input.Keyboard;
import Level.Coordinates;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Player extends CharacterAbstract {
	
	private List<Bomb> _bombs;
	protected Keyboard _input;
	
	protected int _timeBetweenPutBombs = 0;
	
	public static List<PowerUpAbstract> _powerups = new ArrayList<PowerUpAbstract>();
	
	
	public Player(int x, int y, Board board) {
		super(x, y, board);
		_bombs = _board.getPlayerBombs();
		_input = _board.getInput();
		_sprite = Sprite.player_right;
	}
	
	
	/*
	|--------------------------------------------------------------------------
	| Update & Render
	|--------------------------------------------------------------------------
	 */
	@Override
	public void update() {
		clearBombs();
		if(_alive == false) {
			afterKill();
			return;
		}
		
		if(_timeBetweenPutBombs < -7500) _timeBetweenPutBombs = 0; else _timeBetweenPutBombs--; //dont let this get tooo big
		
		animate();
		
		calculateMove();
		
		detectPlaceBomb();
	}
	
	@Override
	public void render(Screen screen) {
		calculateXOffset();
		
		if(_alive)
			chooseSprite();
		else
			_sprite = Sprite.player_dead1;
		//System.out.println("player"+getXTile() +"+" + getYTile());
		screen.renderEntity((int) _x, (int) _y - _sprite.SIZE, this);
	}
	
	public void calculateXOffset() {
		int xScroll = Screen.calculateXOffset(_board, this);
		Screen.setOffset(xScroll, 0);
	}


	/*
	|--------------------------------------------------------------------------
	| CharacterAbstract Unique
	|--------------------------------------------------------------------------
	 */
	private void detectPlaceBomb() {
		if (_input.space && Game.getPlayerBombRate() > 0 && _timeBetweenPutBombs < 0) {

			int xt = Coordinates.pixelToTile(_x + _sprite.getSize() / 2);
			int yt = Coordinates.pixelToTile( (_y + _sprite.getSize() / 2) - _sprite.getSize() ); //subtract half player height and minus 1 y position
			
			placeBomb(xt,yt);
			Game.addPlayerBombRate(-1);

			_timeBetweenPutBombs = 30;
		}
	}
	
	protected void placeBomb(int x, int y) {
		Bomb b = new Bomb(x, y, _board);
		_board.addPlayerBomb(b);
	}
	
	private void clearBombs() {
		Iterator<Bomb> bs = _bombs.iterator();
		
		Bomb b;
		while(bs.hasNext()) {
			b = bs.next();
			if(b.isRemoved())  {
				bs.remove();
				Game.addPlayerBombRate(1);
			}
		}
		
	}
	
	/*
	|--------------------------------------------------------------------------
	| CharacterAbstract Colide & Kill
	|--------------------------------------------------------------------------
	 */
	@Override
	public void kill() {
		if(!_alive) return;
		
		_alive = false;
		
		_board.addLives(-1);

		Message msg = new Message("-1 LIVE", getXMessage(), getYMessage(), 2, Color.white, 14);
		_board.addMessage(msg);
	}
	
	@Override
	protected void afterKill() {
		if(_timeAfter > 0) --_timeAfter;
		else {
			if(_bombs.size() == 0) {
				
				if(_board.getLives() == 0)
					_board.endGame();
				else
					_board.restartLevel();
			}
		}
	}
	
	/*
	|--------------------------------------------------------------------------
	| CharacterAbstract Movement
	|--------------------------------------------------------------------------
	 */
	@Override
	protected void calculateMove() {
		int xa = 0, ya = 0;
		if(_input.up) ya--;
		if(_input.down) ya++;
		if(_input.left) xa--;
		if(_input.right) xa++;
		
		if(xa != 0 || ya != 0)  {
			move(xa * Game.getPlayerSpeed(), ya * Game.getPlayerSpeed());
			_moving = true;
		} else {
			_moving = false;
		}
		
	}
	
	@Override
	public boolean canMove(double x, double y) {
		for (int c = 0; c < 4; c++) { //colision detection for each corner of the player
			double xt = ((_x + x) + c % 2 * 11) / Game.TILES_SIZE; //divide with tiles size to pass to TileAbstract coordinate
			double yt = ((_y + y) + c / 2 * 12 - 13) / Game.TILES_SIZE; //these values are the best from multiple tests
			
			TileAbstract a = _board.getEntity(xt, yt, this);
			
			if(!a.collide(this))
				return false;
		}
		
		return true;
	}

	@Override
	public void move(double xa, double ya) {
		if (xa > 0) _direction = RIGHT;
		if (xa < 0) _direction = LEFT;
		if (ya > 0) _direction = DOWN;
		if (ya < 0) _direction = UP;

		if (canMove(0, ya)) { //separate the moves for the player can slide when is colliding
			_y += ya;
		}
		
		if(canMove(xa, 0)) {
			_x += xa;
		}
	}
	
	@Override
	public boolean collide(TileAbstract e) {
		if(e instanceof AroundBombExplosion) {
			kill();
			return false;
		}
		
		if(e instanceof EnemyAbstract) {
			kill();
			return true;
		}
		
		return true;
	}
	
	/*
	|--------------------------------------------------------------------------
	| Powerups
	|--------------------------------------------------------------------------
	 */
	public void addPowerup(PowerUpAbstract p) {
		if(p.isRemoved()) return;
		
		_powerups.add(p);
		
		p.setValues();
	}
	
	public void clearUsedPowerups() {
		PowerUpAbstract p;
		for (int i = 0; i < _powerups.size(); i++) {
			p = _powerups.get(i);
			if(p.isActive() == false)
				_powerups.remove(i);
		}
	}
	
	public void removePowerups() {
		for (int i = 0; i < _powerups.size(); i++) {
				_powerups.remove(i);
		}
	}
	
	/*
	|--------------------------------------------------------------------------
	| CharacterAbstract Sprite
	|--------------------------------------------------------------------------
	 */
	private void chooseSprite() {
		switch(_direction) {
		case 0:
			_sprite = Sprite.player_up;
			if(_moving) {
				_sprite = Sprite.movingSprite(Sprite.player_up_1, Sprite.player_up_2, _animate, 20);
			}
			break;
		case 1:
			_sprite = Sprite.player_right;
			if(_moving) {
				_sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, _animate, 20);
			}
			break;
		case 2:
			_sprite = Sprite.player_down;
			if(_moving) {
				_sprite = Sprite.movingSprite(Sprite.player_down_1, Sprite.player_down_2, _animate, 20);
			}
			break;
		case 3:
			_sprite = Sprite.player_left;
			if(_moving) {
				_sprite = Sprite.movingSprite(Sprite.player_left_1, Sprite.player_left_2, _animate, 20);
			}
			break;
		default:
			_sprite = Sprite.player_right;
			if(_moving) {
				_sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, _animate, 20);
			}
			break;
		}
	}
}
