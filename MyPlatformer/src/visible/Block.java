package visible;

import game.Game;
import graphics.Sprite;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class Block
{
	public Sprite pic;
	protected Color c;
	protected boolean solid;
	private double friction;
	
	public Block(Sprite p, boolean s, Color k, double f) throws IOException
	{
		BufferedImage i = p.getImage();
		pic = new Sprite(i);
		solid = s;
		c = k;
		friction = f;
	}
	
	public Block()
	{
		pic = new Sprite((BufferedImage)null);
		c = new Color(255,255,255,255);
		solid = false;
		friction = 0;
	}
	
	public Block(Block that, int _x, int _y)
	{
		pic = new Sprite((BufferedImage)that.pic.getImage());
		c  = new Color(that.c.getRed(), that.c.getGreen(), that.c.getBlue(),that.c.getAlpha());
		solid = that.isSolid();
		setPosition(_x, _y);
		friction = that.friction;
	}
	
	public Block(Block that)
	{

		pic = that.pic;
		c  = that.c;
		solid = that.isSolid();
		friction = that.friction;
	}
	
	public String toString()
	{
		return (c.getRed() + ", " + c.getGreen() + ", " + c.getBlue() + ", " + c.getAlpha());
	}
	
	public boolean colorMatch(Color q)
	{
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		int a = c.getAlpha();
		
		int s = q.getRed();
		int h = q.getGreen();
		int c = q.getBlue();
		int z = q.getAlpha();
		
		return ((r == s) && (g == h) && (b == c) && (a == z));
	}
	
	public boolean isSolid()
	{
		return solid;
	}
	
	public void setSolid(boolean b)
	{
		solid = b;
	}
	
	public void setPosition(int x, int y)
	{
		pic.setPosition(x, y);
	}
	
	public void select()
	{
		pic = Game.manager.getSprite("white");
	}
	
	public void draw(Graphics2D g)
	{
		pic.draw(g);
	}
	
	public double getFriction()
	{
		return friction;
	}
	
}
