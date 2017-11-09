package GUI.Menu;

import javax.swing.JMenuBar;

import GUI.Frame;

public class MenuBar extends JMenuBar {
	
	public MenuBar(Frame frame) {
		add( new GameMenu(frame) );
		add( new OptionsMenu(frame) );
		add( new HelpMenu(frame) );
	}
	
}
