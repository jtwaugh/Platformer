package ogpc.earth2300.item;

import java.awt.Graphics2D;
import java.awt.Point;
import java.io.IOException;

import ogpc.earth2300.entity.Entity;
import ogpc.earth2300.entity.Projectile;
import ogpc.earth2300.graphics.Sprite;
import ogpc.earth2300.math.Timer;

public class RangedWeapon extends Weapon
{
	private Projectile proj;
	private Point muzzle;
	
	private Timer reload;
	private Timer recoil;
	
	private double spread;
	
	private int ammo;
	private int clips;
	private int clipSize;
	private int ammoCap;
	
	public RangedWeapon(Sprite sprite, double _r, double _s, double _reload, double _recoil, int _ammoCap, int _clipSize, Point _muzzle, Projectile _proj, Entity _owner) throws IOException
	{
		super(sprite, _r, _owner);
		spread = _s;
  		proj = new Projectile(_proj);
		muzzle = _muzzle;
		
		recoil = new Timer((long) _recoil);
		reload = new Timer((long) _reload);
		
		ammoCap = _ammoCap;
		ammo = ammoCap;
		clipSize = _clipSize;
		clips = 5;
		
		reload.start();
		recoil.start();
	}
	
	public RangedWeapon(Weapon that) throws IOException
	{
		super(that.pic, that.r, that.owner);
		if (that.getClips() > 0)
		{
			spread = that.getSpread();
	  		proj = new Projectile(that.getProjType());
			muzzle = that.getMuzzle();
			
			recoil = new Timer((long) that.getRecoilTime());
			reload = new Timer((long) that.getReloadTime());
			
			clipSize = that.getClipSize();
			ammoCap = that.getMaxAmmo();
			ammo = ammoCap;
			clips = that.getClips();
			
			reload.start();
			recoil.start();
		}
		
		
	}
	
	public void draw(Sprite p, Graphics2D g, Point targ, boolean playerFacing)
	{
		// add reload animation at some point
		super.draw(p, g, targ, playerFacing);
	}
	
	public Projectile attack(Point targ)
	{
		// TODO fix rotation issues
		if (!reload.expired() || !recoil.expired() || ammo == 0)
		{
			return null;
		}
		
		double dx = (targ.x - pic.getPosition().x);
		double dy = (targ.y - pic.getPosition().y);
		
		double x = pic.getPosition().x;
		double y = pic.getPosition().y;
		
		double h = Math.sqrt((dx*dx) + (dy*dy));
		
		double b = ((Math.random() * 2) - 1) * spread;
		double theta = Math.atan2(dy, dx);
		theta += b;
		
		x += h*Math.cos(theta);
		y += h*Math.sin(theta);
		
		Point q = new Point((int)x, (int)y);
		
		recoil.start();
		
		if (ammo != -1)
		{
			ammo--;
			
			if (ammo == 0)
			{
				reload.start();
			}
		}

		return (new Projectile(proj, pic.getPosition(), q, owner));
	}

	private void reload()
	{
		if (ammo == 0)
		{
			if (reload.expired())
			{
				clips--;
				
				if (clips > 0)
				{
					ammo += clipSize;
				}
			}
		}
	}
	
	public int getMaxAmmo()
	{
		return clipSize;
	}
	
	public int getAmmo()
	{
		return ammo;
	}
	
	public int getClips()
	{
		return clips;
	}
	
	public void update()
	{
		reload();
	}
	
	public double getSpread()
	{
		return spread;
	}
	
	public Projectile getProjType()
	{
		return proj;
	}
	
	public Point getMuzzle()
	{
		return muzzle;
	}
	
	public long getRecoilTime() 
	{
		return recoil.getDuration();
	}
	
	public long getReloadTime()
	{
		return reload.getDuration();
	}
	
	public int getClipSize()
	{
		return clipSize;
	}
}
