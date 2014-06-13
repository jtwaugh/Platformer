package game;

import graphics.Backdrop;
import gui.ChoiceInterface;
import gui.OverworldHUD;
import gui.Window;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import resource.ScriptEvent;
import resource.ScriptNode;

public class Overworld
{
	public String name;
	
	private Backdrop bg;
	
	private HashMap<String, RandMission> randMissions;
	private HashMap<String, Mission> missions;
	private HashMap<String, Window> missionWindows;
	
	public HashMap<String, ScriptEvent> scriptEvents;
	public ArrayList<String> randEvents;
	
	public String currentEvent;
	
	public HashMap<String, Window> windows;
	private ArrayList<Window> windowQueue;
	
	private OverworldEventManager events;
	private OverworldScriptManager script;
	
	private OverworldHUD hud;
	
	private Point offset;
	
	private HashMap<String, OverworldRegion> regions;
	private OverworldRegion zoomedRegion;
	
	public int food;
	public int water;
	public int power;
	public int industry;
	public int population;
	public int pollution;
	
	public double qualityOfLife;
	public double expectedQOL;
	public double unrest;
	
	public boolean foodRiotTrigger;
	public boolean waterRiotTrigger;
	public boolean powerRiotTrigger;
	public boolean greedRiotTrigger;
	public boolean uprisingTrigger;
	
	public boolean missionThisTurn;
	
	public Overworld (String _name, String _bg, ArrayList<ScriptNode> scriptNodes) throws IOException
	{
		name = _name;
		events = new OverworldEventManager(this);
		script = new OverworldScriptManager(this);
		
		hud = new OverworldHUD(800, 100);
		
		missions = new HashMap<String, Mission>();
		randMissions = new HashMap<String, RandMission>();
		missionWindows = new HashMap<String, Window>();
		windows = new HashMap<String, Window>();
		windowQueue = new ArrayList<Window>();
		scriptEvents = new HashMap<String, ScriptEvent>();
		regions =  new HashMap<String, OverworldRegion>();
		randEvents = new ArrayList<String>();
		currentEvent = "";
		bg = Game.manager.getBG(_bg);
		
		food = 0;
		water = 0;
		power = 0;
		industry = 0;
		population = 1000;
		pollution = 0;
		
		setQualityOfLife(0);
		setExpectedQOL(0);
		setUnrest(0);
		
		offset = new Point(0, 0);
		
		zoomedRegion = null;
		
		script.parseScript(scriptNodes);
		
		missionThisTurn = false;
		
		
		
	}
	
	public int getPopulation()
	{
		int population = 0;
		
		for (String s : regions.keySet())
		{
			if (regions.get(s).liberated)
			{
				population += regions.get(s).population;
			}
		}
		
		return population;
	}
	
	public int getPower()
	{
		int power = 0;
		
		for (String s : regions.keySet())
		{
			if (regions.get(s).liberated)
			{
				power += regions.get(s).power;
			}
		}
		
		return power;
	}
	
	public int getFood()
	{
		int food = 0;
		
		for (String s : regions.keySet())
		{
			if (regions.get(s).liberated)
			{
				food += regions.get(s).food;
			}
		}
		
		return food;
	}
	
	public int getIndustry()
	{
		int industry = 0;
		
		for (String s : regions.keySet())
		{
			if (regions.get(s).liberated)
			{
				industry += regions.get(s).industry;
			}
		}
		
		return industry;
	}
	
	public int getWater()
	{
		int water = 0;
		
		for (String s : regions.keySet())
		{
			if (regions.get(s).liberated)
			{
				water += regions.get(s).water;
			}
		}
		
		return water;
	}
	
	
	public void loop (Graphics2D BGDraw, Graphics2D gameDraw, Graphics2D infDraw)
	{
		events();
		logic();
		render(BGDraw, gameDraw, infDraw);
	}
	
	private void events()
	{
		getNodesPressed();
		getWindowsPressed();
	}
	
	private void logic()
	{
		events.getEvents();
		updateWorld();
		updateWindows();
		hud.update(this);
		getInterfacePressed();
		getRegionsPressed();
	}

