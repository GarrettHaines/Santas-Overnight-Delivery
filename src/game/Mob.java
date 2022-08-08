package game;

import graphics.Renderer;
import graphics.Rectangle;

public interface Mob
{
	// Called during each iteration of run( ) loop, as fast as PC can run
	public void render(Renderer renderer, int xZoom, int yZoom);
	
	// Called 60 FPS
	public void update(Game game);

	public int getLayer();
}