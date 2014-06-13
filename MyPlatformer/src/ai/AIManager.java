package ai;

import entity.Mob;
import game.Map;

public class AIManager
{
	private Map owner;
	
	private RoamBehavior roam;
	private HostileBehavior attack;
	private ApproachBehavior approach;
	private DrawsAggroBehavior drawsAggro;
	
	public AIManager(Map _owner)
	{
		owner = _owner;
		roam = new RoamBehavior(owner, owner.steve);
		attack = new HostileBehavior(owner, owner.steve);
		approach = new ApproachBehavior(owner, owner.steve);
		drawsAggro = new DrawsAggroBehavior(owner, owner.steve);
	}
	
	public void AI()
	{
		for (String q : owner.characters.keySet())
		{
			Behavior z = getBehavior(owner.characters.get(q));
			
			if (z != null)
			{
				Mob m = (Mob) owner.characters.get(q);
				
				if (!m.inAir && m.atTarg())
				{
					z.run(m);
				}
			}
		}
	}
	
	private Behavior getBehavior(entity.Character c)
	{
		if (c.AIname.equals("roam"))
		{
			return roam;
		}
		if (c.AIname.equals("attack"))
		{
			return attack;
		}
		if (c.AIname.equals("approach"))
		{
			return approach;
		}
		if (c.AIname.equals("draws aggro"))
		{
			return drawsAggro;
		}
		return null;
	}
}
