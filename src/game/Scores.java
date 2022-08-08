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
import java.util.Scanner;
import java.lang.StringBuilder;

public class Scores
{
	public Rectangle leaderboard = new Rectangle(381, 160, 220, 45);
	public Rectangle enterBox = new Rectangle(296, 350, 320, 45);
	public Rectangle back = new Rectangle(415, 580, 150, 45);
	public Rectangle go = new Rectangle(639, 350, 60, 45);
	private Game game1;
	
	public static StringBuilder name = new StringBuilder("");
	public String userName;
	public int nameLength = 0;
	public int highScore = 0;
	public boolean displayingUser = false;
	
	Font font0 = new Font("courier", Font.BOLD, 39);
	Font font1 = new Font("courier", Font.BOLD, 17);
	Font font2 = new Font("courier", Font.BOLD, 26);
	
	public void render(Graphics graphics)
	{
		userName = name.toString();
		nameLength = name.length();
		Graphics2D g2D = (Graphics2D) graphics;
		graphics.setFont(font0);
		graphics.setColor(Color.white);
		graphics.drawString("ENTER USERNAME", 330, 295);
		graphics.setFont(font1);
		graphics.drawString("TO VIEW HIGHEST RECORDED SCORE", 334, 325);
		
		g2D.fill(new Rectangle(381, 160, 220, 45));
		g2D.fill(new Rectangle(415, 580, 150, 45));
		g2D.fill(new Rectangle(639, 350, 60, 45));
		BasicStroke bs = new BasicStroke(3);
        g2D.setStroke(bs);

		g2D.draw(enterBox);
		g2D.draw(back);
		g2D.draw(go);
		
		graphics.setFont(font2);
		
		if(displayingUser)
		{
			
			if(highScore > -1 && highScore < 10)
			{
				graphics.drawString("HIGHEST SCORE", 385, 470);
				graphics.drawString("00" + Integer.toString(highScore), 465, 505);
			}
			else if (highScore > 9 && highScore < 100)
			{
				graphics.drawString("HIGHEST SCORE", 385, 470);
				graphics.drawString("0" + Integer.toString(highScore), 465, 505);
			}
			else if (highScore == -1)
			{
				graphics.drawString("USER DOES NOT EXIST", 340, 470);
			}
			else if (highScore < 1000)
			{
				graphics.drawString("HIGHEST SCORE", 385, 470);
				graphics.drawString(Integer.toString(highScore), 465, 505);
			}
		}

		graphics.drawString(name.toString(), enterBox.x+15, enterBox.y+31);

		graphics.setColor(new Color(18, 0, 89));
		graphics.drawString("LEADERBOARD", leaderboard.x+23, leaderboard.y+31);
		graphics.drawString("BACK", back.x+45, back.y+31);
		graphics.drawString("GO", go.x+15, go.y+31);
	}

}