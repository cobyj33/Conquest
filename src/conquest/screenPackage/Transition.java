package conquest.screenPackage;
import javax.swing.*;

import conquest.Main;
import conquest.Resources;

import java.awt.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


public class Transition extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int currentScreen;
	int nextScreen;
	JLayeredPane container;
	Screens screenGroup;
	Timer timer;
	int transitionTime = 1000;
	int frames = 60;
	int frame = 0;
	int step;
	int height;
	public static final int SLIDEDOWN = 0;
	Color[] colors;
	
	
	Transition(Screens screens, int currentScreen, int nextScreen, int type) {
		screenGroup = screens;
		container = Main.layeredPane;
		setOpaque(false);
		setSize(screens.mainPanel.getSize());
		setPreferredSize(getSize());
		this.currentScreen = currentScreen;
		this.nextScreen = nextScreen;
		colors = new Color[] {Color.BLACK, Color.WHITE};
	}
	
	public void animate() {
		//System.out.println("animate");
		container.add(this, JLayeredPane.DRAG_LAYER);
		step = container.getHeight() / (frames / 2);
		//System.out.println("Step: " + step);
		Future<?> scheduledInstance = Main.scheduler.scheduleAtFixedRate(() -> {
			if (frame == 31) {
				SwingUtilities.invokeLater( () -> screenGroup.switchImmediately(nextScreen));
			}
			
			frame++;
			//System.out.println("gooo");
			if (frame < frames / 2) {
				height += step;
			} else {
				height -= step;
			}
			SwingUtilities.invokeLater(() -> Transition.this.repaint());
			
		}, 0, transitionTime / frames, TimeUnit.MILLISECONDS);
		
//		timer = new Timer(transitionTime / frames, l -> {
//			if (frame == 31) {
//				screenGroup.switchImmediately(nextScreen);
//			}
//			
//			frame++;
//			//System.out.println("gooo");
//			if (frame < frames / 2) {
//				height += step;
//			} else {
//				height -= step;
//			}
//			this.repaint();
//			
//		});
		Timer deleter = new Timer(transitionTime, l -> scheduledInstance.cancel(true));
		deleter.setRepeats(false);
		deleter.start();
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		//System.out.println("drawing, height " + height);
		Graphics2D g2D = (Graphics2D) g;
		if (frame == frames) {
			//timer.stop();
			Main.clearLayeredPane();
			return;
		}
	
		
		int barWidth = getWidth() / 10;
		for (int i = 0; i < 10; i++) {
			g2D.setPaint(colors[i % 2]);
			g2D.fillRect(barWidth * i, 0, barWidth, height);
		}
		
		g2D.setFont(Resources.getFont(Resources.FontEnum.STANDARDFONT).deriveFont(Font.BOLD, 40.0f));
		FontMetrics metrics = getFontMetrics(g2D.getFont());
		int centerX = getWidth() / 2 - metrics.stringWidth("C O N Q U E S T") / 2;
		int centerY = height / 2;
		//g2D.drawString("C O N Q U E S T", centerX, centerY);
		g2D.setPaint(Color.BLUE);
		g2D.fillRect(centerX - 10, centerY - metrics.getHeight(), metrics.stringWidth("C O N Q U E S T") + 20, metrics.getHeight() + 20);
		g2D.setPaint(new Color(173, 216, 230));
		g2D.drawString("C O N Q U E S T", centerX, centerY);
		
	}
	
	
}
