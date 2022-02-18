package conquest.screenPackage;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.event.*;

import conquest.Conquest;
import conquest.Resources;
import conquest.GUIResources.GUIButton;
import conquest.GUIResources.GUILabel;
import conquest.GUIResources.ImageLabel;
import conquest.entityPackage.playerPackage.Player;
import conquest.modifiers.Modifier;
import conquest.utilities.AncestorAdapter;
import conquest.utilities.AutoScroller;

//removed

public class ShopScreen extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Screens screenGroup;
	
	ShopScrollPane scrollPane;
	ShopWindow shopWindow;
	JPanel topBar;
	GUILabel playerScoreLabel;
	JPanel bottomBar;
	
	
	@SuppressWarnings("serial")
	public ShopScreen(Screens screenGroup) {
		this.screenGroup = screenGroup;
		setSize(screenGroup.mainPanel.getSize());
		int barHeight = getHeight() / 8;
		
		shopWindow = new ShopWindow();
		shopWindow.setBackground(Color.BLACK);
		
		shopWindow.setLayout(new GridBagLayout());
		GridBagConstraints shopWindowConstraints = new GridBagConstraints();
		
		
		ShopItem damageUpgrade = new ShopItem("Damage Upgrade", 10, Resources.getImage(Resources.ImageEnum.SWORD)) {
			public void purchase() {
				super.purchase();
				Modifier damageMod = new Modifier(() -> {
					Player player = Conquest.player;
					player.attack += 5;
					System.out.println("ATTACK: " + player.attack);
				}, "Damage", "Adds 5 Damage", Modifier.ENTITY);
				damageMod.setActive(true);
				damageMod.setImage(Resources.getImage(Resources.ImageEnum.SWORD));
				Conquest.player.addToInventory(damageMod);
			}
		};
		
		ShopItem healthUpgrade = new ShopItem("Health Upgrade", 10, Resources.getImage(Resources.ImageEnum.HEART)) {
			public void purchase() {
				super.purchase();
				Modifier healthMod = new Modifier( () -> {
					Conquest.player.maxHealth += 5;
				}, "Health", "Adds 5 HP", Modifier.ENTITY);
//				Modifier healthMod = new Modifier("Health", "Adds 5 HP", Modifier.ENTITY) {
//					public void modify() {
//						// TODO Auto-generated method stub
//						Conquest.player.setMaxHealth(Conquest.player.getMaxHealth() + 5);
//					}
//				};
				healthMod.setImage(Resources.getImage(Resources.ImageEnum.HEART));
				healthMod.setActive(true);
				Conquest.player.addToInventory(healthMod);
			}
		};
		
		ShopItem livesUpgrade = new ShopItem("Lives Upgrade", 10, null) {
			public void purchase() {
				super.purchase();
				Modifier livesModifier = new Modifier( () -> {
					Conquest.player.setLives(Conquest.player.getLives() + 1);
				}, "Lives", "Adds One Life", Modifier.ENTITY);
				livesModifier.setActive(true);
				livesModifier.setImage(Resources.getImage(Resources.ImageEnum.SHIP));
				Conquest.player.addToInventory(livesModifier);
				
			}
		};
		
		shopWindowConstraints.gridx = 0;
		shopWindowConstraints.gridy = 0;
		shopWindowConstraints.fill = GridBagConstraints.BOTH;
		shopWindowConstraints.insets = new Insets(10, 10, 10, 10);
		
		shopWindow.add(damageUpgrade, shopWindowConstraints);
		shopWindowConstraints.gridx = 1;
		shopWindow.add(healthUpgrade, shopWindowConstraints);
		shopWindowConstraints.gridx = 2;
		shopWindow.add(livesUpgrade, shopWindowConstraints);
		
		shopWindow.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				Component c = (Component) e.getSource();
				System.out.println("Width: " + c.getWidth());
			}
		});
		
		//resizes panel;
		//int shopWindowWidth = Arrays.stream(shopWindow.getComponents()).mapToInt(Component::getWidth).sum();
		//shopWindow.setSize(shopWindowWidth, getHeight() - (2 * barHeight));
		
		
		scrollPane = new ShopScrollPane();
		scrollPane.setViewportView(shopWindow);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		topBar = new JPanel();
		topBar.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));
		bottomBar = new JPanel();
		bottomBar.setLayout(new FlowLayout(FlowLayout.TRAILING, 5, 5));
		topBar.setSize(getWidth(), barHeight);
		topBar.setPreferredSize(topBar.getSize());
		bottomBar.setSize(getWidth(), barHeight);
		bottomBar.setPreferredSize(bottomBar.getSize());
		//shopWindow.setSize(getWidth(), getHeight() - bottomBar.getHeight() - topBar.getHeight());
		topBar.setBackground(Color.BLACK);
		bottomBar.setBackground(Color.BLACK);
		
		
		addKeyListener(screenGroup.screenFunctions);
		setLayout(new BorderLayout());
		setBackground(Color.RED);
		setPreferredSize(screenGroup.mainPanel.getSize());
		add(topBar, BorderLayout.NORTH);
		add(bottomBar, BorderLayout.SOUTH);
		add(scrollPane, BorderLayout.CENTER);
		
		playerScoreLabel = new GUILabel("Score: " + Conquest.player.score);
		
		GUIButton backButton = new GUIButton("< Back");
		backButton.addActionListener(l -> screenGroup.goBackToLastScreen());
		
		GUIButton menuButton = new GUIButton("To Main Menu");
		menuButton.addActionListener(l -> screenGroup.switchScreen(Screens.MENUSCREEN));
		
		topBar.add(backButton);
		topBar.add(playerScoreLabel);
		topBar.add(menuButton);
		
		//topBar.add(screenGroup.menuScreen.volumeButton);
		addAncestorListener(new AncestorAdapter() {
			public void ancestorAdded(AncestorEvent e) {
				updateScreen();
			}
		});
	}
	
	public void updateScreen() {
		playerScoreLabel.setText("Score: " + Conquest.player.score);
	}
	
	class ShopScrollPane extends JScrollPane {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		int mouseX;
		AutoScroller autoScroller;
		
		ShopScrollPane() {
			mouseX = 0;
			autoScroller = new AutoScroller(this);
		}
	}
	
	class ShopWindow extends JPanel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		StarAnimator starAnimator;
		ShopWindow() {
			starAnimator = new StarAnimator(this);
			starAnimator.configure(Color.YELLOW, 25, 50);
		}
		
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2D = (Graphics2D) g;
			if (starAnimator.isRunning()) {
				starAnimator.animate(g2D);
			}
		}
	}
	
	class ShopItem extends JPanel implements ActionListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		int cost;
		GUILabel titleLabel;
		JTextField description;
		JScrollPane descBox;
		JLabel imageDisplay;
		BufferedImage image;
		GUILabel type;
		GUILabel currentVersion;
		GUIButton buyButton;
		
		ShopItem(String title, int cost, BufferedImage image) {
			this.cost = cost;
			setLayout(new GridBagLayout());
			GridBagConstraints windowConstraints = new GridBagConstraints();
			setBackground(Color.BLACK);
			setBorder(BorderFactory.createLineBorder(Color.WHITE));
			setSize((int) (screenGroup.mainPanel.getWidth() / 1.5), (int) (screenGroup.mainPanel.getHeight() / 1.5));
			setPreferredSize(getSize());
			
			//11 by 11
			windowConstraints.fill = GridBagConstraints.BOTH;
			windowConstraints.weightx = 1;
			windowConstraints.weighty = 1;
			windowConstraints.insets = new Insets(2, 2, 2, 2);
			windowConstraints.gridx = 2;
			windowConstraints.gridy = 2;
			windowConstraints.gridwidth = 11;
			
			titleLabel = new GUILabel(title);
			add(titleLabel, windowConstraints);
			
			windowConstraints.gridx = 2;
			windowConstraints.gridy = 4;
			windowConstraints.gridwidth = 6;
			windowConstraints.gridheight = 6;
			imageDisplay = new ImageLabel(image);
//			imageDisplay.setBackground(Color.BLUE);
//			if (image != null) {
//				ImageIcon icon = new ImageIcon();
//				icon.setImage(image);
//				imageDisplay.setIcon(icon);
//			}
			
			add(imageDisplay, windowConstraints);
			
			windowConstraints.gridx = 9;
			windowConstraints.gridy = 4;
			windowConstraints.gridwidth = 3;
			windowConstraints.gridheight = 1;
			type = new GUILabel();
			add(type, windowConstraints);
			
			windowConstraints.gridy = 5;
			currentVersion = new GUILabel();
			add(currentVersion, windowConstraints);
			
			windowConstraints.gridy = 6;
			JPanel filler = new JPanel();
			filler.setOpaque(false);
			add(filler, windowConstraints);
			
			windowConstraints.gridy = 7;
			windowConstraints.gridwidth = 3;
			windowConstraints.gridheight = 3;
			description = new JTextField();
			description.setBackground(Color.ORANGE);
			description.setEditable(false);
			descBox = new JScrollPane();
			descBox.setViewportView(description);
			add(descBox, windowConstraints);
			
			windowConstraints.gridx = 2;
			windowConstraints.gridy = 11;
			windowConstraints.gridwidth = 11;
			windowConstraints.gridheight = 1;
			
			buyButton = new GUIButton("B U Y");
			buyButton.addActionListener(this);
			add(buyButton, windowConstraints);
			revalidate();
		}
		
		public void purchase() {
			if (Conquest.player.score < cost) {
				System.out.println("Could not purchase");
				return;
			}
			
			Conquest.player.score -= cost;
			System.out.println(-cost);
		}
		
		public void setDescription(String text) {
			description.setText(text);
		}
		
		public void setImage(BufferedImage image) {
			this.image = image;
			Graphics2D g2D = (Graphics2D) imageDisplay.getGraphics();
			g2D.drawImage(image, 0, 0, imageDisplay.getWidth(), imageDisplay.getHeight(), null);
		}
		

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(buyButton)) {
				purchase();
				ShopScreen.this.requestFocus();
				ShopScreen.this.updateScreen();
			}
		}
		
	}
	
	
}
