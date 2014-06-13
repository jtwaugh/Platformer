package game;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import math.BlockCollision;
import math.Collision;
import math.Timer;
import entity.Character;
import entity.Entity;
import entity.Player;
import entity.Projectile;
import graphics.Backdrop;
import gui.HUD;
import gui.Window;
import ai.AIManager;
import resource.ScriptEvent;
import resource.ScriptNode;
import visible.Block;
import visible.Effect;
import visible.Scenery;


public class Map
{
	public String name;
	
	public Level owner;
	
	public int xSize;
	public int ySize;
	
	public Point spawn;
	
	public static Point cameraPos;
	
	public static ArrayList<Projectile> projArray;
	public static ArrayList<Entity> entityArray;
	public static ArrayList<Effect> effectArray;
	
	private LevelEventManager events;
	private LevelScriptManager script;
	private AIManager ai;
	
	public SceneryManager sceneryMgr;
	
	protected Backdrop bg;
	
	public HashMap<String, ScriptEvent> scriptEvents;
	public HashMap<String, Window> windows;
	public HashMap<String, entity.Character> characters;
	public HashMap<String, Timer> timers; 
	
	private ArrayList<Window> windowQueue;
	
	public final static double gravity = .5;
	
	public ArrayList<ArrayList<Block>> tiles;
	public ArrayList<ArrayList<Scenery>> scenery;
	
	public Player steve;
	
	private HUD hud;
	
	public Map (String _name, String _bg, ArrayList<ScriptNode> _script, Level _owner, Point _spawn) throws IOException
	{
		name = _name;
		entityArray = new ArrayList<Entity> ();
		projArray = new ArrayList<Projectile> (); 
		characters = new HashMap<String, entity.Character> ();
		timers = new HashMap<String, Timer> ();
		cameraPos = new Point(0,0);
		tiles = new ArrayList<ArrayList<Block>> ();
		effectArray = new ArrayList<Effect>();
		scenery = new ArrayList<ArrayList<Scenery>> ();
		windows = new HashMap<String,Window> ();
		script = new LevelScriptManager(this);
		events = new LevelEventManager(this);
		sceneryMgr = new SceneryManager(this);
		ai = new AIManager(this);
		windowQueue = new ArrayList<Window>();
		scriptEvents = new HashMap<String, ScriptEvent>();
		script.parseScript(_script);
		owner = _owner;
		
		spawn = _spawn;
		
		bg = Game.manager.getBG(_bg);
		
		hardcodeZone();
	}
	
	private void hardcodeZone()
	{
	}
	
	public void loop(Graphics2D BGDraw, Graphics2D gameDraw, Graphics2D infDraw)
	{	
		events.getEvents();
		
		logic();
		
		bg.draw(BGDraw);
		
		renderGame(gameDraw);

		renderEffects(gameDraw);

		renderInterface(infDraw);
	}
	
	private void renderBackdrop(Graphics2D g)
	{
		if (bg != null)
		{
			bg.draw(g);
		}
	}
	
	private void renderGame(Graphics2D g)
	{	
		g.translate(cameraPos.x, cameraPos.y);
		 
		panCamera(g);

		drawBlocks(g);
		
		drawScenery(g);
		
		drawEntities(g);
		
		drawProjectiles(g);
	}
	
	private void renderInterface(Graphics2D g)
	{
		hud.render(g);
		
		for (Window w : windowQueue)
		{
			w.draw(g);
		}
	}
	
	public void renderEffects(Graphics2D g)
	{
		for (Effect e : effectArray)
		{
			e.draw(g);
		}
	}
	
	public void logic()
	{
		respondToPlayerInput();
		
		ai.AI();
		
		actEntities();
		
		checkCollisions();
		
		moveProjectiles();
		
		moveEntities();
		
		updateInterface();
	}
	
	public void getSize()
	{
		xSize = tiles.get(0).size();
		ySize = tiles.size();
	}
	
	public boolean isOffScreen(Point pos)
	{
		return (pos.x < 0 || pos.x > xSize*20 || pos.y < 0 || pos.y > ySize*20);
	}
	
	public void spawnPlayer (Player _steve)
	{
		steve = _steve;
		steve.pic.setPosition(spawn.x, spawn.y);
	}
	
	public void importPlayer(Map lvl)
	{
		steve = lvl.getPlayer();
	}
	
	public Player getPlayer()
	{
		return steve;
	}
	
