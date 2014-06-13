package entity;

import game.Map;
import graphics.Sprite;

import java.awt.Point;
import java.awt.Rectangle;

import math.BlockCollision;
import math.Geometry;
import visible.Block;


public class Entity
{
	
	public Sprite pic;
	private boolean facingRight;		// Sprites face left by default
	
	public double xMomentum;
	public double yMomentum;
	protected static double speedCap = 3;
	protected static double terminalVelocity = 100;
	
	protected double weight;
	protected double acceleration;
	
	public Entity (Sprite _pic, double _weight, double acc)
	{
		pic =  _pic;
		weight = _weight;
		acceleration = acc;
		setFacingRight(false);
	}
	
	public Entity(Entity steve)
	{
		pic = new Sprite(steve.pic.getImage());
		weight = steve.weight;
		acceleration = steve.acceleration;
		setFacingRight(steve.isFacingRight());
	}

	public void moveTo(int x, int y)
	{
		pic.setPosition(x, y);
	}
	
	public void move()
	{
		pic.move((int)xMomentum, (int)yMomentum);
	}
	
	public void setMomentum (double x, double y)
	{
		xMomentum = x;
		yMomentum = y;
	}
	
	public void accelerate (double x, double y)
	{
		xMomentum += x;
		yMomentum += y;
	}
	
	public void flip()
	{
		pic.flipHorizontal();
		
		if (isFacingRight())
		{
			setFacingRight(false);
		}
		else
		{
			setFacingRight(true);
		}
	}
	
	public boolean getFacing()
	{
		// True if right, false if left
		return isFacingRight();
	}
	
	public boolean moving()
	{
		return ((xMomentum > 0) || (yMomentum > 0));
	}
	
	public void update()
	{
		gravity();
	}
	
	public void gravity()
	{
		yMomentum += Map.gravity;
	}
	
	public void applyFriction(double fric, double zero)
	{
		if (xMomentum == zero)
		{
			return;
		}
		else
		{
			if (xMomentum < zero)
			{
				if (fric > -xMomentum - zero)
				{
					xMomentum = zero;
				}
				else
				{
					xMomentum += fric;
				}
			}
			else
			{
				if (fric > xMomentum - zero)
				{
					xMomentum = zero;
				}
				else
				{
					xMomentum -= fric;
				}
			}
		}
	}
	
	public void blockCollideHandle(BlockCollision coll, Block that)
	{
		if (!that.isSolid())
		{
			applyFriction(that.getFriction(), 0);
			return;
		}
		
		if (coll.topCollided && coll.bottomCollided)
		{
			boolean r = !(that.pic.getPosition().x + 10 < pic.getPosition().x);

			blockSideCollide(that, r);

		}
		else
		{
			if (coll.bottomCollided)
			{
				blockBottomCollide(that);
			}
			if (coll.topCollided)
			{
				blockTopCollide(that);
			}
			if (coll.rightCollided)
			{
				blockSideCollide(that, true);
			}
			if (coll.leftCollided)
			{
				blockSideCollide(that, false);
			}
		}
		
		
	}
	
	protected void blockTopCollide(Block that)
	{
		if (yMomentum > 0)
		{
			yMomentum = 0;
		}
		
		pic.setPosition(pic.getPosition().x, that.pic.getPosition().y - pic.getHeight());
		applyFriction(that.getFriction(), 0);
	}
	
	protected void blockSideCollide(Block that, boolean isRightSide)
	{
		xMomentum = 0;
		
		if (isRightSide)
		{
			pic.setPosition(that.pic.getPosition().x + that.pic.getWidth(), pic.getPosition().y);
		}
		else
		{
			pic.setPosition(that.pic.getPosition().x - pic.getWidth(), pic.getPosition().y);
		}
	}
	
	protected void blockBottomCollide(Block that)
	{
		if (yMomentum < 0)
		{
			yMomentum = 0;
		}
		
		pic.setPosition(pic.getPosition().x, that.pic.getPosition().y + that.pic.getHeight());
	}

	
	public void projHitHandle(Projectile that)
	{
		
	}
	
	public boolean stillAlive()
	{
		return true;
	}
	
	private void platformTopCollide(Platform that)
	{
		int y = (int) that.yMomentum;
		yMomentum = 0;
		pic.setPosition(pic.getPosition().x, (that.pic.getPosition().y - pic.getHeight()) + y);
	}
	
