package conquest;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.AncestorEvent;
import conquest.entityPackage.*;
import conquest.entityPackage.enemyPackage.*;
import conquest.entityPackage.playerPackage.Player;
import conquest.modifiers.Achievement;
import conquest.modifiers.Modifier;
import conquest.modifiers.PowerUp;
//import conquest.entityPackage.playerPackage.Player;
import conquest.screenPackage.*;
import conquest.utilities.*;
import conquest.weaponPackage.*;
import conquest.weaponPackage.equipment.Abilities;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;


@SuppressWarnings("serial")
public class Conquest extends JPanel {


	public static Conquest game;
	static boolean initialized = false;
    javax.swing.Timer gameTimer;
    public static Player player;
    public PauseGUI pauseGUI;
    MouseChecker mouseChecker;
    KeyChecker keyChecker;
    private List<Position> objects;
    public Point spawn;
    public Screens screenGroup;
    public boolean started;
    Random randomizer;
    
    StarAnimator starAnimator;
    File currentGameFile;
    
    
    public int mouseX;
    public int mouseY;
    
    private int screenShakeOffsetX;
    private int screenShakeOffsetY;
    public boolean bossFight;
    public GameState gameState;
    
    public int difficulty = 0;
    
    public static final int DELAY = 15;
    public static int width;
    public static int height;
    
    public static enum GameState {
    	BOSS, WAVE;
    }
    
    public static enum BossLevel {
    	WARSHIP(5),
    	WALL(10);
    	
    	private int level;
    	BossLevel(int level) {
    		this.level = level;
    	}
    	
    	public int getLevel() {
    		return level;
    	}
    	
    	public static boolean isBossLevel(int difficulty) {
    		for (BossLevel level : BossLevel.values()) {
    			if (level.getLevel() == difficulty)
    				return true;
    		}
    		return false;
    	}
    	
    }

    public Conquest(Screens screenGroup) {
    	if (!initialized) {
    		initialized = true;
    		game = this;
	    	started = false;
	    	System.out.println("[Conquest.init] Creating game");
	    	gameState = GameState.WAVE;
	    	
	    	Abilities.init();
	    	Modifier.init();
	    	PowerUp.init();
	    	Achievement.init();
	    	
	    	randomizer = new Random();
	    	this.screenGroup = screenGroup;
	    	setPreferredSize(screenGroup.mainPanel.getSize());
	    	setSize(screenGroup.mainPanel.getSize());
	    	setLayout(new BorderLayout());
	    	//setBackground(Color.BLACK);
	    	
	    	starAnimator = new StarAnimator(this);
	    	Color[] starColors = new Color[] { new Color(173, 216, 230), Color.YELLOW};
	    	starAnimator.configure(starColors, 25, 40);
	    	
	        mouseChecker = new MouseChecker(this);
	        keyChecker = new KeyChecker(this);
	        addKeyListener(screenGroup.screenFunctions);
	        
	        addAncestorListener(new AncestorAdapter() {
	        	public void ancestorAdded(AncestorEvent e) {
	        		if (player != null) {
	        			player.updateGUI();
	        		}
	        	}
	        	
	        	public void ancestorRemoved(AncestorEvent e) {
	        		
	        	}
	        });
	        
	        System.out.println("[Conquest.init] line 88");
	        
	        addMouseListener(mouseChecker);
	        addMouseMotionListener(mouseChecker);
	        addKeyListener(keyChecker);
	        
	        setFocusable(true);
	        requestFocus(true);
	//        enemylines = new ArrayList<>();
	//        shields = new ArrayList<>();
	        System.out.println("[Conquest.init] right before objects declaration");
	        objects = new ArrayList<>();
	        addObject(new Position(10, 10, 20));
	        System.out.println("[Conquest.init] right after objects declaration");
	        spawn = new Point();
	        System.out.println("[Conquest.init] right before player declaration");
	        player = new Player(spawn.getLocation().x, spawn.getLocation().y);
	        System.out.println("[Conquest.init] right after player declaration");
	        System.out.println("[Conquest.init] Player instance: " + player);
	        gameTimer = new javax.swing.Timer(DELAY, e -> iterateGame());
	        pauseGUI = new PauseGUI(this);
	        //player = new Player(spawn.getLocation().x, spawn.getLocation().y, this);
    	} else {
    		System.out.println("[Conquest]: Tried to initialize new game");
    	}
    }

