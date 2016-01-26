package ogpc.earth2300.gui;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

public class DayLabel
{
	private int day;
	private Point pos;		// of center
	private String fontName;
	private String prefix;
	
	public DayLabel(String _prefix, String _fontName, int x, int y)
	{
		day = 0;
		pos = new Point(x, y);
		prefix = _prefix;
		fontName = _fontName;
	}
	
	public void draw(Graphics g)
	{
		g.setFont(new Font(fontName, Font.BOLD, 16));
		g.drawString((prefix + " "  + day), pos.x - g.getFontMetrics().stringWidth((prefix + " "  + day)), g.getFontMetrics().getAscent());
	}
	
	public void increment()
	{
		day++;
	}
}
