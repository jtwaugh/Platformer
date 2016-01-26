package ogpc.earth2300.ai;

import ogpc.earth2300.entity.Mob;
import ogpc.earth2300.entity.Player;
import ogpc.earth2300.game.Map;
import ogpc.earth2300.math.Geometry;

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
