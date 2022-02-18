package conquest.screenPackage;

import java.awt.BorderLayout;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.AncestorEvent;

import conquest.Resources;
import conquest.GUIResources.GUIButton;
import conquest.utilities.AncestorAdapter;

public class MenuScreen extends JPanel implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel centerPanel;
	JPanel topBar;
	StarAnimator starAnimator;
	
	JLabel gameTitle;
	GUIButton playButton;
	GUIButton optionsButton;
	GUIButton shopButton;
	GUIButton aboutButton;
	GUIButton inventoryButton;
	GUIButton achieveButton;
	JButton volumeButton;
	ArrayList<GUIButton> buttons;
	Screens screenGroup;
	
	public MenuScreen(Screens screenGroup) {
		this.screenGroup = screenGroup;
		addKeyListener(screenGroup.screenFunctions);
		setPreferredSize(screenGroup.mainPanel.getSize());
		setSize(screenGroup.mainPanel.getSize());
		setBackground(Color.BLACK);
		starAnimator = new StarAnimator(this);
		
		addAncestorListener(new AncestorAdapter() {
			@Override
			public void ancestorAdded(AncestorEvent event) {
				if (event.getAncestor().equals(MenuScreen.this) || event.getAncestorParent() == null) {
					resizeImages();
				}
			}
		});
		
		setLayout(new BorderLayout());
		
		centerPanel = new JPanel();
		centerPanel.setOpaque(false);
		topBar = new JPanel();
		topBar.setOpaque(false);
		topBar.setPreferredSize(new Dimension(getWidth(), getHeight() / 6));
		//topBar.setBackground(Color.GREEN);
		topBar.setLayout(new FlowLayout(FlowLayout.LEADING));
		add(centerPanel, BorderLayout.CENTER);
		add(topBar, BorderLayout.NORTH);
		
		centerPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 6;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 1;
		c.weighty = 0;
		c.insets = new Insets(10, 10, 10, 10);
		gameTitle = new JLabel();
		gameTitle.setText("CONQUEST");
		gameTitle.setFont(Resources.getFont(Resources.FontEnum.STANDARDFONT).deriveFont(Font.BOLD, 30));
		gameTitle.setForeground(Color.WHITE);
		gameTitle.setBackground(Color.BLACK);
		gameTitle.setOpaque(true);
		gameTitle.setHorizontalAlignment(JLabel.CENTER);
		gameTitle.revalidate();
		gameTitle.repaint();
		centerPanel.add(gameTitle, c);
		
		c.weighty = 0;
		c.fill = GridBagConstraints.NONE;
		buttons = new ArrayList<>();
		playButton = new GUIButton("P L A Y"); playButton.addActionListener(this);
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 2;
		c.insets = new Insets(5, 5, 5, 5);
		centerPanel.add(playButton, c);
		
		
		optionsButton = new GUIButton("O P T I O N S"); optionsButton.addActionListener(this);
		c.gridx = 1;
		c.gridy = 5;
		c.gridwidth = 2;
		centerPanel.add(optionsButton, c);
		
		shopButton = new GUIButton("S H O P"); shopButton.addActionListener(this);
		c.gridx = 4;
		c.gridy = 3;
		c.gridwidth = 2;
		centerPanel.add(shopButton, c);
		
		aboutButton = new GUIButton("A B O U T"); aboutButton.addActionListener(this);
		c.gridx = 4;
		c.gridy = 5;
		c.gridwidth = 2;
		centerPanel.add(aboutButton, c);
		
		inventoryButton = new GUIButton("I N V E N T O R Y"); inventoryButton.addActionListener(this);
		c.gridx = 1;
		c.gridy = 7;
		c.gridwidth = 2;
		centerPanel.add(inventoryButton, c);
		
		achieveButton = new GUIButton("A C H I E V E M E N T S"); achieveButton.addActionListener(this);
		c.gridx = 4;
		c.gridy = 7;
		c.gridwidth = 2;
		centerPanel.add(achieveButton, c);
		
		volumeButton = new JButton();
		volumeButton.setBackground(Color.BLACK);
		volumeButton.setBorder(BorderFactory.createEmptyBorder());
		volumeButton.addActionListener(this);
		ImageIcon icon = new ImageIcon();
		volumeButton.setIcon(icon);
		topBar.add(volumeButton);
		
		buttons.add(playButton); buttons.add(optionsButton); buttons.add(shopButton); buttons.add(aboutButton);
		
		
		
	}
	
	public void resizeImages() {
		volumeButton.setSize(playButton.getHeight(), playButton.getHeight());
		volumeButton.setPreferredSize(volumeButton.getSize());
		ImageIcon volumeIcon = (ImageIcon) volumeButton.getIcon();
		if (Resources.isMuted()) {
			volumeIcon.setImage(Resources.getImage(Resources.ImageEnum.SPEAKER_MUTE).getScaledInstance(volumeButton.getWidth(), volumeButton.getHeight(), Image.SCALE_DEFAULT));
		} else {
			volumeIcon.setImage(Resources.getImage(Resources.ImageEnum.SPEAKER_THREEBAR).getScaledInstance(volumeButton.getWidth(), volumeButton.getHeight(), Image.SCALE_DEFAULT));
		}
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
		if (starAnimator.isRunning()) {
			starAnimator.animate(g2D);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		
		if (source.equals(playButton)) {
			System.out.println("Clicked play button!");
			screenGroup.switchScreen(Screens.GAMESCREEN);
			
		} else if (source.equals(optionsButton)) {
			System.out.println("Clicked options button!");
			screenGroup.switchScreen(Screens.OPTIONSSCREEN);
			
		} else if (source.equals(shopButton)) {
			System.out.println("Clicked shop button!");
			screenGroup.switchScreen(Screens.SHOPSCREEN);
		} else if (source.equals(aboutButton)) {
			System.out.println("Clicked about button!");
			screenGroup.switchScreen(Screens.ABOUTSCREEN);
		} else if (source.equals(volumeButton)) {
			//System.out.println("clicked mute button");
			ImageIcon icon = (ImageIcon) volumeButton.getIcon();
			Resources.mute();
			if (Resources.isMuted()) {
				icon.setImage(Resources.getImage(Resources.ImageEnum.SPEAKER_MUTE).getScaledInstance(volumeButton.getWidth(), volumeButton.getHeight(), Image.SCALE_DEFAULT));
			} else {
				icon.setImage(Resources.getImage(Resources.ImageEnum.SPEAKER_THREEBAR).getScaledInstance(volumeButton.getWidth(), volumeButton.getHeight(), Image.SCALE_DEFAULT));
			}
		} else if (source.equals(inventoryButton)) {
			screenGroup.switchScreen(Screens.INVENTORYSCREEN);
		} else if (source.equals(achieveButton)) {
			screenGroup.switchScreen(Screens.ACHIEVEMENTSCREEN);
		}
		
	}
	
}
