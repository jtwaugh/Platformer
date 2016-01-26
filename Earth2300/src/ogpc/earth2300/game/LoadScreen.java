package ogpc.earth2300.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;

import ogpc.earth2300.graphics.Sprite;
import ogpc.earth2300.gui.LoadingBar;
import ogpc.earth2300.resource.FileReader;

public class LoadScreen extends State
{
	private LoadingBar loadBar;
	private int q;
	
	private Sprite logo;
	private Sprite bg;
	
	private String s;
	
	public LoadScreen() throws IOException
	{
		Sprite l = new Sprite(FileReader.drive + "sprites/interface/Loaded Bar.png");
		Sprite u = new Sprite(FileReader.drive + "sprites/interface/Loading Bar.png");
		q = 0;
		
		s = "Loading...";
		
		loadBar = new LoadingBar(new Point(50, 450), u, l);
		
		logo = new Sprite(FileReader.drive + "sprites/interface/logo.png");
		logo.setPosition(150, 50);
		
		bg = new Sprite(FileReader.drive + "sprites/interface/bg.jpg");
		bg.setPosition(0,0);
	}
	
	protected void loop(Graphics2D BGDraw, Graphics2D gameDraw, Graphics2D infDraw)
	{
		try 
		{
			q = Game.manager.loadNext();
			
			switch (q)
			{
			case 0:
			{
				s = "Rasterizing graphics...";
				break;
			}
			case 1:
			{
				s = "Performing cubistic renditions...";
				break;
			}
			case 2:
			{
				s = "Moving moving parts...";
				break;
			}
			case 3:
			{
				s = "Charging lazers...";
				break;
			}
			case 4:
			{
				s = "Dealing arms...";
				break;
			}
			case 5:
			{
				s = "Preparing interface...";
				break;
			}
			case 6:
			{
				s = "Terraforming...";
				break;
			}
			case 7:
			{
				s = "Photographing panoramas...";
				break;
			}
			case 8:
			{
				s = "Loading administrative controls...";
				break;
			}
			case 9:
			{
				s = "Loading general controls...";
				break;
			}
			case 10:
			{
				Game.command = "title screen";
				break;
			}
			}
			
			Thread.sleep(0);
		} 
		catch (IOException e)
		{
		
		} 
		catch (InterruptedException e)
		{

		}
		
		render(gameDraw);
	}
	
	private void render(Graphics2D g)
	{
		bg.draw(g);
		loadBar.draw(q, g);
		g.setColor(Color.WHITE);
		g.drawString(s, 400 - g.getFontMetrics().stringWidth(s)/2, 400);
		logo.draw(g);
	}
}
