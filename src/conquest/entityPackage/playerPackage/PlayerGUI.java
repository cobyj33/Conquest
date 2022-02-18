package conquest.entityPackage.playerPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JLabel;

import conquest.Conquest;
import conquest.Resources;
import conquest.GUIResources.ImageLabel;
import conquest.weaponPackage.WeaponProperties;

public class PlayerGUI implements java.io.Serializable {
	JPanel topBar;
	JPanel bottomBar;
	Dimension gameDimensions;
	JPanel livesIndicator;
	JPanel cooldownIndicator;
	JProgressBar bulletCooldown;
	JProgressBar specialCooldown;
	
	JPanel healthDisplay;
	JProgressBar healthBar;
	JLabel heartImage;
	
	JPanel inventoryDisplay;
	ItemDisplay primaryWeapon;
	ItemDisplay specialWeapon;
	ItemDisplay equipment;
	
	Player player;
	
	
	public PlayerGUI(Player player) {
		this.player = player;
		livesIndicator = new JPanel();
		cooldownIndicator = new JPanel();
		topBar = new JPanel();
		topBar.setLayout(new GridBagLayout());
		topBar.setBackground(Color.BLACK);
		
		bottomBar = new JPanel();
		bottomBar.setLayout(new GridBagLayout());
		bottomBar.setBackground(Color.BLUE);
		topBar.setOpaque(false);
		bottomBar.setOpaque(false);
		gameDimensions = new Dimension(Conquest.game.getSize());
		bottomBar.setSize(new Dimension( (int) gameDimensions.getWidth(), (int) gameDimensions.getHeight() / 8));
		bottomBar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 3), BorderFactory.createLineBorder(new Color(200, 200, 200), 3)));
		topBar.setSize(new Dimension( (int) gameDimensions.getWidth(), (int) gameDimensions.getHeight() / 10));
		//Conquest.game.revalidate(); Conquest.game.repaint();
		constructGUI();
	}
	
	private void constructGUI() {
		GridBagConstraints bottomBarConstraints = new GridBagConstraints();
		GridBagConstraints topBarConstraints = new GridBagConstraints();
		//Rectangle LivesBounds = new Rectangle((int) (gameDimensions.width * 0.8), (int) (gameDimensions.height * 0.9), (int) (gameDimensions.width * 0.19), (int) (gameDimensions.height * 0.09));
		//LivesBounds = new Rectangle(0, 0, 50, 50);
		livesIndicator.setBackground(Color.BLACK);
		
		
		livesIndicator.setLayout(new GridBagLayout());
		
		
		cooldownIndicator.setBackground(Color.BLACK);
		cooldownIndicator.setLayout(new GridBagLayout());
		bulletCooldown = new JProgressBar();
		bulletCooldown.setValue(0);
		bulletCooldown.setBackground(Color.BLUE);
		bulletCooldown.setForeground(Color.BLACK);
		bulletCooldown.setMaximum(player.getRateOfFire());
		
		specialCooldown = new JProgressBar();
		specialCooldown.setValue(0);
		specialCooldown.setBackground(Color.BLUE);
		specialCooldown.setForeground(Color.BLACK);
		specialCooldown.setMaximum(player.specialRateOfFire);
		
		GridBagConstraints cooldownIndicatorConstraints = new GridBagConstraints();
		cooldownIndicatorConstraints.gridx = 0;
		cooldownIndicatorConstraints.insets = new Insets(2, 2, 2, 2);
		cooldownIndicatorConstraints.gridy = 0;
		cooldownIndicatorConstraints.gridwidth = 2;
		cooldownIndicatorConstraints.fill = GridBagConstraints.BOTH;
		cooldownIndicator.add(bulletCooldown, cooldownIndicatorConstraints);
		
		cooldownIndicatorConstraints.gridx = 0;
		cooldownIndicatorConstraints.gridy = 1;
		cooldownIndicator.add(specialCooldown, cooldownIndicatorConstraints);
		
//		javax.swing.Timer cooldownUpdater = new javax.swing.Timer(SpaceInvaders.DELAY, l -> {
//			bulletCooldown.setValue(player.toNextBullet);
//			specialCooldown.setValue(player.toSpecial);
//		});
//		cooldownUpdater.start();
		
		bottomBarConstraints.fill = GridBagConstraints.BOTH;
		bottomBarConstraints.gridx = 0;
		bottomBarConstraints.gridy = 0;
		bottomBarConstraints.weightx = 1;
		bottomBar.add(cooldownIndicator, bottomBarConstraints);
		
		bottomBarConstraints.gridx = 1;
		bottomBar.add(livesIndicator, bottomBarConstraints);
		
		//System.out.println(bottomBar.getComponents())
		bottomBar.revalidate();
		
		topBarConstraints.weightx = 1;
		topBarConstraints.weighty = 1;
		topBarConstraints.gridx = 0;
		topBarConstraints.gridy = 0;
		topBarConstraints.fill = GridBagConstraints.VERTICAL;
		topBarConstraints.anchor = GridBagConstraints.WEST;
		//topBarConstraints.anchor = GridBagConstraints.CENTER;
		
		healthDisplay = new JPanel();
		//healthDisplay.setBackground(Color.BLUE);
		healthDisplay.setSize(topBar.getHeight(), topBar.getHeight());
		
		topBar.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				healthDisplay.setPreferredSize(new Dimension(topBar.getHeight(), topBar.getHeight()));
				topBar.revalidate(); topBar.repaint();
			}
		});
		
		healthDisplay.setLayout(new BorderLayout());
		healthDisplay.setOpaque(false);
		//healthDisplay.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		heartImage = new ImageLabel(Resources.getImage(Resources.ImageEnum.HEART));
		
		healthDisplay.add(heartImage, BorderLayout.CENTER);
		
		healthBar = new JProgressBar();
		healthBar.setPreferredSize(new Dimension(heartImage.getWidth(), heartImage.getHeight() / 2));
		healthBar.setMaximum(player.maxHealth);
		healthBar.setValue(player.health);
		healthBar.setMinimum(0);
		healthBar.setBackground(Color.BLACK);
		healthBar.setForeground(Color.RED);
		healthBar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		
		heartImage.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				healthBar.setPreferredSize(new Dimension(heartImage.getWidth(), heartImage.getHeight() / 2));
				healthDisplay.revalidate(); healthDisplay.repaint();
			}
		});
		
		healthDisplay.add(healthBar, BorderLayout.SOUTH);
		topBar.add(healthDisplay, topBarConstraints);
		
		
		topBarConstraints.fill = GridBagConstraints.BOTH;
		topBarConstraints.insets = new Insets(5, 5, 5, 5);
		inventoryDisplay = new JPanel();
		inventoryDisplay.setSize(topBar.getHeight(), topBar.getHeight());
		inventoryDisplay.setPreferredSize(inventoryDisplay.getSize());
		inventoryDisplay.setLayout(new GridLayout(1, 3, 3, 3));
		inventoryDisplay.setOpaque(false);
		
		Inventory inventory = player.getInventory();
		primaryWeapon = new ItemDisplay(inventory.getSelectedPrimary());
		specialWeapon = new ItemDisplay(inventory.getSelectedSpecial());
		equipment = new ItemDisplay(inventory.getSelectedEquipment());
		
		inventoryDisplay.add(primaryWeapon);
		inventoryDisplay.add(specialWeapon);
		inventoryDisplay.add(equipment);
		
