package conquest.screenPackage;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.AncestorEvent;

import conquest.GUIResources.GUIButton;
import conquest.GUIResources.GUILabel;
import conquest.GUIResources.ImageLabel;
import conquest.modifiers.Achievement;
import conquest.utilities.AncestorAdapter;
import conquest.utilities.Functions;

public class AchievementScreen extends JPanel {
	Screens screenGroup;
	JPanel achievementViewer;
	JScrollPane achieveScrollPane;
	JButton toMenuButton;
	
	AchievementScreen(Screens screenGroup) {
		this.screenGroup = screenGroup;
		setSize(Screens.mainPanel.getSize());
		setPreferredSize(getSize());
		setLayout(new BorderLayout());
		construct();
		
		addAncestorListener(new AncestorAdapter() {
			public void ancestorAdded(AncestorEvent event) {
				removeAll();
				construct();
				revalidate();
				repaint();
			}
		});
	}
	
	private void construct() {
		achievementViewer = new AchievementView();
		achieveScrollPane = new JScrollPane();
		achieveScrollPane.setViewportView(achievementViewer);
		toMenuButton = new GUIButton("< Back to Main Menu");
		toMenuButton.addActionListener(l -> screenGroup.switchScreen(Screens.MENUSCREEN));
		
		add(toMenuButton, BorderLayout.NORTH);
		add(achieveScrollPane, BorderLayout.CENTER);
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
	}
	
	class AchievementView extends JPanel {
		StarAnimator animator;
		
		//takes directly from initialized achievements;
		AchievementView() {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			List<Achievement> achievements = Achievement.getAllAchievements();
			int sidePadding = Screens.mainPanel.getWidth() * 9 / 10;
			Dimension achieveSize = new Dimension(Screens.mainPanel.getWidth() - sidePadding * 2, Screens.mainPanel.getHeight() / 6);
			setSize(Screens.mainPanel.getWidth(), achievements.size() * (int)achieveSize.getHeight() * 2);
			
			for (int a = 0; a < achievements.size(); a++) {
				Achievement current = achievements.get(a);
				AchievementItem achieveLabel = new AchievementItem(current);
				achieveLabel.setSize(achieveSize);
				achieveLabel.setPreferredSize(achieveSize);
				add(achieveLabel);
				add(Box.createRigidArea(achieveSize)); //padding
			}
			
			
			animator = new StarAnimator(this);
			animator.setStarColor(Color.RED);
		}
		
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2D = (Graphics2D) g;
			animator.animate(g2D);
		}
		
		
	}
	
	class AchievementItem extends JPanel {
		Achievement achievement;
		
		JLabel achievementImage;
		JLabel achievementName;
		JLabel achievementDesc;
		JLabel statusDisplay;
		
		
		AchievementItem(Achievement achievement) {
			this.achievement = achievement;
			setLayout(new GridBagLayout());
			GridBagConstraints constraints = Functions.getDefaultConstraints();
			
			achievementImage = new ImageLabel(achievement.getImage());
			achievementName = new GUILabel(achievement.getName());
			achievementDesc = new GUILabel(achievement.getDesc());
			createStatusDisplay();
			
			if (achievement.isCompleted()) {
				setOpaque(true);
				setBackground(Color.BLACK);
			} else {
				setOpaque(false);
			}
			
			constraints.gridheight = 2;
			constraints.gridwidth = 2;
			add(achievementImage, constraints);
			
			constraints.gridx = 2;
			constraints.gridheight = 1;
			add(achievementName, constraints);
			
			constraints.gridy = 1;
			add(achievementDesc, constraints);
			
			constraints.gridx = 4;
			constraints.gridy = 0;
			constraints.gridheight = 2;
			constraints.gridwidth = 2;
			add(statusDisplay, constraints);
			
			addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent e) {
					setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
				}
				
				public void mouseExited(MouseEvent e) {
					setBorder(BorderFactory.createEmptyBorder());
				}
			});
		}
		
		private void createStatusDisplay() {
			BufferedImage completed = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
			System.out.println(achievement);
			Graphics2D completedG = completed.createGraphics();
			completedG.setStroke(new BasicStroke(5));
			completedG.setPaint(achievement.getDifficulty().getColor());
			int marginSize = 10;
			
			if (!achievement.isCompleted()) {
				completedG.drawOval(marginSize, marginSize, completed.getWidth() - marginSize, completed.getHeight() - marginSize);
			} else {
				completedG.fillOval(marginSize, marginSize, completed.getWidth() - marginSize, completed.getHeight() - marginSize);
			}
			
			statusDisplay = new ImageLabel(completed);
		}
	}
	
	
	
}
