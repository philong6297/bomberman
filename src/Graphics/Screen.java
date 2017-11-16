package Graphics;

import Client.Game;
import InGameObject.TileAbstract;

import java.awt.*;

public class Screen {
	protected int _width, _height;
	public int[] _pixels;
    private int _transparentColor = 0xff7f7f7f; //grey with alpha channel (ff in the begining)

    //public static int xOffset = 0, yOffset = 0;

    public Screen(int width, int height) {
		_width = width;
		_height = height;
		
		_pixels = new int[width * height];
		
	}
	
	public void clear() {
		for (int i = 0; i < _pixels.length; i++) {
			_pixels[i] = 0;
		}
	}
	
	public void renderEntity(int xp, int yp, TileAbstract tileAbstract) { //save tileAbstract pixels
        //xp -= xOffset;
        //yp -= yOffset;
        for (int y = 0; y < tileAbstract.getSprite().getSize(); y++) {
			int ya = y + yp; //add offset
			for (int x = 0; x < tileAbstract.getSprite().getSize(); x++) {
				int xa = x + xp; //add offset
				if(xa < -tileAbstract.getSprite().getSize() || xa >= _width || ya < 0 || ya >= _height) break; //fix black margins
				if(xa < 0) xa = 0; //start at 0 from left
				int color = tileAbstract.getSprite().getPixel(x + y * tileAbstract.getSprite().getSize());
				if(color != _transparentColor) _pixels[xa + ya * _width] = color;
			}
		}
	}
	
	public void renderEntityWithBelowSprite(int xp, int yp, TileAbstract tileAbstract, Sprite below) {
        //xp -= xOffset;
        //yp -= yOffset;
        for (int y = 0; y < tileAbstract.getSprite().getSize(); y++) {
			int ya = y + yp;
			for (int x = 0; x < tileAbstract.getSprite().getSize(); x++) {
				int xa = x + xp;
				if(xa < -tileAbstract.getSprite().getSize() || xa >= _width || ya < 0 || ya >= _height) break; //fix black margins
				if(xa < 0) xa = 0;
				int color = tileAbstract.getSprite().getPixel(x + y * tileAbstract.getSprite().getSize());
				if(color != _transparentColor) 
					_pixels[xa + ya * _width] = color;
				else
					_pixels[xa + ya * _width] = below.getPixel(x + y * below.getSize());
			}
		}
	}

	/*
	|--------------------------------------------------------------------------
	| GameMenu Screens
	|--------------------------------------------------------------------------
	 */
	public void drawEndGame(Graphics g, int points, String code) {
		g.setColor(Color.black);
		g.fillRect(0, 0, getRealWidth(), getRealHeight());

        Font font = new Font("Arial", Font.PLAIN, 20 * Game.SCALE / 2);
        g.setFont(font);
		g.setColor(Color.white);
		drawCenteredString("GAME OVER", getRealWidth(), getRealHeight(), g);

        font = new Font("Arial", Font.PLAIN, 10 * Game.SCALE / 2);
        g.setFont(font);
		g.setColor(Color.yellow);
        drawCenteredString("POINTS: " + points, getRealWidth(), getRealHeight() + (Game.TILES_SIZE * 2) * Game.SCALE / 2, g);

		
		/*font = new Font("Arial", Font.PLAIN, 10 * Game.SCALE);
		g.setFont(font);
		g.setColor(Color.GRAY);
		drawCenteredString(code, getRealWidth(), getRealHeight() * 2  - (Game.TILES_SIZE * 2) * Game.SCALE, g);
	*/}

	public void drawChangeLevel(Graphics g, int level) {
		g.setColor(Color.black);
		g.fillRect(0, 0, getRealWidth(), getRealHeight());

        Font font = new Font("Arial", Font.PLAIN, 20 * Game.SCALE / 2);
        g.setFont(font);
		g.setColor(Color.white);
		drawCenteredString("LEVEL " + level, getRealWidth(), getRealHeight(), g);
		
	}
	
	public void drawPaused(Graphics g) {
        Font font = new Font("Arial", Font.PLAIN, 20 * Game.SCALE / 2);
        g.setFont(font);
		g.setColor(Color.white);
		drawCenteredString("PAUSED", getRealWidth(), getRealHeight(), g);
		
	}
	
	
	
	public void drawCenteredString(String s, int w, int h, Graphics g) {
	    FontMetrics fm = g.getFontMetrics();
	    int x = (w - fm.stringWidth(s)) / 2;
	    int y = (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
	    g.drawString(s, x, y);
	 }
	
	public int getWidth() {
		return _width;
	}
	
	public int getHeight() {
		return _height;
	}
	
	public int getRealWidth() {
        return _width * Game.SCALE / 2;
    }
	
	public int getRealHeight() {
        return _height * Game.SCALE / 2;
    }
}
