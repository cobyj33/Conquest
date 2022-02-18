package conquest.GUIResources.Particles;

import java.util.*;
import java.util.List;

import javax.swing.*;
import conquest.Conquest;
import conquest.Main;
import java.awt.*;

@SuppressWarnings("serial") 
public class ParticleSystem extends JPanel {
	
	protected List<Particle> currentParticles;
	static int delay = Conquest.DELAY;
	
	public ParticleSystem() {
		currentParticles = new ArrayList<>();
		setBackground(Color.RED);
		setOpaque(false);
		
//		addMouseListener(new MouseAdapter() {
//			public void mousePressed(MouseEvent e) {
//				addParticle(new StringParticle(ParticleSystem.this, e.getX(), e.getY(), 500, "Particle"));
//			}
//			
//			
//		});
//		
//		addMouseMotionListener(new MouseAdapter() {
//			public void mouseDragged(MouseEvent e) {
//				addParticle(new StringParticle(ParticleSystem.this, e.getX(), e.getY(), 500, "Particle"));
//			}
//		});
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		//System.out.println("[ParticleSystem]: " + this.getSize());
		//System.out.println("painting particles in system");
//		System.out.println("Num of particles: " + currentParticles.size());
		if (currentParticles.size() == 0) {
			Main.removeFromLayeredPane(this);
		}
		
		Graphics2D g2D = (Graphics2D) g;
		for (int c = 0; c < currentParticles.size(); c++) {
			currentParticles.get(c).paint(g2D);
		}
	}
	
	public void addParticle(Particle particle) {
		currentParticles.add(particle);
		if (currentParticles.size() == 1) {
			Main.addToLayeredPane(this, JLayeredPane.DRAG_LAYER);
		}
	}
	
	
}
