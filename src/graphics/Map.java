package graphics;

import game.Mob;
import graphics.Renderer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Map
{
	// Map class used for rendering/creating map, handling collisions
	private Renderer renderer;
	private Tiles tiles;
	private int fillTileID = -1;

	private ArrayList<MappedTile> mappedTiles = new ArrayList<MappedTile>( );
	private Block[][] blocks;
	private int blockStartX, blockStartY;

	private int blockWidth = 6; // Width of collision blocks
	private int blockHeight = 6; // Height of collision blocks
	private int blockPixelWidth = blockWidth * 16;
	private int blockPixelHeight = blockHeight * 16;

	private HashMap<Integer, String> comments = new HashMap<Integer, String>( );

	private File mapFile;

	private int numLayers;
	
	private int tileType;

	// Map constructor, loads in map from file
	public Map(File mapFile, Tiles tileSet)
	{
		this.mapFile = mapFile;
		this.tiles = tileSet;
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;
		try 
		{
			Scanner scanner = new Scanner(mapFile);
			int currentLine = 0;
			while(scanner.hasNextLine()) 
			{
				String line = scanner.nextLine();
				if(!line.startsWith("//"))
				{
					if(line.contains(":")) 
					{
						String[] splitString = line.split(":");
						if(splitString[0].equalsIgnoreCase("Fill"))
						{
							fillTileID = Integer.parseInt(splitString[1]);
							continue;
						}
					}


					String[] splitString = line.split(",");
					if(splitString.length >= 4)
					{
						MappedTile mappedTile = new MappedTile(Integer.parseInt(splitString[0]),
															   Integer.parseInt(splitString[1]),
															   Integer.parseInt(splitString[2]),
															   Integer.parseInt(splitString[3]),
															   0);
						if(mappedTile.x < minX)
							minX = mappedTile.x;
						if(mappedTile.y < minY)
							minY = mappedTile.y;
						if(mappedTile.x > maxX)
							maxX = mappedTile.x;
						if(mappedTile.x > maxY)
							maxY = mappedTile.y;

						if(numLayers <= mappedTile.layer)
							numLayers = mappedTile.layer + 1;


						mappedTiles.add(mappedTile);
					}
				}
				else
				{
					comments.put(currentLine, line);
				}
				currentLine++;
			}

			if(mappedTiles.size() == 0) {
				minX = -blockWidth;
				minY = -blockHeight;
				maxX = blockWidth;
				maxY = blockHeight;
			} 

			blockStartX = minX;
			blockStartY = minY;
			int blockSizeX = (maxX + blockWidth) - minX;
			int blockSizeY = (maxY + blockHeight) - minY;
			blocks = new Block[blockSizeX][blockSizeY];

			//Loop through all mappedTiles in the entire level and add them to the blocks.
			for(int i = 0; i < mappedTiles.size(); i++) {
				MappedTile mappedTile = mappedTiles.get(i);
				int blockX = (mappedTile.x - minX)/blockWidth;
				int blockY = (mappedTile.y - minY)/blockHeight;
				assert(blockX >= 0 && blockX < blocks.length && blockY >= 0 && blockY < blocks[0].length);

				if(blocks[blockX][blockY] == null)
					blocks[blockX][blockY] = new Block();

				blocks[blockX][blockY].addTile(mappedTile);
			}

		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	// Method to render tile on screen
		public void setTile(int layer, int tileX, int tileY, int tileID, int type)
		{
			if(layer >= numLayers)
				numLayers = layer + 1;

			for(int i = 0; i < mappedTiles.size(); i++)
			{
				MappedTile mappedTile = mappedTiles.get(i);
				if(mappedTile.x == tileX && mappedTile.y == tileY) {
					mappedTile.id = tileID;
					return;
				}
			}

			MappedTile mappedTile = new MappedTile(layer, tileID, tileX, tileY, type);
			mappedTiles.add(mappedTile);

			//Add to blocks
			int blockX = (tileX - blockStartX)/blockWidth;
			int blockY = (tileY - blockStartY)/blockHeight;
			if(blockX >= 0 && blockY >= 0 && blockX < blocks.length && blockY < blocks[0].length) {
				if(blocks[blockX][blockY] == null)
					blocks[blockX][blockY] = new Block();

				blocks[blockX][blockY].addTile(mappedTile);
			} else {
				int newMinX = blockStartX;
				int newMinY = blockStartY;
				int newLengthX = blocks.length;
				int newLengthY = blocks[0].length;

				if(blockX < 0) {
					int increaseAmount = blockX * -1;
					newMinX = blockStartX - blockWidth*increaseAmount;
					newLengthX = newLengthX + increaseAmount;
				} else if(blockX >= blocks.length)
					newLengthX = blocks.length + blockX;

				if(blockY < 0) {
					int increaseAmount = blockY * -1;
					newMinY = blockStartY - blockHeight*increaseAmount;
					newLengthY = newLengthY + increaseAmount;
				} else if(blockY >= blocks[0].length)
					newLengthY = blocks[0].length + blockY;

				Block[][] newBlocks = new Block[newLengthX][newLengthY];

				for(int x = 0; x < blocks.length; x++)
					for(int y = 0; y < blocks[0].length; y++)
						if(blocks[x][y] != null) {
							newBlocks[x + (blockStartX - newMinX)/blockWidth][y + (blockStartY - newMinY)/blockHeight] = blocks[x][y];
						}

				blocks = newBlocks;
				blockStartX = newMinX;
				blockStartY = newMinY;
				blockX = (tileX - blockStartX)/blockWidth;
				blockY = (tileY - blockStartY)/blockHeight;
				if(blocks[blockX][blockY] == null)
					blocks[blockX][blockY] = new Block();
				blocks[blockX][blockY].addTile(mappedTile);
			}
		}
	
	// Handle collisions, referenced in Player
		public boolean checkCollision(Rectangle rect, int layer, int xZoom, int yZoom)
		{
			int tileWidth = 16 * xZoom;
			int tileHeight = 16 * yZoom;

			//Coordinates to check all tiles in a radius of 4 around the player
			int topLeftX = (rect.x)/tileWidth;
			int topLeftY = (rect.y)/tileHeight;
			int bottomRightX = (rect.x + rect.w)/tileWidth;
			int bottomRightY = (rect.y + rect.h)/tileHeight;

			//Starting at the top left tile and going to the bottom right
			for(int x = topLeftX; x < bottomRightX; x++)
				for(int y = topLeftY; y < bottomRightY; y++) {
					MappedTile tile = getTile(layer, x, y);
					if(tile != null) {
						int collisionType = tiles.collisionType(tile.id);
						this.tileType = tile.type;
						
						//Full tile collision
						if(collisionType == 0) {
							if (this.tileType == -1)
							{
								Rectangle tileRectangle = new Rectangle(tile.x*tileWidth, (tile.y*tileHeight)+64, tileWidth, tileWidth+200);
								
								if(tileRectangle.intersects(rect))
									return true;	
							}
							else
							{
								Rectangle tileRectangle = new Rectangle(tile.x*tileWidth, tile.y*tileHeight, tileWidth, tileWidth);
								
								if(tileRectangle.intersects(rect))
									return true;
							}

						//Top of tile collision
						} else if(collisionType == 1) {
							Rectangle tileRectangle = new Rectangle(tile.x*tileWidth, tile.y*tileHeight, tileWidth, tileWidth);
							if(tileRectangle.intersects(rect))
								return true;
						}
					}
				}
			
			return false;
		}
	
	// Get tile information for collision handling
	public MappedTile getTile(int layer, int tileX, int tileY) {
		int blockX = (tileX - blockStartX)/blockWidth;
		int blockY = (tileY - blockStartY)/blockHeight;

		if(blockX < 0 || blockX >= blocks.length || blockY < 0 || blockY >= blocks[0].length)
			return null;

		Block block = blocks[blockX][blockY];

		if(block == null)
			return null;

		return block.getTile(layer, tileX, tileY);
	}
	
	// Return tile type, used for collision
	public int getTileType( )
	{
		return tileType;
	}
	
	// Render background tiles, used on menu screens
	public void renderBG(Renderer renderer, Mob[] objects, int xZoom, int yZoom)
	{
		int tileWidth = 16 * xZoom;
		int tileHeight = 16 * yZoom;

		if(fillTileID >= 0)
		{
			Rectangle camera = renderer.getCamera();

			for(int y = camera.y - tileHeight - (camera.y % tileHeight); y < camera.y + camera.h; y+= tileHeight)
			{
				for(int x = camera.x - tileWidth - (camera.x % tileWidth); x < camera.x + camera.w; x+= tileWidth)
				{
					tiles.renderTile(fillTileID, renderer, x, y, xZoom, yZoom);
				}
			}
		}
	}

	// Render map
	public void render(Renderer renderer, Mob[] objects, int xZoom, int yZoom)
	{
		int tileWidth = 16 * xZoom;
		int tileHeight = 16 * yZoom;

		if(fillTileID >= 0)
		{
			Rectangle camera = renderer.getCamera();

			for(int y = camera.y - tileHeight - (camera.y % tileHeight); y < camera.y + camera.h; y+= tileHeight)
			{
				for(int x = camera.x - tileWidth - (camera.x % tileWidth); x < camera.x + camera.w; x+= tileWidth)
				{
					tiles.renderTile(fillTileID, renderer, x, y, xZoom, yZoom);
				}
			}
		}

		for(int layer = 0; layer < numLayers; layer++) {
			int topLeftX = renderer.getCamera().x;
			int topLeftY = renderer.getCamera().y;
			int bottomRightX = renderer.getCamera().x + renderer.getCamera().w;
			int bottomRightY = renderer.getCamera().y + renderer.getCamera().h;

			int leftBlockX = (topLeftX/tileWidth - blockStartX - 16)/blockWidth;
			int blockX = leftBlockX;
			int blockY = (topLeftY/tileHeight - blockStartY - 16)/blockHeight;
			int pixelX = topLeftX;
			int pixelY = topLeftY;

			while(pixelX < bottomRightX && pixelY < bottomRightY) {

				if(blockX >= 0 && blockY >= 0 && blockX < blocks.length && blockY < blocks[0].length) {
					if(blocks[blockX][blockY] != null)
						blocks[blockX][blockY].render(renderer, layer, tileWidth, tileHeight, xZoom, yZoom);
				}

				blockX++;
				pixelX += blockPixelWidth;

				if(pixelX > bottomRightX) {
					pixelX = topLeftX;
					blockX = leftBlockX;
					blockY++;
					pixelY += blockPixelHeight;
					if(pixelY > bottomRightY)
						break;
				}
			}

			for(int i = 0; i < objects.length; i++)
				if(objects[i].getLayer() == layer)
					objects[i].render(renderer, xZoom, yZoom);
		}

		for(int i = 0; i < objects.length; i++)
			if(objects[i].getLayer() == Integer.MAX_VALUE)
				objects[i].render(renderer, xZoom, yZoom);
	}

	// Collision block of 6x6 tiles
	private class Block
	{
		public ArrayList<MappedTile>[] mappedTilesByLayer;

		public Block( ) {
			mappedTilesByLayer = new ArrayList[numLayers];
			for(int i = 0; i < mappedTilesByLayer.length; i++)
				mappedTilesByLayer[i] = new ArrayList<MappedTile>();
		}

		// Add tile to collision block
		public void addTile(MappedTile tile) {
			if(mappedTilesByLayer.length <= tile.layer) {
				ArrayList<MappedTile>[] newTilesByLayer = new ArrayList[tile.layer + 1];

				int i = 0;
				for(i = 0; i < mappedTilesByLayer.length; i++)
					newTilesByLayer[i] = mappedTilesByLayer[i];
				for(; i < newTilesByLayer.length; i++)
					newTilesByLayer[i] = new ArrayList<MappedTile>();

				mappedTilesByLayer = newTilesByLayer;
			}
			mappedTilesByLayer[tile.layer].add(tile);
		}
		
		public void render(Renderer renderer, int layer, int tileWidth, int tileHeight, int xZoom, int yZoom) {
			if(mappedTilesByLayer.length > layer) {
				ArrayList<MappedTile> mappedTiles = mappedTilesByLayer[layer];
				for(int tileIndex = 0; tileIndex < mappedTiles.size(); tileIndex++)
				{
					MappedTile mappedTile = mappedTiles.get(tileIndex);
					tiles.renderTile(mappedTile.id, renderer, mappedTile.x * tileWidth, mappedTile.y * tileHeight, xZoom, yZoom);
				}
			}
		}

		// Delete tile from block
		public void removeTile(MappedTile tile) {
			mappedTilesByLayer[tile.layer].remove(tile);
		}
		
		// Get tile in block for collision info
		public MappedTile getTile(int layer, int tileX, int tileY) 
		{
			for(MappedTile tile : mappedTilesByLayer[layer]) 
			{
				if(tile.x == tileX && tile.y == tileY)
					return tile;
			}
			return null;
		}
	}

	//Tile ID in the tileSet and the position of the tile in the map
	private class MappedTile
	{
		// Type: Determines whether tile is good or bad. | -1 = BAD | 0 = Neutral | 1 = GOOD |
		public int layer, id, x, y, type;

		public MappedTile(int layer, int id, int x, int y, int type)
		{
			this.layer = layer;
			this.id = id;
			this.x = x;
			this.y = y;
			this.type = type;
		}
	}
}