	public void generateInterface()
	{
		hud = new HUD(800, 600, steve);
	}
	
	public boolean gameOver()
	{
		return (steve.vitality < 0);
	}
	
	private void updateInterface()
	{
		hud.update(steve);
		
		windowQueue.clear();
		for (String w : windows.keySet())
		{
			if (windows.get(w).getOpened() && !windows.get(w).getClosed())
			{
				windowQueue.add(windows.get(w));
			}
			else
			{
				windows.get(w).reset();
			}
		}
		
		for (int q = 0; q < windowQueue.size(); q++)
		{
			windowQueue.get(q).update();
			
			if (windowQueue.get(q).getClosed())
			{
				windowQueue.remove(q);
				q--;
			}
			else
			{
				steve.stop();
			}
		}
	}
	
	private void actEntities()
	{
		Projectile myProj = steve.attack();
		if (myProj != null)
		{
			projArray.add(myProj);
		}
		
		//steve.setGrav(true);
		steve.update();
		
		
		
		Iterator<String> i = characters.keySet().iterator();
		while (i.hasNext())
		{
		   if (!characters.get(i.next()).stillAlive())
		   {
			   i.remove();
		   }
		}
		
		for (String q : characters.keySet())
		{
			if (!characters.get(q).stillAlive())
			{
				characters.remove(q);
			}
		}
			
			
		for (String q : characters.keySet())
		{
			characters.get(q).update();
			
			Projectile bullet = characters.get(q).attack();
			if (bullet != null)
			{
				projArray.add(bullet);
			}
			
			characters.get(q).setPlayerAction(false);
		}
		
		for (int q = 0; q < entityArray.size(); q++)
		{
			if (!entityArray.get(q).stillAlive())
			{
				entityArray.remove(q);
				q--;
			}
			else
			{
				entityArray.get(q).update();
			}
		}
	}
	
	protected void panCamera(Graphics2D g)
	{
		int x = 400 - steve.pic.getPosition().x;
		
		int xMax = 800 - (xSize*20);
		
		if (x > 0)
		{
			x = 0;
		}
		else if (x < xMax)
		{
			x = xMax;
		}
	
		
		int y = 300 - steve.pic.getPosition().y;
		
		int yMax = 600 - (ySize*20);
		
		if (y > 0)
		{
			y = 0;
		}
		else if (y < yMax)
		{
			y = yMax;
		}
		
		cameraPos = new Point(x, y);
	}
	
	private void moveEntities()
	{
		steve.move();
		
		for (String c : characters.keySet())
		{
			characters.get(c).move();
		}
		
		for (int c = 0; c < entityArray.size(); c++)
		{
			entityArray.get(c).move();
		}
	}
	
	public void fallDamage(double multiplier, entity.Character t)
	{
		t.takeDamage(multiplier * t.yMomentum);
	}
	
	private void drawEntities(Graphics2D g)
	{
		steve.draw(g);
		
		for (String c : characters.keySet())
		{
			characters.get(c).draw(g);
		}
		
		for (int c = 0; c < entityArray.size(); c++)
		{
			entityArray.get(c).pic.draw(g);
		}
		
	}
	
	private void moveProjectiles()
	{
		Point pos;
		int size = projArray.size();
		
		for (int p = 0; p < size; p++)
		{	
			pos = new Point(projArray.get(p).pic.getPosition().x, projArray.get(p).pic.getPosition().y);
			
			pos.x /= 20;
			pos.y /= 20;
			
			projArray.get(p).move();
			
			if (isOffScreen(projArray.get(p).pic.getPosition()))
			{
				projArray.remove(p);
			}
			else
			{
				if (projArray.get(p).ballistic)
				{
					projArray.get(p).gravity();
				}
				try
				{
					boolean b = tiles.get(pos.y).get(pos.x).isSolid();
					
					if (scenery.get(pos.y).get(pos.x) != null)
					{
						if (scenery.get(pos.y).get(pos.x).isSolid())
						{
							b = true;
						}
					}
					
					if (b)
					{
						projArray.remove(p);
					}
				}
				catch (Exception e ) {}
			}
			
			
			size = projArray.size();
		}
	}
	
	private void drawProjectiles(Graphics2D g)
	{
		for (int p = 0; p < projArray.size(); p++)
		{
			projArray.get(p).draw(g);
		}
	}
	
	private void respondToPlayerInput()
	{
		steve.reactToKeys();
		
		steve.reactToMouse();
	}
	
