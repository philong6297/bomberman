package Client;

import Exceptions.InLoadLevelException;
import Graphics.IRender;
import Graphics.Screen;
import InGameObject.Bomb.Bomb;
import InGameObject.Bomb.BombExplosion;
import InGameObject.Character.CharacterAbstract;
import InGameObject.Character.Player;
import InGameObject.Message;
import InGameObject.Tile.PowerUpTile.PowerUpAbstract;
import InGameObject.TileAbstract;
import Input.Keyboard;
import Level.FileLevel;
import Level.LevelAbstract;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//Singleton
public class Board implements IRender {

    public int _width, _height;
    public TileAbstract[] _entities;
    public List<CharacterAbstract> _mobs = new ArrayList<CharacterAbstract>();
    protected List<Bomb> _playerBombs = new ArrayList<Bomb>();
    protected LevelAbstract _level;
    protected Game _game;
    protected Keyboard _input;
    protected Screen _screen;
    protected List<Bomb> _botBombs = new ArrayList<Bomb>();
    private List<Message> _messages = new ArrayList<Message>();
    private int _screenToShow = -1; //1:endgame, 2:changelevel, 3:paused
    private int _time = Game.TIME;
    private int _points = Game.POINTS;
    private int _lives = Game.LIVES;

    public Board(Game game, Keyboard input, Screen screen) {
        _game = game;
        _input = input;
        _screen = screen;

        changeLevel(6); //start in LevelAbstract 5
    }

    public LevelAbstract get_level() {
        return _level;
    }

    /*
    |--------------------------------------------------------------------------
    | Render & Update
    |--------------------------------------------------------------------------
     */
    @Override
    public void update() {
        if (_game.isPaused()) return;

        updateEntities();
        updateMobs();
        updateBombs();
        updateMessages();
        detectEndGame();

        for (int i = 0; i < _mobs.size(); i++) {
            CharacterAbstract a = _mobs.get(i);
            if (a.isRemoved()) _mobs.remove(i);
        }
    }


    @Override
    public void render(Screen screen) {
        if (_game.isPaused()) return;

        //only render the visible part of screen
        int x0 = Screen.xOffset >> 4; //TileAbstract precision, -> left X
        int x1 = (Screen.xOffset + screen.getWidth() + Game.TILES_SIZE) / Game.TILES_SIZE; // -> right X
        int y0 = Screen.yOffset >> 4;
        int y1 = (Screen.yOffset + screen.getHeight()) / Game.TILES_SIZE; //render one TileAbstract plus to fix black margins

        for (int y = y0; y < y1; y++) {
            for (int x = x0; x < x1; x++) {
                _entities[x + y * _level.getWidth()].render(screen);
            }
        }

        renderBombs(screen);
        renderMobs(screen);

    }

    /*
    |--------------------------------------------------------------------------
    | ChangeLevel
    |--------------------------------------------------------------------------
     */
    public void newGame() {
        resetProperties();
        changeLevel(1);
    }

    @SuppressWarnings("static-access")
    private void resetProperties() {
        _points = Game.POINTS;
        _lives = Game.LIVES;
        Player._powerups.clear();

        _game.playerSpeed = 1.0;
        _game.bombRadius = 1;
        _game.playerBombRate = 1;
        _game.botBombRate = 1;

    }

    public void restartLevel() {
        changeLevel(_level.getLevel());
    }

    public void nextLevel() {
        changeLevel(_level.getLevel() + 1);
    }

    public void changeLevel(int level) {
        _time = Game.TIME;
        _screenToShow = 2;
        _game.resetScreenDelay();
        _game.pause();
        _mobs.clear();
        _playerBombs.clear();
        _botBombs.clear();
        _messages.clear();

        try {
            _level = new FileLevel("res/levels/Level" + level + ".txt", this);
            _entities = new TileAbstract[_level.getHeight() * _level.getWidth()];

            _level.createEntities();
        } catch (InLoadLevelException e) {
            endGame(); //failed to load.. so.. no more levels?
        }
    }

    public void changeLevelByCode(String str) {
        int i = _level.validCode(str);

        if (i != -1) changeLevel(i + 1);
    }