//		topBarConstraints.gridx = 1;
//		topBarConstraints.gridwidth = 3;
//		topBar.add(Box.createHorizontalGlue(), topBarConstraints);
		
		topBarConstraints.gridx = 1;
		topBarConstraints.anchor = GridBagConstraints.EAST;
		topBar.add(inventoryDisplay, topBarConstraints);
		//System.out.println(topBar.getComponentCount());
		
		topBar.revalidate();
		topBar.repaint();
		
		updateGUI();
		Conquest.game.revalidate();
		//System.out.println("Game components: " + Conquest.game.getComponents() + " " + Conquest.game.getComponentCount());
		
		
		
	}
	
	public void updateGUI() {
			GridBagConstraints livesIndicatorConstraints = new GridBagConstraints();
			livesIndicator.removeAll();
			livesIndicator.revalidate();
			livesIndicatorConstraints.gridy = 0;
			livesIndicatorConstraints.fill = GridBagConstraints.BOTH;
			livesIndicatorConstraints.insets = new Insets(2, 2, 2, 2);
			livesIndicatorConstraints.weightx = 1;
			
			for (int i = 0; i < player.getLives(); i++) {
				JComponent life = new ImageLabel(Resources.getImage(Resources.ImageEnum.SHIP));
				livesIndicatorConstraints.gridx = i;
				
				livesIndicator.add(life, livesIndicatorConstraints);
				livesIndicator.revalidate();
				
			}
			
			livesIndicator.repaint();
			Conquest.game.revalidate();
			
			healthBar.setValue(player.health);
			healthBar.revalidate();
			
			primaryWeapon.setItem(player.getInventory().getSelectedPrimary());
			specialWeapon.setItem(player.getInventory().getSelectedSpecial());
			equipment.setItem(player.getInventory().getSelectedEquipment());
			
	}
	
	//not an override
	public void repaint() {
		livesIndicator.repaint();
		cooldownIndicator.repaint();
	}
	
	public void hide() {
		Conquest.game.remove(topBar);
		Conquest.game.remove(bottomBar);
		Conquest.game.revalidate();
	}
	
	public void show() {
		Conquest.game.add(topBar, BorderLayout.NORTH);
		Conquest.game.add(bottomBar, BorderLayout.SOUTH);
		Conquest.game.revalidate();
	}
	
}

class ItemDisplay extends JLabel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	BufferedImage image;
	
	ItemDisplay(WeaponProperties item) {
		setHorizontalAlignment(JLabel.CENTER);
		setOpaque(false);
		construct(item);
	}
	
	private void construct(WeaponProperties item) {
		String title = item.getName();
		setText(title);
		image = item.getImage();
		ImageIcon icon = new ImageIcon();
		if (image != null) {
			icon.setImage(image);
			icon.setDescription(title);
		}
		setIcon(icon);
		setIconTextGap(3);
		setHorizontalTextPosition(JLabel.CENTER);
		setVerticalTextPosition(JLabel.BOTTOM);
		setFocusable(false);
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(0, 0, 255), 1), BorderFactory.createLineBorder(new Color(0, 0, 150), 1)));
	}
	
	public void setItem(WeaponProperties item) {
		construct(item);
	}
}
