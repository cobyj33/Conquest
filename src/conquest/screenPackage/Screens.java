package conquest.screenPackage;
import javax.swing.*;
import conquest.Conquest;
import conquest.Main;
import conquest.Resources;
import conquest.weaponPackage.equipment.Abilities;

import java.util.ArrayDeque;
import java.util.HashMap;


public class Screens {
	
	MenuScreen menuScreen;
	AboutScreen aboutScreen;
	OptionsScreen optionsScreen;
	ShopScreen shopScreen;
	InventoryScreen inventoryScreen;
	DeathScreen deathScreen;
	AchievementScreen achieveScreen;
	JButton play;
	public static JPanel mainPanel;
	public ScreenFunctions screenFunctions;
//	private JLayeredPane layeredPane;
	HashMap<Integer, JPanel> screenMap;
	ArrayDeque<Integer> history;
	
	public static final int MENUSCREEN = 0,
			ABOUTSCREEN = 1,
			SHOPSCREEN = 2,
			OPTIONSSCREEN = 3,
			GAMESCREEN = 4,
			INVENTORYSCREEN = 5,
			DEATHSCREEN = 6,
			ACHIEVEMENTSCREEN = 7;
	public int transitionTime = 2000;
	private int currentScreen;
	
	public Screens(JPanel mainPanel) {
		this.mainPanel = mainPanel;
//		layeredPane = (JLayeredPane) mainPanel.getParent();
		
		
		screenFunctions = new ScreenFunctions(this);
		screenMap = new HashMap<>();
		history = new ArrayDeque<>();
		
		Conquest game = new Conquest(this);
		screenMap.put(GAMESCREEN, game);
		
		
		menuScreen = new MenuScreen(this);
		screenMap.put(MENUSCREEN, menuScreen);
		currentScreen = MENUSCREEN;
		
		aboutScreen = new AboutScreen(this);
		screenMap.put(ABOUTSCREEN, aboutScreen);
		
		shopScreen = new ShopScreen(this);
		screenMap.put(SHOPSCREEN, shopScreen);
		
		optionsScreen = new OptionsScreen(this);
		screenMap.put(OPTIONSSCREEN, optionsScreen);
		
		inventoryScreen = new InventoryScreen(game, this);
		screenMap.put(INVENTORYSCREEN, inventoryScreen);
		
		deathScreen = new DeathScreen(this);
		screenMap.put(DEATHSCREEN, deathScreen);
		
		achieveScreen = new AchievementScreen(this);
		screenMap.put(ACHIEVEMENTSCREEN, achieveScreen);
		
		mainPanel.add(menuScreen);
		history.push(MENUSCREEN);
		Resources.playSound(Resources.SoundEnum.MAIN_MENU_MUSIC);
	}
	
	public JPanel getCurrentScreen() {
		return screenMap.get(currentScreen);
	}
	
	public void switchScreen(int screenID) {
		if (screenID == SHOPSCREEN) { return; }
		Main.clearLayeredPane();
		if (screenID == INVENTORYSCREEN) {
			System.out.println("Reconstructing Inventory Screen");
			inventoryScreen = new InventoryScreen(Conquest.game, this);
		}
		Transition transition = new Transition(this, currentScreen, screenID, Transition.SLIDEDOWN);
		transition.animate();
	}
	
	public void switchImmediately(int screenID) {
		history.push(currentScreen);
//		System.out.println(history + " in switch");
		if (history.size() > 5) {
			history.poll();
		}
		mainPanel.remove(screenMap.get(currentScreen));
		System.out.println(mainPanel.getComponents());
		mainPanel.revalidate();
		mainPanel.repaint();
		mainPanel.add(screenMap.get(screenID));
		screenMap.get(screenID).requestFocus();
		
		if (currentScreen == GAMESCREEN) {
			Conquest.game.pause();
		}
		
		mainPanel.revalidate(); mainPanel.repaint();
		currentScreen = screenID;
		if (screenID == GAMESCREEN) {
			if (Conquest.game.started) {
				Conquest.game.resume();
			} else { Conquest.game.start(); }
		}
	}
	
	public void goBackToLastScreen() {
		if (history.size() == 0) { return; }
		int last = history.pop();
//		System.out.println(history + " before switch");
		switchScreen(last);
//		System.out.println(history + " after switch");
		history.pop();
//		System.out.println(history + " after pop");
//		mainPanel.remove(screenMap.get(currentScreen));
//		mainPanel.add(screenMap.get(last));
//		screenMap.get(last).requestFocus();
		if (currentScreen == GAMESCREEN) {
			Conquest.game.pause();
		}
//		
//		mainPanel.revalidate(); mainPanel.repaint();
//		currentScreen = last;
	}
	
}