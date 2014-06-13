package visible;

import game.Game;
import graphics.Sprite;

import java.awt.Color;
import java.io.IOException;

public class Scenery extends Block
{
	public String name;
	
	private int hitpoints;
	
	public boolean colliding;
	
	public String topColl;	// Registered in collision detection
	public String botColl;	// Registered in collision detection
	public String sideColl;	// Registered in collision detection
	public String action;
	public String damage;
	public String death;
	
	public Scenery (String[] strs) throws IOException
	{
		super(Game.manager.getSprite(strs[1]), Boolean.parseBoolean(strs[3]), new Color(Integer.parseInt(strs[2].replace(" ", "").split(",")[0]),Integer.parseInt(strs[2].replace(" ", "").split(",")[1]),Integer.parseInt(strs[2].replace(" ", "").split(",")[2])), 0);
		name = new String(strs[0]);
		topColl = strs[4];
		botColl = strs[5];
		sideColl = strs[6];
		action = strs[7];
		damage = strs[8];
		death = strs[9];
		
		colliding = false;
	}
	
	public Scenery (Scenery that, int x, int y)
	{
		c  = new Color(that.c.getRed(), that.c.getGreen(), that.c.getBlue(),that.c.getAlpha());
		pic = new Sprite(that.pic.getImage(), x, y);
		topColl = that.topColl;
		botColl = that.botColl;
		sideColl = that.sideColl;
		action = that.action;
		damage = that.damage;
		death = that.death;
		
		solid = that.isSolid();
		
		colliding = false;
	}
	
	public String toString()
	{
		return (c.getRed() + ", " + c.getGreen() + ", " + c.getBlue() + ", " + c.getAlpha());
	}
	
	public void transform(Scenery that, int x, int y)
	{
		c  = new Color(that.c.getRed(), that.c.getGreen(), that.c.getBlue(),that.c.getAlpha());
		pic = new Sprite(that.pic.getImage(), x, y);
		topColl = that.topColl;
		botColl = that.botColl;
		sideColl = that.sideColl;
		action = that.action;
		damage = that.damage;
		death = that.death;
		
		solid = that.isSolid();
	}
}
