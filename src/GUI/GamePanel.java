package GUI;

import Client.Game;
import Exceptions.GameException;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    private Game _game;

    public GamePanel(Frame frame) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(Game.WIDTH * Game.SCALE / 2, Game.HEIGHT * Game.SCALE / 2));

        try {
            _game = new Game(frame);

            add(_game);

            _game.setVisible(true);

        } catch (GameException e) {
            e.printStackTrace();
            //TODO: so we got a error hum..
            System.exit(0);
        }
        setVisible(true);
        setFocusable(true);

    }

    public void changeSize() {
        setPreferredSize(new Dimension(Game.WIDTH * Game.SCALE / 2, Game.HEIGHT * Game.SCALE / 2));
        revalidate();
        repaint();
    }

    public Game getGame() {
        return _game;
    }

}
