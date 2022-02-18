package conquest.entityPackage;
import java.awt.*;

import conquest.Conquest;


public class Shield extends Entity {
	
	public Shield(int x, int y) {
		super( x, y);
		maxHealth = 100;
		health = maxHealth;
		minimumSize = 20;
		color = Color.lightGray;
		setSize(40);
		setScaleToDamage(true);
		
		Conquest.game.addObject(this);
	}
	
	public void paint(Graphics2D g2D) {
		g2D.setPaint(color);
		g2D.fillRect(getX(), getY(), getSize(), getSize());
	}
	
	public void takeDamage(int damage) {
		super.takeDamage(damage);
		if (health <= 0) {
			Conquest.game.removeObject(this);
		}
	}
	
	public void move() {}
	
}
