package ogpc.earth2300.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import ogpc.earth2300.game.Game;
import ogpc.earth2300.graphics.Sprite;
import ogpc.earth2300.math.Geometry;

public class ChoiceNode extends FrameInterface
{
	private String text;
	public Point pos;
	private Point realPos;
	public boolean clicked;
	
	public ChoiceNode(int _width, int _height, Point _pos, String frameName, String _text)
	{
		super(_width, _height, frameName);
		pos = _pos;
		clicked = false;
		createText(_text);
		text = _text;
	}
	
	private void createText(String txt)
	{
		BufferedImage text = new BufferedImage(width - 20, 16, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D t = (Graphics2D) text.getGraphics();
		t.setColor(new Color(0, 0, 0));
		t.setFont(Game.textFont);
		t.drawString(txt, 0, t.getFontMetrics().getAscent());
		t.dispose();
		
		Sprite textSprite = new Sprite(text);
		
		int x = (width+side.getWidth())/2 - t.getFontMetrics().stringWidth(txt)/2;
		int y = height/2 - t.getFontMetrics().getAscent()/2;
		
		textSprite.move(x, y);
		
		picArray.add(textSprite);
	}

	public void draw(Graphics2D g, Point c)
	{
		super.draw(g, c);
	}
	
	public void mousePressHandle(MouseEvent e)
	{
		
	}
	
	public boolean mouseReleaseHandle(MouseEvent e, Point parentPos)
	{
		if (Geometry.pointInQuad(Game.mousePos, new Point(pos.x + parentPos.x, pos.y + parentPos.y), new Point(parentPos.x + pos.x + width, parentPos.y + pos.y + height)))
		{
			clicked = true;
			System.out.println(text + " button pressed.");
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public String name()
	{
		return text;
	}
}
