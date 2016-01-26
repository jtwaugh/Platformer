package ogpc.earth2300.visible;

import java.awt.Graphics2D;

import ogpc.earth2300.game.Game;
import ogpc.earth2300.graphics.Sprite;

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
