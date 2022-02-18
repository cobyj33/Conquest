package conquest.entityPackage;

import java.awt.*;
import java.awt.image.BufferedImage;

import conquest.Conquest;
import conquest.entityPackage.enemyPackage.Enemy;

public class Position {
    private int x;
    private int y;
    private int size;
    
    
    public BufferedImage image;
    public Color color;
    public int velocity;
    public double angle;
    public Rectangle hitbox;
    public boolean visible;
    public boolean inBounds = true;
    public volatile boolean canMove = true;


    public Position() {
        hitbox = new Rectangle();
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
        size = 5;
        hitbox = new Rectangle(x, y, size, size);
    }

    public Position(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
        hitbox = new Rectangle(x, y, size, size);
    }

    public int getXVelocity() { return (int) (velocity * Math.cos(angle)); }
    public int getYVelocity() { return -(int) (velocity * Math.sin(angle)); }
    
    public int getX() { return x; }
    public int getY() { return y; }
    public int getSize() { return size; }

    public void setSize(int size) { this.size = size; hitbox.setSize(size, size); }
    public void setX(int x) { this.x = x; hitbox.x = x; }
    public void setY(int y) { this.y = y; hitbox.y = y; }

    public void setLocation(int x, int y) { setX(x); setY(y); }
    
    public void setLocation(Point p) { setX((int) p.getX()); setY((int) p.getY()); }



	public void move() {
		//System.out.println("Moving");
		int xVelocity = (int) (velocity * Math.cos(angle));
		int yVelocity = (int) -(velocity * Math.sin(angle));
		y += yVelocity;
		x += xVelocity;
		

        int hitboxX, hitboxY, hitboxWidth, hitboxHeight;
        hitboxHeight = size + (int) Math.abs(yVelocity);
        hitboxWidth = size + (int) Math.abs(xVelocity);
        
        if (yVelocity <= 0) {
        	hitboxY = y - yVelocity;
        } else {
        	hitboxY = y;
        }
        
        if (xVelocity <= 0) {
        	hitboxX = x - xVelocity;
        } else {
        	hitboxX = x;
        }
        
        hitbox.setBounds(hitboxX, hitboxY, hitboxWidth, hitboxHeight);
    }
    
	public void switchDirection() {
		angle += Math.PI / 2;
	}
}
