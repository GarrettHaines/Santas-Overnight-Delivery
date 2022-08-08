package game;

import game.Game;
import game.Pregame;
import game.Scores;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;

public class KeyboardListener implements KeyListener, FocusListener
{
	// Array of type boolean, within range of keys, true if key is being pressed, false if not
	public boolean[] keys = new boolean[120];
	private Game game;
	private Pregame pregame;
	private Scores scores;
	
	// Constructor for Game
	public KeyboardListener(Game game)
	{
		this.game = game;
	}
	
	// Method called when key is pressed, takes key event
	public void keyPressed(KeyEvent event)
	{
		// Keyboard input for Pregame screen
		if(game.State == Game.STATE.PREGAME && event.getKeyCode() > 47 && event.getKeyCode() < 91 && pregame.name.length() < 18)
		{
			int key = event.getKeyCode();
			char c = (char) key;
			pregame.name.append(c);
		}
		// Backspace for Pregame
		if(game.State == Game.STATE.PREGAME && event.getKeyCode() == 8 && pregame.name.length() > 0)
		{
			pregame.name.deleteCharAt(pregame.name.length()-1);
		}
		
		// Keyboard input for Scores screen
		if(game.State == Game.STATE.SCORES && event.getKeyCode() > 47 && event.getKeyCode() < 91 && scores.name.length() < 18)
		{
			int key = event.getKeyCode();
			char c = (char) key;
			scores.name.append(c);
		}
		// Backspace for Scores
		if(game.State == Game.STATE.SCORES && event.getKeyCode() == 8 && scores.name.length() > 0)
		{
			scores.name.deleteCharAt(scores.name.length()-1);
		}
		
		// Get key pressed
		int keyCode = event.getKeyCode();
		
		// If a key within the 120 keys is pressed, set that key's value to true in array
		if(keyCode < keys.length)
		{
			keys[keyCode] = true;
		}
	}
	
	// Method called when key is released
	public void keyReleased(KeyEvent event)
	{
		// Get key released
		int keyCode = event.getKeyCode();
		
		// If a key within the 120 keys is released, set that key's value to false in array
		if(keyCode < keys.length)
		{
			keys[keyCode] = false;
		}
		
	}
	
	// Method called when key focus changes
	public void focusLost(FocusEvent event)
	{
		// If a key within the 120 keys loses keyboard focus, set that key's value to false in array
		for(int i = 0; i < keys.length; i++)
			keys[i] = false;
	}
	
	
	// Booleans to check if player should be moving up, down, left, or right
	
	public boolean up()
	{
		return keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP];
	}
	
	public boolean down()
	{
		return keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN];
	}
	
	public boolean jump() {
		return keys[KeyEvent.VK_SPACE];
	}
	
	public void keyTyped(KeyEvent event){}
	
	public void focusGained(FocusEvent event) {}
	
	
}