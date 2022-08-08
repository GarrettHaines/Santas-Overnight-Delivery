package graphics;

import java.awt.image.BufferedImage;

public class Sprite
{
	// Sprite class used for uploading sprite images and animated sprites
	protected int width, height; // Dimensions of sprite
	protected int[] pixels; // Array of pixels containing sprite info

	public Sprite(SpriteSheet sheet, int startX, int startY, int width, int height) 
	{
		this.width = width;
		this.height = height;

		pixels = new int[width*height];
		sheet.getImage().getRGB(startX, startY, width, height, pixels, 0, width);
	}

	public Sprite(BufferedImage image) 
	{
		width = image.getWidth();
		height = image.getHeight();

		pixels = new int[width*height];
		image.getRGB(0, 0, width, height, pixels, 0, width);
	}
	
	// Return pixel array of sprite info
	public int[] getPixels( )
	{
		return pixels;
	}

	public Sprite( ) { }


	// Return width of sprite
	public int getWidth( )
	{
		return width;
	}

	// Return height of sprite
	public int getHeight( )
	{
		return height;
	}
}