	private void platformSideCollide(Platform that, boolean isRightSide)
	{
		xMomentum = 0;
		if (isRightSide)
		{
			pic.setPosition((int) (that.pic.getPosition().x + that.pic.getWidth() + that.xMomentum), pic.getPosition().y);
		}
		else
		{
			pic.setPosition(that.pic.getPosition().x - pic.getWidth(), pic.getPosition().y);
		}
	}
	
	private void platformBottomCollide(Platform that)
	{
		yMomentum = 0;
		pic.setPosition(pic.getPosition().x, that.pic.getPosition().y + that.pic.getHeight());
	}
	
	public boolean[] collideWithPlatform(Platform that)
	{
		boolean[] clear = {true, true, true};
		// 0 is horizontal
		// 1 is not on block
		// 2 is not under block
		
		
		int x = pic.getPosition().x;
		int y = pic.getPosition().y;
		
		Point cTopLeft = pic.getPosition();
		Point cTopRight = new Point(x + pic.getWidth() - 1, y);
		Point cBotLeft = new Point(x, y + pic.getHeight() - 1);
		Point cBotRight = new Point(x + pic.getWidth() - 1, y + pic.getHeight() - 1);
		Point cMidLeft = new Point(x, y + (pic.getHeight() / 2));
		Point cMidRight = new Point(x + pic.getWidth() - 1, y + (pic.getHeight() / 2));
		
		Point eTopLeft = new Point((int)(cTopLeft.x + xMomentum), (int)(cTopLeft.y + yMomentum));
		Point eTopRight = new Point((int)(cTopRight.x + xMomentum), (int)(cTopRight.y + yMomentum));
		Point eBotLeft = new Point((int)(cBotLeft.x + xMomentum), (int)(cBotLeft.y + yMomentum));
		Point eBotRight = new Point((int)(cBotRight.x + xMomentum), (int)(cBotRight.y + yMomentum));
		Point eMidLeft = new Point((int)(x + xMomentum), (int) (y + (pic.getHeight() / 2) + yMomentum));
		Point eMidRight = new Point((int)(x + pic.getWidth() + xMomentum), (int) (y + (pic.getHeight() / 2) + yMomentum));
		
		int tx = (int) (that.pic.getPosition().x + that.xMomentum);
		int ty = (int) (that.pic.getPosition().y + that.yMomentum);
		
		Point tTopLeft = new Point(tx, ty);
		Point tTopRight = new Point(tx + that.pic.getWidth() - 1, ty);
		Point tBotLeft = new Point(tx, ty + that.pic.getHeight() - 1);
		Point tBotRight = new Point(tx + that.pic.getWidth() - 1, ty + that.pic.getHeight() - 1);
		
		// Check intersection of top of block
		if ((Geometry.lineIntersect(cBotLeft, eBotLeft, tTopLeft, tTopRight) != null)
			|| (Geometry.lineIntersect(cBotRight, eBotRight, tTopLeft, tTopRight) != null))
		{
			clear[1] = false;
			platformTopCollide(that);
		}
		
		// Check intersection of bottom of block
		if ((Geometry.lineIntersect(cTopLeft, eTopLeft, tBotLeft, tBotRight) != null)
			|| (Geometry.lineIntersect(cTopRight, eTopRight, tBotLeft, tBotRight) != null))
		{
			clear[2] = false;
			platformBottomCollide(that);
		}
		
		if (clear[1] && clear [2])
		{
			// Check intersection of left side of block
			if ((Geometry.lineIntersect(cTopRight, eTopRight, tTopLeft, tBotLeft) != null)
				|| (Geometry.lineIntersect(cMidRight, eMidRight, tTopLeft, tBotLeft) != null)
				|| (Geometry.lineIntersect(cBotRight, eBotRight, tTopLeft, tBotLeft) != null))
			{
				clear[0] = false;
				platformSideCollide(that, false);
			}
			
			// Check intersection of right side of block
			if ((Geometry.lineIntersect(cTopLeft, eTopLeft, tTopRight, tBotRight) != null)
				|| (Geometry.lineIntersect(cMidLeft, eMidLeft, tTopRight, tBotRight) != null)
				|| (Geometry.lineIntersect(cBotLeft, eBotLeft, tTopRight, tBotRight) != null))
			{
				clear[0] = false;
				platformSideCollide(that, true);
			}
		}
		
		return clear;
	}

	public boolean isFacingRight() {
		return facingRight;
	}

	public void setFacingRight(boolean facingRight) {
		this.facingRight = facingRight;
	}

	public Rectangle getFutureBounds()
	{
		Rectangle ret = (Rectangle) pic.getBounds().clone();
		ret.translate((int)xMomentum, (int)yMomentum);
		return ret;
	}
}