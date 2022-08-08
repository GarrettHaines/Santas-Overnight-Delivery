package game;

import graphics.AnimatedSprite;
import graphics.Renderer;
import graphics.Rectangle;
import graphics.Sprite;
import game.Game;

public class Player implements Mob
{
	private Rectangle playerRectangle;
	public static Rectangle collisionCheckRectangle;
	private AnimatedSprite animatedSprite = null;
	private Sprite sprite;
	private int speed = 6;
	private int jumpSpeed = 12; // How many pixels per frame the sleigh moves while jumping
	private boolean moving = true;
	private boolean jumping = false;
	private int timeToJump = 75; // The time it takes to complete a jump in frames
	private int timeLeftToJump;
	public int score = 0;
	public int health = 3;
	private int layer = 0;
	
	public Player(Sprite sprite, int xZoom, int yZoom)
	{
		
		this.sprite = sprite;
		this.timeLeftToJump = timeToJump;
		if(sprite != null && sprite instanceof AnimatedSprite)
		{
			animatedSprite = (AnimatedSprite) sprite;
		}
		playerRectangle = new Rectangle(0, 0, 80, 44);
		playerRectangle.generateGraphics(3, 0xFF00FF90);
		collisionCheckRectangle = new Rectangle(0, 0, 80*2, 44*2+10);
	}
	
	public int getLayer( ) {
		return layer;
	}
	
	// Called as fast as computer can handle
		public void render(Renderer renderer, int xZoom, int yZoom)
		{
			if(animatedSprite != null)
				renderer.renderSprite(animatedSprite, playerRectangle.x - 80, playerRectangle.y - 50, 2, 2, false);
			else if (sprite != null)
				renderer.renderSprite(sprite, playerRectangle.x, playerRectangle.y, xZoom, yZoom, false);
			else
				renderer.renderRectangle(playerRectangle, xZoom, yZoom, false);
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
				game.loadTilesFront( );
				game.updateScore(false);
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
			if(!game.getMap().checkCollision(collisionCheckRectangle, layer, game.getXZoom(), game.getYZoom())
					&& !game.getMap().checkCollision(collisionCheckRectangle, layer + 1, game.getXZoom(), game.getYZoom()))
			{
				game.updateScore(false);
				game.updateHealth(false);
			}
			
			// Checks to see if player collided with a good tile or a bad tile
			
			// If player collided with a bad tile
			else if(game.getMap().getTileType() == -1) {
				//System.out.println("Bad Collision occurred");
				game.updateHealth(true);

			// If player collided with a good tile
			}else if(game.getMap().getTileType() == 1) {
				game.updateScore(true);
				//System.out.println("GOOD");
			}
			
			
			
			animatedSprite.update(game);
			updateCamera(game.getRenderer( ).getCamera( ));
		}
}