    public void start() {
    	System.out.println(UIManager.getLookAndFeel());
    	System.out.println("[Conquest.start()] " + player.image);
    	started = true;
    	removeAll();
    	objects.clear();
    	objects.add(player);
        requestFocus();

        width = getWidth();
        height = getHeight();
        spawn.setLocation(getWidth() / 2, (int) (getHeight() * 0.85));
        if (player.getLives() <= 0) {
        	System.out.println("Resetting Player");
        	player.createDefault();
        } else {
        	player.setLocation(spawn);
        }
        player.showGUI();

        for (int i = getWidth() / 10; i < getWidth(); i += (int) (getWidth() * 0.33)) {
            new Shield(i, (int) (getHeight() * 0.70));
        }
        
        
        constructTestLines();
        player.repaintGUI();
        
        
        gameTimer.restart();
        System.out.println(Thread.currentThread());
    }
    
    public void pause() {
    	gameTimer.stop();
    	pauseGUI.show();
    	player.hideGUI();
    }
    
    public void resume() {
    	System.out.println("resuming");
    	gameTimer.restart();
    	if (pauseGUI != null ) {
    		pauseGUI.hide();
    	}
    	player.showGUI();
    }
    
    public void constructTestLines() {
    	if (difficulty >= 5) {
    		bossFight = true;
    		Enemy.spawnWarShip();
    	} else {
    	EnemyLine testLine = new EnemyLine(10);
        testLine.construct(0, getWidth(), getHeight() / 4);
        EnemyLine testLine2 = new EnemyLine(10);
        testLine2.construct(0, getWidth(), (int) (getHeight() * 0.50));
    	}
    }
    
    
    public void endGame() {
    	started = false;
        screenGroup.switchScreen(Screens.DEATHSCREEN);
    }
    
    public synchronized void iterateGame() {
        player.reduceBulletWait();
        Achievement.testAll();
        
        int chance = randomizer.nextInt(200);
        if (chance > 190) {
        	if (!objects.stream().anyMatch(o -> o instanceof PowerUpHolder)) {
//        		System.out.println("Spawning powerup holder");
        		new PowerUpHolder(Enemy.standardEnemy, 0, getHeight() / 10);
        	}
        }
        
        if (player.shooting) { player.addBullet(); }

        for (int o = 0; o < objects.size(); o++) {
        	Position pos = objects.get(o);
        	pos.move();
        	if (pos instanceof Enemy)
        		((Enemy) pos).addBullet();
        }
        
        checkCollisions();
        
        if (objects.stream().filter(o -> {
        if (o instanceof Enemy && ((Enemy) o).getLine() != null) return true; 
        return false; }
        ).count() == 0 && !bossFight) {
        	constructTestLines();
        }
        
        repaintGame();
    }
    
    public synchronized List<Entity> getEntities() {
    	List<Entity> entities = new ArrayList<>();
    	
    	for (int o = 0; o < objects.size(); o++) {
    		Position pos = objects.get(o);
    		if (pos instanceof Entity) {
    			Entity entity = (Entity) pos;
    			entities.add(entity);
    		}
    	}
    	
    	return entities;
    }
    
    public synchronized List<Bullet> getBullets() {
    	List<Bullet> bullets = new ArrayList<>();
    	
    	for (int o = 0; o < objects.size(); o++) {
    		Position pos = objects.get(o);
    		if (pos instanceof Bullet) {
    			Bullet bullet = (Bullet) pos;
    			bullets.add(bullet);
    		}
    	}
    	
    	return bullets;
    }
    
    public void addObject(Position position) {
    	//System.out.println("Added Object: " + position);
    	objects.add(position);
    }
    
    public List<Position> getObjects() {
    	return objects;
    }
    
    public void removeObject(Position position) {
    	if (position instanceof Player) {
    		System.out.println("ERRORRRRRR");
    		System.exit(0);
    	}
    	
    	if (position instanceof PowerUp)
    		System.out.println("Powerup REMOVED");
    	objects.remove(position);
    }
    
//    public void addPowerUp(int x, int y, int duration, int choice) {
//    	PowerUp.createPowerUp(x, y, 10, duration, choice);
//    	addObject(powerup);
//    	Main.scheduler.schedule(() -> objects.remove(powerup), duration, TimeUnit.MILLISECONDS);
//    }
    

    
    
