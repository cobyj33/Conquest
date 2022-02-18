package conquest.weaponPackage;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.sound.sampled.Clip;

import conquest.Resources;
import conquest.entityPackage.Position;

public class BulletProperties extends WeaponProperties {
	public int damage;
	public Color color;
	public int speed;
	public int size;
	public boolean penetrating;
	public int movementBehavior;
	public int impactBehavior;
	public Point target;
	public double startingAngle;
	public Clip sound = Resources.getSound(Resources.GUN_SHOT);
    
    public static final int MOVE_STRAIGHT = 1, BOOMERANG = 2, SPREAD = 3, CURVED = 4;
    public static final int DESTROY = 1, EXPLODE = 2, STICK = 3;
    
    public int reach;
    public BulletProperties(String name) {
		super(name);
		damage = 1;
		color = Color.BLUE;
		speed = 10;
		size = 10;
		penetrating = false;
		movementBehavior = MOVE_STRAIGHT;
		impactBehavior = DESTROY;
		
		startingAngle = Math.PI / 2;
		reach = 200;
		target = null;
	}
	
	public void move(Bullet b) {
		
	}
	
	public void onImpact(Bullet b) {
		
	}
	
	
	
}
