package graphics;

import java.awt.image.BufferedImage;

public class SpriteSheet 
{
	// Class used to organize sheet of assorted Sprites
	private int[] pixels; // Array of pixels in sheet
	private BufferedImage image; // Image reference
	public final int SIZEX; // Width of image
	public final int SIZEY; // Height of image
	private Sprite[] loadedSprites = null;
	private boolean spritesLoaded = false;

	private int spriteSizeX;

	public SpriteSheet(BufferedImage sheetImage) 
	{
		image = sheetImage;
		SIZEX = sheetImage.getWidth();
		SIZEY = sheetImage.getHeight();

		// Initialize array of pixels to be displayed from size of image specified
		pixels = new int[SIZEX*SIZEY];
		pixels = sheetImage.getRGB(0, 0, SIZEX, SIZEY, pixels, 0, SIZEX);
	}
	
	// Return array of pixels from sheet
	public int[] getPixels( )
	{
		return pixels;
	}

	// Return Sprite from spritesheet
	public Sprite getSprite(int x, int y)
	{
		// Ensure Sprites have been loaded and that specified Sprite is in range of the sheet dimensions
		if(spritesLoaded)
		{
			int spriteID = x + y * (SIZEX / spriteSizeX);

			if(spriteID < loadedSprites.length) 
				return loadedSprites[spriteID];
			else
				System.out.println("SpriteID of " + spriteID + " is out of the range with a length of " + loadedSprites.length + ".");
		}
		else
			System.out.println("SpriteSheet could not get a sprite with no loaded sprites.");

		return null;
	}
	
	// Load individual sprites of spritesheet
		public void loadSprites(int spriteSizeX, int spriteSizeY)
		{
			this.spriteSizeX = spriteSizeX;
			loadedSprites = new Sprite[(SIZEX / spriteSizeX) * (SIZEY / spriteSizeY)];

			// Run through each sprite and add to sheet
			int spriteID = 0;
			for(int y = 0; y < SIZEY; y += spriteSizeY)
			{
				for(int x = 0; x < SIZEX; x += spriteSizeX)
				{
					loadedSprites[spriteID] = new Sprite(this, x, y, spriteSizeX, spriteSizeY);
					spriteID++;
				}
			}

			// Lets user know Sprites have been loaded from sheet
			spritesLoaded = true;
		}

	// Return array of Sprites from sheet
	public Sprite[] getLoadedSprites( )
	{
		return loadedSprites;
	}

	// Return image for sheet
	public BufferedImage getImage( )
	{
		return image;
	}

}