package ai;

import java.awt.Point;
import java.util.ArrayList;

import entity.Mob;
import entity.Player;
import game.Map;
import visible.Block;

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
