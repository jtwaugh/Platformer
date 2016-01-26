package ogpc.earth2300.gui;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Window extends FrameInterface
{
	protected boolean open;
	protected boolean close;
	
	protected Point pos;
	
	public Window(int _width, int _height, String frameName, Point _pos) 
	{
		super(_width, _height, frameName);
		close = false;
		open = false;
		pos = _pos;
	}
	
	public void update()
	{
		
	}
	
	public void setPosition(int x, int y)
	{
		pos = new Point(x, y);
	}
	
	public void keyPressHandle(KeyEvent e) 
	{

	}

	public void keyReleaseHandle(KeyEvent e) 
	{
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			setClosed(true);
		}
	}
	
	public void mousePressHandle(MouseEvent m) 
	{
		
	}
	
	public boolean mouseReleaseHandle(MouseEvent m) 
	{
		return false;
	}

	public void setOpened(boolean b)
	{
		open = b;
	}
	//
	public boolean getOpened()
	{
		return open;
	}
	
	public void setClosed(boolean b)
	{
		close = b;
	}
	
	public boolean getClosed()
	{
		return close;
	}
	
	public void draw(Graphics2D g)
	{
		super.draw(g, pos);
	}
	
	public String getExitChoice()
	{
		return "";
	}
	
	public void reset()
	{
		
	}

	public int getExitIndex() 
	{
		return -1;
	}
}
