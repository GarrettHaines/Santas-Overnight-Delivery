package game;

import java.awt.event.MouseListener;
import game.Game;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MouseEventListener implements MouseListener, MouseMotionListener
{
	private Game game;
	
	public MouseEventListener(Game game)
	{
		this.game = game;
	}
	
	// Method called when mouse event occurs
	public void mousePressed(MouseEvent event)
	{
		// Call leftClick( ) in Game when left mouse button is clicked
		if(event.getButton() == MouseEvent.BUTTON1)
			game.leftClick(event.getX( ), event.getY( ));
	}
	
	public void mouseClicked(MouseEvent event) { }
	
	public void mouseDragged(MouseEvent event) { }
	
	public void mouseEntered(MouseEvent event) { }
	
	public void mouseExited(MouseEvent event) { }
	
	public void mouseMoved(MouseEvent event) { }
	
	public void mouseReleased(MouseEvent event) { }
}
