package graphics;

import game.Game;
import game.Mob;
import java.awt.image.BufferedImage;

public class AnimatedSprite extends Sprite implements Mob
{
	// Class for sprite mob which uses an animated spritesheet
	private Sprite[] sprites; // Array of various sprites to be counted through, each is an animation frame
	private int currentSprite = 0; // Start at sprite 0
	private int speed;
	private int counter = 0;

	// Variables for beginning and end animation frames
	private int startSprite = 0;
	private int endSprite;

	// Constructor that takes spritesheet and speed of animations
	public AnimatedSprite(SpriteSheet sheet, int speed) 
	{
		sprites = sheet.getLoadedSprites( ); // Initialize loaded sprites to be counted through
		this.speed = speed;
		this.endSprite = sprites.length - 1;
	}
	
	
	// Return array of pixels to be displayed of sprite
	public int[] getPixels( )
	{
		return sprites[currentSprite].getPixels( );
	}
	
	// Return width of sprite
	public int getWidth( )
	{
		return sprites[currentSprite].getWidth( );
	}

	// Return height of sprite
	public int getHeight( )
	{
		return sprites[currentSprite].getHeight( );
	}

	// Necessary for implementation of Mob
	public void render(Renderer renderer, int xZoom, int yZoom) { }
	
	// Needed for implementation of Mob
	public int getLayer( ) {
		return -1;
	}

	// Update sprite at 60 FPS, factor speed
	public void update(Game game)
	{
		counter++;
		if(counter >= speed) 
		{
			counter = 0;
			changeSprite();
		}
	}
	
	// Change sprite to next animation frame when called in update
	public void changeSprite( ) 
	{
		currentSprite++;
		if(currentSprite >= endSprite)
			currentSprite = startSprite;
	}

}