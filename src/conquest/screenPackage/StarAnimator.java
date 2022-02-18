package conquest.screenPackage;
import javax.swing.JComponent;
import javax.swing.Timer;
import javax.swing.event.AncestorEvent;

import conquest.utilities.AncestorAdapter;

import java.util.Arrays;
import java.util.Random;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Color;
import java.awt.Component;

public class StarAnimator {
	
	Timer timer;
	Random rng;
	JComponent component;
	private int maxStarSize;
	private int speed;
	private int currentStarSize;
	private int numOfStars;
	private volatile boolean ready;
	public Graphics g;
	Point[] starPositions;
	Color[] starColor;
	
	public StarAnimator(JComponent component) {
		this.component = component;
		speed = 200;
		timer = new Timer(speed, l -> {
			ready = true;
			component.repaint();
		});
		rng = new Random();
		
		component.addAncestorListener(new AncestorAdapter() {
			@Override
			public void ancestorAdded(AncestorEvent event) {
				if (event.getAncestor().equals(component) || event.getAncestorParent() == null || event.getAncestorParent().equals(component.getParent())) {
					start();
				}
			}
			
			public void ancestorRemoved(AncestorEvent event) {
				stop();
			}
		});
		
		component.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				Component component = (Component) e.getSource();
				if (component.getWidth() != 0) {
					start();
				}
			}
		});
		
		
		starColor = new Color[] {Color.YELLOW};
		maxStarSize = 20;
		currentStarSize = 0;
		numOfStars = 15;
		starPositions = new Point[numOfStars];
	}
	
	public boolean isRunning() {
		return timer.isRunning();
	}
	
	public void start() {
		if (component.getWidth() == 0 || component.getHeight() == 0) { return; }
		if (timer.isRunning()) {
			timer.stop();
		}
		starPositions = new Point[numOfStars];
		starPositions = Arrays.stream(starPositions)
				.map(point -> new Point(rng.nextInt(component.getWidth()), rng.nextInt(component.getHeight())))
				.toArray(Point[]::new);
		timer.start();
	}
	
	public void stop() {
		timer.stop();
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public void configure(Color[] colors, int maxStarSize, int numOfStars) {
		this.starColor = colors;
		this.maxStarSize = maxStarSize;
		this.numOfStars = numOfStars;
		starPositions = new Point[numOfStars];
	}
	
	public void configure(Color color, int maxStarSize, int numOfStars) {
		this.starColor = new Color[] {color};
		this.maxStarSize = maxStarSize;
		this.numOfStars = numOfStars;
		starPositions = new Point[numOfStars];
	}
	
	public void setStarColor(Color color) {
		starColor = new Color[] {color};
	}
	
	public void setStarColors(Color[] colors) {
		starColor = colors;
	}
	
	public void animate(Graphics2D g2D) { 
		if (g2D == null) { return; }
		int width = component.getWidth();
		int height = component.getHeight();
		
		if (ready) {
		ready = false;
			currentStarSize++;
			if (currentStarSize > maxStarSize) {
				component.repaint();
				currentStarSize = 1;
				//System.out.println("reset");
				Arrays.stream(starPositions).forEach(point -> point.setLocation(rng.nextInt(width), rng.nextInt(height)));
			}
		}
		
		Arrays.stream(starPositions).forEach(star -> {
			if (star == null) { return; }
			
			if (starColor.length == 1) {
				g2D.setPaint(starColor[0]);
			} else {
				g2D.setPaint(starColor[ (int) (Math.random() * starColor.length)]);
			}
			
			g2D.fillOval(star.x, star.y, currentStarSize, currentStarSize);
		});
		
		
	}
}
