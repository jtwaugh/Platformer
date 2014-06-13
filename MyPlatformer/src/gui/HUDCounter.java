package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;

public class HUDCounter extends Counter
{
	private double c;
	
	public HUDCounter(Point _pos, String spriteName, double _num)
	{
		super(_pos, spriteName, 0);
		c = _num;
	}
	
	public void drawAlt(Graphics2D g)
	{
		pic.draw(g);
		int d = (5 < String.valueOf(c).length()) ? 4 : String.valueOf(c).length();
		String myStr = String.valueOf(c).substring(0, d);
		g.setColor(new Color(255,255,255));
		g.setFont(new Font("sansserif", Font.PLAIN, 10));
		g.drawString(myStr, pos.x + pic.getWidth()/2 - g.getFontMetrics().stringWidth(myStr)/2, pos.y + pic.getHeight() + g.getFontMetrics().getAscent());
	}
	
	public void setC(double n)
	{
		c = n;
	}
}
