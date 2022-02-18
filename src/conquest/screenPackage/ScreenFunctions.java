package conquest.screenPackage;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ScreenFunctions extends KeyAdapter  {
	Screens screens;
	
	public ScreenFunctions(Screens screens) {
		this.screens = screens;
	}
	
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		boolean ctrl = e.isControlDown();
		if (ctrl && keyCode == KeyEvent.VK_Z) {
			screens.goBackToLastScreen();
		}
	}
	
}