	private void checkCollisions()
	{
		
		Collision.entityBlockCollision(this, steve);
		Collision.projEntityCollision(this, steve);
		
		
		if (Collision.entitySceneryCollision(this, steve) == null)
		{
			steve.setAction("default");
		}
		
		for (String i : characters.keySet())
		{
			Collision.entityBlockCollision(this, characters.get(i));
			Collision.projEntityCollision(this, characters.get(i));
			if (Collision.entitySceneryCollision(this, characters.get(i)) == null)
			{
				characters.get(i).setAction("default");
			}
		}
		
		for (int i = 0; i < entityArray.size(); i++)
		{
			Collision.entityBlockCollision(this, entityArray.get(i));
			Collision.projEntityCollision(this, entityArray.get(i));
		}
		
		for (int p = 0; p < projArray.size(); p++)
		{
			if (projArray.get(p).hit)
			{
				projArray.remove(p);
				p--;
			}
		}
	}
	
	public void drawBlocks(Graphics2D g)
	{
		if (tiles.isEmpty())
		{
			return;
		}
		
		int offsetX = -cameraPos.x/20;
		int offsetY = -cameraPos.y/20;
		
		for (int y = offsetY; y < 31 + (offsetY); y++)
		{
			for (int x = offsetX; x < 41 + (offsetX); x++)
			{
				if ((tiles.size() > y) && (tiles.get(0).size() > x))
				{
					if (tiles.get(y).get(x) != null)
					{
						tiles.get(y).get(x).draw(g);
					}
				}
			}
		}
	}
	
	private void drawScenery(Graphics2D g)
	{
		if (scenery.isEmpty())
		{
			return;
		}
		
		int offsetX = -cameraPos.x/20;
		int offsetY = -cameraPos.y/20;
		
		for (int y = offsetY; y < 31 + (offsetY); y++)
		{
			for (int x = offsetX; x < 41 + (offsetX); x++)
			{
				if ((scenery.size() > y) && (scenery.get(0).size() > x))
				{
					if (scenery.get(y).get(x) != null)
					{
						scenery.get(y).get(x).draw(g);
					}
				}
			}
		}
	}

	public ArrayList<ArrayList<Block>> getTiles()
	{
		return tiles;
	}

	public void setTiles(ArrayList<ArrayList<Block>> _tiles)
	{
		this.tiles = _tiles;
	}

	public void actionKeyHandle()
	{
		for (String c : characters.keySet())
		{
			if (Collision.charactersCollision(characters.get(c), steve))
			{
				characters.get(c).setPlayerAction(true);
			}
		}
		
		for (ArrayList<Scenery> y : scenery)
		{
			for (Scenery x : y)
			{
				if (x != null)
				{
					if (BlockCollision.entityInProximity(steve, x, 30))
					{
						sceneryMgr.action(x, steve);
					}
				}
			}
		}
	}
	
	public void mousePressHandle(MouseEvent e)
	{
		if (windowQueue.size() > 0)
		{
			windowQueue.get(windowQueue.size()-1).mousePressHandle(e);
		}
		else
		{
			steve.mousePressHandle(e);
		}
	}
	
	public void mouseReleaseHandle(MouseEvent e)
	{
		if (windowQueue.size() > 0)
		{
			windowQueue.get(windowQueue.size()-1).mouseReleaseHandle(e);
		}
		else
		{
			steve.mouseReleaseHandle(e);
		}
	}
	
	public void keyPressHandle(KeyEvent e)
	{
		if (e.getKeyChar() == 'i')
		{
			// invOpen = true;
			steve.disableInput();
		}
		else
		{
			if (windowQueue.size() > 0)
			{
				windowQueue.get(windowQueue.size()-1).keyPressHandle(e);
			}
			else
			{
				steve.keyPressHandle(e);
			}
		}
	}
	
	public void keyReleaseHandle(KeyEvent e)
	{
		if (e.getKeyChar() == 'i')
		{
			// invOpen = false;
			steve.enableInput();
		}
		else
		{
			if (windowQueue.size() > 0)
			{
				windowQueue.get(windowQueue.size()-1).keyReleaseHandle(e);
			}
			else if (e.getKeyCode() == Game.KEY_ACTION)
			{
				actionKeyHandle();
			}
			else
			{
				steve.keyReleaseHandle(e);
			}
		}
	}	
}
