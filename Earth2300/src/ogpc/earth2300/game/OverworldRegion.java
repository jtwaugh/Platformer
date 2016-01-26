package ogpc.earth2300.game;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import ogpc.earth2300.graphics.Sprite;
import ogpc.earth2300.math.Geometry;
import ogpc.earth2300.resource.FileReader;

public class OverworldRegion
{
	private Sprite picLiberated;
	private Sprite picEnemy;
	
	public boolean liberated;
	public boolean clickedOn;
	
	public int population;
	public int power;
	public int food;
	public int industry;
	public int water;//im ryze
	public double unrest;
	
	public OverworldRegion(String l, String e, Point pos, int p, int r, int f, int i, int w)
	{
		liberated = false;
		clickedOn = false;
		picLiberated = Game.manager.getSprite(l);
		picLiberated.setPosition(pos.x, pos.y);
		picEnemy = Game.manager.getSprite(e);
		picEnemy.setPosition(pos.x, pos.y);
		population = p;
		power = r;
		food = f;
		industry = i;
		water = w;
		unrest = 0;
	}
	
	public void draw(Graphics2D g)
	{
		if (liberated)
		{
			picLiberated.draw(g);
		}
		else
		{
			picEnemy.draw(g);
		}
	}
	
	public Point getPosition()
	{
		if (liberated)
		{
			return picLiberated.getPosition();
		}
		else
		{
			return picEnemy.getPosition();
		}
	}
	
	public Point getScaledCenter(double s)
	// absolute center
	{
		Sprite img;
		
		if (liberated)
		{
			img = picLiberated;
		}
		else
		{
			img = picEnemy;
		}
		
		int x = img.getWidth()/2 + img.getPosition().x;
		int y = img.getHeight()/2 + img.getPosition().y;
		
		return new Point ((int)s * x, (int)s * y);
	}
	
	public void mouseReleaseHandle(MouseEvent e, int i)
	{
		Sprite img;
		
		if (liberated)
		{
			img = picLiberated;
		}
		else
		{
			img = picEnemy;
		}
		
		Point myPoint = new Point (Game.mousePos.x/i, Game.mousePos.y/i);
		
		if (Geometry.pointInQuad(myPoint, img.getPosition(), new Point (img.getPosition().x + img.getWidth(), img.getPosition().y + img.getHeight())))
		{
			if (FileReader.RGBA(myPoint.x - img.getPosition().x, myPoint.y - img.getPosition().y, img.getImage()).getAlpha() > 0)
			{
				clickedOn = true;
			}
		}
	}
}
