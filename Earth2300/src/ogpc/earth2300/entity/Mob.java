package ogpc.earth2300.entity;
import java.awt.Point;
import java.util.ArrayList;

import ogpc.earth2300.math.BlockCollision;
import ogpc.earth2300.visible.Block;



public class Mob extends Character 
{
	private ArrayList<String> routine;
	
	public boolean inAir;
	
	private Point targ;		// Block coords

	public Mob(String _pic, double _weight, double acc, String _AIname, int vit, Point strat) 
	{
		super(_pic, _weight, acc, _AIname, vit, strat);
		routine = new ArrayList<String>();
		targ = new Point (pic.getPosition().x/20, pic.getPosition().y/20 + 2);
		inAir = false;
	}
	
	public void move()
	{
		int i = 0;
		
		if (pic.getPosition().x > getTargPoint().x -1)
		// To the left
		{
			i = -1;
		}
		else if (pic.getPosition().x + pic.getWidth() -1 < getTargPoint().x)
		// To the right
		{
			i = 1;
		}
		else
		// At target already
		{
			//System.out.println("HUE");
		}
		
		xMomentum = i * acceleration;
		
		// Perform normal movement
		super.move();
	}
	
	public Point getTarg()
	{
		return targ;
	}
	
	public Point getTargPoint()
	{
		int x = targ.x * 20;
		int y = targ.y * 20;
		
		return new Point(x, y);
	}
	
	private void validateTarg()
	{
		if (targ.x < 0)
		{
			targ.x = 1;
		}
		if (targ.y < 0)
		{
			targ.y = 1;
		}
	}
	
	public void setTarg(Point _targ)
	{
		targ = new Point (_targ.x, _targ.y);
		validateTarg();
	}
	
	public void moveTarg(int x, int y)
	{
		targ.x += x;
		targ.y += y;
		validateTarg();
	}
	
	public boolean atTarg()
	{
		boolean xColl = false;
		boolean yColl = false;
		
		int top = pic.getPosition().y;
		int bot = top + pic.getHeight() - 1;
		int left = pic.getPosition().x;
		int right = left + pic.getWidth() - 1;
		
		int tx = targ.x * 20;
		int ty = targ.y * 20;
		
		int dx;
		int dy;
		
		int yAllo = 10;
		
		if (xMomentum > 0)
		// Moving right
		{
			dx = tx - right;
			xColl = (dx < xMomentum);
		}
		else if (xMomentum < 0)
		// Moving left
		{
			dx = tx - left;
			xColl = (dx > xMomentum);
		}
		else
		// Glitching
		{
			xColl = (tx <= right && tx >= left);
		}
		
		// Only check feet collision, as the block should be one which can be reached by foot
		// TODO: change for flying mobs
		/*dy = bot - targ.y;
		
		yAllo += Math.abs(yMomentum);
		
		yColl = (Math.abs(dy) < yAllo); */
		
		return (xColl);
	}
	
	public void jump()
	{
		super.jump();
		inAir = true;
	}

	
	protected void blockTopCollide(Block that)
	{
		super.blockTopCollide(that);
		if (targ.y < that.pic.getPosition().y/20 + 1)
		{
			// Target invalid
			// setTarg(new Point(targ.x, pic.getPosition().y/20 + 1));
		}
		inAir = false;
	}
	
	protected void blockSideCollide(Block that, boolean isRightSide)
	{
		xMomentum = 0;
		
		if (isRightSide)
		{
			pic.setPosition(that.pic.getPosition().x + that.pic.getWidth(), pic.getPosition().y);
			if (getTargPoint().x < that.pic.getPosition().x + that.pic.getWidth())
			{
				// Target invalid
				if (!inAir)
				{
					setTarg(new Point(pic.getPosition().x/20 + 2, targ.y));
				}
			}
		}
		else
		{
			pic.setPosition(that.pic.getPosition().x + that.pic.getWidth(), pic.getPosition().y);
			if (getTargPoint().x + 19 > that.pic.getPosition().x)
			{
				// Target invalid
				if (!inAir)
				{
					setTarg(new Point(pic.getPosition().x/20 - 2, targ.y));
				}
			}
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
	
}
