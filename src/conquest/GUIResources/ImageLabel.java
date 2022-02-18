package conquest.GUIResources;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageLabel extends GUILabel  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	BufferedImage image;
	
	public ImageLabel(BufferedImage image) {
		setOpaque(false);
		this.image = image;
		if (image == null) { return; }
	}
	
	public void setImage(BufferedImage image) {
		this.image = image;
		repaint();
	}
	
	protected void paintComponent(Graphics g) {
		int centerX = getWidth() / 2;
		int centerY = getHeight() / 2;
		Graphics2D g2D = (Graphics2D) g;
		if (image != null) {
			if (getWidth() > getHeight()) {
				g2D.drawImage(image, centerX - getHeight() / 2, 0, getHeight(), getHeight(), null);
			} else {
				g2D.drawImage(image, 0, centerY - getWidth() / 2, getWidth(), getWidth(), null);
			}
		}
	}
}
