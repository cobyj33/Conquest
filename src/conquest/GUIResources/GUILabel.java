package conquest.GUIResources;
import javax.swing.*;

import conquest.Resources;

import java.awt.*;

public class GUILabel extends JLabel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GUILabel() {
		construct("");
	}
	
	public GUILabel(String text) {
		construct(text);
	}
	
	private void construct(String text) {
		setText(text);
		setOpaque(true);
		setFont(Resources.getFont(Resources.FontEnum.STANDARDFONT).deriveFont(Font.BOLD, 15));
		setText(text);
		setHorizontalAlignment(JLabel.CENTER);
		setBackground(Color.BLACK);
		setForeground(Color.WHITE);
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	}
}
