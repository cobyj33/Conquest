package conquest.weaponPackage.equipment;

import conquest.weaponPackage.WeaponProperties;

public abstract class Equipment extends WeaponProperties {
	int uses;
	
	protected Equipment(String name) {
		super(name);
		uses = 3;
	}
	
	public void use() {
		if (uses <= 0) {
			return;
		} else {
			uses--;
		}
	}
	
}