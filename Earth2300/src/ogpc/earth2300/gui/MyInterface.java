package ogpc.earth2300.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;


public class MyInterface
{
	protected Point pos;
	
	protected int width;
	protected int height;
	
	protected int xGap;
	protected int yGap;
	
	protected Color bgColor;
	
	public MyInterface(int _width, int _height)
	{
		width = _width;
		height = _height;
	}
	
	public void draw(Graphics2D g)
	{
		
	}
	
	public void setPosition(int x, int y)
	{
		pos = new Point(x, y);
	}
}