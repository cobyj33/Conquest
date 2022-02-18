package conquest.entityPackage.enemyPackage;
import java.awt.Color;
import java.util.ArrayList;

import conquest.Conquest;
import conquest.Resources;
import conquest.entityPackage.Entity;
import conquest.entityPackage.enemyPackage.bosses.WarShip;
import conquest.weaponPackage.Bullet;
import conquest.weaponPackage.BulletProperties;
import conquest.weaponPackage.Ray;


public class Enemy extends Entity {
    public static final EnemyProperties standardEnemy = new EnemyProperties();
    protected EnemyProperties properties;
    protected BulletProperties standardBullet;
    protected int timeToNext;
    private EnemyLine line;


    public Enemy(EnemyProperties properties, int x, int y, EnemyLine line) {
        super(x, y);
        this.properties = properties;
        //game.addObject(this);
        maxHealth = properties.maxHealth;
        health = maxHealth;
        color = Color.RED;
        setSize(properties.size);
        score = properties.score;
        setScaleToDamage(properties.scaleToDamage);
        identifier = Entity.ENEMY;
        bullets = new ArrayList<Bullet>();
        minimumSize = properties.minSize;
        if (line != null) {
		    velocity = line.velocity;
		    angle = line.angle;
		    this.line = line;
        }
        timeToNext = (int) (Math.random() * (properties.maxRof - properties.minRof) + properties.minRof);
        
        standardBullet = new BulletProperties("Enemy Bullet");
        standardBullet.color = Color.red;
        
        Conquest.game.addObject(this);
    }

//    public Enemy(Conquest game, EnemyLine line) {
//    	super(game);
//    	//setScaleToDamage(true);
//        setIdentifier(Entity.ENEMY);
//    }
    
    @Override
    public void move() {
    	if (line != null) {
    		line.requestMove();
    	} else {
    		super.move();
    	}
    }
    
    //called by the EnemyLine whenever all the enemies in that line have requested to move
    public void moveAsLine() {
    	super.move();
    }
    
    public void takeDamage(int damage) {
        super.takeDamage(damage);
//        System.out.println("Health: " + health);
        if (health <= 0) {
        	System.out.println("Dead");
        	Conquest.player.enemiesDestroyed++;
        	System.out.println("Destroyed");
        	Resources.playSound(Resources.SoundEnum.SHIP_DESTROYED);
        	Conquest.game.removeObject(this);
        	if (line != null) { line.removeEnemy(this); }
        }
    }
    
    public void addBullet() {
    	if (timeToNext > 0) { timeToNext -= Conquest.DELAY; return; }
    	if (bullets.size() > 10) { return; }
    	Ray ray = new Ray(getX(), getY() + 10, this);
    	ray.setSize(getSize());
    	if (ray.hitFriendly(this)) { return; }
    	
    	timeToNext = (int) (Math.random() * (properties.maxRof - properties.minRof) + properties.minRof);
//    	if (standardBullet.impactBehavior == BulletProperties.EXPLODE) {
//    		System.out.println("EXPLODING BULLET DETECTED");
//    	}
    	
    	double defaultAngle = Math.PI * 3 / 2;
    	if (standardBullet.startingAngle == Math.PI / 2) {
    		standardBullet.startingAngle = defaultAngle;
    	}
    	
    	Bullet newBullet = new Bullet(standardBullet, getX(), getY() + 10, this);
    	newBullet.velocity = standardBullet.speed;
    	newBullet.angle = standardBullet.startingAngle;
    	bullets.add(newBullet);
    	
    	
    	if (standardBullet.movementBehavior == BulletProperties.SPREAD) {
    		Bullet leftBullet = new Bullet(standardBullet, getX(), getY() + 10, this);
        	leftBullet.velocity = standardBullet.speed;
        	leftBullet.angle = standardBullet.startingAngle + Math.PI / 4;
        	bullets.add(leftBullet);
        	
        	Bullet rightBullet = new Bullet(standardBullet, getX(), getY() + 10, this);
        	rightBullet.velocity = standardBullet.speed;
        	rightBullet.angle = standardBullet.startingAngle - Math.PI / 4;
        	bullets.add(rightBullet);
    	}
    }
    
    public EnemyLine getLine() {
    	return line;
    }
    
    public static void spawnWarShip() {
    	new WarShip();
    }
    
    
}