package Level;

import Client.Board;
import Exceptions.InLoadLevelException;

public abstract class LevelAbstract implements ILevel {

    protected int _width;
    protected int _height;
    protected int _level;

    public int get_width() {
        return _width;
    }

    public int get_height() {
        return _height;
    }

    public String[] get_lineTiles() {
        return _lineTiles;
    }

	protected String[] _lineTiles;
	protected Board _board;

	protected static String[] codes = { //TODO: change this code system to actualy load the code from each LevelAbstract.txt
		"dnibpb5uqy",
		"cuq0vaxstb",
		"38y418wriq",
		"34h8k0chcs",
		"9qztxh6l4s",
		};

	public LevelAbstract(String path, Board board) throws InLoadLevelException {
		loadLevel(path);
		_board = board;
	}

	@Override
	public abstract void loadLevel(String path) throws InLoadLevelException;
	
	public abstract void createEntities();

	/*
	|--------------------------------------------------------------------------
	| Codes
	|--------------------------------------------------------------------------
	 */
	public int validCode(String str) {
		for (int i = 0; i < codes.length; i++) {
			if (codes[i].equals(str)) {
				return i;
			}
		}
		return -1;
	}
	
	public String getActualCode() {
		return codes[_level -1];
	}

	public int getWidth() {
		return _width;
	}

	public int getHeight() {
		return _height;
	}

	public int getLevel() {
		return _level;
	}

}
