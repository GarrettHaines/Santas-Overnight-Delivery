package game;

import data.UserBank;
import data.User;
import game.Game;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class ScoresLB
{
	public Rectangle quit = new Rectangle(390, 530, 208, 45);
	private Game game1;
	
	Font font0 = new Font("courier", Font.BOLD, 39);
	Font font1 = new Font("courier", Font.BOLD, 18);
	Font font2 = new Font("courier", Font.BOLD, 26);
	
	public void render(Graphics graphics)
	{
		Graphics2D g2D = (Graphics2D) graphics;
		graphics.setFont(font0);
		graphics.setColor(Color.white);
		graphics.drawString("LEADERBOARD", 367, 250);
		
		//User[] printBoard = game1.userBank.getBoard( );
		graphics.setFont(font1);
		for(int i = 0; i < 10; i++)
		{
			String un = game1.printBoard[i].username;
			int sc = game1.printBoard[i].highScore;
			if(i > 8)
				graphics.drawString(Integer.toString(i+1) + ". " + un, 367, 300 + (i * 20));
			else
				graphics.drawString("0" + Integer.toString(i+1) + ". " + un, 367, 300 + (i * 20));

			if(sc > 99)
				graphics.drawString(Integer.toString(sc), 589, 300 + (i * 20));
			else if (sc > 9)
				graphics.drawString("0" + Integer.toString(sc), 589, 300 + (i * 20));
			else
				graphics.drawString("00" + Integer.toString(sc), 589, 300 + (i * 20));
		}
		
		g2D.fill(new Rectangle(390, 530, 208, 45));
		BasicStroke bs = new BasicStroke(3);
        g2D.setStroke(bs);
		g2D.draw(quit);
		
		graphics.setFont(font2);
		graphics.setColor(new Color(18, 0, 89));
		graphics.drawString("BACK", quit.x+74, quit.y+31);
	}
}
