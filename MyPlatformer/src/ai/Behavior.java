package ai;

import entity.Mob;
import entity.Player;
import game.Map;


public class Behavior
{
	protected Map myLevel;
	protected Player steve;
	
	public Behavior(Map _myLevel, Player _steve)
	{
		myLevel = _myLevel;
		steve = _steve;
	}
	
	public void run(Mob actor)
	{
		System.out.println("Virtual function call: Behavior.run()!");
	}
	
	protected void moveForward(Mob actor)
	{
		int i = (actor.getFacing()) ? 1 : -1;
		
		actor.moveTarg(i, 0);
	}
	
	protected void moveBackward(Mob actor)
	{
		int i = (actor.getFacing()) ? -1 : 1;
		
		actor.moveTarg(i, 0);
		
		actor.flip();
	}
}