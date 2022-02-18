package conquest;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import conquest.GUIResources.GUIButton;
import conquest.screenPackage.Screens;

public class PauseGUI extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Conquest game;
	
	JLabel pauseLabel;
	JPanel topJPanel;
	JPanel centerJPanel;
	
	GUIButton saveButton;
	GUIButton backButton;
	GUIButton optionsButton;
	GUIButton inventoryButton;
	GUIButton loadButton;
	GUIButton gameButton;
	Border defaultBorder;
	
	
	public PauseGUI(Conquest game) {
		this.game = game;
		defaultBorder = BorderFactory.createCompoundBorder
				(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.BLACK, Color.lightGray), BorderFactory.createLineBorder(Color.WHITE));
		//defaultBorder = BorderFactory.createEmptyBorder();
		setBackground(Color.BLACK);
		//setBackground(new Color(0, 0, 0, 200));
		setLayout(new BorderLayout());
		
		topJPanel = new JPanel();
		topJPanel.setOpaque(false);
		topJPanel.setSize(game.getWidth(), game.getHeight() / 2);
		topJPanel.setPreferredSize(topJPanel.getSize());
		topJPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		centerJPanel = new JPanel();
		centerJPanel.setOpaque(false);
		centerJPanel.setSize(game.getWidth(), game.getHeight() / 2);
		centerJPanel.setLayout(new GridBagLayout());
		add(topJPanel, BorderLayout.NORTH);
		add(centerJPanel, BorderLayout.CENTER);
		
		GridBagConstraints centerConstraints = new GridBagConstraints();
		
		
		pauseLabel = new JLabel();
		pauseLabel.setText("P A U S E");
		pauseLabel.setBorder(BorderFactory.createEmptyBorder());
		//pauseLabel.setBackground(Color.BLACK);
		pauseLabel.setForeground(Color.WHITE);
		//System.out.println("CenterJPanel width: " + centerJPanel.getWidth() + " CenterJPanel height: " + centerJPanel.getHeight());
		pauseLabel.setSize(centerJPanel.getWidth() / 2, centerJPanel.getHeight());
		pauseLabel.setFont(new Font("Times New Roman", Font.ITALIC, 30));
		pauseLabel.setPreferredSize(pauseLabel.getSize());
		pauseLabel.setHorizontalAlignment(JLabel.CENTER);
		topJPanel.add(pauseLabel);
		
		
		
		centerConstraints.gridx = 0;
		centerConstraints.gridy = 0;
		centerConstraints.fill = GridBagConstraints.BOTH;
		centerConstraints.insets = new Insets(10, 10, 10, 10);
//		centerJPanel.add(Box.createRigidArea(new Dimension(0, centerJPanel.getHeight() / 10)));
		saveButton = new GUIButton("S A V E");
		saveButton.addActionListener( l -> {
			game.saveGame();
			game.requestFocus();
		} );
		centerJPanel.add(saveButton, centerConstraints);
//		centerJPanel.add(Box.createVerticalGlue());
		
		centerConstraints.gridx = 1;
		centerConstraints.gridy = 0;
		optionsButton = new GUIButton("O P T I O N S");
		optionsButton.addActionListener(l -> {
			game.screenGroup.switchScreen(Screens.OPTIONSSCREEN);
		});
		centerJPanel.add(optionsButton, centerConstraints);
//		centerJPanel.add(Box.createVerticalGlue());
		
		centerConstraints.gridx = 0;
		centerConstraints.gridy = 1;
		inventoryButton = new GUIButton("I N V E N T O R Y");
		inventoryButton.addActionListener(l -> game.screenGroup.switchScreen(Screens.INVENTORYSCREEN));
		centerJPanel.add(inventoryButton, centerConstraints);
//		centerJPanel.add(Box.createVerticalGlue());
		
		centerConstraints.gridx = 1;
		centerConstraints.gridy = 1;
		loadButton = new GUIButton("L O A D");
		loadButton.addActionListener( l -> {
			game.loadGame();
			game.requestFocus();
		} );
		centerJPanel.add(loadButton, centerConstraints);
		
		
		
		
		centerConstraints.gridx = 0;
		centerConstraints.gridy = 2;
		centerConstraints.gridwidth = 2;
		backButton = new GUIButton("Back to Title Screen");
		backButton.addActionListener(l -> {
			game.screenGroup.switchScreen(Screens.MENUSCREEN);
			PauseGUI.this.hide();
		});
		centerJPanel.add(backButton, centerConstraints);
		
		centerConstraints.gridx = 0;
		centerConstraints.gridy = 3;
		centerConstraints.gridwidth = 2;
		gameButton = new GUIButton("Back to Conquest");
		gameButton.addActionListener(l -> {
			game.resume();
			game.requestFocus();
		});
		centerJPanel.add(gameButton, centerConstraints);
	}
	
	
	public void show() {
		//ystem.out.println("showing gui");
		game.add(this, BorderLayout.CENTER);
		game.revalidate(); game.repaint();
	}
	
	public void hide() {
		game.remove(this);
		game.revalidate(); game.repaint();
		game.requestFocus();
	}
}


