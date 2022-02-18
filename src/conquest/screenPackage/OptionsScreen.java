package conquest.screenPackage;

import java.awt.*;

import javax.swing.*;

import conquest.GUIResources.GUIButton;

public class OptionsScreen extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Screens screenGroup;
	StarAnimator starAnimator;
	
	GUIButton saveButton;
	GUIButton loadButton;
	
	public OptionsScreen(Screens screenGroup) {
		
		this.screenGroup = screenGroup;
		addKeyListener(screenGroup.screenFunctions);
		setBackground(Color.BLACK);
		setSize(screenGroup.mainPanel.getSize());
		setPreferredSize(getSize());
		setLayout(new GridBagLayout());
		//GridBagLayout constraints = new GridBagLayout();
		
		starAnimator = new StarAnimator(this);
		starAnimator.configure(Color.GREEN, 20, 30);
		
		GUIButton backButton = new GUIButton("< Back");
		backButton.addActionListener(l -> screenGroup.goBackToLastScreen());
		add(backButton);
		
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
		//if (getParent() == null) { Main.scheduler.shutdown(); }
		
		if (starAnimator.isRunning()) {
			starAnimator.animate(g2D);
		}
	}
	
	
	
	
}
