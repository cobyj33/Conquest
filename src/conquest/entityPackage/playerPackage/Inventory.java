package conquest.entityPackage.playerPackage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import conquest.Resources;
import conquest.modifiers.Modifier;
import conquest.weaponPackage.BulletProperties;
import conquest.weaponPackage.WeaponProperties;
import conquest.weaponPackage.equipment.Abilities;
public class Inventory implements java.io.Serializable {
	Player player;
	
	List<Modifier> items;
	
	public static final int PRIMARY = 1, SPECIAL = 2, EQUIPMENT = 3;
	
	private BulletProperties selectedPrimary;
	private BulletProperties selectedSpecial;
	private WeaponProperties selectedEquipment;
	
	
	Inventory(Player player) {
		this.player = player;
		items = new ArrayList<>();
		
		createDefaultInventory();
	}
	
	public void createDefaultInventory() {
		clear();
		setDefault();
	}
	
	public void setDefault() {
		BulletProperties special = new BulletProperties("Special");
		special.damage = 10;
		special.size = 20;
		special.sound = Resources.getSound(Resources.LASER);
		
		BulletProperties standardBullet = new BulletProperties("Standard");
		standardBullet.setImage(Resources.getImage(Resources.ImageEnum.BASICBULLET));
		
		selectedPrimary = standardBullet;
		selectedSpecial = special;
		selectedEquipment = Abilities.get(Abilities.EMP);
	}
	
	public BulletProperties getSelectedPrimary() {
		//System.out.println("test 2: " + selectedPrimary.getName());
		return selectedPrimary;
	}
	
	public BulletProperties getSelectedSpecial() {
		return selectedSpecial;
	}
	
	public WeaponProperties getSelectedEquipment() {
		return selectedEquipment;
	}
	
	public List<Modifier> getPrimaryModifiers() {
		return items.stream()
				.filter(p -> p.getTarget() == Modifier.PRIMARY)
				.collect(Collectors.toList());
	}
	
	public List<Modifier> getSpecialModifiers() {
		return items.stream()
				.filter(p -> p.getTarget() == Modifier.SPECIAL)
				.collect(Collectors.toList());
	}
	
	public List<Modifier> getEquipmentModifiers() {
		return items.stream()
				.filter(p -> p.getTarget() == Modifier.TERTIARY)
				.collect(Collectors.toList());
	}
	
	public List<Modifier> getStatModifiers() {
		return items.stream()
				.filter(p -> p.getTarget() == Modifier.ENTITY)
				.collect(Collectors.toList());
	}
	
	public void addToInventory(Modifier mod) { 
		items.add(mod);
		applyModifiers();
	} 
	
	public void removeFromInventory(Modifier mod) {
		if (items.contains(mod)) {
			items.remove(mod);
			applyModifiers();
		}
	}
	
	
	
	public void select(Modifier mod) {
		if (!items.contains(mod)) { return; }
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).equals(mod)) {
				mod.setActive(true);
				System.out.println("adding modifiers");
				applyModifiers();
				return;
			}
		}
	}
	
	public boolean contains(Modifier mod) {
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).equals(mod)) {
				return true;
			}
		}
		return false;
	}
	
	public void deselect(Modifier mod) {
		if (!items.contains(mod)) { return; }
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).equals(mod)) {
				mod.setActive(false);
				System.out.println("removing modifiers");
				applyModifiers();
				return;
			}
		}
	}
	
	public void applyModifiers() {
		
		//standard properties
		setDefault();
		player.resetStats();
		
		System.out.println("applying modifiers");
		for (int i = 0; i < items.size(); i++) {
			Modifier current = items.get(i);
			if (current.isActive()) {
				System.out.println("applied modifier -> " + current.getName());
				current.modify();
			}
		}
		
	}
	
	public void clear() {
		items.clear();
		
//		Modifier boomerangBullet = new Modifier(() -> {
//			selectedPrimary.movementBehavior = BulletProperties.BOOMERANG;
//			selectedPrimary.setImage(Resources.getImage(Resources.BOOMERANG));
//		}, "Boomerang", "desc", Modifier.PRIMARY);
//		
//		boomerangBullet.setImage(Resources.getImage(Resources.BOOMERANG));
//		
//		Modifier spreadBullet = new Modifier( () -> {
//		selectedPrimary.movementBehavior = BulletProperties.SPREAD;
//		selectedPrimary.setImage(Resources.getImage(Resources.BASICBULLET));
//		}, "Spread", "desc", Modifier.PRIMARY);
//		
//		spreadBullet.setImage(Resources.getImage(Resources.BASICBULLET));
//		
//		Modifier explosion = new Modifier( () -> {
//			selectedPrimary.impactBehavior = BulletProperties.EXPLODE;
//		}, "Explosion", "desc", Modifier.PRIMARY);
//		
//		addToInventory(boomerangBullet);
//		addToInventory(spreadBullet);
//		addToInventory(explosion);
	}
	

	
	
	
}
