package ogpc.earth2300.game;

import java.util.ArrayList;
import java.util.HashMap;

import ogpc.earth2300.math.Collision;
import ogpc.earth2300.math.Geometry;
import ogpc.earth2300.resource.Assignment;
import ogpc.earth2300.resource.Keywords;
import ogpc.earth2300.resource.ScriptEvent;
import ogpc.earth2300.resource.ScriptNode;

public class LevelEventManager
{
	private Map owner;
	
	public LevelEventManager(Map _owner)
	{
		owner = _owner;
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
		if (a.variable == Keywords.gameLoad.code)
		{
			return Boolean.parseBoolean(a.value);
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
		else if (node.name == Keywords.Character.code)
		{
			q = checkCharacterTrigger(node);
		}
		else if (node.name == Keywords.Timer.code)
		{
			q = checkTimerListTrigger(node);
		}
		else
		{
			q = false;
		}
		return q;
	}

	private boolean checkTimerListTrigger(ScriptNode node)
	{
		boolean b = true;
		
		for (int a = 0; a < node.getChildren() && b; a++)
		{
			if (node.getChild(a).name == Keywords.member.code)
			// Referencing a specific member
			{
				b =	checkTimerTrigger(node.getChild(a));
			}
		}
		
		return b;
	}

	private boolean checkTimerTrigger(ScriptNode node)
	{
		boolean b = true;
		
		for (int a = 1; a < node.contents.size() && b; a++)
		{
			if (node.contents.get(a).variable == Keywords.end.code)
			{
				boolean e = owner.timers.get(node.contents.get(0).value).expired();
				boolean v = Boolean.parseBoolean(node.contents.get(a).value);
				
				return (e == v);
			}
		}
		
		return b;
	}

	private boolean checkCharacterTrigger(ScriptNode node)
	{
		boolean q = true;
		
		for (int a = 0; a < node.getChildren() && q; a++)
		{
			if (node.getChild(a).name == Keywords.member.code)
			// Referencing a specific member
			{
				q =	checkCharTrigger(node.getChild(a));
			}
		}
		
		return q;
	}
	
	private boolean checkCharTrigger(ScriptNode node)
	{
		boolean q = true;
		
		ogpc.earth2300.entity.Character myChar = owner.characters.get(node.contents.get(0).value);
		
		for (int a = 1; a < node.contents.size() && q; a++)
		{
			if (node.contents.get(a).variable == Keywords.playerTalk.code)
			{
				if (myChar != null)
				{
					if ((myChar.getPlayerAction() != Boolean.parseBoolean(node.contents.get(a).value))
							|| !Collision.charactersCollision(myChar, owner.steve))
					{
						q = false;
					}
				}
				else
				{
					q = false;
				}
				
			}
			else if (node.contents.get(a).variable == Keywords.pos.code)
			{
				if (!(myChar.pic.getPosition().x == Integer.parseInt(node.contents.get(a).value.split(",")[0]) &&
					myChar.pic.getPosition().y == Integer.parseInt(node.contents.get(a).value.split(",")[1])))
				{
					q = false;
				}
			}
			else if (node.contents.get(a).variable == Keywords.proximity.code)
			{
				if (!Geometry.entityInProximity(myChar, owner.steve, Double.parseDouble(node.contents.get(a).value)))
				{
					q = false;
				}
				else
				{
					System.out.println();
				}
			}
			else
			{
				q = false;
			}
		}
		
		return q;
	}

	private boolean checkPlayerTrigger(ScriptNode node)
	{
		boolean q = true;
		
		for (int a = 0; a < node.contents.size() && q; a++)
		{
			if (node.contents.get(a).variable == Keywords.lowHP.code)
			// Referencing whether or not the player's health is below half
			{
				if (owner.steve.lowHP() != Boolean.parseBoolean(node.contents.get(a).value))
				{
					q = false;
				}
			}
			else if (node.contents.get(a).variable == Keywords.pos.code)
			{
				if (!(owner.steve.pic.getPosition().x == Integer.parseInt(node.contents.get(a).value.split(",")[0]) &&
					owner.steve.pic.getPosition().y == Integer.parseInt(node.contents.get(a).value.split(",")[1])))
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
		else if (node.name == Keywords.Level.code)
		{
			checkLevelEffect(node);
		}
		else if (node.name == Keywords.Timer.code)
		{
			checkTimerListEffect(node);
		}
		else if (node.name == Keywords.Character.code)
		{
			checkCharEffect(node);
		}
	}
	
	private void checkCharEffect(ScriptNode node)
	{
		String name = "null";
		
		for (int n = 0; n < node.contents.size(); n++)
		{
			if (node.contents.get(n).variable == Keywords.name.code)
			{
				name = node.contents.get(n).value;
			}
			
			if (node.contents.get(n).variable == Keywords.ai.code)
			{
				owner.characters.get(name).AIname = new String(node.contents.get(n).value);
			}
		}
	}

	private void checkTimerListEffect(ScriptNode node)
	{
		for (int n = 0; n < node.getChildren(); n++)
		{
			if (node.getChild(n).name == Keywords.member.code)
			{
				checkTimerEffect(node.getChild(n));
			}
		}
	}

	private void checkTimerEffect(ScriptNode node)
	{
		for (int n = 1; n < node.contents.size(); n++)
		{
			if (node.contents.get(n).variable == Keywords.start.code && node.contents.get(n).value.equals("true"))
			{
				owner.timers.get(node.contents.get(0).value).start();
			}
		}
		
	}

	private boolean checkLevelEffect(ScriptNode node)
	{
		boolean q = true;
		
		for (int a = 0; a < node.contents.size() && q; a++)
		{
			if (node.contents.get(a).variable == Keywords.currentMap.code)
			// Changing the scene
			{
				owner.owner.changeMap(node.contents.get(a).value);
			}
			if (node.contents.get(a).variable == Keywords.complete.code)
			// Ending the map
			{
				Game.command = "overworld";
				owner.owner.complete = true;
			}
			else
			{
				q = false;
			}
		}
		
		return q;
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
		if (assn.variable == Keywords.backdrop.code)
		{
			owner.bg = Game.manager.getBG(assn.value);
		}
	}
}
