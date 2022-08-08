package game;

import graphics.AnimatedSprite;
import graphics.Renderer;
import graphics.Rectangle;
import graphics.Sprite;
import game.Game;

public class Health implements Mob
{
	private Rectangle playerRectangle;
	private Player player;
	private Rectangle collisionCheckRectangle;
	private AnimatedSprite animatedSprite = null;
	private Sprite sprite;
	private int speed = 6;
	private int jumpSpeed = 12; // How many pixels per frame the sleigh moves while jumping
	private boolean moving = true;
	private boolean jumping = false;
	private int timeToJump = 75; // The time it takes to complete a jump in frames
	private int timeLeftToJump;
	private int layer = 1;
	private int xOffset = 0;
	private int yOffset = 0;
	
	public Health(Sprite sprite, int xZoom, int yZoom, int xOffset, int yOffset)
	{
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.sprite = sprite;
		this.timeLeftToJump = timeToJump;
		if(sprite != null && sprite instanceof AnimatedSprite)
		{
			animatedSprite = (AnimatedSprite) sprite;
		}
		playerRectangle = new Rectangle(0, 0, 80, 44);
		playerRectangle.generateGraphics(3, 0xFF00FF90);
		collisionCheckRectangle = new Rectangle(0, 0, 80*2, 44*2);
	}
	
	public int getLayer() {
		return layer;
	}
	
	// Called as fast as computer can handle
		public void render(Renderer renderer, int xZoom, int yZoom)
		{
			if(animatedSprite != null)
				renderer.renderSprite(animatedSprite, playerRectangle.x - 460 + xOffset, playerRectangle.y + 295 + yOffset, 3, 3, false);
			else if (sprite != null)
				renderer.renderSprite(sprite, playerRectangle.x, playerRectangle.y, xZoom, yZoom, false);
			else
				renderer.renderRectangle(playerRectangle, xZoom, yZoom, false);
		}
		
		public void setOffset(int x, int y)
		{
			this.xOffset = x;
			this.yOffset = y;
		}
		
		public void updateCamera(Rectangle camera)
		{
			camera.x = playerRectangle.x - (camera.w / 2);
			camera.y = playerRectangle.y - (camera.h / 2);
		}
		
		// Called at 60 fps
		public void update(Game game)
		{
			KeyboardListener keyListener = game.getKeyListener( );
			
			boolean didMove = false;
			collisionCheckRectangle.x = playerRectangle.x;
			collisionCheckRectangle.y = playerRectangle.y;
			
			if(keyListener.up( ))
			{
				collisionCheckRectangle.y -= speed;
				didMove = true;
			}
			if(keyListener.down( ))
			{
				collisionCheckRectangle.y += speed;
				didMove = true;
			}
			
			if(moving && game.State == Game.STATE.GAME)
			{
				collisionCheckRectangle.x += speed;
				didMove = true;
			}
			
			if(keyListener.jump( ) && !jumping) {
				// Space bar is pressed once, then it can't be pressed again while the sleigh jumps
				jumping = true;
				didMove = true;
			}
			
			if(jumping) {
				
				if(timeLeftToJump > 0) {
					if(timeToJump / timeLeftToJump >= 2) {
						collisionCheckRectangle.y += jumpSpeed;
					}else {
						collisionCheckRectangle.y -= jumpSpeed;
					}
					timeLeftToJump--;
				}else {
					collisionCheckRectangle.y += jumpSpeed;
					jumping = false;
					timeLeftToJump = timeToJump;
				}
				
			}
			
			playerRectangle.x = collisionCheckRectangle.x;
			if(collisionCheckRectangle.y < 91)
				playerRectangle.y = collisionCheckRectangle.y;
			
			// Checks to see if player collided with a good tile or a bad tile
			
			// If player collided with a bad tile
			//
			
			
			
			animatedSprite.update(game);
			//updateCamera(game.getRenderer( ).getCamera( ));
		}
		
		//Call whenever mouse is clicked on Canvas.
		public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom) { return false; }
}