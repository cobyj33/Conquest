package conquest.weaponPackage;

import java.util.ArrayList;
import java.util.List;
import java.util.ConcurrentModificationException;
import conquest.Conquest;
import conquest.entityPackage.Entity;
import conquest.entityPackage.Position;
import conquest.entityPackage.enemyPackage.Enemy;
import conquest.entityPackage.playerPackage.Player;
import conquest.utilities.Colliding;

public class Bullet extends Position implements Colliding {
    public int damage;
    public boolean destroyed = false;
    int distanceTraveled;
    Entity source;
	public BulletProperties properties;
	
	boolean switchedDirection;
	int reach;
	private java.awt.Point target;
    
	public Bullet(BulletProperties properties, int x, int y, Entity source) {
		super(x, y, properties.size);
    	this.source = source;
    	damage = properties.damage;
    	color = (properties.color);
    	distanceTraveled = 0;
    	//setSize(properties.size);
    	image = (properties.getImage());
    	this.properties = properties;
    	
    	Conquest.game.addObject(this);
    	
    	if (!conquest.Resources.isMuted()) {
    		properties.sound.setMicrosecondPosition(0);
        	properties.sound.start();
    	}
    	
    	//boomerang
    	if (properties.movementBehavior == BulletProperties.BOOMERANG) {
    		if (source.identifier == Entity.PLAYER) {
    			reach = Math.abs(Conquest.game.mouseY - Conquest.player.getY());
    		} else {
    			reach = properties.reach;
    		}
    	}
    	
    	target = properties.target;
    	
    }

    public void move() {
    	//System.out.println("Bullet moving");
    	int oldY = getY();
    	int oldX = getX();
    	
        if (properties.movementBehavior == BulletProperties.MOVE_STRAIGHT) {
        	super.move();
        } else if (properties.movementBehavior == BulletProperties.BOOMERANG) {
        	super.move();
        	
        	if (distanceTraveled >= reach && !switchedDirection) {
        		switchDirection();
        		switchedDirection = true;
        	}
        } else if (properties.movementBehavior == BulletProperties.SPREAD) {
        	super.move();
        } else if (properties.movementBehavior == BulletProperties.CURVED) {
        	if (target == null) {
        		System.out.println("[Bullet.move()] ERROR: CURVED BULLET MUST HAVE A TARGET");
        	} else {
        		double addedAngle = Math.acos( ((double) target.x - getX()) / (conquest.utilities.Functions.getDistance(target.x, getX(), -target.y, -getY())));
        		addedAngle = addedAngle > Math.PI / 3 ? Math.PI / 3 : addedAngle;
    			double angle = target.x - getX() > 0 ? Math.PI * 3 / 2 + addedAngle : Math.PI * 3 / 2 - addedAngle;
    			this.angle = (angle);
        		//setAngle( Math.atan( Math.abs(target.getX() - getX()) / Math.abs(target.getY() - getY()))  );
        	}
        	super.move();
        }
        
        distanceTraveled += (int) Math.sqrt( Math.pow((getX() - oldX), 2) + Math.pow((getY() - oldY), 2)  );
        
        if (!Conquest.game.isInBounds(hitbox)) {
        	Conquest.game.removeObject(this);
        	source.removeBullet(this);
        }
        
        //System.out.println(distanceTraveled);
    }
    
    public void checkCollisions() {
    		
    		Entity[] possibleHits;
    		try {
    			possibleHits = getPossibleCollisions();
    		} catch (ConcurrentModificationException e) {
    			System.out.println("Bad Bulet Detected");
    			e.printStackTrace();
    			return;
    		}
	        
    		boolean hitSomething = false;
    		
	        for (int e = 0; e < possibleHits.length; e++) {
	            if (Conquest.game.checkOverlap(possibleHits[e], this)) {
	            	
	                if (source.identifier == possibleHits[e].identifier) {
	                	continue;
	                }

	                possibleHits[e].takeDamage(this.damage);
	                
	                if (properties.impactBehavior == BulletProperties.EXPLODE) {
//                		System.out.println("EXPLOSION");
                		new Explosion(getX(), getY(), 500, 50, source);
                	}
	                
	                if (possibleHits[e] instanceof Enemy && source instanceof Player) {
	                	if (!hitSomething) {
	                		Conquest.player.shotsHit++;
	                	}
	                	hitSomething = true;

	                	Conquest.player.damageDealt += this.damage;
	                
	                	
	                	//Main.particleSystem.addParticle(new StringParticle(Main.particleSystem, getX(), getY(), 500, "" + damage));
	                	Enemy hit = (Enemy) possibleHits[e];
	                	if (hit.health <= 0) {
	                		Conquest.player.score += hit.score;
	                	}
	                }
	                if (!properties.penetrating) {
	                	Conquest.game.getObjects().remove(this);
	                	source.removeBullet(this);
	                }
	                break;
	            }
	        }
    }
    
    protected Entity[] getPossibleCollisions() {
    	List<Entity> entities = Conquest.game.getEntities();
    	
    	
    	int bulletLeft = getX(); int bulletRight = getX() + getSize();
    	List<Entity> hits = new ArrayList<>();
    	
    	for (int e = 0; e < entities.size(); e++) {
    		Entity entity = entities.get(e);
    		int left = entity.getX();
            int right = entity.getX() + entity.getSize();
            if (bulletLeft > left && bulletLeft < right) {
                hits.add(entity);
            } else if (bulletRight > left && bulletRight < right) {
            	hits.add(entity);
            }
    	}
    	
    	return hits.toArray(Entity[]::new);
    }

	public Entity getSource() {
		return source;
	}

	public void setSource(Entity source) {
		this.source = source;
	}
    
    
   
}