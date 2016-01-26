package ogpc.earth2300.entity;

import java.awt.Point;

import ogpc.earth2300.graphics.Sprite;

public class RocketProjectile extends Projectile
{
	private Sprite explosion;
	private double radius;
	
	public RocketProjectile(Sprite _pic, double damage, double acc, double _radius, Point _origin, Point _target)
	{
		super(_pic, damage, acc, false, _origin, _target);
	}

}
