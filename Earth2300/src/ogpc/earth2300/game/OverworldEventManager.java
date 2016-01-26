package ogpc.earth2300.game;

import java.util.ArrayList;
import java.util.HashMap;

import ogpc.earth2300.math.Collision;
import ogpc.earth2300.resource.Assignment;
import ogpc.earth2300.resource.Keywords;
import ogpc.earth2300.resource.ScriptEvent;
import ogpc.earth2300.resource.ScriptNode;

public class OverworldEventManager
{
	private Overworld owner;
	public ArrayList<String> randEvents;
	
	public OverworldEventManager(Overworld _owner)
	{
		owner = _owner;
		randEvents = new ArrayList<String>();
	}
	
	public boolean checkMissionReqs(ScriptNode node)
	{
		boolean b = true;
		
		for(Assignment a : node.contents)
		{
			b = globalTriggerCheck(a);
		}
		
		for (int x = 0; x < node.getChildren(); x++)
		{
			if (node.getChild(x).name == Keywords.Mission.code)
			{
				b = parseMissionReq(node.getChild(x));
			}
		}
		
		return b;
	}

	private boolean parseMissionReq(ScriptNode child)
	{
		boolean b = true;
		
		String name = "";
		boolean comp = true;
		
		for (Assignment a : child.contents)
		{
			if (a.variable == Keywords.name.code)
			{
				name = a.value;
			}
			if (a.variable == Keywords.complete.code)
			{
				comp = Boolean.parseBoolean(a.value);
			}
		}
		
		if (owner.getMission(name) == null)
		{
			return true;
		}
		
		b = (comp == owner.getMission(name).getLevel().complete);
	
		return b;
	}

	public void getEvents()
	{
		for (String key : owner.scriptEvents.keySet())
		{
			if (checkTrigger(owner.scriptEvents.get(key)))
			{
				// Perform effect
				checkEffect(owner.scriptEvents.get(key));
			}
		}
	}

	private boolean checkTrigger(ScriptEvent event)
	{
		// Check contents (global effects), then peruse nodes
		
		boolean q = true;
		
		ArrayList<Assignment> asgns = event.trigger.contents;
		
		for (int a = 0; a < asgns.size() && q; a++)
		{
			q = globalTriggerCheck(asgns.get(a));
		}
		
		for (int n = 0; n < event.trigger.getChildren() && q; n++)
		{
			q = checkNodeTrigger(event.trigger.getChild(n));
		}
		
		return q;
	}
	
	private boolean globalTriggerCheck(Assignment a)
	{
		if (a.variable == Keywords.population.code)
		{
			return (owner.getPopulation() >= Integer.parseInt(a.value));
		}
		if (a.variable == Keywords.gameLoad.code)
		{
			return Boolean.parseBoolean(a.value);
		}
		if (a.variable == Keywords.foodRiot.code)
		{
			return (owner.foodRiotTrigger == Boolean.parseBoolean(a.value));
		}
		if (a.variable == Keywords.waterRiot.code)
		{
			return (owner.waterRiotTrigger == Boolean.parseBoolean(a.value));
		}
		if (a.variable == Keywords.powerRiot.code)
		{
			return (owner.powerRiotTrigger == Boolean.parseBoolean(a.value));
		}
		if (a.variable == Keywords.greedRiot.code)
		{
			return (owner.greedRiotTrigger == Boolean.parseBoolean(a.value));
		}
		if (a.variable == Keywords.Event.code)
		{
			return (owner.currentEvent.equals(a.value));
		}
		
		return false;
	}
	
	private boolean checkNodeTrigger(ScriptNode node)
	{
		boolean q = true;
		
		if (node.name == Keywords.Window.code)
		{
			q = checkWindowListTrigger(node);
		}
		else if (node.name == Keywords.player.code)
		{
			q = checkPlayerTrigger(node);
		}
		else if (node.name == Keywords.Mission.code)
		{
			q = checkMissionTrigger(node);
		}
		else
		{
			q = false;
		}
		return q;
	}

