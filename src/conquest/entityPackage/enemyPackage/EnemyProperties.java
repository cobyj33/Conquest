package conquest.entityPackage.enemyPackage;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class EnemyProperties {
	public int maxHealth;
	public int score;
	public int maxRof;
	public int minRof;
	public boolean scaleToDamage;
	public int size;
	public int minSize;
	public int speed; //currently unused in most cases
	public Color color;
	public BufferedImage image;
	
	public EnemyProperties() {
		maxHealth = 10;
		score = 10;
		maxRof = 8000;
		minRof = 500;
		scaleToDamage = true;
		size = 20;
		minSize = 10;
		color = Color.red;
		image = null;
		speed = 2;
	}
}
