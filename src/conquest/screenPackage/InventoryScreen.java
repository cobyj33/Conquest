package conquest.screenPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.AncestorEvent;
import conquest.Conquest;
import conquest.Main;
import conquest.Resources;
import conquest.GUIResources.GUIButton;
import conquest.GUIResources.GUITextArea;
import conquest.entityPackage.playerPackage.Inventory;
import conquest.entityPackage.playerPackage.Player;
import conquest.modifiers.Modifier;
import conquest.utilities.AncestorAdapter;
import conquest.utilities.AutoScroller;
import conquest.utilities.Functions;

public class InventoryScreen extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Inventory inventory;
	Player player;
	Screens screenGroup;
	Conquest game;
	JPanel currentScreen;
	JPanel holderPanel;
	
	JLabel titleLabel;
	JLabel scoreLabel;
	InventoryPane invPane;
	
	GUIButton closeButton;
	GUIButton toShopButton;
	
	
	StarAnimator starAnimator;
	
	
	public InventoryScreen(Conquest game, Screens screens) {
		this.game = game;
		screenGroup = screens;
		construct();
		
		addAncestorListener(new AncestorAdapter() {
			public void ancestorAdded(AncestorEvent event) {
				removeAll();
				revalidate();
				construct();
			}
		});
		
	}
	
	public void construct() {
		player = Conquest.player;
		this.inventory = player.getInventory();
		
		setSize(screenGroup.mainPanel.getSize());
		setPreferredSize(getSize());
		addKeyListener(screenGroup.screenFunctions);
		setLayout(new GridBagLayout());
		setBackground(Color.BLACK);
		currentScreen = screenGroup.getCurrentScreen();
		
		
		starAnimator = new StarAnimator(this);
		starAnimator.setStarColor(Color.RED);
		
		JPanel holderPanel = new JPanel();
		holderPanel.setBackground(Color.BLUE);
		holderPanel.setSize((int) (screenGroup.mainPanel.getWidth() * 0.9), (int) (screenGroup.mainPanel.getHeight() * 0.9));
		holderPanel.setPreferredSize(holderPanel.getSize());
		holderPanel.setLayout(new GridBagLayout());
		holderPanel.setOpaque(false);
		holderPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
		GridBagConstraints inventoryConstraints = new GridBagConstraints();
		
		inventoryConstraints.weightx = 1;
		//inventoryConstraints.weighty = 1;
		titleLabel = new JLabel();
		titleLabel.setFont(Resources.getFont(Resources.FontEnum.STANDARDFONT).deriveFont(Font.PLAIN, 12));
		titleLabel.setText("INVENTORY");
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		holderPanel.add(titleLabel, inventoryConstraints);
		
		inventoryConstraints.gridx = 1;
		inventoryConstraints.gridwidth = 3;
		holderPanel.add(Functions.createEmptyPanel(), inventoryConstraints);
		
		
		inventoryConstraints.gridx = 4;
		inventoryConstraints.gridwidth = 1;
		scoreLabel = new JLabel();
		scoreLabel.setFont(Resources.getFont(Resources.FontEnum.STANDARDFONT).deriveFont(Font.BOLD, 15));
		scoreLabel.setForeground(Color.WHITE);
		scoreLabel.setText("Score: " + Conquest.player.score);
		scoreLabel.setHorizontalAlignment(JLabel.CENTER);
		holderPanel.add(scoreLabel, inventoryConstraints);
		
		inventoryConstraints.fill = GridBagConstraints.BOTH;
		inventoryConstraints.gridx = 0;
		inventoryConstraints.gridy = 1;
		inventoryConstraints.insets = new Insets(20, 20, 20, 20);
		inventoryConstraints.gridwidth = 7;
		inventoryConstraints.gridheight = 7;
		inventoryConstraints.weightx = 1;
		inventoryConstraints.weighty = 1;
		invPane = new InventoryPane(player);
		
		holderPanel.add(invPane, inventoryConstraints);
		
		inventoryConstraints.fill = GridBagConstraints.NONE;
		inventoryConstraints.gridx = 0;
		inventoryConstraints.insets = new Insets(0, 0, 0, 0);
		inventoryConstraints.gridy = 8;
		inventoryConstraints.gridwidth = 2;
		inventoryConstraints.gridheight = 1;
		inventoryConstraints.weighty = 0;
		closeButton = new GUIButton("C L O S E");
		closeButton.addActionListener(l -> screenGroup.goBackToLastScreen());
		holderPanel.add(closeButton, inventoryConstraints);
		
		inventoryConstraints.gridx = 1;
		inventoryConstraints.gridwidth = 3;
		holderPanel.add(Functions.createEmptyPanel(), inventoryConstraints);
		
		inventoryConstraints.gridx = 4;
		inventoryConstraints.gridy = 8;
		toShopButton = new GUIButton("TO SHOP");
		toShopButton.addActionListener(l -> screenGroup.switchScreen(Screens.SHOPSCREEN));
		holderPanel.add(toShopButton, inventoryConstraints);
		
		
		
		
		
		GridBagConstraints holderConstraints = new GridBagConstraints();
		holderConstraints.insets = new Insets(20, 20, 20, 20);
		holderConstraints.fill = GridBagConstraints.BOTH;
		add(holderPanel, holderConstraints);
		repaint();
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
		
		if (starAnimator.isRunning()) {
			starAnimator.animate(g2D);
		}
	}
	
}

