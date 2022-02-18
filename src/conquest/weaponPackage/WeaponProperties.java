package conquest.weaponPackage;

import java.awt.image.BufferedImage;

import conquest.entityPackage.Entity;

public class WeaponProperties {
	private String name;
	private String description;
	private BufferedImage image;
	private Entity source;
	
	public WeaponProperties(String name) {
		this.name = name;
		description = "NO DESCRIPTION AVAILABLE";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean equals(WeaponProperties w) {
		if (w.getName().equals(getName())) {
			return true;
		} return false;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Entity getSource() {
		return source;
	}

	public void setSource(Entity source) {
		this.source = source;
	}
	
	
	
}
