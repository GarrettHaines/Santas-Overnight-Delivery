package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Respawn
{
	private Player player;
	private int score = 0;
	public Rectangle restart = new Rectangle(390, 370, 208, 45);
	public Rectangle quit = new Rectangle(390, 440, 208, 45);
	
	Font font0 = new Font("courier", Font.BOLD, 45);
	Font font1 = new Font("courier", Font.BOLD, 26);
	
	public void setScore(int scor)
	{
		score = scor;
	}
	public void render(Graphics graphics)
	{
		Graphics2D g2D = (Graphics2D) graphics;
		graphics.setFont(font0);
		graphics.setColor(Color.white);
		graphics.drawString("GAME OVER", 372, 284);
		
		if(score > -1 && score < 10)
			graphics.drawString("00" + Integer.toString(score), 452, 340);
		else if (score > 9 && score < 100)
			graphics.drawString("0" + Integer.toString(score), 452, 340);
		else if (score < 1000)
			graphics.drawString(Integer.toString(score), 452, 340);
		
		g2D.fill(new Rectangle(390, 370, 208, 45));
		g2D.fill(new Rectangle(390, 440, 208, 45));
		BasicStroke bs = new BasicStroke(3);
        g2D.setStroke(bs);

		g2D.draw(restart);
		g2D.draw(quit);

		graphics.setFont(font1);
		graphics.setColor(new Color(18, 0, 89));
		graphics.drawString("RESTART", restart.x+50, restart.y+31);
		graphics.drawString("QUIT", quit.x+72, quit.y+31);
	}
}
