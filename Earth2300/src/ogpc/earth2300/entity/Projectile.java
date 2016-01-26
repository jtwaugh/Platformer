package ogpc.earth2300.entity;
import java.awt.Graphics2D;
import java.awt.Point;

import ogpc.earth2300.graphics.Sprite;


public class Projectile extends Entity
{
	Point origin;
	Point target;
	
	public Entity owner;
	
	public int damage;
	
	public boolean hit = false;
	
	private double x_error = 0;
	private double y_error = 0;
	
	public boolean ballistic;

	public Projectile(Sprite _pic, double _weight, double acc, boolean _ballistic, Point _origin, Point _target)
	{
		super(_pic, _weight, acc);
		origin = new Point(_origin.x, _origin.y);
		target = new Point (_target.x, _target.y);
		pic.setPosition(origin.x, origin.y);
		ballistic = _ballistic;
		
		damage = (int) weight;
	}
	
	public Projectile(Projectile that)
	{
		super(that.pic, that.weight, that.acceleration);
		origin = null;
		target = null;
		ballistic = that.ballistic;
		
		damage = (int) weight;
	}
	
	public Projectile (Projectile that, Point _origin, Point _target, Entity _owner)
	{
		super(null, that.weight, that.acceleration);
		Sprite mySprite = new Sprite(that.pic.getImage());
		pic = mySprite;
		origin = new Point(_origin.x, _origin.y);
		target = new Point (_target.x, _target.y);
		pic.setPosition(origin.x, origin.y);
		ballistic = that.ballistic;
		damage = (int) weight;
		owner = _owner;
	}

	public void draw(Graphics2D g)
	{
		int dx = target.x - origin.x;
		int dy = target.y - origin.y;
		g.rotate(Math.atan2(dy, dx), pic.getPosition().x, pic.getPosition().y);
		super.pic.draw(g);
		g.rotate(-Math.atan2(dy, dx), pic.getPosition().x, pic.getPosition().y);
	}
	
	private double correctError(double error, boolean moveY)
	{
		if (error >= 1)
		{
			error -= 1;
			if (moveY)
			{
				pic.move(0, 1);
			}
			else
			{
				pic.move(1, 0);
			}
		}
		if (error <= -1)
		{
			error += 1;
			if (moveY)
			{
				pic.move(0, -1);
			}
			else
			{
				pic.move(-1, 0);
			}
		}
		
		return error;
	}
	public void move()
	{
		int x0 = origin.x;
		int x1 = target.x;
		
		int y0 = origin.y;
		int y1 = target.y;
		
		//x component of direction
		double direction_x = x1 - x0;
		//y component of direction
		double direction_y = y1 - y0;
		
		double length = (double) Math.sqrt(direction_x*direction_x + direction_y*direction_y);
		
		// find the distance in the x direction it will move
		direction_x /= length;
		direction_x *= acceleration;
		direction_x -= x_error;
		
		// find the distance in the y direction it will move
		direction_y /= length;
		direction_y *= acceleration;
		direction_y -= y_error;
		
		x_error = ((int)direction_x) - direction_x;
		y_error = ((int)direction_y) - direction_y;
		
		pic.move((int)direction_x, (int) (direction_y + yMomentum));
	}
}
