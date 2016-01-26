package ogpc.earth2300.resource;

import java.util.ArrayList;

import ogpc.earth2300.game.Game;

public class ScriptNode
{
	public int name;
	private ScriptNode parent;
	public ArrayList<Assignment> contents;
	private ArrayList<ScriptNode> children;
	
	public int index;
	
	public ScriptNode (String _name, ScriptNode _parent)
	{
		name = Game.keywords.hash(_name);
		parent = _parent;
		contents = new ArrayList<Assignment>();
		children = new ArrayList<ScriptNode>();
	}
	
	public boolean equals(ScriptNode that)
	{
		boolean b = true;
		
		if (that.contents.size() == contents.size())
		{
			for (int a = 0; a < contents.size(); a++)
			{
				b = (that.contents.get(a).equals(contents.get(a)));
			}
		}
		else return false;
		
		if (that.getChildren() == getChildren())
		{
			for (int c = 0; c < getChildren(); c++)
			{
				b = (that.getChild(c).equals(children.get(c)));
			}
		}
		else return false;
		
		return b;
	}
	
	public void add(Assignment a)
	{
		contents.add(a);
	}
	
	public void addChild(String str)
	{
		children.add(new ScriptNode(str, this));
	}
	
	public ScriptNode getChild (int index)
	{
		return children.get(index);
	}
	
	public int getChildren()
	{
		return children.size();
	}
	
	public ScriptNode getParent()
	{
		return parent;
	}
	
	public ScriptNode getNewestChild ()
	{
		return children.get(children.size()-1);
	}
}
