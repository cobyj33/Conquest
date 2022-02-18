package conquest.entityPackage.enemyPackage.bosses;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

import conquest.Conquest;
import conquest.Main;
import conquest.Resources;
import conquest.entityPackage.enemyPackage.Enemy;
import conquest.weaponPackage.BulletProperties;

public class Wall extends Enemy {
	
	private int state;
	public static final int ENTRANCE = 0,
			ATTACK = 1,
			SPAWN = 2,
			PANIC = 3,
			LAUNCH = 4;
	
	public Wall() {
		super(Enemy.standardEnemy, 0, 0, null);
		setSize(Conquest.game.getWidth());
		minimumSize = 50;
		maxHealth = 4000;
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
		setScaleToDamage(false);
		state = ENTRANCE;
		Resources.repeatSoundFor(Resources.SoundEnum.BOSS_INTRO, Conquest.DELAY * getSize());
	}
	
	public void addBullet() {
		if (state == ATTACK) {
			if (timeToNext > 0) { timeToNext -= Conquest.DELAY; return; }
			
			
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
			state = SPAWN;
		} else if (health <= maxHealth / 5 && state == PANIC) {
			System.out.println("MOVING TO PANIC STATE");
			state = PANIC;
			angle = Math.random() * (Math.PI * 5 / 4 + Math.PI * 7 / 4) + Math.PI * 5 / 4;
			velocity = 8;
		} else if (health <= maxHealth / 10 && state == PANIC) {
			System.out.println("MOVING TO LAUNCH STATE");
			state = LAUNCH;
			angle = Math.PI / 2;
			velocity = 10;
			Main.scheduler.schedule(() -> {
				angle = Math.PI * 3 / 2;
			}, 1000, TimeUnit.MILLISECONDS)
			;
		}
		
		if (health <= 0) {
			Conquest.game.bossFight = false;
		}
	}
	
	public void move() {
		if (state == ENTRANCE) {
			super.move();
			if (getY() >= -getSize() + getSize() / 10) {
				System.out.println("Moving to Attack State! ");
				state = ATTACK;
			}
			
		} else if (state == LAUNCH) {
			super.move();
		}
		
	}
}
