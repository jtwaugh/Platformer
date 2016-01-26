package ogpc.earth2300.gui;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

public class ChoiceInterface extends MessageBox
{
	protected ArrayList<ChoiceNode> choices;
	protected String exitChoice;
	protected int exitIndex;
	
	protected ChoiceInterface(int _width, int _height, String frameName, String _text, Point _pos)
	{
		super(_width, _height, frameName, _text, _pos);
	}
	
	public ChoiceInterface(int _width, int _height, String frameName, String choiceFrameName, String _text, Point _pos, String[] options)
	{
		super(_width, _height, frameName, _text, _pos);
		
		choices = new ArrayList<ChoiceNode>();
		
		for (int q = 0; q < options.length; q++)
		{
			choices.add(new ChoiceNode(width - 20, 30, new Point(15, height - 40*(q+1)), choiceFrameName, options[q]));
		}
		
		exitChoice = "";
		exitIndex = -1;
	}
	
	public void draw(Graphics2D g)
	{
		super.draw(g);
		
		int z = 0;
		
		for (ChoiceNode q : choices)
		{
			q.draw(g, new Point(pos.x + width/2 - (q.width - q.side.getWidth())/2, pos.y + height - end.getHeight()*2 - (q.end.getHeight() + q.height) * z));
			z++;
		}
	}
	
	public void update()
	{
		for (ChoiceNode q : choices)
		{
			if (q.clicked)
			{
				exitChoice = q.name();
				exitIndex = choices.indexOf(q);
				q.clicked = false;
				close = true;
				open = false;
			}
		}
	}
	
	public String getExitChoice()
	{
		return exitChoice;
	}
	
	public int getExitIndex()
	{
		return exitIndex;
	}
	
	public void reset()
	{
		exitChoice = "";
	}
	
	@Override
	public void keyReleaseHandle(KeyEvent e) 
	{
		// Do nothing
	}
	
	public boolean mouseReleaseHandle(MouseEvent e)
	{
		if (open)
		{
			for (ChoiceNode o : choices)
			{
				if (o.mouseReleaseHandle(e, pos))
				{
					return true;
				}
			}
		}
		return false;
	}
}
