package graphics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Tiles 
{
	// Tile class used for storing information about individual tiles loaded to screen as Sprites from a SpriteSheet
	private SpriteSheet spriteSheet;
	private ArrayList<Tile> tilesList = new ArrayList<Tile>();

	// Initialize tile if sprites have been loaded in SpriteSheet
	public Tiles(File tilesFile, SpriteSheet spriteSheet)
	{
		this.spriteSheet = spriteSheet;
		try 
		{
			Scanner scanner = new Scanner(tilesFile);
			while(scanner.hasNextLine()) 
			{
				String line = scanner.nextLine();
				if(!line.startsWith("//"))
				{
					String[] splitString = line.split("-");
					String tileName = splitString[0];
					int spriteX = Integer.parseInt(splitString[1]);
					int spriteY = Integer.parseInt(splitString[2]);
					Tile tile = new Tile(tileName, spriteSheet.getSprite(spriteX, spriteY));
					
					if(splitString.length >= 4)
					{
						tile.collidable = true;
						tile.collisionType = Integer.parseInt(splitString[3]);
					}
					
					tilesList.add(tile);
				}
			}
		} 
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	// Return size of tile list from file
	public int size( )
	{
		return tilesList.size();
	}
	
	// Render specified tile to screen
		public void renderTile(int tileID, Renderer renderer, int xPosition, int yPosition, int xZoom, int yZoom)
		{
			if(tileID >= 0 && tilesList.size() > tileID)
			{
				renderer.renderSprite(tilesList.get(tileID).sprite, xPosition, yPosition, xZoom, yZoom, false);
			}
			else
			{
				System.out.println("TileID " + tileID + " is not within range " + tilesList.size() + ".");
			}
		}
	
	// Return collisionType for determining where player can collide from
	public int collisionType(int tileID)
	{
		if(tileID >= 0 && tilesList.size( ) > tileID)
		{
			return tilesList.get(tileID).collisionType;
		}
		else
		{
			System.out.println("TileID " + tileID + " is not within range " + tilesList.size( ) + ".");
		}
		return -1;
	}
	
	// Return array of tile Sprites
		public Sprite[] getSprites( )
		{
			Sprite[] sprites = new Sprite[size()];

			for(int i = 0; i < sprites.length; i++)
				sprites[i] = tilesList.get(i).sprite;

			return sprites;
		}

	// Tile class containing individual tile information
	class Tile 
	{
		public String tileName;
		public Sprite sprite;
		public boolean collidable = false;
		public int collisionType = -1;

		public Tile(String tileName, Sprite sprite) 
		{
			this.tileName = tileName;
			this.sprite = sprite;
		}
	}
}