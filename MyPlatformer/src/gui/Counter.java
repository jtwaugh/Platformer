package gui;

import game.Game;
import graphics.Sprite;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class Counter
{
	protected Sprite pic;
	private int num;
	public Point pos;
	
	public Counter(Point _pos, String spriteName, int _num)
	{
		pic = Game.manager.getSprite(spriteName);
		num = _num;
		pos = new Point(_pos.x, _pos.y);
		pic.setPosition(pos.x, pos.y);
	}
	
	public void draw(Graphics2D g)
	{
		pic.draw(g);
		String myStr = "x" + String.valueOf(num);
		g.setColor(new Color(255,255,255));
		g.drawString(myStr, pos.x + pic.getWidth() + 2, pos.y + pic.getHeight()/2 + g.getFontMetrics().getAscent()/2);
	}
	
	public void drawAlt(Graphics2D g)
	{
		pic.draw(g);
		String myStr = String.valueOf(num);
		g.setColor(new Color(255,255,255));
		g.drawString(myStr, pos.x + pic.getWidth()/2 - g.getFontMetrics().stringWidth(myStr)/2, pos.y + pic.getHeight() + g.getFontMetrics().getAscent());
	}
	
	public void setNum(int n)
	{
		num = n;
	}
}
