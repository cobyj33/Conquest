package conquest.screenPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import conquest.GUIResources.GUIButton;

public class AboutScreen extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Screens screenGroup;
	JTextArea text;
	GUIButton backButton;
	StarAnimator starAnimator;
	
	public AboutScreen(Screens screenGroup) {
		this.screenGroup = screenGroup;
		setLayout(new BorderLayout());
		setSize(screenGroup.mainPanel.getSize());
		addKeyListener(screenGroup.screenFunctions);
		setBackground(Color.BLACK);
		setPreferredSize(screenGroup.mainPanel.getSize());
		
		starAnimator = new StarAnimator(this);
		starAnimator.configure(Color.BLUE, 20, 30);
		
		text = new JTextArea();
		text.setEditable(false);
		text.setLineWrap(true);
		text.setText("At first, after learning Java Swing which was my first real \n experience with GUI Programming. "
				+ "I had realized that with all the timers, inputs, and tools, that I'd finally reached the point \n where "
				+ "I could really start creating my own ideas in code. \n I began by recreating a few games that I already knew so "
				+ "that I could really get the jist of developing games without any sort of engine helping along, \n those games being Snake, Tetris, Chess, "
				+ "and Connect 4. \n This was supposed to simply be Space Invaders, which should be evident by how similar the games are, but I figured "
				+ "that instead of making a one and done program, \n I should finally take one program all the way, with save states, progress, a full working "
				+ "GUI, and more complex logic than I'm used to. \n I hope that you enjoy, that is if I ever even share this project with others in the Future "
				+ "\n\n"
				+ "Jacoby Johnson -> December 22, 2021");
		text.setFont(new Font("Times New Roman", Font.BOLD, 16));
		text.setOpaque(false);
		//text.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
		text.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE, 6), BorderFactory.createEmptyBorder(40, 40, 40, 40)));
		text.setForeground(Color.WHITE);
		add(text, BorderLayout.CENTER);
		//BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE, 6), BorderFactory.createEmptyBorder(40, 40, 40, 40));
		
		backButton = new GUIButton("Back to Main Menu");
//		backButton.setContentAreaFilled(false);
		backButton.setText("Back to Main Menu");
//		backButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
//		backButton.setHorizontalAlignment(JButton.CENTER);
		backButton.addActionListener(l -> screenGroup.switchScreen(Screens.MENUSCREEN));
//		backButton.setForeground(Color.WHITE);
		backButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(backButton, BorderLayout.NORTH);
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
