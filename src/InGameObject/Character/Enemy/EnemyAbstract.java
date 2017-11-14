package InGameObject.Character.Enemy;

import Client.Board;
import Client.Game;
import Graphics.Screen;
import Graphics.Sprite;
import InGameObject.Bomb.AroundBombExplosion;
import InGameObject.Character.CharacterAbstract;
import InGameObject.Character.Enemy.AI.AIAbstract;
import InGameObject.Character.Player;
import InGameObject.Message;
import InGameObject.TileAbstract;
import Level.Coordinates;

import java.awt.*;

public abstract class EnemyAbstract extends CharacterAbstract {

	protected int _points;
	
	protected double _speed; //Speed should change on LevelAbstract transition
	protected AIAbstract _ai;
	
	//necessary to correct move
	protected final double MAX_STEPS;
	protected final double rest;
	protected double _steps;
	
	protected int _finalAnimation = 30;
	protected Sprite _deadSprite;
	
	public EnemyAbstract(int x, int y, Board board, Sprite dead, double speed, int points) {
		super(x, y, board);
		
		_points = points;
		_speed = speed;
		
		MAX_STEPS = Game.TILES_SIZE / _speed;
		rest = (MAX_STEPS - (int) MAX_STEPS) / MAX_STEPS;
		_steps = MAX_STEPS;
		
		_timeAfter = 20;
		_deadSprite = dead;
	}
	
	/*
	|--------------------------------------------------------------------------
	| CharacterAbstract Render & Update
	|--------------------------------------------------------------------------
	 */
	@Override
	public void update() {
		animate();
		
		if(_alive == false) {
			afterKill();
			return;
		}
		
		if(_alive)
			calculateMove();
	}
	
	@Override
	public void render(Screen screen) {
		
		if(_alive)
			chooseSprite();
		else {
			if(_timeAfter > 0) {
				_sprite = _deadSprite;
				_animate = 0;
			} else {
				_sprite = Sprite.movingSprite(Sprite.mob_dead1, Sprite.mob_dead2, Sprite.mob_dead3, _animate, 60);
			}
				
		}
        //System.out.println("enemy"+getXTile() +"+" + getYTile());
        screen.renderEntity((int) _x, (int) _y - _sprite.SIZE, this);
    }
	
	/*
	|--------------------------------------------------------------------------
	| CharacterAbstract Move
	|--------------------------------------------------------------------------
	 */
	@Override
	public void calculateMove() {
		int xa = 0, ya = 0;
		if(_steps <= 0){
			_direction = _ai.calculateDirection();
			_steps = MAX_STEPS;
		}
        if (_direction == 0) ya--;
        if (_direction == 2) ya++;
        if(_direction == 3) xa--;
		if(_direction == 1) xa++;
		
		if(canMove(xa, ya)) {
			_steps -= 1 + rest;
			move(xa * _speed, ya * _speed);
			_moving = true;
		} else {
			_steps = 0;
			_moving = false;
		}
	}
	
	@Override
	public void move(double xa, double ya) {
		if(!_alive) return;

			_y += ya;
			_x += xa;
	}
	
	@Override
	public boolean canMove(double x, double y) {
		
		double xr = _x, yr = _y - 16; //subtract y to get more accurate results
		
		//the thing is, subract 15 to 16 (sprite size), so if we add 1 TileAbstract we get the next pixel TileAbstract with this
		//we avoid the shaking inside tiles with the help of steps
		if(_direction == 0) { yr += _sprite.getSize() -1 ; xr += _sprite.getSize()/2; } 
		if(_direction == 1) {yr += _sprite.getSize()/2; xr += 1;}
		if(_direction == 2) { xr += _sprite.getSize()/2; yr += 1;}
		if(_direction == 3) { xr += _sprite.getSize() -1; yr += _sprite.getSize()/2;}
		
		int xx = Coordinates.pixelToTile(xr) +(int)x;
		int yy = Coordinates.pixelToTile(yr) +(int)y;
		
		TileAbstract a = _board.getEntity(xx, yy, this); //entity of the position we want to go
		
		return a.collide(this);
	}
	
	/*
	|--------------------------------------------------------------------------
	| CharacterAbstract Colide & Kill
	|--------------------------------------------------------------------------
	 */
	@Override
	public boolean collide(TileAbstract e) {
		if(e instanceof AroundBombExplosion) {
			kill();
			return false;
		}
		
		if(e instanceof Player) {
			((Player) e).kill();
			return false;
		}
		
		return true;
	}
	
	@Override
	public void kill() {
		if(_alive == false) return;
		_alive = false;
		
		_board.addPoints(_points);

		Message msg = new Message("+" + _points, getXMessage(), getYMessage(), 2, Color.white, 14);
		_board.addMessage(msg);
	}
	
	
	@Override
	protected void afterKill() {
		if(_timeAfter > 0) --_timeAfter;
		else {
			
			if(_finalAnimation > 0) --_finalAnimation;
			else
				remove();
		}
	}
	
	protected abstract void chooseSprite();
}