    public void repaintGame() {
    	repaint();
    }

    public boolean isInBounds(Rectangle rect) {
    	if (rect == null) { return true; }
        if (rect.x < 0 || rect.y < 0 || rect.x + rect.width > getWidth() || rect.y + rect.height > getHeight()) {
            return false;
        }
        return true;
    }

    private void checkCollisions() {
    	for (int o = 0; o < objects.size(); o++) {
    		if (objects.get(o) instanceof Colliding) {
    			Colliding collider = (Colliding) objects.get(o);
    			collider.checkCollisions();
    		}
    	}
    }

    public boolean checkOverlap(Position pos1, Position pos2) {
        Rectangle rect1 = pos1.hitbox;
        Rectangle rect2 = pos2.hitbox;

        if (rect1.intersects(rect2) || rect2.intersects(rect1)) { return true; }
        return false;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setPaint(Color.BLACK);
        g2D.fillRect(0, 0, getWidth(), getHeight());
        //System.out.println("repaint");
        
        if (starAnimator.isRunning()) {
			starAnimator.animate(g2D);
		}
        
        
        
        for (int o = 0; o < objects.size(); o++) {
        	Position pos = objects.get(o);
        	Rectangle hitbox = pos.hitbox;
        	
        	
        	
        	if (pos.image != null) {
//        		g2D.drawImage(pos.getImage(), pos.getX() + screenShakeOffsetX, pos.getY() + screenShakeOffsetY, pos.getSize(), pos.getSize(), null);
        		g2D.drawImage(pos.image, hitbox.x + screenShakeOffsetX, hitbox.y + screenShakeOffsetY, hitbox.width, hitbox.height, null);
        	} else {
        		g2D.setPaint(pos.color);
//        		g2D.fillRect(pos.getX() + screenShakeOffsetX, pos.getY() + screenShakeOffsetY, pos.getSize(), pos.getSize());
        		g2D.fillRect(hitbox.x + screenShakeOffsetX, hitbox.y + screenShakeOffsetY, hitbox.width, hitbox.height);
        	}
        }
    

    }



    public void mousePressed(MouseEvent e) {
    	if (SwingUtilities.isLeftMouseButton(e)) {
    		player.shooting = true;
        }
    	
//    	System.out.println(Functions.getAngle(Conquest.game.getPlayer().getX(), Conquest.game.getPlayer().getY(), e.getX(), e.getY()));
//    	Functions.getAngle(e.getX(), e.getY(), player.getX(), player.getY());
    }


    public void mouseReleased(MouseEvent e) {
    	player.shooting = false;
        if (SwingUtilities.isRightMouseButton(e)) {
            player.addSpecial();
        }
    }


    public void mouseDragged(MouseEvent e) {
    	mouseMoved(e);
    	if (SwingUtilities.isLeftMouseButton(e)) {
    		player.addBullet();
    	}
    }

    public void mouseMoved(MouseEvent e) {
    	if (e.getX() <= 5) {
    		player.move(5);
    	} else if (e.getX() >= getWidth() - 1 - player.getSize()) {
    		player.move(getWidth() - 1 - player.getSize());
    	} else {
    		player.move(e.getX());
    	}
        updateMousePosition(e);
    }

    public void keyPressed(KeyEvent e) {
        int keycode = e.getKeyCode();

        switch (keycode) {
            case KeyEvent.VK_SPACE: player.addSpecial(); break;
            case KeyEvent.VK_ENTER: player.repaintGUI(); break;
            case KeyEvent.VK_ESCAPE: if (gameTimer.isRunning()) { pause(); } else { resume(); } break;
            case KeyEvent.VK_R: player.locked = true; break;
            case KeyEvent.VK_E: shieldRepair(); break;
            case KeyEvent.VK_Q: player.useEquipment(); break;
        }
    }
    
    public void shieldRepair() {
    	Ray ray = new Ray(player.getX(), player.getY(), player);
    	Shield toRepair = ray.hitShield();
    	System.out.println("can repair, " + toRepair);
    	if (toRepair != null) {
    		toRepair.heal(5);
    	}
    }
    
