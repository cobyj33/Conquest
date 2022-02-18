package conquest.GUIResources;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import conquest.Resources;

public class GUIButton extends JButton {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GUIButton() {
		construct("");
	}
	
	public GUIButton(String text) {
		construct(text);
	}
	
	private void construct(String text) {
		setFont(Resources.getFont(Resources.FontEnum.STANDARDFONT).deriveFont(Font.PLAIN, 15));
		setText(text);
		setBackground(Color.BLACK);
		setForeground(Color.WHITE);
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				GUIButton.this.setBackground(Color.WHITE);
				GUIButton.this.setForeground(Color.BLACK);
			}
			
			public void mouseExited(MouseEvent e) {
				GUIButton.this.setBackground(Color.BLACK);
				GUIButton.this.setForeground(Color.WHITE);
			}
		});
		
		addActionListener(l -> Resources.playSound(Resources.SoundEnum.BUTTON_CLICK));
	}
}