package game;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.HashMap;

import entity.Player;

public class Level
{
	public String name;
	public String title;
	
	public String description;
	
	private Player steve;
	
	private HashMap<String, Map> maps;
	private Map currentMap;
	public boolean complete;
	
	public Level(String _name, String _title, String desc) throws IOException
	{
		name = _name;
		title = _title;
		description = desc;
		maps = new HashMap<String, Map>();
		complete = false;
		
		// Hardcode
		steve = new Player("robot2_concept", 1.0, .2, 20);
		steve.pic.setPosition(100, 100);
		steve.addWeapon("Rock");
		steve.addWeapon("Debug Laser");
	}
	
	public void initOnMap()
	{
		currentMap.spawnPlayer(steve);
		currentMap.generateInterface();
	}
	
	public void loop(Graphics2D BGDraw, Graphics2D gameDraw, Graphics2D infDraw)
	{
		currentMap.loop(BGDraw, gameDraw, infDraw);
	}
	
	public void changeMap(String mapName)
	{
		currentMap = maps.get(mapName);
		initOnMap();
	}
	
	public void addMap(String name, Map myMap)
	{
		maps.put(name, myMap);
	}
	
	protected void keyPressHandle(KeyEvent e)
	{
		currentMap.keyPressHandle(e);
	}
	
	protected void keyReleaseHandle(KeyEvent e)
	{
		currentMap.keyReleaseHandle(e);
	}
	
	protected void mousePressHandle(MouseEvent e)
	{
		currentMap.mousePressHandle(e);
	}
	
	protected void mouseReleaseHandle(MouseEvent e)
	{
		currentMap.mouseReleaseHandle(e);
	}

}
