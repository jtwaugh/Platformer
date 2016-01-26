package ogpc.earth2300.resource;

public class ScriptEvent
{
	public String name;
	public ScriptNode trigger;
	public ScriptNode effect;
	
	public ScriptEvent (Assignment _name, ScriptNode _trigger, ScriptNode _effect)
	{
		name = _name.value;
		name.replace("\"", "");
		trigger = _trigger;
		effect = _effect;
	}
}
