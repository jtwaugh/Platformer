package game;

import graphics.Sprite;
import gui.Button;
import gui.ChoiceNode;
import gui.MessageBox;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

import resource.FileReader;

public class TitleScreen extends State
{
	private ArrayList<ChoiceNode> choices;
	
	private Sprite logo;
	private Sprite bg;
	
	private MessageBox controls;
	
	public TitleScreen() throws IOException
	{
		choices = new ArrayList<ChoiceNode>();
		choices.add(new ChoiceNode (100, 50, new Point(0,0), "robo frame small", "New Game"));
		choices.add(new ChoiceNode (100, 50, new Point(0,0), "robo frame small", "Controls"));
		choices.add(new ChoiceNode (100, 50, new Point(0,0), "robo frame small", "Credits"));
		choices.add(new ChoiceNode (100, 50, new Point(0,0), "robo frame small", "Terminate"));
		
		logo = new Sprite(FileReader.drive + "sprites/interface/logo.png");
		logo.setPosition(150, 50);
		
		bg = new Sprite(FileReader.drive + "/sprites/interface/bg.jpg");
		bg.setPosition(0,0);
		
		controls = new MessageBox(300, 200, "robo frame", "WASD to move, spacebar to interact, click to shoot. Q and E to change weapons. Press spacebar to close this message.", new Point (250, 300));
	}
	
	public void loop(Graphics2D g1, Graphics2D g, Graphics2D g2)
	{
		getEvents();
		render(g);
	}
	
	private void getEvents()
	{	
		if (controls.getClosed())
		{
			controls.setOpened(false);
			controls.setClosed(false);
		}
		
		for (int i = 0; i < choices.size(); i++)
		{
			if (choices.get(i).clicked)
			{
				choices.get(i).clicked = false;
				
				switch (i)
				{
				case 0:
				{
					Game.command = "overworld";
					break;
				}
				case 1:
				{
					controls.setOpened(true);
					break;
				}
				case 3:
				{
					System.exit(0);
					break;
				}
				}
			}
		}
	}
	
	private void render(Graphics2D g)
	{
		bg.draw(g);
		logo.draw(g);
		for (int i = 0; i < choices.size(); i++)
		{
			choices.get(i).draw(g, new Point(125 + 150*i, 400));
		}
		
		if (controls.getOpened())
		{
			controls.draw(g);
		}
	}
	
	public void mouseReleaseHandle(MouseEvent e)
	{
		for (int i = 0; i < choices.size(); i++)
		{
			choices.get(i).mouseReleaseHandle(e, new Point(125 + 150*i, 400));
		}
	}
	
	public void keyReleaseHandle(KeyEvent e)
	{
		controls.keyReleaseHandle(e);
	}
}
