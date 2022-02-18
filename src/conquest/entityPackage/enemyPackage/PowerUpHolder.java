package conquest.entityPackage.enemyPackage;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import conquest.Conquest;
import conquest.Main;
import conquest.Resources;
import conquest.modifiers.Modifier;
import conquest.modifiers.PowerUp;

public class PowerUpHolder extends Enemy {
	PowerUp powerUp;
	static int defaultDuration = 10000;

	public PowerUpHolder(EnemyProperties properties, int x, int y) {
		super(properties, x, y, null);
		color = Color.ORANGE;
		
//		powerUp = PowerUp.getPowerUp(PowerUp.DAMAGE);
		powerUp = PowerUp.getRandomPowerUp();
//		powerUp = PowerUp.getPowerUp(PowerUp.SPREAD);
		
		powerUp.setSize(40);
		maxHealth = 5;
		health = maxHealth;
		velocity = 1;
		angle = 0;
		score = 150;
		Conquest.game.addObject(this);
		Main.scheduler.schedule(() -> SwingUtilities.invokeLater(() -> Conquest.game.removeObject(this)), defaultDuration, TimeUnit.MILLISECONDS);
	}
	
	public void move() {
		
		if (!Conquest.game.isInBounds(hitbox) && inBounds) {
			inBounds = false;
			angle = angle == 0 ? Math.PI : 0;
		} else if (Conquest.game.isInBounds(hitbox) && !inBounds) {
			inBounds = true;
		}		
//		if (!Conquest.game.isInBounds(hitbox)) {
//			setAngle(getAngle() + Math.PI);
//		}
		super.move();
	}
	
	@Override
	public void addBullet() { //does not shoot 
		
	}
	
	public void takeDamage(int damage) {
		super.takeDamage(damage);
		if (health <= 0) {
			dropPowerUp();
			Conquest.game.removeObject(this);
		}
	}
	
	public void dropPowerUp() {
		System.out.println("Dropped powerup");
		powerUp.setX(getX());
		powerUp.setY(getY());
		powerUp.velocity = 5;
		powerUp.angle = Math.PI * 3 / 2;
		int duration = 2000;
		System.out.println("Size: " + getSize());
		Conquest.game.addObject(powerUp);
		Main.scheduler.schedule(() -> SwingUtilities.invokeLater(() -> Conquest.game.removeObject(powerUp)), duration, TimeUnit.MILLISECONDS);
		//PowerUp.createPowerUp(getX(), getY(), powerUp.getSize(), 3000, powerUp.getModifier());
	}
	
	
	
	
	
	
	
}
