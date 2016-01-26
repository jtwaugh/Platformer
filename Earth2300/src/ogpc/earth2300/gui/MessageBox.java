package ogpc.earth2300.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import ogpc.earth2300.game.Game;
import ogpc.earth2300.graphics.Sprite;

public class MessageBox extends Window
{
	
	public MessageBox(int _width, int _height, String graphicName, String text, Point _pos)
	{
		super(_width, _height, graphicName, _pos);
		
		createText(text);
	}
	
	private void createText(String _text)
	{
		int w = side.getWidth();
		int h = end.getHeight();

		BufferedImage text = new BufferedImage(width - w, height - h, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D t = (Graphics2D) text.getGraphics();
		t.setColor(new Color(0, 0, 0));
		t.setFont(Game.textFont);
		
		int charW = 0;
		int c = 0;
		int d = 0;
		
		int row = 0;
		int rowSize = width - 2*w;
		int rowUsed = 0;
		
		String myStr = "";
		
		boolean end = false;
		
		_text += "\n";
		
		while (!end)
		{
			while(rowUsed < rowSize)
			{
				if (c >= _text.length())
				{
					end = true;
					c--;
					break;
				}
				
				charW = t.getFontMetrics().charWidth(_text.charAt(c));
				
				rowUsed += charW;
				
				c++;
			}
			
     			rowUsed = 0;
			
			for (int e = c; e > d; e--)
			{
				if (_text.charAt(e) == ' ')
				{
					c = e;
					break;
				}
			}
			
			myStr = _text.substring(d, c);
			
			d = c;
			
			t.drawString(myStr, (width-w)/2 - t.getFontMetrics().stringWidth(myStr)/2, (row+1)*t.getFontMetrics().getAscent());
			row++;
		}
		
		myStr = _text.substring(c, _text.length()-1);
		
		t.drawString(myStr, (width-w)/2 - t.getFontMetrics().stringWidth(myStr)/2, (row+1)*t.getFontMetrics().getAscent());
		
		t.dispose();
			
		picArray.add(new Sprite(text, w, h));
	}

	
}
	