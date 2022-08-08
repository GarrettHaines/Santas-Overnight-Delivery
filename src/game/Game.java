package game;

/*
 * Santa's Programming Department
 * Austin Elliot
 * Garrett Haines
 * Matthew Westemeier
 * 
 * SANTA'S OVERNIGHT DELIVERY
 * 
 */

import data.User;
import data.UserBank;
import graphics.AnimatedSprite;
import graphics.Map;
import graphics.Rectangle;
import graphics.Renderer;
import graphics.Sprite;
import graphics.SpriteSheet;
import graphics.Tiles;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.Runnable;
import java.lang.Thread;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Game extends JFrame implements Runnable
{	
	public static int alpha = 0xFFFF00DC; // Variable hex for transparent pixels
	private int xZoom = 4; // Width pixel zoom factor
	private int yZoom = 4; // Height pixel zoom factor
	public static int nameLength = 0; // Initialize username length to 0
	private int numberObjects = 0; // Initialize number of objects rendered to 0
	private int selectedLayer = 0; // Set selected layer for setTile to 0
	private int selectedTileID = 2; // Set selected Tile ID to 2
	private int timeSinceHealthUpdate = 100; // Cooldown period for health to update
	private int timeSinceScoreUpdate = 100; // Cooldown period for score to update
	private int towerCounter = 0;
	private int towerSpacing = 100; // How many frames in between each tower being rendered
	public boolean colliding = false;
	
	private Canvas canvas = new Canvas(); // Canvas for drawing pixels while taking user input

	// Initialize class objects
	private Mob[] objects;
	private Mob[] objectsEmpty;
	private Health health1;
	private Health health2;
	private Health health3;
	private KeyboardListener keyListener = new KeyboardListener(this); // For keyboard input
	private Map map;
	private Menu menu;
	private ScoresLB scoresLB;
	private MouseEventListener mouseListener = new MouseEventListener(this); // For mouse input
	private Player player1;
	private Pregame pregame;
	private Rectangle testRectangle = new Rectangle(30, 30, 100, 100); // Initializes ......
	private Renderer renderer;
	private Respawn respawn;
	private Scores scores;
	private SpriteSheet sheet;
	private SpriteSheet healthSheet;
	private SpriteSheet playerSheet;
	private Tiles tiles;
	private Tiles hearts;
	public UserBank userBank = new UserBank(100);
	public static User[] printBoard;
	
	// Create enumeration for setting state of game
	public enum STATE
	{
		MENU,
		PREGAME,
		SCORES,
		LEADERBOARD,
		GAME,
		RESPAWN
	}
	
	// Initialize state of game to the menu
	public STATE State = STATE.MENU;
	
	public Game( ) 
	{
		// Forces program to terminate once the frame is closed/exited
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Initialize game frame with position and size of frame
		setBounds(0,0, 1000, 800);

		// Sets frame to center of screen
		setLocationRelativeTo(null);

		// Create canvas for screen to draw pixels, supports user input in same window
		add(canvas);

		// Allows frame to be visible
		setVisible(true);
		
		// Disables frame resizing
		setResizable(false);

		// Initializes mechanism to organize data to be displayed and user input data on Canvas
		// Initialize 3 buffers
		canvas.createBufferStrategy(3);
		
		// Initialize menu
		menu = new Menu( );
		scoresLB = new ScoresLB( );
		pregame = new Pregame( );
		scores = new Scores( );
		respawn = new Respawn( );

		// Initialize RenderHandler class with width and height of frame
		renderer = new Renderer(getWidth( ), getHeight( ));

		// Initializes a BufferedImage, image with a buffer that stores the image data from file
		BufferedImage backgroundImage = loadImage("Tilesss.png"); // Loads "Tiles.png" for background image
		BufferedImage playerSheetImage = loadImage("Player.png"); // Loads "Player.png" for player graphics
		BufferedImage healthImage3 = loadImage("Heartsss.png"); // Loads "Player.png" for player graphics
		
		// Initialize background graphics SpriteSheet
		sheet = new SpriteSheet(backgroundImage); // Initializes SpriteSheet with sheetImage image
		sheet.loadSprites(16, 16); // Loads sprite image from "Tiles1.png" with 16x16 pixel sprites
		
		// Initialize player graphics playerSheetImage
		playerSheet = new SpriteSheet(playerSheetImage); // Initializes SpriteSheet with playerSheetImage image
		playerSheet.loadSprites(80, 44); // Load sprite image from "Player.png" with 20x26 pixel sprites
		healthSheet = new SpriteSheet(healthImage3);
		healthSheet.loadSprites(13, 12);
		
		// Load Tiles from "Tiles.txt" with "sheet" SpriteSheet
		tiles = new Tiles(new File("Tiles.txt"), sheet);
		
		// Load Map from "Map.txt" file and tiles object
		map = new Map(new File("Map.txt"), tiles);

		// Initialize object playerAnimations of AnimatedSprite with playerSheet
		AnimatedSprite playerAnimations = new AnimatedSprite(playerSheet, 8); // 8 represents FPS update of player
		AnimatedSprite healthAnimations = new AnimatedSprite(healthSheet, 8);
	
		// Load in objects
		numberObjects = 4;
		objects = new Mob[numberObjects]; // Loads in 1 objects below
		objectsEmpty = new Mob[0]; // Loads in 1 objects below
		
		// Object 0, player1
		player1 = new Player(playerAnimations, xZoom, yZoom); // Initialize Player
		objects[0] = player1; // Set first object in objects array to player1
		health1 = new Health(healthAnimations, xZoom, yZoom, 0, 0);
		objects[1] = health1;
		health2 = new Health(healthAnimations, xZoom, yZoom, 40, 0);
		objects[2] = health2;
		health3 = new Health(healthAnimations, xZoom, yZoom, 80, 0);
		objects[3] = health3;
		
		// Add keyboard/mouse listeners to Canvas for user input
		canvas.addKeyListener(keyListener); // Add listener for keyboard input
		canvas.addFocusListener(keyListener); // Add listener for determining if key has keyboard focus
		canvas.addMouseListener(mouseListener); // Add listener for mouse button input
		canvas.addMouseMotionListener(mouseListener); // Add listener for mouse motion input
	
		addComponentListener(new ComponentListener() {
			public void componentResized(ComponentEvent e) {
				int newWidth = canvas.getWidth();
				int newHeight = canvas.getHeight();

				if(newWidth > renderer.getMaxWidth())
					newWidth = renderer.getMaxWidth();

				if(newHeight > renderer.getMaxHeight())
					newHeight = renderer.getMaxHeight();

				renderer.getCamera().w = newWidth;
				renderer.getCamera().h = newHeight;
				canvas.setSize(newWidth, newHeight);
				pack();
			}

			public void componentHidden(ComponentEvent e) {}
			public void componentMoved(ComponentEvent e) {}
			public void componentShown(ComponentEvent e) {}
		});
		canvas.requestFocus();
	}
	
	// run( ) function automatically called by Thread when initialized with this (Game)
	public void run( ) 
	{
		// Initialize long with nanotime
		long lastTime = System.nanoTime( ); // Long, 2^63
		double nanoSecondConversion = 1000000000.0 / 60; // 60 frames per second
		double changeInSeconds = 0; // Initialize change in seconds

		// Loop that runs while program is active, calls update( ) and render( )
		while(true) 
		{
				// Catch current time in nanotime
				long now = System.nanoTime();
	
				// Update changeInSeconds
				changeInSeconds += (now - lastTime) / nanoSecondConversion;
				
				// Call update( ) 60 times per second, determined by changeInSeconds
				if(State == STATE.GAME)
				{
					while(changeInSeconds >= 1)
					{
						update( );
						changeInSeconds--; // Reset changeInSeconds
					}
	
					render( ); // Call render( ) each iteration
				}
				
				if (State == STATE.MENU)
				{
					while(changeInSeconds >= 1)
					{
						changeInSeconds--; // Reset changeInSeconds
					}
					BufferStrategy bufferStrategy = canvas.getBufferStrategy( ); /// !!! DELETE? !!!
					Graphics graphics = bufferStrategy.getDrawGraphics( ); // Graphics initialized
					super.paint(graphics); // Paint method called to begin painting buffered graphics
					map.renderBG(renderer, objectsEmpty, xZoom, yZoom);
					renderer.render(graphics); // RenderHandler draws image
					menu.render(graphics);
					graphics.dispose( ); // Dispose of graphics to be painted
					bufferStrategy.show( ); // Makes next available buffer visible
					renderer.clear( ); // Clears array of pixels to be drawn in RenderHandler
					}
				if (State == STATE.PREGAME)
				{
					while(changeInSeconds >= 1)
					{
						changeInSeconds--; // Reset changeInSeconds
					}
					BufferStrategy bufferStrategy = canvas.getBufferStrategy( ); /// !!! DELETE? !!!
					Graphics graphics = bufferStrategy.getDrawGraphics( ); // Graphics initialized
					super.paint(graphics); // Paint method called to begin painting buffered graphics
					map.renderBG(renderer, objectsEmpty, xZoom, yZoom);
					renderer.render(graphics); // RenderHandler draws image
					pregame.render(graphics);
					graphics.dispose( ); // Dispose of graphics to be painted
					bufferStrategy.show( ); // Makes next available buffer visible
					renderer.clear( ); // Clears array of pixels to be drawn in RenderHandler
				}
				if (State == STATE.SCORES)
				{
					while(changeInSeconds >= 1)
					{
						changeInSeconds--; // Reset changeInSeconds
					}
					BufferStrategy bufferStrategy = canvas.getBufferStrategy( ); /// !!! DELETE? !!!
					Graphics graphics = bufferStrategy.getDrawGraphics( ); // Graphics initialized
					super.paint(graphics); // Paint method called to begin painting buffered graphics
					map.renderBG(renderer, objectsEmpty, xZoom, yZoom);
					renderer.render(graphics); // RenderHandler draws image
					scores.render(graphics);
					graphics.dispose( ); // Dispose of graphics to be painted
					bufferStrategy.show( ); // Makes next available buffer visible
					renderer.clear( ); // Clears array of pixels to be drawn in RenderHandler
				}
				if (State == STATE.LEADERBOARD)
				{
					while(changeInSeconds >= 1)
					{
						changeInSeconds--; // Reset changeInSeconds
					}
					BufferStrategy bufferStrategy = canvas.getBufferStrategy( ); /// !!! DELETE? !!!
					Graphics graphics = bufferStrategy.getDrawGraphics( ); // Graphics initialized
					super.paint(graphics); // Paint method called to begin painting buffered graphics
					map.renderBG(renderer, objectsEmpty, xZoom, yZoom);
					renderer.render(graphics); // RenderHandler draws image
					scoresLB.render(graphics);
					graphics.dispose( ); // Dispose of graphics to be painted
					bufferStrategy.show( ); // Makes next available buffer visible
					renderer.clear( ); // Clears array of pixels to be drawn in RenderHandler
				}
				if (State == STATE.RESPAWN)
				{
					while(changeInSeconds >= 1)
					{
						changeInSeconds--; // Reset changeInSeconds
					}
					BufferStrategy bufferStrategy = canvas.getBufferStrategy( ); /// !!! DELETE? !!!
					Graphics graphics = bufferStrategy.getDrawGraphics( ); // Graphics initialized
					super.paint(graphics); // Paint method called to begin painting buffered graphics
					map.renderBG(renderer, objectsEmpty, xZoom, yZoom);
					renderer.render(graphics); // RenderHandler draws image
					respawn.render(graphics);
					graphics.dispose( ); // Dispose of graphics to be painted
					bufferStrategy.show( ); // Makes next available buffer visible
					renderer.clear( ); // Clears array of pixels to be drawn in RenderHandler
				}
			lastTime = now; // Update time of last iteration, lastTime
		}
	}

	// Method called to update objects at given FPS of 60, called with FPS specified in run( )
	public void update( )
	{
		// Update each object in objects array at given FPS
		for(int i = 0; i < numberObjects; i++)
			objects[i].update(this); // Calls update(Game game) in GameObject
	}
	
	// Method called to render graphics, called during every iteration of run( )
	public void render( ) 
	{
		// Initialize Canvas BufferStrategy for frame run on thread
		BufferStrategy bufferStrategy = canvas.getBufferStrategy( );
		
		// Creates a graphics context for the Canvas drawing buffer
		Graphics graphics = bufferStrategy.getDrawGraphics( ); // Graphics initialized
		super.paint(graphics); // Paint method called to begin painting buffered graphics
		
		// Render map with RenderHandler and given width and height zoom variables
		map.render(renderer, objects, xZoom, yZoom);
			
		// Render objects in objects array
		//for(int i = 0; i < objects.length; i++)
			//objects[i].render(renderer, xZoom, yZoom);
			
		renderer.render(graphics); // RenderHandler draws image
		
		printScore(graphics);
		
		graphics.dispose( ); // Dispose of graphics to be painted
		bufferStrategy.show( ); // Makes next available buffer visible
		renderer.clear( ); // Clears array of pixels to be drawn in RenderHandler
	}

	// Method to create BufferedImage by loading image from given file path 
	private BufferedImage loadImage(String path)
	{
		try 
		{
			BufferedImage loadedImage = ImageIO.read(Game.class.getResource(path));
			BufferedImage formattedImage = new BufferedImage(loadedImage.getWidth(), loadedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
			formattedImage.getGraphics().drawImage(loadedImage, 0, 0, null);

			return formattedImage;
		}
		catch(IOException exception) 
		{
			exception.printStackTrace();
			return null;
		}
	}

	// Method called when mouse is left clicked, sets tile
	public void leftClick(int x, int y)
	{
		if(State == STATE.MENU)
		{
			if(x >= 390 && x <= 598)
			{
				if(y >= 370 && y <= 415)
				{
					// Play button pressed
					player1.score = 0;
					State = STATE.PREGAME;
				}
				else if(y >= 440 && y <= 485)
				{
					// Scores button pressed
					State = STATE.SCORES;
				}
				else if(y >= 510 && y <= 555)
				{
					// Quit button pressed
					System.exit(0);
				}
			}
		}
		else if(State == STATE.PREGAME)
		{
			if(y >= 430 && y <= 475)
			{
				if(x >= 331 && x <= 481)
				{
						// Back button pressed
						pregame.name.delete(0, pregame.userName.length());
						State = STATE.MENU;
				}
				else if(x >= 501 && x <= 651)
				{
					if(pregame.nameLength > 2)
					{
						// Go button pressed
						State = STATE.GAME;
					}
				}
			}
		}
		else if(State == STATE.SCORES)
		{
			if(x >= 381 && x <= 601)
			{
				if(y >= 160 && y <= 205)
				{
					printBoard = userBank.getBoard( );
					State = STATE.LEADERBOARD;
				}
			}
			if(x >= 415 && x <= 565)
			{
				if(y >= 580 && y <= 625)
				{
					State = STATE.MENU;
				}
			}
			if(x >= 639 && x <= 699)
			{
				if(y >= 350 && y <= 395)
				{
					if(scores.nameLength > 2)
					{
						int highScore = userBank.getHighScore(scores.userName);
						scores.highScore = highScore;
						scores.displayingUser = true;
					}
				}
			}
			
		}
		else if(State == STATE.LEADERBOARD)
		{
			if(x >= 390 && x <= 598)
			{
				if(y >= 530 && y <= 575)
				{
					// Back button pressed
					State = STATE.SCORES;
				}
			}
		}
		/*
		 * g2D.fill(new Rectangle(390, 370, 208, 45));
		g2D.fill(new Rectangle(390, 440, 208, 45));
		 */
		else if(State == STATE.RESPAWN)
		{
			if(x >= 390 && x <= 598)
			{
				if(y >= 370 && y <= 405)
				{
					player1.score = 0;
					player1.health = 3;
					health3.setOffset(80,0);
					health2.setOffset(40,0);
					health1.setOffset(0,0);
					numberObjects = 4;
					State = STATE.GAME;
				}
				if(y >= 440 && y <= 485)
				{
					player1.score = 0;
					player1.health = 3;
					health3.setOffset(80,0);
					health2.setOffset(40,0);
					health1.setOffset(0,0);
					numberObjects = 4;
					State = STATE.MENU;
				}
			}
		}
	}
	
	public void changeTile(int tileID) 
	{
		selectedTileID = tileID;
	}
	
	public int getSelectedTile()
	{
		return selectedTileID;
	}
	
	public void printScore(Graphics graphics)
	{
		Font font0 = new Font("courier", Font.BOLD, 40);
		graphics.setColor(new Color(255, 181, 28));
		graphics.setFont(font0);
		if(player1.score > -1 && player1.score < 10)
			graphics.drawString("00" + Integer.toString(player1.score), 63, 690);
		else if (player1.score > 9 && player1.score < 100)
			graphics.drawString("0" + Integer.toString(player1.score), 63, 690);
		else if (player1.score < 1000)
			graphics.drawString(Integer.toString(player1.score), 63, 690);
	}
	
	public void endGame()
	{
		userBank.add(pregame.userName, player1.score);
		respawn.setScore(player1.score);
		State = STATE.RESPAWN;
	}
	
	public void loadTilesFront( )
	{
		int xPos = ((int)Math.floor(30 + (renderer.getCamera().x)/(16.0 * xZoom)));
		
		// Renders the towers a set distance apart
		if(towerCounter > towerSpacing) {
			
			// Determines whether to render a GOOD tile or not
			double goodTileProb = Math.random() * 6.0;
			//System.out.println(goodTileProb);
			
			// This will render a good tile around 1/3 of the time
			if(goodTileProb >= 4.5) {
				map.setTile(selectedLayer, xPos, -2, 23, -1); // Temporary good tile
				towerCounter = 0;
				
			// Else render a bad tile
			}
			else if(goodTileProb >= 4 && goodTileProb < 4.5) {
				map.setTile(selectedLayer, xPos, 0, 23, -1); // Temporary good tile
				towerCounter = 0;
			}
			
			else if(goodTileProb >= 2 && goodTileProb < 4)
			{
				map.setTile(selectedLayer, xPos+4, 0, 24, -1);
				map.setTile(selectedLayer, xPos+4, 1, 25, -1);
			}
			
			else if (goodTileProb >= 1 && goodTileProb < 2){
				map.setTile(selectedLayer, xPos, -2, 11, 1);
				map.setTile(selectedLayer, xPos+1, -2, 12, 1);
				map.setTile(selectedLayer, xPos+2, -2, 13, 1);
				map.setTile(selectedLayer, xPos, -1, 14, 1);
				map.setTile(selectedLayer, xPos+1, -1, 15, 1);
				map.setTile(selectedLayer, xPos+2, -1, 16, 1);
				map.setTile(selectedLayer, xPos, 0, 17, 1);
				map.setTile(selectedLayer, xPos+1, 0, 18, 1);
				map.setTile(selectedLayer, xPos+2, 0, 19, 1);
				map.setTile(selectedLayer, xPos, 1, 20, 1);
				map.setTile(selectedLayer, xPos+1, 1, 21, 1);
				map.setTile(selectedLayer, xPos+2, 1, 22, 1);
							
				towerCounter = 0;
			}
			else{
				map.setTile(selectedLayer, xPos, -2, 26, 1);
				map.setTile(selectedLayer, xPos+1, -2, 27, 1);
				map.setTile(selectedLayer, xPos+2, -2, 28, 1);
				map.setTile(selectedLayer, xPos, -1, 29, 1);
				map.setTile(selectedLayer, xPos+1, -1, 30, 1);
				map.setTile(selectedLayer, xPos+2, -1, 31, 1);
				map.setTile(selectedLayer, xPos, 0, 32, 1);
				map.setTile(selectedLayer, xPos+1, 0, 33, 1);
				map.setTile(selectedLayer, xPos+2, 0, 34, 1);
				map.setTile(selectedLayer, xPos, 1, 35, 1);
				map.setTile(selectedLayer, xPos+1, 1, 36, 1);
				map.setTile(selectedLayer, xPos+2, 1, 37, 1);
							
				towerCounter = 0;
			}
		}else{
			towerCounter++;
		}
							
		// Renders the ground tiles
		if(State == STATE.GAME)
		{
			//map.setTile(((int)Math.floor(30 + (renderer.getCamera().x)/(16.0 * xZoom))), (10 + (int)Math.floor((renderer.getCamera().y)/(16.0 * yZoom))), 2);
			map.setTile(1, ((int)Math.floor(30 + (renderer.getCamera().x)/(16.0 * xZoom))), 2, 2, 0);
			map.setTile(1, ((int)Math.floor(30 + (renderer.getCamera().x)/(16.0 * xZoom))), 3, 3, 0);
			map.setTile(1, ((int)Math.floor(30 + (renderer.getCamera().x)/(16.0 * xZoom))), 4, 4, 0);
			map.setTile(1, ((int)Math.floor(30 + (renderer.getCamera().x)/(16.0 * xZoom))), 5, 5, 0);
			map.setTile(1, ((int)Math.floor(30 + (renderer.getCamera().x)/(16.0 * xZoom))), 6, 6, 0);
			map.setTile(1, ((int)Math.floor(30 + (renderer.getCamera().x)/(16.0 * xZoom))), 7, 7, 0);
			map.setTile(1, ((int)Math.floor(30 + (renderer.getCamera().x)/(16.0 * xZoom))), 8, 8, 0);
			map.setTile(1, ((int)Math.floor(30 + (renderer.getCamera().x)/(16.0 * xZoom))), 9, 9, 0);
			
			
		}
	}
	
	public void updateScore(boolean colliding)
	{
		if(colliding && timeSinceScoreUpdate > 100)
		{
			timeSinceScoreUpdate = 0;
			player1.score++;
		}
		else
		{
			timeSinceScoreUpdate++;
		}
	}
	
	public void updateHealth(boolean colliding)
	{
		if(colliding && timeSinceHealthUpdate > 100)
		{
			timeSinceHealthUpdate = 0;
			player1.health--;
			if(player1.health == 2)
			{
				health3.setOffset(-200,900);
			}
			if(player1.health == 1)
			{
				health2.setOffset(-200,900);
			}
			if(player1.health == 0)
			{
				health1.setOffset(-200,900);
				endGame();
			}
		}
		else
		{
			timeSinceHealthUpdate++;
		}
	}
	
	// Return key listener, renderer, mouse listener
	public KeyboardListener getKeyListener( ) { return keyListener; }
	public Renderer getRenderer( ) { return renderer; }
	public MouseEventListener getMouseListener( ) { return mouseListener; }
	public Map getMap() { return map; }
	public int getXZoom() { return xZoom; }
	public int getYZoom() { return yZoom; }
	
	// Start
	public static void main(String[] args) 
	{
		Game game = new Game( ); // Initialize Game
		Thread gameThread = new Thread(game); // Initialize thread with game
		gameThread.start( ); // Start thread
	}

}