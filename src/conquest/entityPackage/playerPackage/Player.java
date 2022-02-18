package conquest.entityPackage.playerPackage;
import conquest.Conquest;
import conquest.Main;
import conquest.Resources;
import conquest.entityPackage.Entity;
import conquest.entityPackage.Position;
import conquest.modifiers.Modifier;
import conquest.weaponPackage.Bullet;
import conquest.weaponPackage.BulletProperties;
import conquest.weaponPackage.WeaponProperties;
import conquest.weaponPackage.equipment.Equipment;

import java.awt.*;
import java.io.Serializable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.List;

public class Player extends Entity implements Serializable {
	
	
	public transient PlayerGUI gui;
	private Inventory inventory;
    public int lives;
    Position originalPosition;
    public Color color = Color.BLACK;
    private int maxBullets;
    int rateOfFire;
    int specialRateOfFire;
    public boolean shooting;
    public boolean locked;
    volatile boolean canMove;
    
    //Game stats
    public int enemiesDestroyed;
    public int damageDealt;
    public int linesCleared;
    public int shotsHit;
    public int shotsFired;
    public int damageTaken;
    
    //player stats
    
    int mouseX;
    int mouseY;
    
    
    int toSpecial;
    int toNextBullet;
    
    int timeSinceHit;

    public Player(int x, int y) {
        super(x, y);
        image = Resources.getImage(Resources.ImageEnum.SHIP);
        inventory = new Inventory(this);
        createDefault();
        gui = new PlayerGUI(this);
        //if (this == null) { System.out.println("[Player.init] player is null"); }
    }
    
    public void createDefault() {
    	bullets = new ArrayList<Bullet>();
        identifier = Entity.PLAYER;
        maxHealth = 20;
        health = maxHealth;
        score = 0;
        setSize(40);
        
        lives = 2;
        maxBullets = 100;
        
        image = Resources.getImage(Resources.ImageEnum.SHIP);
        System.out.println("[Player.createDefault()]: " + this.image);
        Position test = (Position) this;
        System.out.println("[Player.createDefault()] Position instance: " + test.image);
        
        
        rateOfFire = 50;
        specialRateOfFire = 1000;
        toNextBullet = rateOfFire;
        toSpecial = specialRateOfFire;
        
        canMove = true;
        shooting = false;
        locked = false;
        
        inventory.applyModifiers();
        enemiesDestroyed = 0;
        damageDealt = 0;
        
        
        resetStats();
    }

    public void takeDamage(int damage) {
        super.takeDamage(damage);
        if (vulnerable) {
	        damageTaken += damage;
	        gui.healthBar.setValue(health);
	        image = Resources.getImage(Resources.ImageEnum.DAMAGED_SHIP);
	        Main.scheduler.schedule(() -> {
	        	if (image.equals(Resources.getImage(Resources.ImageEnum.DAMAGED_SHIP))) {
	        		image = Resources.getImage(Resources.ImageEnum.SHIP);
	        	}
	        }, 1, TimeUnit.SECONDS);
	        if (health <= 0) {
	            lives--;
	            gui.updateGUI();
	            if (lives == 0) {
	                kill();
	            } else {
	            	respawn();
	            }
	        }
        }
    }
    
    public void kill() {
    	lives = 0;
    	inventory.setDefault();
    	inventory.clear();
    	Conquest.game.endGame();
    }
    
    public void respawn() {
    	ScheduledExecutorService scheduler = Main.scheduler;
    	health = maxHealth;
        setLocation(Conquest.game.spawn);
        gui.updateGUI();
        canMove = false;
        vulnerable = false;
        image = Resources.getImage(Resources.ImageEnum.GOLDEN_SHIP);
		scheduler.schedule(() -> canMove = true, Conquest.DELAY * 120, TimeUnit.MILLISECONDS);
		scheduler.schedule(() -> {vulnerable = true; image = Resources.getImage(Resources.ImageEnum.SHIP); color = Color.BLACK; }, Conquest.DELAY * 240, TimeUnit.MILLISECONDS);
    }

    public int getLives() { return lives; }
    public int getRateOfFire() { return rateOfFire; }
    public List<Bullet> getBullets() { return bullets; }
    public void setLives(int lives) { this.lives = lives; updateGUI(); }
    public void setRateOfFire(int rof) { rateOfFire = rof; }

    
    


