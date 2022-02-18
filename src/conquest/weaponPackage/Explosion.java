package conquest.weaponPackage;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import conquest.Conquest;
import conquest.Main;
import conquest.GUIResources.Particles.ExplosionParticle;
import conquest.entityPackage.Entity;
import conquest.entityPackage.playerPackage.Player;
import conquest.utilities.Colliding;

public class Explosion implements Colliding {
	Rectangle hitbox;
	ExplosionParticle particle;
	int damage;
	Entity source;
	List<Entity> hits;
	
	
	public Explosion(int x, int y, int duration, int maxSize, Entity source) {
		damage = 10;
		this.source = source;
		particle = new ExplosionParticle(Main.particleSystem, x, y, duration, maxSize);
		particle.setExplosion(this);
		Main.particleSystem.addParticle(particle);
		hitbox = new Rectangle(0, 0, 0, 0);
		hits = new ArrayList<>();
		Conquest.game.shakeScreen(5, duration);
	}
	
	public void setHitBox(int x, int y, int width, int height) {
		hitbox.x = x;
		hitbox.y = y;
		hitbox.width = width;
		hitbox.height = height;
	}
	
	public void checkCollisions() {
		
		List<Entity> entities = Conquest.game.getEntities();
    	
    	for (int e = 0; e < entities.size(); e++) {
    		Entity entity = entities.get(e);
    		if (entity.hitbox.intersects(hitbox)) {
    			if (entity.identifier != source.identifier && !hits.contains(entity)) {
    				entity.takeDamage(damage);
    				hits.add(entity);
    			}
    			
    			if (source instanceof Player && entity.health <= 0) {
    				Conquest.player.score += entity.score;
    			}
    		}
    	}
    	
	}
	
}