    public boolean isPowerupUsed(int x, int y, int level) {
        PowerUpAbstract p;
        for (int i = 0; i < Player._powerups.size(); i++) {
            p = Player._powerups.get(i);
            if (p.getX() == x && p.getY() == y && level == p.getLevel())
                return true;
        }

        return false;
    }

    /*
    |--------------------------------------------------------------------------
    | Detections
    |--------------------------------------------------------------------------
     */
    protected void detectEndGame() {
        if (_time <= 0)
            restartLevel();
    }

    public void endGame() {
        _screenToShow = 1;
        _game.resetScreenDelay();
        _game.pause();
    }

    public boolean detectNoEnemies() {
        int total = 0;
        for (int i = 0; i < _mobs.size(); i++) {
            if (_mobs.get(i) instanceof Player == false)
                ++total;
        }

        return total == 0;
    }

    /*
    |--------------------------------------------------------------------------
    | Pause & Resume
    |--------------------------------------------------------------------------
     */
    public void gamePause() {
        _game.resetScreenDelay();
        if (_screenToShow <= 0)
            _screenToShow = 3;
        _game.pause();
    }

    public void gameResume() {
        _game.resetScreenDelay();
        _screenToShow = -1;
        _game.run();
    }

    /*
    |--------------------------------------------------------------------------
    | Screens
    |--------------------------------------------------------------------------
     */
    public void drawScreen(Graphics g) {
        switch (_screenToShow) {
            case 1:
                _screen.drawEndGame(g, _points, _level.getActualCode());
                break;
            case 2:
                _screen.drawChangeLevel(g, _level.getLevel());
                break;
            case 3:
                _screen.drawPaused(g);
                break;
        }
    }

    /*
    |--------------------------------------------------------------------------
    | Getters And Setters
    |--------------------------------------------------------------------------
     */
    public TileAbstract getEntity(double x, double y, CharacterAbstract m) {

        TileAbstract res = null;

        res = getPlayerExplosionAt((int) x, (int) y);
        if (res != null) return res;
        res = getBotExplosionAt((int) x, (int) y);
        if (res != null) return res;
        res = getPlayerBombAt(x, y);
        if (res != null) return res;
        res = getBotBombAt((int) x, (int) y);
        if (res != null) return res;
        res = getMobAtExcluding((int) x, (int) y, m);
        if (res != null) return res;

        res = getEntityAt((int) x, (int) y);

        return res;
    }

    public List<Bomb> getPlayerBombs() {
        return _playerBombs;
    }

    public List<Bomb> getBotBombs() {
        return _botBombs;
    }

    public Bomb getPlayerBombAt(double x, double y) {
        Iterator<Bomb> bs = _playerBombs.iterator();
        Bomb b;
        while (bs.hasNext()) {
            b = bs.next();
            if (b.getX() == (int) x && b.getY() == (int) y)
                return b;
        }

        return null;
    }

    public Bomb getBotBombAt(double x, double y) {
        Iterator<Bomb> bs = _botBombs.iterator();
        Bomb b;
        while (bs.hasNext()) {
            b = bs.next();
            if (b.getX() == (int) x && b.getY() == (int) y)
                return b;
        }

        return null;
    }

    public CharacterAbstract getMobAt(double x, double y) {
        Iterator<CharacterAbstract> itr = _mobs.iterator();

        CharacterAbstract cur;
        while (itr.hasNext()) {
            cur = itr.next();

            if (cur.getXTile() == x && cur.getYTile() == y)
                return cur;
        }

        return null;
    }

    public Player getPlayer() {
        Iterator<CharacterAbstract> itr = _mobs.iterator();

        CharacterAbstract cur;
        while (itr.hasNext()) {
            cur = itr.next();

            if (cur instanceof Player)
                return (Player) cur;
        }

        return null;
    }

    public CharacterAbstract getMobAtExcluding(int x, int y, CharacterAbstract a) {
        Iterator<CharacterAbstract> itr = _mobs.iterator();

        CharacterAbstract cur;
        while (itr.hasNext()) {
            cur = itr.next();
            if (cur == a) {
                continue;
            }

            if (cur.getXTile() == x && cur.getYTile() == y) {
                return cur;
            }

        }

        return null;
    }

    public BombExplosion getPlayerExplosionAt(int x, int y) {
        Iterator<Bomb> bs = _playerBombs.iterator();
        Bomb b;
        while (bs.hasNext()) {
            b = bs.next();

            BombExplosion e = b.explosionAt(x, y);
            if (e != null) {
                return e;
            }

        }

        return null;
    }

