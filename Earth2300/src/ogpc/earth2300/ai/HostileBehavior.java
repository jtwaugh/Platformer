package ogpc.earth2300.ai;

import java.awt.Point;

import ogpc.earth2300.entity.Mob;
import ogpc.earth2300.entity.Player;
import ogpc.earth2300.game.Map;

public class HostileBehavior extends ApproachBehavior
{
	public HostileBehavior(Map _myLevel, Player _steve)
	{
		super(_myLevel, _steve);
	}
	
	public void run (Mob actor)
	{
		super.run(actor);
		
		actor.targPt = new Point(myLevel.steve.pic.getPosition().x + myLevel.steve.pic.getWidth()/2, myLevel.steve.pic.getPosition().y + myLevel.steve.pic.getHeight()/2);
		Point t = new Point(actor.pic.getPosition().x + actor.pic.getWidth()/2, actor.pic.getPosition().y + actor.pic.getHeight()/2);
		
		if (Math.sqrt((actor.targPt.x-t.x)*(actor.targPt.x-t.x) + (actor.targPt.y-t.y)*(actor.targPt.y-t.y)) <= 1000)
		// TODO use Brensenham's algorithm to detect line of sight
		{
			actor.willAttack = true;
		}
	}
	
}