	private void render(Graphics2D bgg, Graphics2D gg, Graphics2D ig)
	{
		bg.draw(bgg);
		
		if (zoomedRegion != null)
		{
			renderRegion(gg);
		}
		else
		{
			renderGame(gg);
		}
		
		renderInterface(ig);
	}
	
	private void renderGame(Graphics2D g)
	{
		drawRegions(g);
		drawNodes(g);
	}
	
	private void renderInterface(Graphics2D g)
	{
		drawWindows(g);
		hud.setZoomed((zoomedRegion != null));		
		hud.render(g);
	}
	
	private void renderRegion(Graphics2D g)
	{
		
		double scale = 2;
		
		int x = 400 - (zoomedRegion.getScaledCenter(scale).x);
		int y = 300 - (zoomedRegion.getScaledCenter(scale).y);
		
		offset = new Point(x, y);
		
		g.translate(x, y);
		
		g.scale(scale, scale);
		
		zoomedRegion.draw(g);
		
		drawNodes(g);
		
		g.scale(1/scale, 1/scale);
		
		g.translate(-x, -y);
	}
	
	private void advanceDay()
	// TODO:
	//	* Power & Industry generate even at the minimum of 32 people. Change this.
	//	* Randomize to some degree the resource income
	{
		fixWindows();
		
		missionThisTurn =  false;
		
		hud.dayPlusPlus();
		food += getFood();
		water += getWater();
		power += getPower();
		industry += getIndustry();
		
		double foodRiot = 0;
		double waterRiot = 0;
		double powerRiot = 0;
		double greedRiot = 0;
		
		double pop = population / 100;
		
		if (pop >  0)
		{
			double temp = ((food + water + power)/3)/pop;
			
			if (expectedQOL > temp)
			{
				greedRiot += (temp - getQualityOfLife());
			}
			
			if (expectedQOL < temp)
			{
				expectedQOL = (expectedQOL + (temp - getExpectedQOL())/10);
			}
			  
			qualityOfLife = temp;
		}
		
		if ((pop / 2) > food)
		{
			population -= ((pop / 2) - food)/7;
			food = 0;
			foodRiot -= (food - (pop / 20));
		}
		else
		{
			food -= pop/2;
		}
		
		if (pop > water)
		{
			population -= (pop  - water)/3;
			water = 0;
			waterRiot -= (water - pop/10);
		}
		else
		{
			water -= pop;
		}
		
		if (pop/3 > power)
		{
			population -= ((pop/3) - power)/30;
			power = 0;
			powerRiot -= (power - (pop/30));
		}
		else
		{
			power -= pop / 3;
		}
		
		population += getQualityOfLife() * (population/1000);
		
		// TODO deal with pollution
		
		double unrestBoost = (foodRiot + waterRiot + powerRiot + greedRiot);
		
		if (unrestBoost == 0)
		{
			unrest -= unrest/10;
		}
		else
		{
			unrest += unrestBoost;
			boolean riot = (Math.random()*100 < unrest);
			
			if (riot)
			{
				double ayn  = Math.random()*unrestBoost;
				System.out.println("Riot triggered over...");
				
				if (ayn < foodRiot)
				{
					foodRiotTrigger = true;
					System.out.println("Food (" + foodRiot + ")");
				}
				else if (ayn < foodRiot + greedRiot)
				{
					greedRiotTrigger = true;
					System.out.println("Living Conditions (" + greedRiot + ")");
					
				}
				else if (ayn < foodRiot + powerRiot + greedRiot)
				{
					powerRiotTrigger = true;
					System.out.println("Food (" + powerRiot + ")");
				}
				else
				{
					waterRiotTrigger = true;
					System.out.println("Water (" + waterRiot + ")");
				}
			}
		}
		
		currentEvent = randEvents.get((int) (Math.random()*randEvents.size()));
		System.out.println(currentEvent);
		
		
	}
	
	private void getNodesPressed()
	{
		if (!missionThisTurn)
		{
			for (String s : missions.keySet())
			{
				if(missions.get(s).buttonClicked())
				{
					missionWindows.get(s).setOpened(true);
				}
			}
			
			for (String t : randMissions.keySet())
			{
				if (randMissions.get(t).getActive())
				{
					if (randMissions.get(t).buttonClicked())
					{
						missionWindows.get(t).setOpened(true);
					}
				}
			}
		}
	}
	
