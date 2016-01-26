package ogpc.earth2300.game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import ogpc.earth2300.entity.Mob;
import ogpc.earth2300.gui.ChoiceInterface;
import ogpc.earth2300.gui.MessageBox;
import ogpc.earth2300.gui.Window;
import ogpc.earth2300.math.Timer;
import ogpc.earth2300.resource.Keywords;
import ogpc.earth2300.resource.ScriptEvent;
import ogpc.earth2300.resource.ScriptNode;

public class LevelScriptManager
{
	private Map owner;
	
	public LevelScriptManager(Map _owner)
	{
		owner = _owner;
		owner.windows = new HashMap<String, Window>();
	}
	
	public void parseScript(ArrayList<ScriptNode> script) 
	{
		for (int s = 0; s < script.size(); s++)
		{
			parseNode(script.get(s));
		}
	}

	private void addEvent(ScriptEvent e)
	{
		owner.scriptEvents.put(e.name, e);
	}
	
	public void parseNode(ScriptNode node)
	{
		if (node.getParent() == null)
		{
			parseTopLevelNode(node);
		}
		else
		{
			parseChildNode(node);
		}
	}
	
	private void parseTopLevelNode(ScriptNode node)
	{
		if (node.name == Keywords.Event.code)
		{
			addEvent(new ScriptEvent(node.contents.get(0), node.getChild(0), node.getChild(1)));
		}
		if (node.name == Keywords.MessageBox.code)
		{
			parseMessageBox(node);
		}
		if (node.name == Keywords.Character.code)
		{
			parseCharacter(node);
		}
		if (node.name == Keywords.ChoiceInterface.code)
		{
			parseChoiceInterface(node);
		}
		if (node.name == Keywords.Timer.code)
		{
			parseTimer(node);
		}
	}

	private void parseTimer(ScriptNode node)
	{
		owner.timers.put(node.contents.get(0).value, new Timer(Long.parseLong(node.contents.get(1).value)));
	}

	private void parseMessageBox(ScriptNode node)
	{
		// TODO verify validity of tokens
		
		String name = node.contents.get(0).value;
		int width = Integer.parseInt(node.contents.get(1).value);
		int height = Integer.parseInt(node.contents.get(2).value);
		String style = node.contents.get(3).value;
		String text = node.contents.get(4).value;
		Point pos = new Point(Integer.parseInt(node.contents.get(5).value.replace(" ", "").split(",")[0]), Integer.parseInt(node.contents.get(5).value.replace(" ", "").split(",")[1]));
		owner.windows.put(name, new MessageBox(width, height, style, text, pos));
	}
	
	private void parseChoiceInterface(ScriptNode node)
	{
		// TODO verify validity of tokens
		
		String name = node.contents.get(0).value;
		int width = Integer.parseInt(node.contents.get(1).value);
		int height = Integer.parseInt(node.contents.get(2).value);
		String style = node.contents.get(3).value;
		String choiceStyle = node.contents.get(4).value;
		String text = node.contents.get(5).value;
		Point pos = new Point(Integer.parseInt(node.contents.get(6).value.replace(" ", "").split(",")[0]), Integer.parseInt(node.contents.get(6).value.replace(" ", "").split(",")[1]));
		String[] options = node.contents.get(7).value.replace(" ", "").split(",");
		owner.windows.put(name, new ChoiceInterface(width, height, style, choiceStyle, text, pos, options));
	}
	
	private void parseCharacter(ScriptNode node)
	{
		// TODO verify validity of tokens
		
		String name = node.contents.get(0).value;
		String sprite = node.contents.get(1).value;
		double weight = Double.parseDouble(node.contents.get(2).value);
		double speed = Double.parseDouble(node.contents.get(3).value);
		String AIname = node.contents.get(4).value;
		String weaponName = node.contents.get(5).value;
		
		int hp = Integer.parseInt(node.contents.get(6).value);
		
		Point pos = new Point(Integer.parseInt(node.contents.get(7).value.replace(" ", "").split(",")[0]), Integer.parseInt(node.contents.get(7).value.replace(" ", "").split(",")[1]));
	
		Mob myMob = new Mob(sprite, weight, speed, AIname, hp, pos);
	
		owner.characters.put(name, myMob);
		
		owner.characters.get(name).setWeapon(weaponName);
		
		if (hp == -1)
		{
			owner.characters.get(name).invincible = true;
		}
	}

	private void parseChildNode(ScriptNode node)
	{
		
	}
}
