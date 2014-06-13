package entity;

import game.Game;
import item.Weapon;

import java.awt.Graphics2D;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;

import visible.Block;


public class Character extends Entity
{
	protected int xSize;
	protected int ySize;
	
	public int vitalityMax;
	public int vitality;
	
	protected boolean isJumping;
	protected boolean canJump;
	public boolean willJump;
	
	protected boolean hasGravity;
	
	public boolean willAttack;
	
	protected boolean playerAction;
	
	protected Weapon heldWep;
	
	private ArrayList<String> routine;
	
	public String AIname;
	
	protected String action;
	protected String anim;
	
	public Point targPt;
	
	public boolean invincible;
	
	public Character(String _pic, double _weight, double acc, String _AIname, int vit, Point strat)
	{
		super(Game.manager.getSprite(_pic), _weight, acc);
		
		pic.setPosition(strat.x, strat.y);
		
		vitalityMax = vit;
		vitality = vit;
		
		isJumping = false;
		canJump = true;
		
		playerAction = false;
		
		hasGravity = true;
		
		action = "default";
		anim = "default";
		
		AIname = _AIname;
		
		invincible = false;
	}
	
	public void draw(Graphics2D g)
	{
		pic.playAnimation(action);
		pic.draw(g);
		
		if (heldWep != null)
		{
			heldWep.pic.setPosition(pic.getPosition().x, pic.getPosition().y);
		}
		
		
		if (targPt != null && heldWep != null)
		{
			heldWep.draw(pic, g, targPt, isFacingRight());
		}
		// else point straight ahead
	}
	
	public void setAction(String a)
	{
		action = a;
	}
	
	public boolean stillAlive()
	{
		if (invincible)
		{
			return true;
		}
		return (vitality >= 0);
	}
	
	public boolean lowHP()
	{
		return (vitality <= (vitalityMax/2));
	}
	
	public void setGrav(boolean b)
	{
		hasGravity = b;
	}
	
	public boolean hasGravity()
	{
		return hasGravity;
	}
	
	public void update()
	{
		if(action.equals("climb"))
		{
			hasGravity = false;
			yMomentum -= (yMomentum / 10);
			if (Math.abs(yMomentum - 0) < .001)
			{
				yMomentum = 0;
			}
			
			xMomentum -= (xMomentum / 10);
			if (Math.abs(xMomentum - 0) < .001)
			{
				xMomentum = 0;
			}
		}
		else
		{
			hasGravity = true;
			if (willJump)
			{
				jump();
			}
		}
		if (hasGravity)
		{
			gravity();
		}
	}
	
	public void jump()
	{
		if (yMomentum < 1 && yMomentum > -1 && !isJumping)
		{
			yMomentum -= 9.5;
			isJumping = true;
			canJump = false;
			willJump = false;
		}
	}
	
	public Projectile attack()
	{
		if (heldWep == null)
		{
			return null;
		}
		
		Point myPoint = null;
		
		if (willAttack)
		{
			myPoint = targPt;
			willAttack = false;
		}
		
		heldWep.update();
		
		if (myPoint != null)
		{
			return heldWep.attack(myPoint);
		}
		
		return null;
	}
	
	public Weapon getWeapon()
	{
		return heldWep;
	}
	
	public void setWeapon(String wep)
	{
		try 
		{
			heldWep = Game.manager.getWeapon(wep);
			
			if (heldWep != null)
			{
				heldWep.owner = this;
				heldWep.pic.setPosition(pic.getPosition().x, pic.getPosition().y);
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	protected void blockTopCollide(Block that)
	{
		takeDamage((yMomentum/3) - 4);
		
		super.blockTopCollide(that);
		
		if (isJumping)
		{
			isJumping = false;
		}
		
		
		hasGravity = true;
	}
	
	public void takeDamage(double d)
	{
		if (invincible)
		{
			return;
		}
		
		if (d > 0)
		{
			vitality -= d;
		}
		
	}
	
	public void projHitHandle(Projectile that)
	{
		takeDamage(that.damage);
	}
	
	public void setPlayerAction(boolean b)
	{
		playerAction = b;
	}
	
	public boolean getPlayerAction()
	{
		return playerAction;
	}

	public boolean[] collideWithPlatform(Platform that)
	{
		boolean[] p = super.collideWithPlatform(that);
		
		if (!p[1])
		{
			isJumping = false;
		}
		
		return p;
	}
}
