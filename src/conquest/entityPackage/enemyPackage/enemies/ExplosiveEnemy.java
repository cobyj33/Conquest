package conquest.entityPackage.enemyPackage.enemies;

import java.awt.Color;

import conquest.entityPackage.enemyPackage.Enemy;
import conquest.entityPackage.enemyPackage.EnemyLine;
import conquest.entityPackage.enemyPackage.EnemyProperties;
import conquest.weaponPackage.BulletProperties;

public class ExplosiveEnemy extends Enemy {
	
	public ExplosiveEnemy(EnemyProperties properties, int x, int y, EnemyLine line) {
		super(properties, x, y, line);
		System.out.println("EXPLOSIVE ENEMY CREATED");
		color = Color.BLUE;
		standardBullet = new BulletProperties("Explosive Bullet");
		standardBullet.color = Color.BLUE;
		standardBullet.impactBehavior = BulletProperties.EXPLODE;
	}
	
}