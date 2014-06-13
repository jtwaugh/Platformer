package visible;

import game.Game;
import graphics.Sprite;

import java.awt.Graphics2D;

public class Effect
{
	private Sprite pic;
	
	public Effect(String spriteName)
	{
		pic = Game.manager.getSprite(spriteName);
	}
	
	public void draw(Graphics2D g)
	{ 
		pic.draw(g);
	}
	
	public void setPos(int x, int y)
	{
		pic.setPosition(x, y);
	}
}
