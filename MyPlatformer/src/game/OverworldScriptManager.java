package game;

import gui.ChoiceInterface;
import gui.MessageBox;
import gui.Window;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import resource.Assignment;
import resource.FileReader;
import resource.Keywords;
import resource.ScriptEvent;
import resource.ScriptNode;

public class OverworldScriptManager
{
	private Overworld owner;
	
	public OverworldScriptManager(Overworld _owner)
	{
		owner = _owner;
		owner.windows = new HashMap<String, Window>();
	}
	
	public void parseScript(ArrayList<ScriptNode> script) throws IOException 
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
	
	public void parseNode(ScriptNode node) throws IOException
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
	
	private void parseTopLevelNode(ScriptNode node) throws IOException
	{
		if (node.name == Keywords.Event.code)
		{
			addEvent(new ScriptEvent(node.contents.get(0), node.getChild(0), node.getChild(1)));
		}
		if (node.name == Keywords.MessageBox.code)
		{
			parseMessageBox(node);
		}
		if (node.name == Keywords.Mission.code)
		{
			parseMission(node);
		}
		if (node.name == Keywords.ChoiceInterface.code)
		{
			parseChoiceInterface(node);
		}
		if (node.name == Keywords.Region.code)
		{
			parseRegion(node);
		}
		if (node.name == Keywords.RandMission.code)
		{
			parseRandMission(node);
		}
		if (node.name == Keywords.Events.code)
		{
			parseEventsList(node);
		}
	}

	private void parseEventsList(ScriptNode node) throws IOException
	{
		String listFile = FileReader.drive + "overworld/" + node.contents.get(0).value;
		FileReader r = new FileReader();
		r.parseStringList(listFile, owner.randEvents);	
	}

	private void parseRegion(ScriptNode node)
	{
		String name = node.contents.get(0).value;
		String libGraphic = node.contents.get(1).value.replace(" ","").split(",")[0];
		String enGraphic = node.contents.get(1).value.replace(" ","").split(",")[1];
		Point pos = new Point(Integer.parseInt(node.contents.get(2).value.replace(" ", "").split(",")[0]), Integer.parseInt(node.contents.get(2).value.replace(" ", "").split(",")[1]));
		int population = Integer.parseInt(node.contents.get(3).value);
		int power = Integer.parseInt(node.contents.get(4).value);
		int food = Integer.parseInt(node.contents.get(5).value);
		int industry = Integer.parseInt(node.contents.get(6).value);
		int water = Integer.parseInt(node.contents.get(7).value);
		
		owner.addRegion(name, new OverworldRegion(libGraphic, enGraphic, pos, population, power, food, industry, water));
		
	}

	private void parseMission(ScriptNode node) throws IOException
	{
		String name = node.contents.get(0).value;
		String graphicName = node.contents.get(1).value;
		Point pos = new Point(Integer.parseInt(node.contents.get(2).value.replace(" ", "").split(",")[0]), Integer.parseInt(node.contents.get(2).value.replace(" ", "").split(",")[1]));
		ScriptNode requirements = null;
		
		int[] reward = {0,0,0,0,0,0};
	
		for (int i = 0; i < node.getChildren(); i++)
		{
			if (node.getChild(i).name == Keywords.requirements.code)
			{
				requirements = node.getChild(i);
			}
			if (node.getChild(i).name == Keywords.reward.code)
			{
				reward = parseMissionReward(node.getChild(i));
			}
			if (node.getChild(i).name == Keywords.ChoiceInterface.code)
			{
				ChoiceInterface myWindow = parseMissionMessage(node.getChild(i), name);
				myWindow.setPosition(pos.x + 20, pos.y + 20);
				owner.addMissionWindow(name, myWindow);
			}
		}
		
		owner.addMission(name, graphicName, pos, requirements, reward);
	}
	
	private void parseRandMission(ScriptNode node) throws IOException
	{
		String name = node.contents.get(0).value;
		String graphicName = node.contents.get(1).value;
		
		double weight = Double.parseDouble(node.contents.get(2).value);
		
		ScriptNode requirements = null;
		
		int[] reward = {0,0,0,0,0,0};
	
		for (int i = 0; i < node.getChildren(); i++)
		{
			if (node.getChild(i).name == Keywords.requirements.code)
			{
				requirements = node.getChild(i);
			}
			if (node.getChild(i).name == Keywords.reward.code)
			{
				reward = parseMissionReward(node.getChild(i));
			}
			if (node.getChild(i).name == Keywords.ChoiceInterface.code)
			{
				ChoiceInterface myWindow = parseMissionMessage(node.getChild(i), name);
				myWindow.setPosition(120, 120);
				owner.addMissionWindow(name, myWindow);
			}
		}
		
		owner.addRandMission(name, graphicName, requirements, reward, weight);
	}

	private int[] parseMissionReward(ScriptNode node)
	{
		int[] reward = {0,0,0,0,0,0};
		
		for (Assignment a : node.contents)
		{
			if (a.variable == Keywords.water.code)
			{
				reward[1] = Integer.parseInt(a.value);
			}
			if (a.variable == Keywords.power.code)
			{
				reward[2] = Integer.parseInt(a.value);
			}
		}
		
		return reward;
	}
	
	private ChoiceInterface parseMissionMessage(ScriptNode node, String name) throws IOException
	{
		int width = Integer.parseInt(node.contents.get(0).value);
		int height = Integer.parseInt(node.contents.get(1).value);
		String style = node.contents.get(2).value;
		String btnStyle = node.contents.get(3).value;
		String[] options = node.contents.get(4).value.replace(" ", "").split(",");
		
		ChoiceInterface myWindow = new ChoiceInterface(width, height, style, btnStyle, Game.manager.getLevel(name).description, new Point(0, 0), options);
		return myWindow;
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

	private void parseChildNode(ScriptNode node)
	{
		
	}
}
