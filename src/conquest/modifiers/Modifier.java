package conquest.modifiers;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Optional;

import conquest.Conquest;
import conquest.Resources;
import conquest.entityPackage.Entity;
import conquest.weaponPackage.BulletProperties;
import conquest.weaponPackage.WeaponProperties;

//Similar to WeaponProperties

public class Modifier implements java.io.Serializable {
	private String description;
	private String name;
	private int amount;
	private boolean active;
	private BufferedImage image;
	private int target;
	private Runnable mod;
	
	public static final int PRIMARY = 0, SPECIAL = 1, TERTIARY = 2, ENTITY = 3;
	
	static HashMap<Integer, Modifier> modifiers;
	public static final int HEALTH = 1,
			DAMAGE = 2,
			DEFENSE = 3,
			LIVES = 4,
			BOOMERANG = 5,
			SPREAD = 6,
			EXPLOSION = 7;
	
	public Modifier(Runnable run, String name, String description, int target) {
		this.description = description;
		this.name = name;
		this.target = target;
		active = false;
		mod = run;
	}
	
	public static void init() {
		modifiers = new HashMap<>();
		
		Modifier damageBoost = new Modifier( () -> Conquest.player.attack += 5, "Damage Boost", "Increases Damage by 5", Modifier.ENTITY);
		damageBoost.setImage(Resources.getImage(Resources.ImageEnum.SWORD));
		modifiers.put(DAMAGE, damageBoost);
		
		Modifier healthBoost = new Modifier( () -> {
			Conquest.player.maxHealth += 5;
			Conquest.player.health += 5; }, "Health Boost", "Increases Health by 5", Modifier.ENTITY);
		healthBoost.setImage(Resources.getImage(Resources.ImageEnum.HEART));
		modifiers.put(HEALTH, healthBoost);
		
		Modifier livesBoost = new Modifier( () -> {
			Conquest.player.setLives( Conquest.player.getLives() + 1); }, "Lives Boost", "Adds an extra life", Modifier.ENTITY);
		livesBoost.setImage(Resources.getImage(Resources.ImageEnum.SHIP));
		modifiers.put(LIVES, livesBoost);
		
		Modifier defenseBoost = new Modifier( () -> {
			Conquest.player.defense += 5; }, "Defense Boost", "Increases Defense by 5", Modifier.ENTITY);
		defenseBoost.setImage(Resources.getImage(Resources.ImageEnum.SHIELD));
		modifiers.put(DEFENSE, defenseBoost);
		
		Modifier boomerangBullet = new Modifier(() -> {
			Conquest.player.getInventory().getSelectedPrimary().movementBehavior = BulletProperties.BOOMERANG;
			Conquest.player.getInventory().getSelectedPrimary().setImage(Resources.getImage(Resources.ImageEnum.BOOMERANG));
		}, "Boomerang", "desc", Modifier.PRIMARY);
		boomerangBullet.setImage(Resources.getImage(Resources.ImageEnum.BOOMERANG));
		modifiers.put(BOOMERANG, boomerangBullet);
		
		Modifier spreadBullet = new Modifier( () -> {
			Conquest.player.getInventory().getSelectedPrimary().movementBehavior = BulletProperties.SPREAD;
			Conquest.player.getInventory().getSelectedPrimary().setImage(Resources.getImage(Resources.ImageEnum.BASICBULLET));
		}, "Spread", "desc", Modifier.PRIMARY);
		spreadBullet.setImage(Resources.getImage(Resources.ImageEnum.BASICBULLET));
		modifiers.put(SPREAD, spreadBullet);
		
		Modifier explosion = new Modifier( () -> {
			Conquest.player.getInventory().getSelectedPrimary().impactBehavior = BulletProperties.EXPLODE;
		}, "Explosion", "desc", Modifier.PRIMARY);
		modifiers.put(EXPLOSION, explosion);
		
	}
	
	public static Modifier getMod(int choice) {
		if (modifiers.containsKey(choice));
			return modifiers.get(choice);
	}
	
	public void modify() {
		mod.run();
	}
	
	public boolean equals(Modifier mod) {
		if (mod.getName().equals(name)) {
			return true;
		}
		return false;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public Runnable getMod() {
		return mod;
	}

	public void setMod(Runnable mod) {
		this.mod = mod;
	}
	
	
	
	
}