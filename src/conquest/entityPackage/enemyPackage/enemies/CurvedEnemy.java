package conquest.entityPackage.enemyPackage.enemies;

import java.awt.Color;

import conquest.Conquest;
import conquest.entityPackage.enemyPackage.Enemy;
import conquest.entityPackage.enemyPackage.EnemyLine;
import conquest.entityPackage.enemyPackage.EnemyProperties;
import conquest.weaponPackage.BulletProperties;

public class CurvedEnemy extends Enemy {
	
	public CurvedEnemy(EnemyProperties properties, int x, int y, EnemyLine line) {
		super(properties, x, y, line);
//		System.out.println("CURVED ENEMY CREATED");
		color = Color.MAGENTA;
		standardBullet.movementBehavior = BulletProperties.CURVED;
		standardBullet.speed = 2;
	}
	
	public void addBullet() {
		int x = Conquest.player.getX();
		int y = Conquest.player.getY();
		if (timeToNext <= 0) {
			standardBullet.target = new java.awt.Point(x, y);
		}
		super.addBullet();
	}
	

}