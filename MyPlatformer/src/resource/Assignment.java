package resource;

import game.Game;

public class Assignment
{
	public int variable;
	public String value;
	
	public Assignment(String var, String val)
	{
		variable = Game.keywords.hash(var);
		value = val;
	}
	
	public boolean equals(Assignment that)
	{
		return (variable == that.variable && value.equals(that.value));
	}
}
