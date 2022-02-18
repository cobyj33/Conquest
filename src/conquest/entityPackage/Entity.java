package conquest.entityPackage;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import conquest.Conquest;
import conquest.Main;
import conquest.GUIResources.Particles.StringParticle;
import conquest.weaponPackage.Bullet;

public class Entity extends Position {
    public int health;
    public int maxHealth;
    public int identifier;
    public int minimumSize;
    protected boolean vulnerable;
    public boolean destroyed;
    protected List<Bullet> bullets;

    public static final int ENEMY = 0;
    public static final int PLAYER = 1;
    public static final int OTHER = 2;

    private boolean scaleToDamage;
    private double scaleFactor;
    public int score = 10;
    
    public int attack;
    public int defense;


    public Entity(int x, int y) {
        super(x, y);
        bullets = new ArrayList<>();
        identifier = OTHER;
        scaleToDamage = false;
        scaleFactor = 2;
        minimumSize = 10;
        vulnerable = true;
        destroyed = false;
    }

    public Entity() {
    	super(0, 0);
    	bullets = new ArrayList<>();
        identifier = OTHER;
        scaleToDamage = false;
        scaleFactor = 2;
        minimumSize = 10;
        vulnerable = true;
        destroyed = false;
    }
    
    public void takeDamage(int damage) {
    	if (!vulnerable) { return; }
    	damage = defense > damage ? 0 : damage - defense;
        health -= damage;
        Main.particleSystem.addParticle(new StringParticle(Main.particleSystem, getX(), getY(), 500, "-" + damage));
        if (scaleToDamage) {
            setSize((int) (health * scaleFactor + minimumSize));
        }
    }
    
    public void heal(int healAmount) {
    	if (health + healAmount > maxHealth) {
    		healAmount = maxHealth - health;
    	}
    	health += healAmount;
    	StringParticle healingParticle = new StringParticle(Main.particleSystem, getX(), getY(), 500, "+" + healAmount);
    	healingParticle.color = (new Color(173, 216, 230));
    	Main.particleSystem.addParticle(healingParticle);
    	
    	if (scaleToDamage) {
    		setSize((int) (health * scaleFactor + minimumSize));
    	}
    }
    
    public void addBullet(Bullet bullet) {
    	bullets.add(bullet);
    }
    
    public void removeBullet(Bullet bullet) {
    	bullets.remove(bullet);
    }
    
    public void setScaleToDamage(boolean wish) {
        scaleToDamage = wish;
        if (wish) {
        	scaleFactor = (double) (getSize() - minimumSize) / health;
        }
        //setSize((int) (health * scaleFactor + minimumSize));
    }
	
	public String toString() {
		return "" + getClass() + " Health: " + health + " Score: " + score;
	}
	 

	


}