    public void keyReleased(KeyEvent e) {
    	int keyCode = e.getKeyCode();
    	
    	switch (keyCode) {
    	case KeyEvent.VK_R: player.locked = false; break;
    	}
    }
    
    public void updateMousePosition(MouseEvent e) {
    	mouseX = e.getX();
    	mouseY = e.getY();
    }
    
    public void shakeScreen(int violence, int duration) {
    	Timer timer = new Timer(50, l -> {
    		screenShakeOffsetX = (int) (Math.random() * (violence + violence) - violence);
    		screenShakeOffsetY = (int) (Math.random() * (violence + violence) - violence);
    	});
    	timer.start();
    	
    	Main.scheduler.schedule(() -> {timer.stop(); screenShakeOffsetX = 0; screenShakeOffsetY = 0; }, duration, TimeUnit.MILLISECONDS);
    }
    
    public void saveGame() {
    	if (currentGameFile == null) {
    		System.out.println("choose save file");
    		JFileChooser save = new JFileChooser();
    		save.showSaveDialog(this);
    		currentGameFile = save.getSelectedFile();
    	}
    	
    	try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(currentGameFile));
			writer.write("" + player.score);
			writer.newLine();
			writer.write("" + player.getLives());
			writer.newLine();
			writer.write("" + player.damageDealt);
			writer.newLine();
			writer.write("" + player.damageTaken);
			writer.newLine();
			writer.write("" + player.linesCleared);
			writer.newLine();
			writer.write("" + player.enemiesDestroyed);
			writer.newLine();
			writer.write("" + player.shotsFired);
			writer.newLine();
			writer.write("" + player.shotsHit);
			writer.newLine();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void loadGame() {
    	JFileChooser load = new JFileChooser();
    	load.showOpenDialog(this);
    	File toRead = load.getSelectedFile();
    	try {
    		BufferedReader reader = new BufferedReader(new FileReader(toRead));
    		player.score = Integer.parseInt(reader.readLine());
    		player.lives = Integer.parseInt(reader.readLine());
    		player.damageDealt = Integer.parseInt(reader.readLine());
    		player.damageTaken = Integer.parseInt(reader.readLine());
    		player.linesCleared = Integer.parseInt(reader.readLine());
    		player.enemiesDestroyed = Integer.parseInt(reader.readLine());
    		player.shotsFired = Integer.parseInt(reader.readLine());
    		player.shotsHit = Integer.parseInt(reader.readLine());
    		reader.close();
    		currentGameFile = toRead;
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    
//    public void saveGame() {
//    	String name = JOptionPane.showInputDialog("What is the name of the save file?: ");
//    	
//    	
//		try {
//			FileOutputStream fileOut = new FileOutputStream(name + ".ser");
//			ObjectOutputStream out = new ObjectOutputStream(fileOut);
//			out.writeObject(player);
//	    	out.close();
//	    	fileOut.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	
//    	
//    }
//    
//    public void loadGame() {
//    	JFileChooser load = new JFileChooser();
//    	String filePath = load.getSelectedFile().getAbsolutePath();
//    	
//    	try {
//    		FileInputStream fileIn = new FileInputStream(filePath);
//        	ObjectInputStream in = new ObjectInputStream(fileIn);
//        	player = (Player) in.readObject();
//        	fileIn.close();
//        	in.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	
//    }
    
    


}













class MouseChecker extends MouseAdapter {

    Conquest game;
    MouseChecker(Conquest game) {
        this.game = game;
    }

    public void mousePressed(MouseEvent e) {
        game.mousePressed(e);
        game.updateMousePosition(e);
    }


    public void mouseReleased(MouseEvent e) {
        game.mouseReleased(e);
        game.updateMousePosition(e);
    }


    public void mouseDragged(MouseEvent e) {
        game.mouseDragged(e);
        game.updateMousePosition(e);

    }

    public void mouseMoved(MouseEvent e) {
        game.mouseMoved(e);
        game.updateMousePosition(e);
    }


}

class KeyChecker extends KeyAdapter {

    Conquest game;
    KeyChecker(Conquest game) {
        this.game = game;
    }

    public void keyPressed(KeyEvent e) {
        game.keyPressed(e);
    }
    
    public void keyReleased(KeyEvent e) {
    	game.keyReleased(e);
    }


}