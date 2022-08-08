package graphics;

import game.Game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;


public class Renderer 
{
	// Main class used for rendering graphics
	private BufferedImage view; // BufferedImage which represents image to be displayed
	private Rectangle camera; // Camera rectangle that follows player as player moves
	private int[] pixels; // Array of pixels to be displayed to screen
	private int maxScreenWidth, maxScreenHeight;

	// Initialize render handler with size of screen
	public Renderer(int width, int height) 
	{
		GraphicsDevice[] graphicsDevices = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();

		for(int i = 0; i < graphicsDevices.length; i++) {
			if(maxScreenWidth < graphicsDevices[i].getDisplayMode().getWidth())
				maxScreenWidth = graphicsDevices[i].getDisplayMode().getWidth();

			if(maxScreenHeight < graphicsDevices[i].getDisplayMode().getHeight())
				maxScreenHeight = graphicsDevices[i].getDisplayMode().getHeight();
		}

		// Create BufferedImage that fits screen
		view = new BufferedImage(maxScreenWidth, maxScreenHeight, BufferedImage.TYPE_INT_RGB);

		camera = new Rectangle(0, 0, width, height);

		// Create array of pixels to be displayed on screen
		pixels = ((DataBufferInt) view.getRaster().getDataBuffer()).getData();

	}
	
	// Set individual pixel to specified pixel
		private void setPixel(int pixel, int x, int y, boolean fixed) 
		{
			int pixelIndex = 0;
			if(!fixed) 
			{
				if(x >= camera.x && y >= camera.y && x <= camera.x + camera.w && y <= camera.y + camera.h)
					pixelIndex = (x - camera.x) + (y - camera.y) * view.getWidth();
			}
			else
			{
				if(x >= 0 && y >= 0 && x <= camera.w && y <= camera.h)
					pixelIndex = x + y * view.getWidth();
			}

			if(pixels.length > pixelIndex && pixel != Game.alpha)
				pixels[pixelIndex] = pixel;
		}

	// Rectangle that represents screen "camera" which follows the player
	public Rectangle getCamera( ) 
	{
		return camera;
	}
		
	// Render Sprite to pixel array to be displayed on screen
	public void renderSprite(Sprite sprite, int xPosition, int yPosition, int xZoom, int yZoom, boolean fixed) {
			renderArray(sprite.getPixels(), sprite.getWidth(), sprite.getHeight(), xPosition, yPosition, xZoom, yZoom, fixed);
	}
		
	// Render BufferedImage to pixel array to be displayed on screen
	public void renderImage(BufferedImage image, int xPosition, int yPosition, int xZoom, int yZoom, boolean fixed)
	{
		int[] imagePixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		renderArray(imagePixels, image.getWidth(), image.getHeight(), xPosition, yPosition, xZoom, yZoom, fixed);
	}
	
	// Render Rectangle to pixel array to be displayed on screen
	public void renderRectangle(Rectangle rectangle, Rectangle offset, int xZoom, int yZoom, boolean fixed)
	{
		int[] rectanglePixels = rectangle.getPixels();
		if(rectanglePixels != null)
			renderArray(rectanglePixels, rectangle.w, rectangle.h, rectangle.x + offset.x, rectangle.y + offset.y, xZoom, yZoom, fixed);	
	}
	
	// Render Rectangle to pixel array to be displayed on screen
	public void renderRectangle(Rectangle rectangle, int xZoom, int yZoom, boolean fixed)
	{
		int[] rectanglePixels = rectangle.getPixels();
		if(rectanglePixels != null)
			renderArray(rectanglePixels, rectangle.w, rectangle.h, rectangle.x, rectangle.y, xZoom, yZoom, fixed);	
	}

	// Render pixel array to screen
	public void renderArray(int[] renderPixels, int renderWidth, int renderHeight, int xPosition, int yPosition, int xZoom, int yZoom, boolean fixed) 
	{
		for(int y = 0; y < renderHeight; y++)
			for(int x = 0; x < renderWidth; x++)
				for(int yZoomPosition = 0; yZoomPosition < yZoom; yZoomPosition++)
					for(int xZoomPosition = 0; xZoomPosition < xZoom; xZoomPosition++)
						setPixel(renderPixels[x + y * renderWidth], (x * xZoom) + xPosition + xZoomPosition, ((y * yZoom) + yPosition + yZoomPosition), fixed);
	}
	
	// Render array of pixels to the screen
	public void render(Graphics graphics)
	{
			graphics.drawImage(view.getSubimage(0, 0, camera.w, camera.h), 0, 0, camera.w, camera.h, null);
	}
		
	// Clear pixel array to be displayed
	public void clear( )
	{
		for(int i = 0; i < pixels.length; i++)
			pixels[i] = 0;
	}

	// Return max screen width
	public int getMaxWidth( ) {
		return maxScreenWidth;
	}

	// Return max screen height
	public int getMaxHeight( ) {
		return maxScreenHeight;
	}

}