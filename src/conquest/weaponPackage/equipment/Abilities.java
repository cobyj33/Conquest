package conquest.weaponPackage.equipment;

import java.util.HashMap;

import conquest.Conquest;
import conquest.entityPackage.Entity;
import conquest.weaponPackage.WeaponProperties;
import java.util.List;
import conquest.weaponPackage.Bullet;

public class Abilities {
	
	public static HashMap<Integer, Equipment> equipment;
	
	public static final int EMP = 0,
			GRENADE = 1,
			INVUNERABILITY = 2;
	
	public static void init() {
		equipment = new HashMap<>();
		Equipment emp = new Equipment("EMP") {

			@Override
			public void use() {
				super.use();
				List<Bullet> bullets = Conquest.game.getBullets();
				for (int b = 0; b < bullets.size(); b++) {
					Bullet bullet = bullets.get(b);
					if (bullet.getSource() != this.getSource()) {
						bullet.destroyed = true;
					}
				}
			}
		};
		
		Equipment Grenade = new Equipment("Grenade") {

			@Override
			public void use() {
				super.use();
			}
			
		};
		
		Equipment invulnerability = new Equipment("Invulnerability") {

			@Override
			public void use() {
				super.use();
			}
			
		};
		
		equipment.put(EMP, emp);
		equipment.put(GRENADE, Grenade);
		equipment.put(INVUNERABILITY, invulnerability);
	}
	
	public static Equipment get(int choice) {
		if (equipment == null) { return new Equipment("None") {}; }
		return equipment.get(choice);
	}
	
}
