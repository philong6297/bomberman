package GUI.Menu;

import Client.Game;
import GUI.Frame;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class OptionsMenu extends JMenu implements ChangeListener {

	Frame _frame;
	
	public OptionsMenu(Frame frame) {
        super("Options");

        _frame = frame;
		
		JMenuItem pause = new JMenuItem("Pause");
        pause.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0));
        pause.addActionListener(new MenuActionListener(frame));
		add(pause);
		
		JMenuItem resume = new JMenuItem("Resume");
        resume.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0));
        resume.addActionListener(new MenuActionListener(frame));
		add(resume);
		
		addSeparator();
		
		add(new JLabel("Size: "));
		
		JSlider sizeRange = new JSlider(JSlider.HORIZONTAL,
                1, 9, 5);

        //Turn on labels at major tick marks.
		sizeRange.setMajorTickSpacing(2);
		sizeRange.setMinorTickSpacing(1);
		sizeRange.setPaintTicks(true);
		sizeRange.setPaintLabels(true);
		sizeRange.addChangeListener(this);
		
		add(sizeRange);
		
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
	    if (!source.getValueIsAdjusting()) {
            int fps = source.getValue();

            Game.SCALE = fps;
	        System.out.println( Game.SCALE);
	        
	        _frame._gamepane.changeSize();
			  _frame.revalidate();
			  _frame.pack();
	    }
	}
	
	class MenuActionListener implements ActionListener {
		public Frame _frame;
		public MenuActionListener(Frame frame) {
			_frame = frame;
		}
		
		  @Override
		public void actionPerformed(ActionEvent e) {
			  
			  if(e.getActionCommand().equals("Pause")) {
				  _frame.pauseGame();
			  }
				  
			  if(e.getActionCommand().equals("Resume")) {
				  _frame.resumeGame();
			  }
		  }
	}
}