	private boolean checkMissionTrigger(ScriptNode node)
	{
		String name = "";
		boolean comp = false;
		
		for (Assignment a : node.contents)
		{
			if (a.variable == Keywords.name.code)
			{
				name = a.value;
			}
			if (a.variable == Keywords.complete.code)
			{
				comp = Boolean.parseBoolean(a.value);
			}
		}
		
		try
		{
			return (owner.getMission(name).getLevel().complete == comp);
		}
		catch (NullPointerException e)
		{
			return true;
		}
	}

	private boolean checkPlayerTrigger(ScriptNode node)
	{
		boolean q = true;
		
		for (int a = 0; a < node.contents.size() && q; a++)
		{
		
		}
		
		return q;
	}

	private boolean checkWindowListTrigger(ScriptNode node) 
	// Refers to the HashMap of windows
	{
		boolean q = true;
		
		// Direct content checks are generic window events
		for (int c = 0; c < node.contents.size() && q; c++)
		{
			//TODO Check generic events
		}
		
		// Acceptable node: "member"
		for (int n = 0; n < node.getChildren() && q; n++)
		{
			if (node.getChild(n).name == Keywords.member.code)
			{
				q = checkWindowTrigger(node.getChild(n));
			}
			else
			{
				q = false;
			}
		}
		
		return q;
	}

	private boolean checkWindowTrigger(ScriptNode node)
	// Assumes name is first argument, only sorts by name
	{
		boolean q = true;
		
		for (int a = 1; a < node.contents.size() && q; a++)
		{
			if (node.contents.get(a).variable == Keywords.display.code)
			// Referencing whether or not window is displayed
			{
				if (owner.windows.get(node.contents.get(0).value).getOpened() != Boolean.parseBoolean(node.contents.get(a).value))
				// If booleans do not match
				{
					q = false;
				}
			}
			else if (node.contents.get(a).variable == Keywords.close.code)
			{
				if (owner.windows.get(node.contents.get(0).value).getClosed() != Boolean.parseBoolean(node.contents.get(a).value))
				// If booleans do not match
				{
					q = false;
				}
			}
			else if (node.contents.get(a).variable == Keywords.exitString.code)
			{
				if (!owner.windows.get(node.contents.get(0).value).getExitChoice().equals(node.contents.get(a).value))
				// If strings do not match
				{
					q = false;
				}
			}
			else
			{
				q = false;
			}
		}
		
		return q;
	}
	
	private void checkEffect(ScriptEvent event)
	{
		ArrayList<Assignment> asgns = event.effect.contents;
		
		for (int a = 0; a < asgns.size(); a++)
		{
			globalAssignmentEvent(asgns.get(a));
		}
		
		for (int n = 0; n < event.effect.getChildren(); n++)
		{
			checkNodeEvent(event.effect.getChild(n));
		}
		
	}

	private void checkNodeEvent(ScriptNode node)
	{
		if (node.name == Keywords.Window.code)
		{
			checkWindowListEffect(node);
		}
		if (node.name == Keywords.Region.code)
		{
			checkRegionEffect(node);
		}
	}
	
	private void checkRegionEffect(ScriptNode node)
	{
		for (int a = 1; a < node.contents.size(); a++)
		{
			if (node.contents.get(a).variable == Keywords.liberate.code)
			{
				owner.liberateRegion(node.contents.get(0).value);
			}
		}
		
	}

	private void checkWindowListEffect(ScriptNode node)
	{
		for (int n = 0; n < node.getChildren(); n++)
		{
			if (node.getChild(n).name == Keywords.member.code)
			{
				checkWindowEffect(node.getChild(n));
			}
		}
	}

	private void checkWindowEffect(ScriptNode node)
	{
		for (int a = 1; a < node.contents.size(); a++)
		{
			if (node.contents.get(a).variable == Keywords.display.code)
			// Setting whether or not window should be displayed
			{
				owner.windows.get(node.contents.get(0).value).setOpened(Boolean.parseBoolean(node.contents.get(a).value));
			}
			if (node.contents.get(a).variable == Keywords.close.code)
			// Setting whether or not window has "fired"
			{
				owner.windows.get(node.contents.get(0).value).setClosed(Boolean.parseBoolean(node.contents.get(a).value));
			}
		}
	}

	private void globalAssignmentEvent(Assignment assn)
	{
		
	}
}
