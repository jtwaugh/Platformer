package gui;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public class TickBar extends MyInterface
{
	private ArrayList<Tick> ticks; 
	private Point pos;
	String pic;
	String greyPic;
	
	
	public TickBar(Point _pos, String _pic, String _greyPic, int size)
	{
		super(0, 0);
		
		pos = _pos;
		pic = _pic;
		greyPic = _greyPic;
		
		ticks = new ArrayList<Tick>();
		
		for (int q = 0; q < size; q++)
		{
			ticks.add(new Tick(pic, greyPic, true, pos.x+q*10, pos.y));
		}
	}
	
	public void draw(Graphics2D g)
	{
		for (int c = 0; c < ticks.size(); c++)
		{
			ticks.get(c).draw(g);
		}
	}
	
	public void reduce (int q)
	{
		int strat = ticks.size();
		
		for (int e = 0; e < ticks.size(); e++)
		{
			if (ticks.get(e).active)
			strat = e;
		}
		
		for (int f = 0; f <= q; f++)
		{
			ticks.get(ticks.size() - strat).active = false;
		}
	}
	
	public void setSize(int q)
	{
		for (int e = 0; e < ticks.size(); e++)
		{
			ticks.get(e).active = false;
		}
		
		if (q > ticks.size())
		{
			ticks = new ArrayList<Tick>();
			
			for (int r = 0; r < q; r++)
			{
				ticks.add(new Tick(pic, greyPic, true, pos.x+q*10, pos.y));
			}
		}
		else
		{
			for (int f = 0; f < q; f++)
			{	
				ticks.get(f).active = true;
			}
		}
		
	}
	
	public void move (int x, int y)
	{
		for (int q = 0; q < ticks.size(); q++)
		{
			ticks.get(q).move(x, y);
		}
	}
}