    public BombExplosion getBotExplosionAt(int x, int y) {
        Iterator<Bomb> bs = _botBombs.iterator();
        Bomb b;
        while (bs.hasNext()) {
            b = bs.next();

            BombExplosion e = b.explosionAt(x, y);
            if (e != null) {
                return e;
            }

        }

        return null;
    }

    public TileAbstract getEntityAt(double x, double y) {
        return _entities[(int) x + (int) y * _level.getWidth()];
    }

    /*
    |--------------------------------------------------------------------------
    | Adds and Removes
    |--------------------------------------------------------------------------
     */
    public void addEntitie(int pos, TileAbstract e) {
        _entities[pos] = e;
    }

    public void addMob(CharacterAbstract e) {
        _mobs.add(e);
    }

    public void addPlayerBomb(Bomb e) {
        _playerBombs.add(e);
    }

    public void addBotBomb(Bomb e) {
        _botBombs.add(e);
    }

    public void addMessage(Message e) {
        _messages.add(e);
    }

    /*
    |--------------------------------------------------------------------------
    | Renders
    |--------------------------------------------------------------------------
     */
    protected void renderEntities(Screen screen) {
        for (int i = 0; i < _entities.length; i++) {
            _entities[i].render(screen);
        }
    }

    protected void renderMobs(Screen screen) {
        Iterator<CharacterAbstract> itr = _mobs.iterator();

        while (itr.hasNext())
            itr.next().render(screen);
    }

    protected void renderBombs(Screen screen) {
        Iterator<Bomb> itr = _playerBombs.iterator();

        while (itr.hasNext())
            itr.next().render(screen);

        itr = _botBombs.iterator();
        while (itr.hasNext())
            itr.next().render(screen);
    }

    public void renderMessages(Graphics g) {
        Message m;
        for (int i = 0; i < _messages.size(); i++) {
            m = _messages.get(i);

            g.setFont(new Font("Arial", Font.PLAIN, m.getSize()));
            g.setColor(m.getColor());
            g.drawString(m.getMessage(), (int) m.getX() - Screen.xOffset * Game.SCALE, (int) m.getY());
        }
    }

    /*
    |--------------------------------------------------------------------------
    | Updates
    |--------------------------------------------------------------------------
     */
    protected void updateEntities() {
        if (_game.isPaused()) return;
        for (int i = 0; i < _entities.length; i++) {
            _entities[i].update();
        }
    }

    protected void updateMobs() {
        if (_game.isPaused()) return;
        Iterator<CharacterAbstract> itr = _mobs.iterator();

        while (itr.hasNext() && !_game.isPaused())
            itr.next().update();
    }

    protected void updateBombs() {
        if (_game.isPaused()) return;
        Iterator<Bomb> itr = _playerBombs.iterator();

        while (itr.hasNext())
            itr.next().update();
        itr = _botBombs.iterator();
        while (itr.hasNext())
            itr.next().update();

    }

    protected void updateMessages() {
        if (_game.isPaused()) return;
        Message m;
        int left = 0;
        for (int i = 0; i < _messages.size(); i++) {
            m = _messages.get(i);
            left = m.getDuration();

            if (left > 0)
                m.setDuration(--left);
            else
                _messages.remove(i);
        }
    }

    /*
    |--------------------------------------------------------------------------
    | Getters & Setters
    |--------------------------------------------------------------------------
     */
    public Keyboard getInput() {
        return _input;
    }

    public LevelAbstract getLevel() {
        return _level;
    }

    public Game getGame() {
        return _game;
    }

    public int getShow() {
        return _screenToShow;
    }

    public void setShow(int i) {
        _screenToShow = i;
    }

    public int getTime() {
        return _time;
    }

    public int getLives() {
        return _lives;
    }

    public int subtractTime() {
        if (_game.isPaused())
            return this._time;
        else
            return this._time--;
    }

    public int getPoints() {
        return _points;
    }

    public void addPoints(int points) {
        this._points += points;
    }

    public void addLives(int lives) {
        this._lives += lives;
    }

    public int getWidth() {
        return _level.getWidth();
    }

    public int getHeight() {
        return _level.getHeight();
    }

}
