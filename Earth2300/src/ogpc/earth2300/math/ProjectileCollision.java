package ogpc.earth2300.math;

import java.awt.Point;

import ogpc.earth2300.entity.Entity;
import ogpc.earth2300.entity.Projectile;

public class ProjectileCollision
{
	public boolean collided;
	
	public ProjectileCollision (boolean _collided)
	{
		collided = _collided;
	}
	
	public static ProjectileCollision collProjEntity (Projectile p, Entity e)
	{
		if (e == p.owner)
		{
			return new ProjectileCollision(false);
		}
		
		int w = e.pic.getWidth() - 1;
		int h = e.pic.getHeight() - 1;
		
		int xm = (int) e.xMomentum;
		int ym = (int) e.yMomentum;
		
		int px = (int) p.xMomentum;
		int py = (int) p.yMomentum;
		
		Point tl = new Point (e.pic.getPosition().x + xm, e.pic.getPosition().y + ym);
		Point tr = new Point (tl.x + w, tl.y);
		Point bl = new Point (tl.x, tl.y + h);
		Point br = new Point (tr.x, bl.y);
		
		Point pos = new Point(p.pic.getPosition().x + px, p.pic.getPosition().y + py);
		
		boolean coll = (pos.x > tl.x && pos.x < tr.x && pos.y > tl.y && pos.y < bl.y);
		
		return new ProjectileCollision(coll);
	}
}
