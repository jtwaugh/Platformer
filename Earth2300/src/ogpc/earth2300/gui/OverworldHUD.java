package ogpc.earth2300.gui;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JLabel;

import ogpc.earth2300.game.Game;
import ogpc.earth2300.game.Overworld;
import ogpc.earth2300.graphics.Sprite;

public class OverworldHUD extends MyInterface
{
	private Sprite bg;
	
	private HUDCounter food;
	private HUDCounter water;
	private HUDCounter power;
	private HUDCounter industry;
	
	private DayLabel day;
	
	private Button forward;
	private Button zoomout;
	
	private boolean zoomed;

	private HUDCounter population;
	private HUDCounter qol;
	private HUDCounter expectedQol;
	private HUDCounter unrest;
	
	public OverworldHUD(int _width, int _height) throws IOException
	{
		super(_width, _height);
		
		bg = Game.manager.getSprite("HUD");
		bg.setPosition(0, 0);
		
		food = new HUDCounter(new Point(630, 2), "food", 0);
		water = new HUDCounter(new Point(660, 2), "water", 0);
		power = new HUDCounter(new Point(690, 2), "power", 0);
		population = new HUDCounter(new Point(720, 2), "population", 0);
		industry = new HUDCounter(new Point(750, 2), "industry", 0);
		
		qol = new HUDCounter(new Point(50, 2), "qol", 0);
		expectedQol = new HUDCounter(new Point(80, 2), "eqol", 0);
		unrest = new HUDCounter(new Point(110, 2), "unrest", 0);
		
		forward = new Button(new Point(20, 5), "forwardArrow", "forwardArrowClicked");
		zoomout = new Button(new Point(20, 5), "zoomoutArrow", "zoomoutArrowClicked");
		
		zoomed = false;
		
		pos = new Point(0, 0);
	
		day = new DayLabel("Day", "sansserif", bg.getWidth()/2, 2);
	}
	
	public void render(Graphics2D g)
	{	
		bg.draw(g);
		food.drawAlt(g);
		water.drawAlt(g);
		power.drawAlt(g);
		population.drawAlt(g);
		industry.drawAlt(g);
		qol.drawAlt(g);
		expectedQol.drawAlt(g);
		unrest.drawAlt(g);
		day.draw(g);
		
		if (zoomed)
		{
			zoomout.draw(g);
		}
		else
		{
			forward.draw(g);
		}
	}

	public void update(Overworld welt)
	// Called each frame
	{
		food.setC(welt.food);
		water.setC(welt.water);
		power.setC(welt.power);
		population.setC(welt.population);
		industry.setC(welt.industry);
		
		qol.setC(welt.getQualityOfLife());
		expectedQol.setC(welt.getExpectedQOL());
		
		double n = welt.getUnrest();
		
		if (n > 99)
		{
			n = 99;
		}
		
		unrest.setC(n);
	}	
	
	public void dayPlusPlus()
	{
		day.increment();
	}
	
	public void setZoomed(boolean b)
	{
		zoomed = b;
	}
	
	public void mousePressHandle(MouseEvent e)
	{
		if (zoomed)
		{
			zoomout.mousePressHandle(pos, e, 1, 0, 0);
		}
		else
		{
			forward.mousePressHandle(pos, e, 1, 0, 0);
		}
	}
	
	public void mouseReleaseHandle(MouseEvent e)
	{
		if (zoomed)
		{
			zoomout.mouseReleaseHandle(e);
		}
		else
		{
			forward.mouseReleaseHandle(e);
		}
	}
	
	public boolean shouldZoomOut()
	{
		boolean b = (zoomout.getClicked());
		if (b)
		{
			zoomout.setClicked(false);
			return true;
		}
		else return false;
	}
	
	public boolean shouldGoForward()
	{
		boolean b = (forward.getClicked());
		if (b)
		{
			forward.setClicked(false);
			return true;
		}
		else return false;
	}
}
