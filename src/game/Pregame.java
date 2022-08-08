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

public class Pregame
{
	public Rectangle enterBox = new Rectangle(331, 350, 320, 45);
	public Rectangle back = new Rectangle(331, 430, 150, 45);
	public Rectangle go = new Rectangle(501, 430, 150, 45);
	private Game game1;
	
	public static StringBuilder name = new StringBuilder("");
	public String userName;
	public int nameLength = 0;
	
	Font font0 = new Font("courier", Font.BOLD, 39);
	Font font1 = new Font("courier", Font.BOLD, 18);
	Font font2 = new Font("courier", Font.BOLD, 26);
	
	public void render(Graphics graphics)
	{
		userName = name.toString();
		nameLength = name.length();
		Graphics2D g2D = (Graphics2D) graphics;
		graphics.setFont(font0);
		graphics.setColor(Color.white);
		graphics.drawString("ENTER USERNAME", 330, 320);
		
		g2D.fill(new Rectangle(331, 430, 150, 45));
		g2D.fill(new Rectangle(501, 430, 150, 45));
		BasicStroke bs = new BasicStroke(3);
        g2D.setStroke(bs);

		g2D.draw(enterBox);
		g2D.draw(back);
		g2D.draw(go);
		
		graphics.setFont(font2);

		graphics.drawString(name.toString(), enterBox.x+15, enterBox.y+31);

		graphics.setColor(new Color(18, 0, 89));
		graphics.drawString("BACK", back.x+45, back.y+31);
		graphics.drawString("GO", go.x+60, go.y+31);
	}

}
