package math;

import java.awt.Point;

import entity.Entity;
import game.Map;
import graphics.Sprite;
import visible.Block;
import visible.Scenery;

public class Collision
{
	public static BlockCollision entityBlockCollision(Map myLevel, Entity steve)
	{
		BlockCollision myColl = null;
	
		int steveBlockX = (steve.pic.getPosition().x + 10) / 20;
		int steveBlockY = (steve.pic.getPosition().y + 10) / 20;
		
		int qx;
		int qy;
		
		int yAllowance = 3;
		
		if (steve.yMomentum >= 10)
		{
			yAllowance += (steve.yMomentum/20);
		}
		
		for (int y = steveBlockY - 1; y <= steveBlockY + yAllowance; y++)
		{
			if (y < 0)
			{
				qy = 0;
			}
			else if (y >= myLevel.ySize)
			{
				qy = myLevel.ySize-1;
			}
			else 
			{
				qy = y;
			}
			
			for (int x = steveBlockX - 1; x <= steveBlockX + 1; x++)
			{
				
				if (x < 0)
				{
					qx = 0;
				}
				else if (x >= myLevel.xSize)
				{
					qx = myLevel.xSize-1;
				}
				else 
				{
					qx = x;
				}
				
				Block that = myLevel.getTiles().get(qy).get(qx);
				
				if (that != null)
				{
					myColl = BlockCollision.collBlockEntity(steve, that);
					steve.blockCollideHandle(myColl, that);	
				}
			}
		}
		
		return myColl;
	}
	
	public static BlockCollision entitySceneryCollision(Map myLevel, entity.Character steve)
	{
		BlockCollision myColl = null;
	
		int steveBlockX = (steve.pic.getPosition().x + 10) / 20;
		int steveBlockY = (steve.pic.getPosition().y + 10) / 20;
		
		int qx;
		int qy;
		
		int yAllowance = 3;
		
		if (steve.yMomentum >= 10)
		{
			yAllowance += (steve.yMomentum/20);
		}
		
		for (int y = steveBlockY - 1; y <= steveBlockY + yAllowance; y++)
		{
			if (y < 0)
			{
				qy = 0;
			}
			else if (y > myLevel.ySize)
			{
				qy = myLevel.ySize;
			}
			else 
			{
				qy = y;
			}
			
			for (int x = steveBlockX - 1; x <= steveBlockX + 1; x++)
			{
				
				if (x < 0)
				{
					qx = 0;
				}
				else if (x > myLevel.xSize)
				{
					qx = myLevel.xSize;
				}
				else 
				{
					qx = x;
				}
				
				Scenery that;
				
				try
				{
					that = myLevel.scenery.get(qy).get(qx);
				}
				catch (Exception e)
				{
					that = null;
				}
				
				if (that != null)
				{
					myColl = BlockCollision.collBlockEntity(steve, that);
					
					if (that.isSolid())
					{
						steve.blockCollideHandle(myColl, that);
					}
					
					if (!that.colliding)
					{
						if (myColl.topCollided)
						{
							myLevel.sceneryMgr.topColl(that, steve);
							that.colliding = true;
						}
						if (myColl.bottomCollided)
						{
							myLevel.sceneryMgr.botColl(that, steve);
							that.colliding = true;
						}
						if (myColl.leftCollided || myColl.rightCollided)
						{
							myLevel.sceneryMgr.sideColl(that, steve);
							that.colliding = true;
						}
					}
					
					if (!myColl.bottomCollided && !myColl.topCollided && !myColl.leftCollided && !myColl.rightCollided)
					{
						that.colliding = false;
					}
				}
			}
		}
		
		return myColl;
	}
	
	public static void projEntityCollision(Map myLevel, Entity steve)
	{
		ProjectileCollision coll = new ProjectileCollision(false);
		
		for (int p = 0; p < myLevel.projArray.size(); p++)
		{
			coll = ProjectileCollision.collProjEntity(myLevel.projArray.get(p), steve);
			if (coll.collided)
			{
				steve.projHitHandle(myLevel.projArray.get(p));
				myLevel.projArray.get(p).hit = true;
				
				if (!steve.stillAlive())
				{
					if(myLevel.projArray.get(p).owner == myLevel.steve)
					{
						myLevel.steve.kills++;
					}
				}
			}
		}
	}
	
	public static boolean charactersCollision(Entity e1, Entity e2)
	// Note: only checks the corners of two 20x40 entities. Do not use for movement-related collision detection!
	{
		return (Geometry.pointInQuad(e1.pic.getPosition(), e2.pic.getPosition(), new Point(e2.pic.getPosition().x + e2.pic.getWidth() - 1, e2.pic.getPosition().y + e2.pic.getHeight() - 1))
				|| Geometry.pointInQuad(new Point(e1.pic.getPosition().x + e1.pic.getWidth() - 1, e1.pic.getPosition().y + e1.pic.getHeight() - 1), e2.pic.getPosition(), new Point(e2.pic.getPosition().x + e2.pic.getWidth() - 1, e2.pic.getPosition().y + e2.pic.getHeight() - 1))
				|| Geometry.pointInQuad(new Point(e1.pic.getPosition().x + e1.pic.getWidth() - 1, e1.pic.getPosition().y), e2.pic.getPosition(), new Point(e2.pic.getPosition().x + e2.pic.getWidth() - 1, e2.pic.getPosition().y + e2.pic.getHeight() - 1))
				|| Geometry.pointInQuad(new Point(e1.pic.getPosition().x + e1.pic.getWidth() - 1, e1.pic.getPosition().y + e1.pic.getHeight() - 1), e2.pic.getPosition(), new Point(e2.pic.getPosition().x, e2.pic.getPosition().y + e2.pic.getHeight() - 1)));
	}
}

