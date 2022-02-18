package conquest.entityPackage.enemyPackage.enemies;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

import conquest.Conquest;
import conquest.Main;
import conquest.entityPackage.enemyPackage.Enemy;
import conquest.entityPackage.enemyPackage.EnemyLine;
import conquest.entityPackage.enemyPackage.EnemyProperties;
import conquest.utilities.Colliding;
import conquest.weaponPackage.Explosion;

public class KamiKazeEnemy extends Enemy implements Colliding {
	public boolean chasing;
	
	public KamiKazeEnemy(EnemyProperties properties, int x, int y, EnemyLine line) {
		super(properties, x, y, line);
		System.out.println("KAMIKAZE ENEMY CREATED");
		health = 2;
		color = Color.YELLOW;
		timeToNext = (int) (Math.random() * (5000 - 2500) + 2500);
		chasing = false;
		Main.scheduler.schedule(() -> chasing = true, timeToNext, TimeUnit.MILLISECONDS);
	}
	
	public void addBullet() {
		//nothing, it doesn't shoot
	}
	
	public void move() {
		if (!chasing) {
			super.move();
		} else {
			double addedAngle = Math.atan( Math.abs((double) Conquest.player.getX() - getX()) / Math.abs((double) Conquest.player.getY() - getY()) );
			angle = Conquest.player.getX() - getX() > 0 ? Math.PI * 3 / 2 + addedAngle : Math.PI * 3 / 2 - addedAngle;
			super.moveAsLine();
		}
	}

	@Override
	public void checkCollisions() {
		if (Conquest.player.hitbox.intersects(this.hitbox)) {
			new Explosion(getX(), getY(), 500, 100, this);
			super.takeDamage(maxHealth);
		}
	}
	
	
}