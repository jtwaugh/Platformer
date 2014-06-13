package gui;

import game.Game;
import graphics.Sprite;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import math.Geometry;

public class Button extends MyInterface
{
	private Sprite pic;
	private Sprite pressedPic;
	private boolean pressed;
	private boolean willAct;
	
	public Button(Point _pos, String picString, String ppString)
	{
		super(Game.manager.getSprite(picString).getWidth(), Game.manager.getSprite(picString).getHeight());
		pic = Game.manager.getSprite(picString);
		pressedPic = Game.manager.getSprite(ppString);
		pressed = false;
		willAct = false;
		pos = _pos;
	}
	
	public boolean getClicked()
	{
		return willAct;
	}
	
	public void setClicked(boolean b)
	{
		willAct = b;
	}
	
	public void mousePressHandle(Point parentPos, MouseEvent e, int s, int x, int y)
	{
		Point myPt = new Point((Game.mousePos.x - x)/s, (Game.mousePos.y - y)/s);
		if (Geometry.pointInQuad(myPt, new Point(pos.x + parentPos.x, pos.y + parentPos.y), new Point(parentPos.x + pos.x + width, parentPos.y + pos.y + height)))
		{
			pressed = true;
		}
	}

	public boolean mouseReleaseHandle(MouseEvent e)
	{
		if (pressed)
		{
			pressed = false;
			willAct = true;
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void draw(Graphics2D g)
	{
		g.translate(pos.x, pos.y);
		
		if (pressed)
		{
			pressedPic.draw(g);
		}
		else
		{
			pic.draw(g);
		}
		
		g.translate(-pos.x, -pos.y);
	}
}
