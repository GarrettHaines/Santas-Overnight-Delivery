package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Menu
{
	public Rectangle play = new Rectangle(390, 370, 208, 45);
	public Rectangle scores = new Rectangle(390, 440, 208, 45);
	public Rectangle quit = new Rectangle(390, 510, 208, 45);
	
	Font font0 = new Font("courier", Font.BOLD, 50);
	Font font1 = new Font("courier", Font.BOLD, 39);
	Font font2 = new Font("courier", Font.BOLD, 44);
	Font font3 = new Font("courier", Font.BOLD, 26);
	
	public void render(Graphics graphics)
	{
		Graphics2D g2D = (Graphics2D) graphics;
		graphics.setFont(font0);
		graphics.setColor(Color.black);
		graphics.drawString("SANTA'S", 394, 254);
		graphics.setFont(font1);
		graphics.drawString("OVERNIGHT", 394, 294);
		graphics.setFont(font2);
		graphics.drawString("DELIVERY", 394, 334);
		graphics.setColor(Color.white);
		graphics.setFont(font0);
		graphics.drawString("SANTA'S", 390, 250);
		graphics.setFont(font1);
		graphics.drawString("OVERNIGHT", 390, 290);
		graphics.setFont(font2);
		graphics.drawString("DELIVERY", 390, 330);

		graphics.setColor(Color.black);
		g2D.fill(new Rectangle(394, 374, 208, 45));
		g2D.fill(new Rectangle(394, 444, 208, 45));
		g2D.fill(new Rectangle(394, 514, 208, 45));

		graphics.setColor(Color.white);
		g2D.fill(new Rectangle(390, 370, 208, 45));
		g2D.fill(new Rectangle(390, 440, 208, 45));
		g2D.fill(new Rectangle(390, 510, 208, 45));
		BasicStroke bs = new BasicStroke(3);
        g2D.setStroke(bs);

		g2D.draw(play);
		g2D.draw(scores);
		g2D.draw(quit);
		
		graphics.setFont(font3);
		graphics.setColor(new Color(18, 0, 89));
		graphics.drawString("PLAY", play.x+72, play.y+31);
		graphics.drawString("SCORES", scores.x+58, scores.y+31);
		graphics.drawString("QUIT", quit.x+74, quit.y+31);
	}
}