class InventoryPane extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	InvPanel primaryInvPanel;
	InvPanel specialInvPanel;
	InvPanel equipmentInvPanel;
	InvPanel statsInvPanel;
	
	InvScrollPane primaryScrollPane;
	InvScrollPane specialScrollPane;
	InvScrollPane equipmentScrollPane;
	InvScrollPane statsScrollPane;
	
	InventoryPane(Player player) {
		setLayout(new GridLayout(4, 1, 5, 5));
		setOpaque(false);
		primaryInvPanel = new InvPanel(player, player.getInventory().getPrimaryModifiers());
		specialInvPanel = new InvPanel(player, player.getInventory().getSpecialModifiers());
		equipmentInvPanel = new InvPanel(player, player.getInventory().getEquipmentModifiers());
		statsInvPanel = new InvPanel(player, player.getInventory().getStatModifiers());
		
		primaryScrollPane = new InvScrollPane(primaryInvPanel);
		specialScrollPane = new InvScrollPane(specialInvPanel);
		equipmentScrollPane = new InvScrollPane(equipmentInvPanel);
		statsScrollPane = new InvScrollPane(statsInvPanel);
		
		add(primaryScrollPane);
		add(specialScrollPane);
		add(equipmentScrollPane);
		add(statsScrollPane);
	}
	
	public void resizePanels(int width) {
		primaryInvPanel.setPreferredSize(new Dimension(width, 0));
		specialInvPanel.setPreferredSize(new Dimension(width, 0));
		equipmentInvPanel.setPreferredSize(new Dimension(width, 0));
	}
}

class InvScrollPane extends JScrollPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	AutoScroller autoScroller;
	
	InvScrollPane(Component view) {
		super(view);
		setOpaque(false);
		autoScroller = new AutoScroller(this);
	}
}

