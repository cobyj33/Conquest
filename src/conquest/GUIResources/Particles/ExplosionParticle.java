package conquest.GUIResources.Particles;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import conquest.Main;
import conquest.weaponPackage.Explosion;

public class ExplosionParticle extends Particle {
	float bloom;
	float size;
	float maxSize;
	Explosion explosion;
	
	
	public ExplosionParticle(ParticleSystem system, int x, int y, int duration, float maxSize) {
		super(system, x, y, 0, 0, duration);
		this.bloom = maxSize / frames;
		size = 0;
		frames = 20;
		
		Future<?> future = Main.scheduler.scheduleAtFixedRate(() -> {
			this.size += bloom;
			if (explosion != null) {
				SwingUtilities.invokeLater(() -> { 
					explosion.setHitBox((int) (x - size), (int) (y - size), (int) (size * 2), (int) (size * 2));
					explosion.checkCollisions();
				explosion.checkCollisions(); });
			}
			
			SwingUtilities.invokeLater(() -> system.repaint() );
		}, 0, duration / frames, TimeUnit.MILLISECONDS);
		
		deleter = new javax.swing.Timer(duration, l -> {
			system.currentParticles.remove(this);
			future.cancel(true);
			system.repaint();
			//System.out.println("Particle deleted!");
		});
		deleter.setRepeats(false);
		
		deleter.start();
	}
	
	public void setExplosion(Explosion explosion) {
		this.explosion = explosion;
	}

	@Override
	public void paint(Graphics2D g2D) {
		g2D.setPaint(new Color(255, 255, 0, 150));
		g2D.fillOval((int) (x - size), (int) (y - size), (int) (size * 2), (int) (size * 2));
		
		g2D.setPaint(new Color(255, 0, 0, 150));
		g2D.fillOval((int) (x - size / 2), (int) (y - size / 2), (int) (size), (int) size);
	}
}
