package ogpc.earth2300.game;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;

import ogpc.earth2300.gui.ChoiceInterface;
import ogpc.earth2300.gui.PauseMenu;
import ogpc.earth2300.resource.FileReader;

public class OverworldState extends State
{
	private Overworld currentWorld;
	
	private PauseMenu pauseMenu;
	
	public OverworldState() throws IOException
	{
		FileReader r = new FileReader();
		currentWorld = r.parseWorld(r.drive + "overworld/portland.txt");
		
		String[] choices = {"Return to Game", "Save Game", "Title Screen", "Quit"};
		pauseMenu = new PauseMenu(200, 300, "robo frame", "robo frame small", "Menu", new Point(250, 100), choices);

	}
	
	protected void loop(Graphics2D BGDraw, Graphics2D gameDraw, Graphics2D infDraw)
	{
		currentWorld.loop(BGDraw, gameDraw, infDraw);
		if (pauseMenu.getOpened())
		{
			pauseMenu.draw(infDraw);
		}
	}

	protected void keyPressHandle(KeyEvent e)
	{
		currentWorld.keyPressHandle(e);
	}
	
	protected void keyReleaseHandle(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_P)
		{
			pauseMenu.setOpened(!pauseMenu.getOpened());
		}
		else
		{
			if (pauseMenu.getOpened())
			{
				return;
			}
			currentWorld.keyReleaseHandle(e);
		}
	}
	
	protected void mousePressHandle(MouseEvent e)
	{
		currentWorld.mousePressHandle(e);
	}
	
	protected void mouseReleaseHandle(MouseEvent e)
	{
		if (pauseMenu.getOpened())
		{
			pauseMenu.mouseReleaseHandle(e);
		}
		else
		{
			currentWorld.mouseReleaseHandle(e);
		}
	}
}