class InvPanel extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	AutoScroller autoScroller;
	List<InventoryItem> items;
	Inventory inventory;
	Player player;
	
	InvPanel(Player player, List<Modifier> modList) {
		this.player = player;
		//setLayout(new GridLayout(1, propertyList.size(), 5, 5));
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.weightx = 1;
		constraints.weighty = 1;
		inventory = player.getInventory();
		setBackground(Color.BLACK);
		items = new ArrayList<>();
		
		
		for (int m = 0; m < modList.size(); m++) {
			Modifier mod = modList.get(m);
			InventoryItem itemDisplay = new InventoryItem(mod);
			itemDisplay.addActionListener(InvPanel.this);
			if (mod.isActive()) {
				System.out.println("Active mod detected");
				itemDisplay.setContentAreaFilled(true);
				itemDisplay.setBackground(Color.YELLOW);
			}
			constraints.gridx = m;
			InvPanel.this.add(itemDisplay, constraints);
			items.add(itemDisplay);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof InventoryItem) {
			System.out.println("switching mod activity");
			InventoryItem selectedMod = (InventoryItem) e.getSource();
			Modifier mod = selectedMod.mod;
			
			if (!mod.isActive()) {
				inventory.select(mod);
				selectedMod.setOpaque(true);
				selectedMod.setContentAreaFilled(true);
			} else {
				inventory.deselect(mod);
				selectedMod.setOpaque(false);
				selectedMod.setContentAreaFilled(false);
			}
			
			player.updateGUI();
			repaint();
		}
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		//Graphics2D g2D = (Graphics2D) g;
		int size = this.getParent().getHeight() - (this.getParent().getHeight() / 10);
		if (getSize().getWidth() != size * items.size() + 1 || getSize().getHeight() != this.getParent().getHeight()) {
			//setSize(size * items.size() + 1, this.getParent().getHeight());
		}
		//System.out.println("repainting");
		Arrays.stream(this.getComponents()).forEach(component -> {
			if (component instanceof InventoryItem) {
				if (component.getPreferredSize().getHeight() == size) { return; }
				InventoryItem item = (InventoryItem) component;
				item.setPreferredSize(new Dimension(size, size));
				revalidate();
			}
		});
	}
	
}

class InventoryItem extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	BufferedImage itemImage;
	Modifier mod;
	GUITextArea descriptionLabel;
	
	InventoryItem(Modifier mod) {
		this.mod = mod;
		setLayout(new BorderLayout());
		setBackground(Color.YELLOW);
		setOpaque(false);
		ImageIcon icon = new ImageIcon();
		if (mod.getImage() != null) {
			icon.setImage(mod.getImage());
		}
		
		setIcon(icon);
		if (mod.isActive()) {
			System.out.println("Yellow");
			setContentAreaFilled(true);
		} else {
			setContentAreaFilled(false);
		}
		
		setText(mod.getName());
		setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
		
		setFont(Resources.getFont(Resources.FontEnum.STANDARDFONT).deriveFont(Font.BOLD, 12));
		setForeground(Color.WHITE);
		setVerticalTextPosition(JLabel.BOTTOM);
		setIconTextGap(5);
		setHorizontalAlignment(JLabel.CENTER);
		setHorizontalTextPosition(JLabel.CENTER);
		
		descriptionLabel = new GUITextArea();
		descriptionLabel.setFont(getFont().deriveFont(Font.BOLD, 12));
		//descriptionLabel.setFocusable(false);
		descriptionLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
		descriptionLabel.setLineWrap(true);
		descriptionLabel.setText(mod.getDescription());
		
		addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				descriptionLabel.setBounds(
						InventoryItem.this.getLocationOnScreen().x - Main.gameHolder.getLocationOnScreen().x + getWidth(),
						InventoryItem.this.getLocationOnScreen().y - Main.gameHolder.getLocationOnScreen().y + getHeight(),
						getWidth(), getHeight());
				Main.addToLayeredPane(descriptionLabel, Integer.valueOf(10));
			}
			
			public void mouseExited(MouseEvent e) {
				Main.removeFromLayeredPane(descriptionLabel);
			}
		});
	}
	
	public void displayDescription(MouseEvent e) {
		int mouseX = e.getX();
		int mouseY = e.getY();
		descriptionLabel.setBounds(mouseX, mouseY, getWidth(), getHeight());
	}
	
	public void hideDescription() {
		Main.removeFromLayeredPane(descriptionLabel);
	}
	
	
}
