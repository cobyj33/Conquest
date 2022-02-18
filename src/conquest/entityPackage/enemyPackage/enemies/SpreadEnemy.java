package conquest.entityPackage.enemyPackage.enemies;

import java.awt.Color;

import conquest.Conquest;
import conquest.entityPackage.enemyPackage.Enemy;
import conquest.entityPackage.enemyPackage.EnemyLine;
import conquest.entityPackage.enemyPackage.EnemyProperties;
import conquest.weaponPackage.Bullet;
import conquest.weaponPackage.BulletProperties;
import conquest.weaponPackage.Ray;

public class SpreadEnemy extends Enemy {
	
	public SpreadEnemy(EnemyProperties properties, int x, int y, EnemyLine line) {
		super(properties, x, y, line);
		System.out.println("SPREAD ENEMY CREATED");
		color = Color.PINK;
		standardBullet = new BulletProperties("SpreadBullet");
		standardBullet.color = Color.PINK;
		standardBullet.movementBehavior = BulletProperties.SPREAD;
	}
	
	public void addBullet() {
		if (timeToNext > 0) { timeToNext -= Conquest.DELAY; return; }
		if (bullets.size() > 10) { return; }
    	Ray ray = new Ray(getX(), getY() + 10, this);
    	ray.setSize(getSize());
    	if (ray.hitFriendly(this)) { return; }
    	
    	double angle = Math.PI * 3 / 2;
    	
    	timeToNext = (int) (Math.random() * (properties.maxRof - properties.minRof) + properties.minRof);
    	Bullet newBullet = new Bullet(standardBullet, getX(), getY() + 10, this);
    	newBullet.velocity = 10;
    	newBullet.angle = angle;
    	bullets.add(newBullet);
    	
    	Bullet leftBullet = new Bullet(standardBullet, getX(), getY() + 10, this);
    	leftBullet.velocity = 10;
    	leftBullet.angle = angle + Math.PI / 4;
    	bullets.add(leftBullet);
    	
    	Bullet rightBullet = new Bullet(standardBullet, getX(), getY() + 10, this);
    	rightBullet.velocity = 10;
    	rightBullet.angle = angle - Math.PI / 4;
    	bullets.add(rightBullet);
    	
	}
	
	
	
}