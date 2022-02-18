package conquest.screenPackage;

import javax.swing.*;
import javax.swing.event.AncestorEvent;

import conquest.Conquest;
import conquest.Resources;
import conquest.GUIResources.GUIButton;
import conquest.GUIResources.GUILabel;
import conquest.GUIResources.GUITextArea;
import conquest.GUIResources.ImageLabel;
import conquest.entityPackage.playerPackage.Player;
import conquest.utilities.AncestorAdapter;

import java.awt.*;

public class DeathScreen extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Screens screenGroup;
	GUIButton retryButton;
	GUIButton exitButton;
	GUILabel deathLabel;
	JTextArea statsDisplay;
	StarAnimator starAnimator;
	
	ImageLabel shipLabel;
	
	
	public DeathScreen(Screens screenGroup) {
		this.screenGroup = screenGroup;
		starAnimator = new StarAnimator(this);
		
		setLayout(new GridBagLayout());
		setSize(screenGroup.mainPanel.getSize());
		setPreferredSize(getSize());
		setBackground(Color.BLACK);
		GridBagConstraints panelConstraints = new GridBagConstraints();
		panelConstraints.fill = GridBagConstraints.BOTH;
		panelConstraints.weightx = 1;
		panelConstraints.weighty = 1;
		panelConstraints.gridwidth = 7;
		deathLabel = new GUILabel("Y O U   D I E D");
		add(deathLabel, panelConstraints);
		
		panelConstraints.gridy = 1;
		panelConstraints.gridx = 0;
		panelConstraints.gridwidth = 1;
		panelConstraints.gridheight = 3;
		statsDisplay = new GUITextArea();
		add(statsDisplay, panelConstraints);
		statsDisplay.addAncestorListener(new AncestorAdapter() {
			public void ancestorAdded(AncestorEvent event) {
				Player player = Conquest.player;
				DeathScreen.this.statsDisplay.setText("ENEMIES DESTROYED: " + player.enemiesDestroyed + "\n"
						+ "DAMAGE DEALT: " + player.damageDealt + "\n"
						+ "DAMAGE TAKEN: " + player.damageTaken + "\n"
						+ "LINES CLEARED: " + player.linesCleared + "\n"
						+ "SHOTS HIT: " + player.shotsHit + "\n"
						+ "SHOTS FIRED: " + player.shotsFired + "\n"
						+ "ACCURACY: %" + ( ((double)player.shotsHit / player.shotsFired ) * 100) + "\n"
						+ "");
			}
		});
		
		panelConstraints.gridy = 1;
		panelConstraints.gridx = 2;
		panelConstraints.gridwidth = 3;
		panelConstraints.gridheight = 3;
		shipLabel = new ImageLabel(Resources.getImage(Resources.ImageEnum.SHIP));
		add(shipLabel, panelConstraints);
		
		panelConstraints.gridx = 0;
		panelConstraints.gridwidth = 2;
		panelConstraints.gridheight = 1;
		panelConstraints.gridy = 4;
		retryButton = new GUIButton("R E T R Y");
		retryButton.addActionListener(l -> {
			screenGroup.switchScreen(Screens.GAMESCREEN);
			//game.start();
			});
		add(retryButton, panelConstraints);
		
		panelConstraints.gridx = 5;
		exitButton = new GUIButton("E X I T");
		exitButton.addActionListener(l -> screenGroup.switchScreen(Screens.MENUSCREEN));
		add(exitButton, panelConstraints);
		
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
		
		if (starAnimator.isRunning()) {
			starAnimator.animate(g2D);
		}
	}
}
