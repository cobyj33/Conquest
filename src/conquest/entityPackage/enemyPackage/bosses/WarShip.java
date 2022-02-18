package conquest.entityPackage.enemyPackage.bosses;

import java.awt.Color;

import conquest.Conquest;
import conquest.Resources;
import conquest.entityPackage.enemyPackage.Enemy;
import conquest.entityPackage.enemyPackage.enemies.KamiKazeEnemy;
import conquest.utilities.Colliding;
import conquest.utilities.Functions;
import conquest.weaponPackage.Bullet;
import conquest.weaponPackage.BulletProperties;

public class WarShip extends Enemy implements Colliding {
	
	int state;
	double spiralAngle = 0;
	public static final int ENTRANCE = 0, ATTACK = 1, REST = 2, PANIC = 3;

	public WarShip() {
		super(Enemy.standardEnemy, 0, 0, null);
		setSize(200);
		minimumSize = 50;
		maxHealth = 2000;
		health = maxHealth;
		color = Color.RED;
		properties.maxRof = 400;
		properties.minRof = 100;
		timeToNext = (int) (Math.random() * (properties.maxRof - properties.minRof) + properties.minRof);
		setScaleToDamage(true);
		setX( Conquest.game.getWidth() / 2 - (getSize() / 2) );
		setY(-getSize());
		angle = Math.PI * 3 / 2;
		velocity = 1;
		attack = 10;
		state = ENTRANCE;
		Resources.repeatSoundFor(Resources.SoundEnum.BOSS_INTRO, Conquest.DELAY * getSize());
	}
	
	public void addBullet() {
		if (state == ATTACK) {
			int chance = (int) (Math.random() * 5);
			
			if (timeToNext <= 0) {
				switch (chance) {
				case 0: standardBullet = new BulletProperties("Spread");
				standardBullet.movementBehavior = BulletProperties.SPREAD;
				standardBullet.penetrating = true;
				break;
				case 1: standardBullet = new BulletProperties("Explosive");
				standardBullet.impactBehavior = BulletProperties.EXPLODE;
				break;
				case 2: standardBullet = new BulletProperties("Laser");
				standardBullet.size = 50;
				standardBullet.damage = 30;
				standardBullet.speed = 20;
				break;
				default: standardBullet = new BulletProperties("Turret");
				double addedAngle = Math.atan( Math.abs((double) Conquest.player.getX() - getX()) / Math.abs((double) Conquest.player.getY() - getY()) );
				double angle = Conquest.player.getX() - getX() > 0 ? Math.PI * 3 / 2 + addedAngle : Math.PI * 3 / 2 - addedAngle;
				standardBullet.startingAngle = angle;
				break;
				}
				
			}
			super.addBullet();
		} else if (state == REST) {
			if (timeToNext > 0) { timeToNext -= Conquest.DELAY; return; }
			
			int spawnChance = (int) (Math.random() * 10);
			if (spawnChance == 1) {
				KamiKazeEnemy minion = new KamiKazeEnemy(Enemy.standardEnemy, getX() + getSize() / 2, getY() + getSize(), null);
				minion.velocity = 5;
				minion.chasing = true;
			}
			
			spiralAngle += 10;
			if (spiralAngle > 360) spiralAngle = 0;
			
			Bullet[] bullets = new Bullet[4];
			for (int i = 0; i < bullets.length; i++) {
				Bullet bullet = bullets[i];
				bullet = new Bullet(standardBullet, getX() + (getSize() / 2), getY() + (getSize() / 2), this);
				bullet.velocity = 10;
				bullet.angle = Math.toRadians(spiralAngle + (90 * i));
				this.bullets.add(bullet);
			}
			
			timeToNext = 50;
			
			
		}
		
	}
	
	public void takeDamage(int damage) {
		super.takeDamage(damage);
		if (health <= maxHealth / 2 && state == ATTACK) {
			System.out.println("MOVING TO REST STATE");
			standardBullet = new BulletProperties("Spiral");
			standardBullet.damage = 20;
			standardBullet.color = Color.RED;
			state = REST;
		} else if (health <= maxHealth / 5 && state == REST) {
			System.out.println("MOVING TO PANIC STATE");
			state = PANIC;
			angle = (Math.random() * (Math.PI * 5 / 4 + Math.PI * 7 / 4) + Math.PI * 5 / 4);
			velocity = (8);
		}
		
		if (health <= 0) {
			Conquest.game.bossFight = false;
		}
	}
	
	public void move() {
		if (state == ENTRANCE) {
			super.move();
			if (getY() > Conquest.game.getHeight() / 8) {
				System.out.println("MOVING TO ATTACK STATE");
				state = ATTACK;
				angle = (0);
			}
		} else if (state == ATTACK) {
			if (!Conquest.game.isInBounds(hitbox) && inBounds) {
				inBounds = false;
				angle = angle == 0 ? Math.PI : 0;
			} else if (Conquest.game.isInBounds(hitbox) && !inBounds) {
				inBounds = true;
			}		
			
			//moves back and forth
			super.move();
			
		} else if (state == REST) {
			int center = Conquest.game.getWidth() / 2 - (getSize() / 2);
			if (getX() != center) {
				if (getX() > center ) {
					angle = Math.PI;
				} else {
					angle = 0;
				}
				super.move();
			}
			//does not move if it is already at the center in the resting state
		} else if (state == PANIC) {
			
//			Rectangle bouncer = new Rectangle(getX(), getY(), 1, 1);
			velocity = 8;
			if (!Conquest.game.isInBounds(hitbox) && inBounds) {
				inBounds = false;

				double angle = Functions.getAngle(getX(), getY(), Conquest.player.getX(), Conquest.player.getY());
				
				if (getY() > Conquest.player.getY()) {
					java.util.Random randomizer = new java.util.Random();
					switch (randomizer.nextInt(3)) {
					case 0: angle = Functions.getAngle(getX(), getY(), Conquest.game.getWidth() / 2 - (getSize() / 2), 0); break;
					case 1: angle = Functions.getAngle(getX(), getY(), 0, Conquest.game.getHeight() / 2 - (getSize() / 2)); break;
					case 2: angle = Functions.getAngle(getX(), getY(), Conquest.game.getWidth() - getSize(), Conquest.game.getHeight() / 2 - (getSize() / 2)); break;
					}
				} else if ( (angle > 0 && angle < Math.PI / 6) || (angle > Math.PI * 5 / 6 && angle < Math.PI * 7 / 6) || (angle < Math.PI / 2 && angle > Math.PI * 11 / 6) ) {	
					angle = Functions.getAngle(getX(), getY(), Conquest.game.getWidth() / 2 - (getSize() / 2), 0);
				}
				
				this.angle = (angle);
				
			} else if (Conquest.game.isInBounds(hitbox) && !inBounds) {
				inBounds = true;
			}		
			
			//moves back and forth
			super.move();
			
			
		}
	}

	@Override
	public void checkCollisions() {
		
		if (Conquest.player.hitbox.intersects(this.hitbox)) {
			Conquest.player.takeDamage(this.attack);
		}
	}
}