	private void fixWindows()
	{
		windowQueue.clear();
		
		for (String s : missions.keySet())
		{
			missionWindows.get(s).setOpened(false);
			missions.get(s).getPic().setClicked(false);
		}
		
		for (String t : randMissions.keySet())
		{
			missionWindows.get(t).setOpened(false);
		}
	}
	
	private void getRegionsPressed()
	{
		for (String s : regions.keySet())
		{
			if (regions.get(s).clickedOn)
			{
				if (zoomedRegion == null)
				{
					zoomedRegion = regions.get(s);
					regions.get(s).clickedOn = false;
					break;
				}
			}
		}	
	}
	
	private void getWindowsPressed()
	{
		for (String w : missionWindows.keySet())
		{
			if (missionWindows.get(w).getExitIndex() == 1)
			{
				if (missions.get(w) != null)
				{
					if (missions.get(w).getLevel().complete)
					{
						if (Game.command == w)
						{
							Game.command = "";
						}
						assignRewards(missions.get(w));
					}
					else
					{
						Game.command = w;
						missionThisTurn = true;
					}
				}
				else if (randMissions.get(w) != null)
				{
					if (randMissions.get(w).getLevel().complete)
					{
						if (Game.command == w)
						{
							Game.command = "";
						}
						assignRewards(missions.get(w));
					}
					else
					{
						Game.command = w;
						missionThisTurn = true;
					}
				}
			}
		}
	}
	
	private void assignRewards(Mission mission)
	{
		food += mission.rewards[0];
		water += mission.rewards[1];
		power += mission.rewards[2];
		industry += mission.rewards[3];
		pollution += mission.rewards[4];
		unrest += mission.rewards[5];
	}

	private void getInterfacePressed()
	{
		if (hud.shouldZoomOut())
		{
			zoomedRegion = null;
		}
		
		if (hud.shouldGoForward())
		{
			advanceDay();
		}
	}
	
	private void updateWorld()
	{
		ArrayList<String> considered = new ArrayList<String>();
		
		for (String s : randMissions.keySet())
		{
			if (!considered.contains(s) && events.checkMissionReqs(randMissions.get(s).getReqs()))
			{
				ArrayList<RandMission> pot = new ArrayList<RandMission>();
				
				pot.add(randMissions.get(s));
				
				considered.add(s);
				
				for (String q : randMissions.keySet())
				{
					if (randMissions.get(q).getReqs().equals(randMissions.get(s).getReqs()))
					{
						if (randMissions.get(q).getActive())
						{
							pot = new ArrayList<RandMission>();
							considered.remove(s);
							break;
						}
						else
						{
							pot.add(randMissions.get(q));
							considered.add(q);
						}
					}
				}
				
				double r = 0;
				
				double t = 0;
				
				for (RandMission m : pot)
				{
					t += m.weight;
				}
				
				double c = Math.random() * t;
				
				for (RandMission n : pot)
				{
					r += n.weight;
					if (c < r)
					{
						n.activate();
						n.randomize();
						break;
					}
					else
					{
						n.deactivate();
					}
				}
			}
		}
		
	}

