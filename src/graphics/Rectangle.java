package graphics;

import game.Game;

public class Rectangle 
{
	// Rectangle class for collisions, testing
	public int x,y,w,h;
	private int[] pixels;

	// Set position and size of rectangle
	public Rectangle(int x, int y, int w, int h) 
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	Rectangle( ) 
	{
		this(0,0,0,0);
	}
	
	// Get array of pixels to be drawn, must be generated
	public int[] getPixels() 
	{
		if(pixels != null)
			return pixels;
		else
			System.out.println("ERROR! Rectangle class...");

		return null;
	}
	
	// Check to see if two collision rectangles intersect, vital to collisions
		public boolean intersects(Rectangle otherRectangle)
		{
			if(x > otherRectangle.x + otherRectangle.w || otherRectangle.x > x + w)
				return false;

			if(y > otherRectangle.y + otherRectangle.h || otherRectangle.y > y + h)
				return false;

			return true;
		}

	// Generate visible rectangle
	public void generateGraphics(int borderWidth, int color) {
		pixels = new int[w*h];
		
		for(int i = 0; i < pixels.length; i++)
			pixels[i] = Game.alpha;

		for(int y = 0; y < borderWidth; y++)
			for(int x = 0; x < w; x++)
				pixels[x + y * w] = color;

		for(int y = 0; y < h; y++)
			for(int x = 0; x < borderWidth; x++)
				pixels[x + y * w] = color;

		for(int y = 0; y < h; y++)
			for(int x = w - borderWidth; x < w; x++)
				pixels[x + y * w] = color;

		for(int y = h - borderWidth; y < h; y++)
			for(int x = 0; x < w; x++)
				pixels[x + y * w] = color;
		
	}
	
	// Print rectangle info
	public String toString()
	{
		return "[" + x + ", " + y + ", " + w + ", " + h + "]";
	}
	
	// Generate rectangle graphics, unused currently, can be used for collisions
		public void generateGraphics(int color) 
		{
			pixels = new int[w*h];
			for(int y = 0; y < h; y++)
				for(int x = 0; x < w; x++)
					pixels[x + y * w] = color;
		}
}