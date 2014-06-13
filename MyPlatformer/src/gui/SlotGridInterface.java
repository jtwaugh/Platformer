package gui;

import java.awt.Color;
import java.util.ArrayList;

public class SlotGridInterface extends FrameInterface
{
	protected int slotX;
	protected int slotY;
	protected int gridX;
	protected int gridY;

	protected int borderWidth;
	
	protected Color slotColor;
	protected Color bgColor;
	private ArrayList<ArrayList<Slot>> slots;
	
	public SlotGridInterface(int _width, int _height, String frameName)
	{
		super(_width, _height, frameName);
	}
	
//	public SlotGridInterface (int gameWidth, int gameHeight, int _xGap, int _yGap, int _gridX, int _gridY, int _borderWidth, Color _slotColor, Color _bgColor)
//	{
//		super (_bgColor);
//
//		gridX = _gridX;
//		gridY = _gridY;
//		borderWidth = _borderWidth;
//		
//		slotColor = _slotColor;
//		
//		slotX = (width - (borderWidth * (gridX + 1))) / gridX;
//		slotY = (height - (borderWidth * (gridY + 1))) / gridY;
//		
//		width = (gridX * slotX) + (borderWidth * (gridX + 1));
//		height = (gridY * slotY) + (borderWidth * (gridY + 1));
//		
//		yGap = (gameWidth - width)/2;
//		xGap = (gameHeight - height)/2;
//		
//		pos = new Point(xGap, yGap);
//		
//	}
	
//	private void createSlots()
//	{
//		int dx = slotX + borderWidth;
//		int dy = slotY + borderWidth;
//		
//		Point pos = new Point(xGap, yGap);
//		
//		for (int y = 0; y < gridY; y++)
//		{	
//			pos.y =yGap + borderWidth + (y * dy);
//			
//			slots.add(new ArrayList<Slot>());
//			
//			for (int x = 0; x < gridX; x++)
//			{
//				pos.x = xGap + borderWidth + (x * dx);
//				
//				slots.get(y).add(new Slot(slotColor, new Point(pos.x, pos.y), slotX, slotY));
//			}
//		}
//	}
	
//	public void draw (Graphics g)
//	{
//		super.draw(g, new Point(0,0));
//		
//		for (int y = 0; y < gridY; y++)
//		{	
//			for (int x = 0; x < gridX; x++)
//			{
//				slots.get(y).get(x).draw(g);
//			}
//		}
//		
//	}
	
}