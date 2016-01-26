package ogpc.earth2300.ai;

import java.awt.Point;
import java.util.ArrayList;

import ogpc.earth2300.entity.Mob;
import ogpc.earth2300.entity.Player;
import ogpc.earth2300.game.Map;
import ogpc.earth2300.visible.Block;

public class RoamBehavior extends Behavior
{
	public RoamBehavior(Map _myLevel, Player _steve)
	{
		super(_myLevel, _steve);
	}
	
	public void run(Mob actor)
	// Call if the mob has reached its target
	{
		ArrayList<ArrayList<Block>> tiles = myLevel.tiles;
		
		int i = (actor.isFacingRight()) ? 1 : -1;
		Point targ = actor.getTarg();
		
		if (!detectDrops(actor, tiles, targ))
		{
			detectClimbs(actor, tiles, targ);
		}
	}	
	
	protected void detectClimbs(Mob actor, ArrayList<ArrayList<Block>> tiles, Point targBlock)
	{
		int i = (actor.isFacingRight()) ? 1 : -1;
		
		boolean feetCollideBlock = (myLevel.scenery.get(targBlock.y).get(targBlock.x + i) != null && myLevel.scenery.get(targBlock.y).get(targBlock.x + i).isSolid());
		boolean headCollideBlock = (myLevel.scenery.get(targBlock.y-1).get(targBlock.x + i) != null && myLevel.scenery.get(targBlock.y-1).get(targBlock.x + i).isSolid());
		
		boolean feetCollideScenery = (tiles.get(targBlock.y).get(targBlock.x + i) != null && tiles.get(targBlock.y).get(targBlock.x + i).isSolid());	
		boolean headCollideScenery = (tiles.get(targBlock.y-1).get(targBlock.x + i) != null && tiles.get(targBlock.y-1).get(targBlock.x + i).isSolid());	
			
		if (feetCollideBlock || headCollideBlock || feetCollideScenery || headCollideScenery)
		// If jumping is needed
		{
			int d = 0;
			
			// count number of blocks in wall
			for (d = 0; d <= 2; d++)
			{
				if (tiles.get(targBlock.y - d).get(targBlock.x + i) == null || !tiles.get(targBlock.y - d).get(targBlock.x + i).isSolid())
				{
					break;
				}
			}
			
			if (d > 2)
			{
				moveBackward(actor);
			}
			else
			{	
				int q;
				
				for (q = 0; q < 3; q++)
				{
					if (tiles.get(targBlock.y - (q + 4)).get(targBlock.x) != null && tiles.get(targBlock.y - (q + 4)).get(targBlock.x).isSolid())
					{
						break;
					}
				}
				
				if (q == 3)
				{
					actor.setTarg(new Point(targBlock.x + i, targBlock.y - d));
					actor.willJump = true;
				}
				else
				{
					moveBackward(actor);
				}	
			}
		}
		else
		{
			moveForward(actor);
		}
	}
	
	protected boolean detectDrops(Mob actor, ArrayList<ArrayList<Block>> tiles, Point targBlock)
	{
		int i = (actor.isFacingRight()) ? 1 : -1;
		
		boolean q = false;				// if there is a drop which cannot be jumped back up
		
		if (tiles.get(targBlock.y).get(targBlock.x + i) == null || !tiles.get(targBlock.y).get(targBlock.x + i).isSolid())
		{
			int d = 0;
			
			for (d = 1; d <= 4; d++)
			{
				if (tiles.get(targBlock.y + d).get(targBlock.x + i) != null && tiles.get(targBlock.y + d).get(targBlock.x + i).isSolid())
				{
					q = true;
					break;
				}
			}
			
			if (d > 4)
			{
				moveBackward(actor);
			}
			else
			{
				actor.setTarg(new Point(targBlock.x + i, targBlock.y + (d-1)));
			}
		}
		
		return q;
	}
}