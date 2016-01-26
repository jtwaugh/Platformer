package ogpc.earth2300.entity;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import ogpc.earth2300.graphics.Sprite;
import ogpc.earth2300.resource.FileReader;


public class Platform extends Entity
{

	private Point targ;
	private ArrayList<String> routine;
	private int routineIndex;
	private double friction;
	
	public Platform(String filename, Sprite s, Point pos) throws IOException
	{
		super(null, 0, 0);
		
		BufferedImage img;
		String[] zeug;
		
		FileReader reader = new FileReader();
		
		zeug = reader.parseEntity(filename);//im better than you -gabe
		
		img = s.getImage();
		pic = new Sprite(img);
		
		pic.setPosition(pos.x, pos.y);
		
		routine = reader.parseEntityRoutine(zeug[1]);
		routineIndex = 0;
		targ = pic.getPosition();
		
		acceleration = Double.parseDouble(zeug[2]);
		
		friction = Double.parseDouble(zeug[3]);
	}//im ryze
	
	public void move()
	{
		getCommand();
		super.move();
	}
	
	public void update()
	{
		// Ignore
	}
	
	private void getCommand()
	{
		String route;
		
		if (isAtTarg())
		{
			pic.setPosition(targ.x, targ.y);
			routineIndex++;
			
			if (routineIndex >= routine.size())
			{
				routineIndex %= routine.size();
			}
			
			route = routine.get(routineIndex);
			
			if (route.equals("left"))
			{
				xMomentum  = -acceleration;
				
				targ.x -= 20;
			}
			else if (route.equals("right"))
			{
				xMomentum  = acceleration;
				
				targ.x += 20;
			}
			else if (route.equals("up"))
			{
				yMomentum  = -acceleration;
				
				targ.y -= 20;
			}
			else if (route.equals("down"))
			{
				yMomentum  = acceleration;
				
				targ.y += 20;
			}
			else if (route.equals("stop"))
			{
				xMomentum = 0;
				
				yMomentum = 0;
			}
		}
		
		
	}
	
	private boolean isAtTarg()
	{
		int x = pic.getPosition().x;
		int y = pic.getPosition().y;
		
		boolean xWillCollide = false;
		boolean yWillCollide = false;
		
		if (xMomentum  == 0)
		{
			xWillCollide = (x == targ.x);
		}
		else if (xMomentum < 0)
		{
			if(Math.abs(x - targ.x) < -xMomentum)
			{
				xWillCollide = true;
			}
		}
		else if (Math.abs(x-targ.x) < xMomentum)
		{
			xWillCollide = true;
		}
		
		if (yMomentum  == 0)
		{
			yWillCollide = (y == targ.y);
		}
		else if (yMomentum < 0)
		{
			if(Math.abs(y - targ.y) < -yMomentum)
			{
				yWillCollide = true;
			}
		}
		else if (Math.abs(y-targ.y) < yMomentum)
		{
			yWillCollide = true;
		}
		
		return (xWillCollide && yWillCollide);
	}
	
	public double getFriction()
	{
		return friction;
	}
}
