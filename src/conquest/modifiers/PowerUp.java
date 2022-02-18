package conquest.modifiers;

import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import conquest.Conquest;
import conquest.Main;
import conquest.Resources;
import conquest.entityPackage.Position;
import conquest.entityPackage.playerPackage.Player;
import conquest.utilities.Colliding;

public class PowerUp extends Position implements Colliding {
	private Modifier modifier;
	int duration;
	private boolean oneTime = false;
	private Runnable mod;
	
	static HashMap<Integer, PowerUp> powerUps = new HashMap<>();
	public static final int DAMAGE = 0,
			HEALTH = 1,
			DEFENSE = 2,
			LIVES = 3,
			BOOMERANG = 4,
			SPREAD = 5,
			EXPLOSION = 6;

	private PowerUp(int x, int y, int size, Modifier modifier, int duration) {
		super(x, y, size);
		color = (Color.ORANGE);
		this.modifier = modifier;
		this.duration = duration;
		image = (modifier.getImage());
		velocity = (10);
		angle = (Math.PI * 3 / 2);
		modifier.setActive(true);
	}
	
	public PowerUp(Modifier modifier) { //change to private
		super();
		this.modifier = modifier;
		image = (modifier.getImage());
		modifier.setActive(true);
	}
	
	public static void init() {
		powerUps = new HashMap<>();
		
		PowerUp damageBoost = new PowerUp(Modifier.getMod(Modifier.DAMAGE));
		powerUps.put(DAMAGE, damageBoost);
		
		PowerUp healthBoost = new PowerUp(Modifier.getMod(Modifier.HEALTH));
		powerUps.put(HEALTH, healthBoost);
		
		PowerUp defenseBoost = new PowerUp(Modifier.getMod(Modifier.DEFENSE));
		powerUps.put(DEFENSE, defenseBoost);
		
		PowerUp livesBoost = new PowerUp(Modifier.getMod(Modifier.LIVES));
		powerUps.put(LIVES, livesBoost);
		
		PowerUp boomerangBullet = new PowerUp(Modifier.getMod(Modifier.BOOMERANG));
		boomerangBullet.setOneTime(true);
		powerUps.put(BOOMERANG, boomerangBullet);
		
		PowerUp spreadBullet = new PowerUp(Modifier.getMod(Modifier.SPREAD));
		spreadBullet.setOneTime(true);
		powerUps.put(SPREAD, spreadBullet);
		
		PowerUp explosiveBullet = new PowerUp(Modifier.getMod(Modifier.EXPLOSION));
		explosiveBullet.setOneTime(true);
		powerUps.put(EXPLOSION, explosiveBullet);
	}
	
	public static PowerUp getPowerUp(int choice) {
		return powerUps.get(choice);
	}
	
	public static PowerUp getRandomPowerUp() {
		return powerUps.get( (int) (Math.random() * powerUps.size() ) );
	}
	
	//to add a powerup to the game, use the addPowerUp method in Conquest
	public static void createPowerUp(int x, int y, int size, int duration, int choice) {
		PowerUp powerup = new PowerUp(x, y, size, powerUps.get(choice).getModifier(), duration);
		powerup.velocity = (5);
		powerup.angle = (Math.PI * 3 / 2);
		Conquest.game.addObject(powerup);
		Main.scheduler.schedule(() -> SwingUtilities.invokeLater(() -> Conquest.game.removeObject(powerup)), duration, TimeUnit.MILLISECONDS);
	}
	
	public static void createPowerUp(int x, int y, int size, int duration, Modifier modifier) {
		PowerUp powerup = new PowerUp(x, y, size, modifier, duration);
		powerup.velocity = (5);
		powerup.angle = (Math.PI * 3 / 2);
		Conquest.game.addObject(powerup);
		System.out.println("Powerup created");
		Main.scheduler.schedule(() -> SwingUtilities.invokeLater(() -> Conquest.game.removeObject(powerup)), duration, TimeUnit.MILLISECONDS);
	}
	
	public static void createPowerUp(PowerUp powerUp) {
		Conquest.game.addObject(new PowerUp(powerUp.getX(), powerUp.getY(), powerUp.getSize(), powerUp.getModifier(), powerUp.duration));
	}
	
	public void checkCollisions() {		
		
    	if (Conquest.player.hitbox.intersects(this.hitbox)) {
    		System.out.println("POWERUP HIT PLAYER");
    		if ((oneTime && !Conquest.player.getInventory().contains(getModifier())) || !oneTime) {
    			getModifier().setActive(true);
        		Conquest.player.addToInventory(this.getModifier());
    		}
    		Conquest.game.removeObject(this);
    	}
    	
    	if (!Conquest.game.isInBounds(hitbox)) {
    		System.out.println("POWERUP IS OUT OF BOUNDS");
    		Conquest.game.removeObject(this);
    	}
    	
	}

	public Modifier getModifier() {
		return modifier;
	}

	public void setModifier(Modifier modifier) {
		this.modifier = modifier;
	}

	public boolean isOneTime() {
		return oneTime;
	}

	public void setOneTime(boolean oneTime) {
		this.oneTime = oneTime;
	}

	public Runnable getMod() {
		return mod;
	}

	public void setMod(Runnable mod) {
		this.mod = mod;
	}
	
	
	
	
}
