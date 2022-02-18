package conquest.utilities;

import java.awt.GridBagConstraints;

import javax.swing.JPanel;

import conquest.Conquest;

public class Functions {
	public static JPanel createEmptyPanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		return panel;
	}
	
	public static double getDistance(int x1, int y1, int x2, int y2) {
		return Math.sqrt( Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2)  );
	}
	
	//1 is start, 2 is target
	public static double getAngle(int x1, int y1, int x2, int y2) {
//		double addedAngle = Math.atan( Math.abs(x2 - x1) / Math.abs(y2 - y1) );
		double addedAngle = 0;
//		System.out.println(Math.atan2(Math.abs(x2 - x1), Math.abs(y2 - y1)));
		try {
			addedAngle = Math.atan2(Math.abs(x2 - x1), Math.abs(y2 - y1));
			System.out.println("added angle Degrees: " + addedAngle * 180 / Math.PI);
		} catch (ArithmeticException e) {
			System.out.println("Divide by zero");
		}
		double angle = 0;
		
		if (x2 > x1 && y2 < y1) { //q1
			angle = Math.PI / 2 - addedAngle;
			System.out.println("Q1");
		} else if (x2 < x1 && y2 < y1) { //q2
			angle = addedAngle + Math.PI / 2;
			System.out.println("Q2");
		} else if (x2 < x1 && y2 > y1) { //q3
			angle = Math.PI * 3 / 2 - addedAngle;
			System.out.println("Q3");
		} else if (x2 > x1 && y2 > y1) { //q4
			angle = addedAngle + Math.PI * 3 / 2;
			System.out.println("Q4");
		} else {
			angle = addedAngle;
		}
		
		
		
		System.out.println("Degrees: " + angle * 180 / Math.PI);
		
		return angle;
		
		
	}
	
	public static boolean inRange(double test, double least, double most) {
		if (test >= least && test <= most) {
			return true;
		} return false;
	}
	
	public static java.awt.GridBagConstraints getDefaultConstraints() {
		java.awt.GridBagConstraints constraints = new java.awt.GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new java.awt.Insets(1, 1, 1, 1);
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.gridx = 0;
		constraints.gridy = 0;
		return constraints;
	}
	 
	
}
