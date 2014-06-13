package gui;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;


public class Slot
{
	private Color bgColor;
	private Point pos;
	private int width;
	private int height;
	
	public Slot (Color _bgColor, Point _pos, int _width, int _height) 
	{
		bgColor = _bgColor;
		pos = _pos;
		width = _width;
		height = _height;
	}
	
	public void draw (Graphics2D g)
	{
		g.setColor(bgColor);
		g.fillRoundRect(pos.x, pos.y, width, height, 3, 3);
	}
}

class InvSlot extends Slot
{
	public InvSlot(Color _bgColor, Point pos, int _width, int _height)
	{
		super(_bgColor, pos, _width, _height);
	}
}