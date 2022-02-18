package conquest.entityPackage.enemyPackage;
import java.util.ArrayList;
import conquest.entityPackage.enemyPackage.enemies.*;
import java.util.List;
import java.util.OptionalInt;

import conquest.Conquest;

public class EnemyLine {
    ArrayList<Enemy> enemies;
    public static ArrayList<Integer> heights = new ArrayList<>();
    int maxCapacity;
    public int top;
    public int bottom;
    public int left;
    public int right;
    
    public int moveRequests;
    public double angle; //angle that the line moves in
    
    public int velocity;
    //public int direction;

    public EnemyLine(int enemyCount) {
        maxCapacity = enemyCount;
        enemies = new ArrayList<>(10);
        velocity = 3;
        angle = 0;
        moveRequests = 0;
    }

    public void construct(int left, int right, int height) {
        heights.add(height);
        this.left = left; this.right = right; this.top = height; this.bottom = height;
        OptionalInt bottom = enemies.stream().mapToInt(Enemy::getSize).max();
        
        if (bottom.isPresent()) {
        	this.bottom = height + bottom.getAsInt();
        }
        
        
        int length = right - left;
        int totalSize = enemies.stream().mapToInt(e -> e.getSize()).sum();
        int maxPadding = (length - totalSize) / (enemies.size() + 1);

        for (int e = 0; e < maxCapacity; e++) {
//        	int x = left + maxPadding * (e + 1) + ( Enemy.standardEnemy.size * (e - 1));
        	int x = e * 30 + left;
        	int chance = (int) (Math.random() * 10);
        	
        	Enemy newEnemy = null;
        	switch (Conquest.game.difficulty) {
        	case 0: 
        		if (chance < 9) {
        			newEnemy = new Enemy(Enemy.standardEnemy, x, height, this);
        		} else {
        			newEnemy = new TurretEnemy(Enemy.standardEnemy, x, height, this);
        		}
        		break;
        	case 1:
        		if (chance < 5) {
        			newEnemy = new Enemy(Enemy.standardEnemy, x, height, this);
        		} else if (chance >= 5 && chance < 7) {
        			newEnemy = new TurretEnemy(Enemy.standardEnemy, x, height, this);
        		} else if (chance >= 7) {
        			newEnemy = new SpreadEnemy(Enemy.standardEnemy, x, height, this);
        		}
        		break;
        	case 2:
        		if (chance < 2) {
        			newEnemy = new Enemy(Enemy.standardEnemy, x, height, this);
        		} else if (chance >= 2 && chance < 4) {
        			newEnemy = new TurretEnemy(Enemy.standardEnemy, x, height, this);
        		} else if (chance >= 4 && chance < 7) {
        			newEnemy = new SpreadEnemy(Enemy.standardEnemy, x, height, this);
        		} else if (chance >= 7) {
        			newEnemy = new KamiKazeEnemy(Enemy.standardEnemy, x, height, this);
        		}
        		break;
        	case 3:
        		if (chance < 2) {
        			newEnemy = new Enemy(Enemy.standardEnemy, x, height, this);
        		} else if (chance >= 2 && chance < 7) {
        			newEnemy = new TurretEnemy(Enemy.standardEnemy, x, height, this);
        		} else if (chance >= 7) {
        			newEnemy = new SpreadEnemy(Enemy.standardEnemy, x, height, this);
        		}
        		break;
        	default:
        		if (chance < 2) {
        			newEnemy = new SpreadEnemy(Enemy.standardEnemy, x, height, this);
        		} else if (chance >= 2 && chance < 3) {
        			newEnemy = new TurretEnemy(Enemy.standardEnemy, x, height, this);
        		} else if (chance >= 3 && chance < 5) {
        			newEnemy = new KamiKazeEnemy(Enemy.standardEnemy, x, height, this);
        		} else if (chance >= 5 && chance < 7) {
        			newEnemy = new CurvedEnemy(Enemy.standardEnemy, x, height, this);
        		} else if (chance >= 7) {
        			newEnemy = new ExplosiveEnemy(Enemy.standardEnemy, x, height, this);
        		}
        		break;
//        	default:
//        		
        	}
        	
        	if (newEnemy == null) System.out.println("ERROR IN CREATING ENEMYLINES");
        	else enemies.add(newEnemy);
        }

    }
    
    
    public void requestMove() {
    	moveRequests++;
    	
    	for (int e = 0; e < enemies.size(); e++) {
		Enemy enemy = enemies.get(e);
		if (!Conquest.game.isInBounds(enemy.hitbox) && enemy.inBounds) {
			enemy.inBounds = false;
			angle = angle == 0 ? Math.PI : 0;
//			System.out.println("[EnemyLine.move()] switched direction");
			break;
		} else if (Conquest.game.isInBounds(enemy.hitbox) && !enemy.inBounds) {
			enemy.inBounds = true;
		}
	}
    	
    	if (moveRequests >= enemies.size()) {
    		for (int e = 0; e < enemies.size(); e++) {
    			enemies.get(e).angle = this.angle;
    			enemies.get(e).moveAsLine();
    		}
    		moveRequests = 0;
    	}
    }

    public void removeEnemy(Enemy enemy) {
        enemies.remove(enemy);
        if (enemies.size() == 0) {
        	Conquest.player.linesCleared++;
//        	Conquest.game.setDifficulty( Conquest.game.getPlayer().linesCleared / 3);
        	Conquest.game.difficulty++;
        }
    }

    public List<Enemy> getEnemies() { return enemies; }

    public void isFull() {
        boolean full = enemies.size() == maxCapacity ? true : false;
        System.out.println(full);
    }
}
