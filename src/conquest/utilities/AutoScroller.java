package conquest.utilities;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.Timer;
import javax.swing.event.AncestorEvent;

public class AutoScroller {
	JScrollPane pane;
	Timer scrollTimer;
	int mouseX;
	int mouseY;
	
	int viewX;
	int viewY;
	
	boolean verticalEnabled;
	boolean horizontalEnabled;
	
	boolean vertical;
	boolean horizontal;
	
	public AutoScroller(JScrollPane pane) {
		this.pane = pane;
		horizontal = true;
		scrollTimer = new Timer(10, l -> autoScroll());
		viewX = 0;
		viewY = 0;
		
		pane.addMouseMotionListener(new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
				
				if (horizontal) {
					if (mouseX < pane.getWidth() / 10 || mouseX > pane.getWidth() - (pane.getWidth() / 10)) {
						
						if (!scrollTimer.isRunning()) {
							scrollTimer.start();
						}
						
					} else {
						
					}
					
				}
				
				if (vertical) {
					if (mouseY < pane.getHeight() / 10 || mouseY > pane.getHeight() - (pane.getHeight() / 10)) {
						
						if (!scrollTimer.isRunning()) {
							scrollTimer.start();
						}
						
					}
				}
				
				
				if (mouseX < pane.getWidth() / 10 || mouseX > pane.getWidth() - (pane.getWidth() / 10)) {
					if (!scrollTimer.isRunning()) {
						scrollTimer.start();
					}
				} else {
					if (scrollTimer.isRunning()) {
						scrollTimer.stop();
					}
				}
			}
			
			public void mouseDragged(MouseEvent e) { mouseMoved(e); }
		});
		
		pane.addMouseListener(new MouseAdapter() {
			public void mouseExited(MouseEvent e) {
				if (scrollTimer.isRunning()) {
					scrollTimer.stop();
				}
			}
		});
		
		pane.addAncestorListener(new AncestorAdapter() {
			public void ancestorAdded(AncestorEvent e) {
				//autoScroller.start();
			}
			
			public void ancestorRemoved(AncestorEvent e) {
				scrollTimer.stop();
			}
		});
	}
	
	public void autoScroll() {
		System.out.println("scrollin");
		JViewport view = pane.getViewport();
		//System.out.println("view width: " + view.getWidth());
		Point viewPos = view.getViewPosition();
		if (mouseX < pane.getWidth() / 10) {
			if (viewPos.x - 10 < 0) { view.setViewPosition(new Point(0, viewPos.y)); return; }
			view.setViewPosition(new Point(viewPos.x - 10, viewPos.y));
		} else if (mouseX > pane.getWidth() * 9 / 10) {
			if (viewPos.x + 10 >= view.getWidth()) { System.out.println("found"); view.setViewPosition(new Point(view.getWidth(), viewPos.y)); return; }
			view.setViewPosition(new Point(viewPos.x + 10, viewPos.y));
		}
	}
}
