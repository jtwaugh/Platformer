package ogpc.earth2300.ai;

import java.awt.Point;
import java.util.ArrayList;

import ogpc.earth2300.entity.Mob;
import ogpc.earth2300.entity.Player;
import ogpc.earth2300.game.Map;
import ogpc.earth2300.visible.Block;

public class ApproachBehavior extends RoamBehavior
{
	public ApproachBehavior(Map _myLevel, Player _steve)
	{
		super(_myLevel, _steve);
	}
	
	public void run(Mob actor)
	{
		if (actor.isFacingRight() != (myLevel.steve.pic.getPosition().x > actor.pic.getPosition().x))
		{
			actor.flip();
		}
		
		super.run(actor);
	}
	
	protected void moveBackward(Mob actor)
	{
		super.moveBackward(actor);
		actor.AIname = "draws aggro";
	}
}
