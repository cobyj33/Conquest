package conquest;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;

import javax.swing.*;
import conquest.GUIResources.Particles.ParticleSystem;
import conquest.screenPackage.Screens;

public class Main {
	public static final int cores = Runtime.getRuntime().availableProcessors();
	public static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(cores);
	public static final ExecutorService threadPool = Executors.newFixedThreadPool(cores);
	
	
	public static JLayeredPane layeredPane;
	public static JPanel gameHolder;
	public static ParticleSystem particleSystem;
	public static int mouseXInGame;
	public static int mouseYInGame;

	public static void main(String[] args) {
		//System.out.println(UIManager.getLookAndFeel());
		Resources.init();
//		Resources.mute();
		
		GameFrame frame = new GameFrame();
		frame.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		layeredPane = new JLayeredPane();
		layeredPane.setSize(frame.getSize());
		layeredPane.setPreferredSize(frame.getSize());
		
		particleSystem = new ParticleSystem();
		particleSystem.setLocation(0, 0);
		particleSystem.setSize(frame.getSize());
		
		gameHolder = new JPanel();
		gameHolder.setLocation(0, 0);
		gameHolder.setSize(frame.getSize());
		
		layeredPane.add(gameHolder, JLayeredPane.DEFAULT_LAYER);
		layeredPane.add(particleSystem, Integer.valueOf(100));
		
		System.out.println(gameHolder.getSize());
		new Screens(gameHolder);
		
		frame.add(layeredPane);
		frame.pack();
		frame.setVisible(true);
		layeredPane.setVisible(true);
		layeredPane.revalidate(); layeredPane.repaint();
		
		
	}
	
	public static void addToLayeredPane(JComponent component, Integer zIndex) {
		boolean inPane = Arrays.stream(layeredPane.getComponents()).anyMatch(c -> c.equals(component));
		if (!inPane) {
		layeredPane.add(component, zIndex);
		layeredPane.revalidate(); layeredPane.repaint();
		} else {
			System.out.println("already in pane");
		}
		
	}
	
	public static void removeFromLayeredPane(JComponent component) {
		layeredPane.remove(component);
		layeredPane.revalidate(); layeredPane.repaint();
	}
	
	public static void clearLayeredPane() {
		Arrays.stream(layeredPane.getComponents()).forEach(component -> {
			if (component.equals(gameHolder)) { return; }
			if (component.equals(particleSystem)) { return; }
			layeredPane.remove(component);
		});
		layeredPane.revalidate(); layeredPane.repaint();
	}

}

class GameFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	BufferedImage background;
	
	GameFrame() {
		setTitle("Conquest");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 600);
		//background = Resources.getImage(Resources.FRAMEBACKGROUND);
		
		
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				GameFrame.this.repaint();
			}
		});
	}
	
	public void paint(Graphics g) {
		super.paint(g);
	 	Graphics2D g2D = (Graphics2D) g;
	 	g2D.setPaint(Color.BLACK);
	 	if (background != null) {
	 		
	 		if (background.getHeight() < getHeight() && background.getWidth() < getWidth()) {
				g2D.drawImage(background.getSubimage(0, 0, getWidth(), getHeight()), 0, 0 , null);
			} else {
				g2D.drawImage(background, 0, 0, getWidth(), getHeight(), null);
			}
	 		
	 		
	 	} else {
	 		g2D.fillRect(0, 0, getWidth(), getHeight());
	 	}
	}
	
	
}
