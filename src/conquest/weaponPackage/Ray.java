package conquest.weaponPackage;

import java.util.ArrayList;
import java.util.List;

import conquest.Conquest;
import conquest.entityPackage.Entity;
import conquest.entityPackage.Position;
import conquest.entityPackage.Shield;
import conquest.entityPackage.enemyPackage.Enemy;

public class Ray extends Position {
	Entity source;
	
	public Ray(int x, int y, Entity source) {
		super(x, y);
		this.source = source;
	}
	
	public Entity[] hits() {
		List<Position> positions = Conquest.game.getObjects();
    	
    	
    	int bulletLeft = getX(); int bulletRight = getX() + getSize();
    	List<Entity> entities = new ArrayList<>();
    	
    	for (int p = 0; p < positions.size(); p++) {
    		Position pos = positions.get(p);
    		if (!(pos instanceof Entity) || pos == null) { continue; }
    		Entity entity = (Entity) pos;
    		int left = entity.getX();
            int right = entity.getX() + entity.getSize();
            if (bulletLeft > left && bulletLeft < right) {
                entities.add(entity);
            } else if (bulletRight > left && bulletRight < right) {
            	entities.add(entity);
            }
    	}
    	
    	return entities.toArray(Entity[]::new);
	}
	
	public boolean hitFriendly(Enemy enemy) {
		
    	Entity[] possibleHits = hits();
		
		for (int e = 0; e < possibleHits.length; e++) {
			if (source.identifier == possibleHits[e].identifier && source.getY() < possibleHits[e].getY()) {
				return true;
			}
		}
		return false;
	}
	
	public Shield hitShield() {
		Entity[] possibleHits = hits();
		for (int e = 0; e < possibleHits.length; e++) {
			if (possibleHits[e] instanceof Shield) {
				System.out.println("hit shield");
				return (Shield) possibleHits[e];
			}
		}
		
		return null;
	}
}
