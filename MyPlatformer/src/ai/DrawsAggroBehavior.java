package ai;

import math.Geometry;
import entity.Mob;
import entity.Player;
import game.Map;

public class DrawsAggroBehavior extends RoamBehavior
{

	public DrawsAggroBehavior(Map _myLevel, Player _steve)
	{
		super(_myLevel, _steve);
	}
	
	public void run(Mob actor)
	{
		super.run(actor);
		if (Geometry.entityInProximity(actor, myLevel.steve, 300))
		{
			actor.AIname = "attack";
		}
	}

}
