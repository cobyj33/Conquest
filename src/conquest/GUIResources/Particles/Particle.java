package conquest.GUIResources.Particles;

import java.awt.Graphics2D;

public abstract class Particle {
	ParticleSystem system;
	int x;
	int y;
	int width;
	int height;
	
	int lifeSpan;
	int lifeTime;
	
	int frames;
	int frame;
	javax.swing.Timer deleter;
	
	
	protected Particle(ParticleSystem system, int x, int y, int width, int height, int duration) {
		this.system = system;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.lifeSpan = duration;
		this.frames = 10;
		this.frame = 0;
		this.lifeTime = 0;
	}
	
	public abstract void paint(Graphics2D g2D);
}
