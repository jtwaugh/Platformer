package game;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class LevelState extends State
{
	private Level currentLevel;
	
	public LevelState() throws IOException
	{
		super();
		//currentLevel = Game.manager.getLevel("Test Level");
	}
	
	public void setLevel(String levelName) throws IOException
	{
		currentLevel = Game.manager.getLevel(levelName);
	}
	
	protected void loop(Graphics2D BGDraw, Graphics2D gameDraw, Graphics2D infDraw)
	{
		currentLevel.loop(BGDraw, gameDraw, infDraw);
	}
	
	protected void keyPressHandle(KeyEvent e)
	{
		currentLevel.keyPressHandle(e);
	}
	
	protected void keyReleaseHandle(KeyEvent e)
	{
		currentLevel.keyReleaseHandle(e);
	}
	
	protected void mousePressHandle(MouseEvent e)
	{
		currentLevel.mousePressHandle(e);
	}
	
	protected void mouseReleaseHandle(MouseEvent e)
	{
		currentLevel.mouseReleaseHandle(e);
	}
}
