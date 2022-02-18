package conquest.entityPackage.enemyPackage.enemies;

import java.awt.Color;

import conquest.Conquest;
import conquest.entityPackage.enemyPackage.Enemy;
import conquest.entityPackage.enemyPackage.EnemyLine;
import conquest.entityPackage.enemyPackage.EnemyProperties;
import conquest.weaponPackage.Bullet;

public class TurretEnemy extends Enemy {
	public TurretEnemy(EnemyProperties properties, int x, int y, EnemyLine line) {
		super(properties, x, y, line);
		System.out.println("TURRET ENEMY CREATED");
		color = Color.CYAN;
		standardBullet.color = Color.CYAN;
	}
	
	public void addBullet() {
		super.addBullet();
		for (int b = 0; b < bullets.size(); b++) {
			Bullet bullet = bullets.get(b);
			double addedAngle = Math.atan( Math.abs((double) Conquest.player.getX() - getX()) / Math.abs((double) Conquest.player.getY() - getY()) );
			bullet.angle = Conquest.player.getX() - getX() > 0 ? Math.PI * 3 / 2 + addedAngle : Math.PI * 3 / 2 - addedAngle;;
		}
	}
}
