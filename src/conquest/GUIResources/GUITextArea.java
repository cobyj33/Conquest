package conquest.GUIResources;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;

import conquest.Resources;

public class GUITextArea extends JTextArea {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GUITextArea() {
		construct("");
	}
	
	public GUITextArea(String text) {
		construct(text);
	}
	
	private void construct(String text) {
		setText(text);
		setOpaque(true);
		setEditable(false);
		setFont(Resources.getFont(Resources.FontEnum.STANDARDFONT).deriveFont(Font.BOLD, 15));
		setText(text);
		setAlignmentX(CENTER_ALIGNMENT);
		setAlignmentY(CENTER_ALIGNMENT);
		setBackground(Color.BLACK);
		setForeground(Color.WHITE);
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	}
}
