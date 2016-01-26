package ogpc.earth2300.gui;

import java.awt.Graphics2D;
import java.awt.Point;

import ogpc.earth2300.graphics.Sprite;

public class LoadingBar
{
	private Sprite unloaded;
	private Sprite loaded;
	
	public LoadingBar(Point pos, Sprite u, Sprite l)
	{
		unloaded = new Sprite(u.getImage());
		unloaded.setPosition(pos.x, pos.y);
		
		loaded = new Sprite(l.getImage());
		loaded.setPosition(pos.x, pos.y);
	}
	
	public void draw(int q, Graphics2D g)
	{
		unloaded.draw(g);
		
		Sprite s = new Sprite(loaded);
		
		int w = s.getImage().getWidth();
		int h = s.getImage().getHeight();
		
		s = new Sprite(s.getImage().getSubimage(0, 0, (w*q)/10, h));
		s.setPosition(unloaded.getPosition().x, unloaded.getPosition().y);
		s.draw(g);
	}
}
