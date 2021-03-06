package ogpc.earth2300.math;

import java.awt.Point;

import ogpc.earth2300.entity.Entity;
import ogpc.earth2300.graphics.Sprite;
import ogpc.earth2300.visible.Block;

public class BlockCollision extends Collision
{
	public boolean rightCollided;
	public boolean leftCollided;
	public boolean topCollided;
	public boolean bottomCollided;
	
	public BlockCollision(boolean rColl, boolean lColl, boolean tColl, boolean bColl)
	{
		rightCollided = rColl;
		leftCollided = lColl;
		topCollided = tColl;
		bottomCollided = bColl;
	}
	
	public BlockCollision(BlockCollision that)
	{
		rightCollided = that.rightCollided;
		leftCollided = that.leftCollided;
		topCollided = that.topCollided;
		bottomCollided = that.bottomCollided;
	}
	
	public static boolean entityInProximity(Entity n, Block q, double r)
	{
		Point p = new Point((q.pic.getPosition().x + q.pic.getWidth()/2),(q.pic.getPosition().y + q.pic.getHeight()/2));
		Point e = new Point((n.pic.getPosition().x + n.pic.getWidth()/2),(n.pic.getPosition().y + n.pic.getHeight()/2)); 
		return (r >= Math.sqrt((e.y-p.y)*(e.y-p.y) + (e.x-p.x)*(e.x-p.x)));
	}
	
	public static BlockCollision collBlockEntity(Entity e, Block that)
	{
		boolean[] coll = {false, false, false, false};
		// 0 is right
		// 1 is left
		// 2 is top
		// 3 is bottom
		
		Sprite pic = e.pic;
		double xMomentum = e.xMomentum;
		double yMomentum = e.yMomentum;
		
		int x = pic.getPosition().x;
		int y = pic.getPosition().y;
		
		Point cTopLeft = pic.getPosition();
		Point cTopRight = new Point(x + pic.getWidth() - 1, y);
		Point cBotLeft = new Point(x, y + pic.getHeight() - 1);
		Point cBotRight = new Point(x + pic.getWidth() - 1, y + pic.getHeight() - 1);
		Point cMidLeft = new Point(x, y + ((pic.getHeight() - 1)/ 2));
		Point cMidRight = new Point(x + pic.getWidth() - 1, y + ((pic.getHeight() - 1) / 2));
		
		Point eTopLeft = new Point((int)(cTopLeft.x + xMomentum), (int)(cTopLeft.y + yMomentum));
		Point eTopRight = new Point((int)(cTopRight.x + xMomentum), (int)(cTopRight.y + yMomentum));
		Point eBotLeft = new Point((int)(cBotLeft.x + xMomentum), (int)(cBotLeft.y + yMomentum));
		Point eBotRight = new Point((int)(cBotRight.x + xMomentum), (int)(cBotRight.y + yMomentum));
		Point eMidLeft = new Point((int)(x + xMomentum), (int) (y + ((pic.getHeight() - 1)/ 2) + yMomentum));
		Point eMidRight = new Point((int)(cMidRight.x + xMomentum), (int) (y + ((pic.getHeight() - 1)/ 2) + yMomentum));
		
		int tx = that.pic.getPosition().x;
		int ty = that.pic.getPosition().y;
		
		Point tTopLeft = that.pic.getPosition();
		Point tTopRight = new Point(tx + that.pic.getWidth() - 1, ty);
		Point tBotLeft = new Point(tx, ty + that.pic.getHeight() - 1);
		Point tBotRight = new Point(tx + that.pic.getWidth() - 1, ty + that.pic.getHeight() - 1);
		
		// Check intersection of top of block
		if ((Geometry.lineIntersect(cBotLeft, eBotLeft, tTopLeft, tTopRight) != null)
			|| (Geometry.lineIntersect(cBotRight, eBotRight, tTopLeft, tTopRight) != null)
			|| (Geometry.lineIntersect(cTopLeft, cBotLeft, tTopLeft, tTopRight) != null))
		{
			coll[2] = true;
		}
		
		if (!that.isSolid())
		{
			if (Geometry.lineIntersect(cTopRight, cBotRight, tTopLeft, tTopRight) != null)
			{
				coll[2] = true;
			}
		}
		
		// Check intersection of bottom of block
		if ((Geometry.lineIntersect(cTopLeft, eTopLeft, tBotLeft, tBotRight) != null)
			|| (Geometry.lineIntersect(cTopRight, eTopRight, tBotLeft, tBotRight) != null)
			|| (Geometry.lineIntersect(cTopLeft, cBotLeft, tBotLeft, tBotRight) != null))
		{
			coll[3] = true;
		}
		
		if (!that.isSolid())
		{
			if (Geometry.lineIntersect(cTopRight, eBotRight, tBotLeft, tBotRight) != null)
			{
				coll[3] = true;
			}
		}
		
		if (!coll[2] && !coll[3])
		{
			// Check intersection of left side of block
			if ((Geometry.lineIntersect(cTopRight, eTopRight, tTopLeft, tBotLeft) != null)
				|| (Geometry.lineIntersect(cMidRight, eMidRight, tTopLeft, tBotLeft) != null)
				|| (Geometry.lineIntersect(cBotRight, eBotRight, tTopLeft, tBotLeft) != null)
				|| (cBotRight.x > tBotLeft.x && cBotLeft.y >= tBotLeft.y && cBotLeft.y <= tTopLeft.y))
			{
				coll[1] = true;
			}
			
			// Check intersection of right side of block
			if ((Geometry.lineIntersect(cTopLeft, eTopLeft, tTopRight, tBotRight) != null)
				|| (Geometry.lineIntersect(cMidLeft, eMidLeft, tTopRight, tBotRight) != null)
				|| (Geometry.lineIntersect(cBotLeft, eBotLeft, tTopRight, tBotRight) != null)
				|| (cBotLeft.x < tBotRight.x && cBotLeft.y >= tBotLeft.y && cBotLeft.y <= tTopLeft.y))
			{
				coll[0] = true;
			}
		}
		
		
		BlockCollision collision = new BlockCollision(coll[0], coll[1], coll[2], coll[3]);
		return collision;
	}
}