	private void updateWindows()
	{
		for (String w : missionWindows.keySet())
		{
			if (missionWindows.get(w).getOpened())
			{
				missionWindows.get(w).update();
			}
		}		
		
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
		}
	}

	private void drawRegions(Graphics2D g)
	{
		for (String s : regions.keySet())
		{
			regions.get(s).draw(g);
		}
	}
	
	
	
	private void drawNodes(Graphics2D g)
	{
		ArrayList<Mission> candidates = new ArrayList<Mission>();
		
		Iterator<String> i = missions.keySet().iterator();
		while (i.hasNext())
		{
			String s = i.next();
			if (missions.get(s).getLevel().complete)
			{
				i.remove();
				missionWindows.remove(s);
			}
			else if (events.checkMissionReqs(missions.get(s).getReqs()))
			{
				missions.get(s).drawButton(g);
			}
		}
		
		for (String t : randMissions.keySet())
		{
			if (randMissions.get(t).getActive())
			{
				if (randMissions.get(t).getLevel().complete)
				{
					randMissions.get(t).deactivate();
					missionWindows.remove(t);
				}
				else
				{
					randMissions.get(t).drawButton(g);
				}
			}
		}
	}
	
	private void drawWindows(Graphics2D g)
	{
		for (String w : missionWindows.keySet())
		{
			if (missionWindows.get(w).getOpened())
			{
				missionWindows.get(w).draw(g);
			}
		}
		
		for (Window w : windowQueue)
		{
			if (w.getOpened())
			{
				w.draw(g);
			}
		}
	}
	
	public void addMission(String levelName, String btnGraphic, Point pos, ScriptNode requirements, int[] rewards) throws IOException
	{
		missions.put(levelName, new Mission(levelName, btnGraphic, pos, requirements, rewards));
	}
	
	public void addMissionWindow(String name, ChoiceInterface window)
	{
		missionWindows.put(name, window);
	}
	
	public void addRegion(String name, OverworldRegion region)
	{
		regions.put(name, region);
	}
	
	protected void keyPressHandle(KeyEvent e)
	{
		if (windowQueue.size() > 0)
		{
			windowQueue.get(windowQueue.size()-1).keyPressHandle(e);
		}
	}
	
	protected void keyReleaseHandle(KeyEvent e)
	{
		if (windowQueue.size() > 0)
		{
			windowQueue.get(windowQueue.size()-1).keyReleaseHandle(e);
		}
	}
	
	protected void mousePressHandle(MouseEvent e)
	{
		int i = 1;
		if (zoomedRegion != null)
		{
			i = 2;
		}
		
		for (String s : missions.keySet())
		{
			missionWindows.get(s).mousePressHandle(e);
			missions.get(s).getPic().mousePressHandle(new Point(0, 0), e, i, offset.x, offset.y);
		}
		for (String s : randMissions.keySet())
		{
			missionWindows.get(s).mousePressHandle(e);
			randMissions.get(s).getPic().mousePressHandle(new Point(0, 0), e, i, offset.x, offset.y);
		}
		hud.mousePressHandle(e);
	}
	
	protected void mouseReleaseHandle(MouseEvent e)
	{
		int i = 1;
		if (zoomedRegion != null)
		{
			i = 2;
		}
		
		for (String s : missions.keySet())
		{
			if (missionWindows.get(s).mouseReleaseHandle(e))
			{
				return;
			}
			if (missions.get(s).getPic().mouseReleaseHandle(e))
			{
				return;
			}
		}
		
		for (String s : randMissions.keySet())
		{
			if (missionWindows.get(s).mouseReleaseHandle(e))
			{
				return;
			}
			if (randMissions.get(s).getPic().mouseReleaseHandle(e))
			{
				return;
			}
		}
		
		hud.mouseReleaseHandle(e);
		
		for (String s : regions.keySet())
		{
			regions.get(s).mouseReleaseHandle(e, i);
		}
		
	}

	public Mission getMission(String s)
	{
		return missions.get(s);
	}
	
	public OverworldRegion getRegion(String s)
	{
		return regions.get(s);
	}
	
	public void liberateRegion(String s)
	{
		regions.get(s).liberated = true;
	}

	public double getQualityOfLife() 
	{
		return qualityOfLife;
	}

	public void setQualityOfLife(double qualityOfLife) 
	{
		this.qualityOfLife = qualityOfLife;
	}

	public double getExpectedQOL() 
	{
		return expectedQOL;
	}

	public void setExpectedQOL(double expectedQOL) 
	{
		this.expectedQOL = expectedQOL;
	}

	public double getUnrest() 
	{
		return unrest;
	}

	public void setUnrest(double unrest) 
	{
		this.unrest = unrest;
	}

	public void addRandMission(String levelName, String btnGraphic, ScriptNode requirements, int[] rewards, double weight) throws IOException
	{
		randMissions.put(levelName, new RandMission(levelName, btnGraphic, requirements, rewards, weight));
	}
}
