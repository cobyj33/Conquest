package conquest.GUIResources.Particles;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import conquest.Main;
//import particles.Particle;
//import particles.ParticleSystem;

public class StringParticle extends Particle {
	String string;
	Font font;
	float fontSize;
	public float bloom;
	int width;
	int height;
	public Color color;
	
	public StringParticle(ParticleSystem system, int x, int y, int duration, String string) {
		super(system, x, y, 0, 0, duration);
		this.string = string;
		color = Color.YELLOW;
		//font = Main.fonts.defaultFont;
		font = new Font("Times New Roman", Font.PLAIN, 12);
		fontSize = 12f;
		bloom = 0.5f;
		frames = 30;
		
		Future<?> future = Main.scheduler.scheduleAtFixedRate(() -> {
			fontSize += bloom;
			font = font.deriveFont(fontSize);
			//System.out.println("calling repaint");
			Graphics g = system.getGraphics();
			g.setFont(font);
			FontMetrics metrics = g.getFontMetrics();
			//metrics.get
			int width = metrics.stringWidth(string);
			int height = metrics.getHeight();
			
			//system.revalidate();
			//system.repaint(x, y, width, metrics.getMaxDescent());
			SwingUtilities.invokeLater( () -> system.repaint(x, y - height, width + 5, height));
			//system.repaint();
		}, 0, duration / frames, TimeUnit.MILLISECONDS);
		
		deleter = new javax.swing.Timer(duration, l -> {
			system.currentParticles.remove(this);
			future.cancel(true);
			//System.out.println("Particle deleted!");
		});
		deleter.setRepeats(false);
		
		deleter.start();
	}

	@Override
	public void paint(Graphics2D g2D) {
		g2D.setPaint(color);
		g2D.setFont(font);
		g2D.drawString(string, x, y);
	}
	
}
