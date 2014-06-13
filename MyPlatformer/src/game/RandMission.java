package game;

import java.awt.Point;
import java.io.IOException;

import resource.ScriptNode;


public class RandMission extends Mission
{
	public double weight;
	private boolean active;

	public RandMission(String _name, String btnGraphic, ScriptNode _requirements, int[] _rewards, double _weight) throws IOException
	{
		super(_name, btnGraphic, new Point(100,100), _requirements, _rewards);
		weight = _weight;
		active = false;
	}

	public void activate()
	{
		active = true;	
	}
	
	public void randomize()
	{
		int x = (int)(Math.random() * 800);
		int y = (int) (Math.random() * 500 + 100);
		btn.setPosition(x, y);
		
		System.out.println(x + ", " + y);
	}
	
	public void deactivate()
	{
		active = false;
	}
	
	public boolean getActive()
	{
		return active;
	}
}