	public void addBullet() { 
		//System.out.println("[Player.addBullet()] " + getImage());
		int mouseX = Conquest.game.mouseX;
		int mouseY = Conquest.game.mouseY;
		double launchangle = Math.asin( (getY() - mouseY)  / Math.sqrt( Math.pow((mouseX - getX()), 2) + Math.pow((mouseY - getY()), 2)   ) );
		if (mouseX < getX()) {
			launchangle = Math.PI - launchangle;
		}
		
		WeaponProperties selectedPrimary = inventory.getSelectedPrimary();
    	BulletProperties primaryBullet;
    	if (selectedPrimary instanceof BulletProperties) {
    		primaryBullet = (BulletProperties) inventory.getSelectedPrimary();
//    		System.out.println("damage before: " + primaryBullet.damage);
//    		System.out.println("damage after: " + primaryBullet.damage);
    		//System.out.println(primaryBullet.getName());
    	} else {
    		return;
    	}
    	
        if (bullets.size() < maxBullets && toNextBullet <= 0) {
        	shotsFired++;
        	Bullet newBullet = new Bullet(primaryBullet, getX(), getY() - 10, this);
        	newBullet.damage += this.attack;
        	newBullet.velocity = primaryBullet.speed;
        	newBullet.angle = launchangle;
            bullets.add(newBullet);
            
            if (primaryBullet.movementBehavior == BulletProperties.SPREAD) {
            	Bullet leftBullet = new Bullet(primaryBullet, getX(), getY() - 10, this);
            	Bullet rightBullet = new Bullet(primaryBullet, getX(), getY() - 10, this);
            	double leftAngle = launchangle - Math.PI / 6;
            	double rightAngle = launchangle + Math.PI / 6;
            	
            	leftBullet.velocity = (primaryBullet.speed);
            	leftBullet.angle = (leftAngle);
            	
            	rightBullet.velocity = (primaryBullet.speed);
            	rightBullet.angle = (rightAngle);
            	
                bullets.add(leftBullet);
                bullets.add(rightBullet);
            }
            
            toNextBullet = rateOfFire;
        }
    }
	

    public void addSpecial() {
    	WeaponProperties selectedSpecial = inventory.getSelectedSpecial();
    	BulletProperties specialBullet;
    	
    	int mouseX = Conquest.game.mouseX;
		int mouseY = Conquest.game.mouseY;
    	double angle = Math.asin( (getY() - mouseY)  / Math.sqrt( Math.pow((mouseX - getX()), 2) + Math.pow((mouseY - getY()), 2)   ) );
		if (mouseX < getX()) {
			angle = Math.PI - angle;
		}
    	
    	if (selectedSpecial instanceof BulletProperties) {
    		specialBullet = (BulletProperties) inventory.getSelectedSpecial();
    	} else {
    		return;
    	}
    	
        if (bullets.size() < maxBullets && toSpecial <= 0) {
        	shotsFired++;
        	Bullet newBullet = new Bullet(specialBullet, getX(), getY() - specialBullet.size - 5, this);
        	newBullet.velocity = (specialBullet.speed);
            newBullet.angle = (angle);
            bullets.add(newBullet);
            Conquest.game.shakeScreen(10, 1000);
            toSpecial = specialRateOfFire;
        }
    }
    
    public void useEquipment() {
    	Equipment equipment = (Equipment) inventory.getSelectedEquipment();
    	equipment.use();
    }
    
	
	public void move(int x) {
		if (canMove && !locked) {
			setX(x);
		}
	}

    public void reduceBulletWait() {
        if (toNextBullet >= 0) {
		toNextBullet -= Conquest.DELAY; }
        if (toSpecial >= 0) {
		toSpecial -= Conquest.DELAY; }
        gui.bulletCooldown.setValue(toNextBullet);
		gui.specialCooldown.setValue(toSpecial);
		gui.healthBar.setValue(health);
    }
    
    public void repaintGUI() {
    	gui.repaint();
    }
    
    public void updateGUI() {
    	gui.updateGUI();
    }
    
    public void hideGUI() {
    	gui.hide();
    }
    
    public void showGUI() {
    	gui.show();
    }
    
    public Inventory getInventory() {
    	return inventory;
    }
    
    public void addToInventory(Modifier modifier) {
    	inventory.addToInventory(modifier);
    }
    
    public void resetStats() {
    	attack = 5;
    }

}
