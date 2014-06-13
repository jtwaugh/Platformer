package gui;

import java.awt.Graphics2D;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;


public class Inventory extends SlotGridInterface
{

	private ArrayList<ArrayList<InvSlot>> slots;
	
	
	public Inventory(int _width, int _height, String frameName)
	{
		super(_width, _height, frameName);
	}
	
	private void createSlots()
	{
		int dx = slotX + borderWidth;
		int dy = slotY + borderWidth;
		
		Point pos = new Point(xGap, yGap);
		
		for (int y = 0; y < gridY; y++)
		{	
			pos.y =yGap + borderWidth + (y * dy);
			
			slots.add(new ArrayList<InvSlot>());
			
			for (int x = 0; x < gridX; x++)
			{
				pos.x = xGap + borderWidth + (x * dx);
				
				slots.get(y).add(new InvSlot(slotColor, new Point(pos.x, pos.y), slotX, slotY));
			}
		}
	}
	
	public void draw (Graphics2D g)
	{
		Graphics2D g2d = (Graphics2D)(g);
		
		g2d.setColor(bgColor);
		g2d.fillRoundRect(pos.x, pos.y, width, height, 5, 5);
		
		for (int y = 0; y < gridY; y++)
		{	
			for (int x = 0; x < gridX; x++)
			{
				slots.get(y).get(x).draw(g);
			}
		}
		
	}
}