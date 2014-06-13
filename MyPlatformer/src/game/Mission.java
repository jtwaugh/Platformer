package game;
import graphics.Sprite;
import gui.Button;

import java.awt.Graphics2D;
import java.awt.Point;
import java.io.IOException;

import resource.ScriptNode;


public class Mission
{
	public String name;
	
	protected Level level;
	protected Button btn;
	protected ScriptNode requirements;
	
	public int[] rewards; // 0 food, 1 water, 2 power, 3 industry, 4 pollution, 5 unrest
	
	public Mission(String _name, String btnGraphic, Point pos, ScriptNode _requirements, int[] _rewards) throws IOException
	{
		name = _name;
		level = Game.manager.getLevel(name);
		btn = new Button(pos, btnGraphic, btnGraphic + "Clicked");
		requirements = _requirements;
		rewards = _rewards;
	}
	
	public void drawButton(Graphics2D g)
	{
		btn.draw(g);
	}
	
	public boolean buttonClicked()
	{
		boolean b =  btn.getClicked();
		
		if (b)
		{
			btn.setClicked(false);
		}
		
		return b;
	}
	
	public Level getLevel()
	{
		return level;
	}
	
	public Button getPic()
	{
		return btn;
	}

	public ScriptNode getReqs()
	{
		return requirements;
	}
}
