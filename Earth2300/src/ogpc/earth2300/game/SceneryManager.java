package ogpc.earth2300.game;

import java.util.ArrayList;

import ogpc.earth2300.math.BlockCollision;
import ogpc.earth2300.resource.Keywords;
import ogpc.earth2300.visible.Scenery;

public class SceneryManager
{
	private Map owner;
	
	public SceneryManager(Map myMap)
	{
		owner = myMap;
	}
	
	private void parseEffect(Scenery s, String effect, ogpc.earth2300.entity.Character t)
	{
		if (effect.replace(" ", "").equals("null"))
		{
			return;
		}
		
		String[] effects = effect.split(" ");
		
		if (Game.keywords.hash(effects[0]) == Keywords.falldamage.code)
		{
			double m = Double.parseDouble(effects[1]);
			owner.fallDamage(m, t);
		}
		if (Game.keywords.hash(effects[0]) == Keywords.ladder.code)
		{
			t.setAction("climb");
		}
		if (Game.keywords.hash(effects[0]) == Keywords.transform.code)
		{
			String	i = effects[1];
			
			Scenery q = Game.manager.getScenery(i);
			
			if (!(q.isSolid() && BlockCollision.entityInProximity(owner.steve, s, 21)))
			{
				s.transform(q, s.pic.getPosition().x, s.pic.getPosition().y);
			}
		}
	}
	
	public void topColl (Scenery s, ogpc.earth2300.entity.Character t)
	{
		parseEffect(s, s.topColl, t);
	}
	
	public void botColl (Scenery s, ogpc.earth2300.entity.Character t)
	{
		parseEffect(s, s.botColl, t);
	}
	
	public void sideColl (Scenery s, ogpc.earth2300.entity.Character t)
	{
		parseEffect(s, s.sideColl, t);
	}
	
	public void action (Scenery s, ogpc.earth2300.entity.Character t)
	{
		parseEffect(s, s.action, t);
	}
	
	public void damage (Scenery s, ogpc.earth2300.entity.Character t)
	{
		parseEffect(s, s.damage, t);
	}
	
	public void death (Scenery s, ogpc.earth2300.entity.Character t)
	{
		parseEffect(s, s.death, t);
	}
}