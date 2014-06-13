package item;
import java.awt.Graphics2D;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.IOException;

import entity.Entity;
import entity.Projectile;
import graphics.Sprite;

public class Weapon
{	
	public Sprite pic;
	
	public Entity owner;
	
	protected double r;			// Distance held away from player
	private Point handle; 		// Point of rotation
	boolean facingRight;
	
	public Weapon()
	{
		
	}
	
	public Weapon(Sprite sprite, double _r, Entity _owner) throws IOException
	{
		handle = new Point(0,0);
		pic = new Sprite(sprite.getImage());
		r = _r;
		facingRight = false;
		owner = _owner;
	}
	
	public Weapon(Sprite sprite, double _r) throws IOException
	{
		handle = new Point(0,0);
		pic = new Sprite(sprite.getImage());
		r = _r;
		facingRight = false;
		owner = null;
	}
	
	public Weapon(Weapon that)
	{
		handle = new Point(0,0);
		pic = new Sprite(that.pic.getImage());
		r = that.r;
		owner = that.owner;
	}

	public void draw(Sprite p, Graphics2D g, Point targ, boolean playerFacing)
	{
		Graphics2D g2d = (Graphics2D) (g);
		
		int cx = p.getPosition().x + p.getWidth()/2;
		int cy = p.getPosition().y + p.getHeight()/2;
		
		double dx = targ.x - cx;
		double dy = targ.y - cy;
		
		double xRat = dx/Math.sqrt(dx*dx + dy*dy);
		double yRat = dy/Math.sqrt(dx*dx + dy*dy);
		
		double rx = r * xRat;
		double ry = r * yRat;
		
		double theta = Math.atan2(dy, dx);
		
		g2d.rotate(theta + Math.PI/2, cx + rx, cy + ry);
		
		pic.setPosition((int)rx + cx, (int)ry + cy);
		
		pic.draw(g2d);
		
		g2d.rotate(-(theta + Math.PI/2), cx + rx, cy + ry);
		
		if (playerFacing)
		{
			if (!facingRight)
			{
				pic.flipHorizontal();
				facingRight = true;
			}
		}
		else
		{
			if (facingRight)
			{
				pic.flipHorizontal();
				facingRight = false;
			}
		}
	}
	
	public Projectile attack(Point targ)
	{
		return null;
		
	}
	
	public int getMaxAmmo()
	{
		return -1;
	}
	
	public int getAmmo()
	{
		return -1;
	}
	
	public int getClips()
	{
		return -1;
	}
	
	public void update()
	{
		
	}

	public double getSpread()
	{
		return 0;
	}

	public Projectile getProjType()
	{
		return null;
	}

	public Point getMuzzle()
	{
		return null;
	}

	public long getRecoilTime() 
	{
		return 0;
	}

	public long getReloadTime()
	{
		return 0;
	}

	public int getClipSize()
	{
		return 0;
	}
	
}