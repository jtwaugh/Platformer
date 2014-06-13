package gui;

import game.Game;
import graphics.Sprite;

import java.awt.Graphics2D;
import java.awt.Point;

public class Tick
{
	private Sprite pic;
	private Sprite greyPic;
	
	private Point pos;
	
	public boolean active;
	
	public Tick (String picStr, String greyPicStr, boolean _active, int posx, int posy)
	{
		pos = new Point(posx, posy);
		
		
		pic = new Sprite(Game.manager.getSprite(picStr).getImage());
		pic.setPosition(pos.x, pos.y);
		
		greyPic = new Sprite(Game.manager.getSprite(greyPicStr).getImage());
		greyPic.setPosition(pos.x, pos.y);
		
		active = _active;
	}
	
	public void draw(Graphics2D g)
	{
		if (active)
		{
			pic.draw(g);
		}
		else
		{
			greyPic.draw(g);
		}
	}
	
	public void move (int x, int y)
	{
		pos = new Point(x, y);
		pic.setPosition(x, y);
		greyPic.setPosition(x, y);
	}
}
