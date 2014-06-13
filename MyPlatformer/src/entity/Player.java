package entity;
import game.Game;
import game.Map;
import item.Weapon;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.KeyStroke;

import visible.Block;


public class Player extends Character
{

	private boolean waitingForKeyPress;
	
	private boolean leftPressed;
	private boolean rightPressed;
	private boolean upPressed;
	private boolean downPressed;
	
	private boolean qPressed;
	private boolean ePressed;
	
	private boolean mousePressed;
	private boolean mouseReleased;

	public int kills;
	
	private ArrayList<Weapon> weapons;
	
	public Player(String _pic, double _weight, double acc, int vit)
	{
		super(_pic, _weight, acc, "", vit, new Point(0,0));
		
		waitingForKeyPress = true;
		
		kills = 0;
		
		weapons = new ArrayList<Weapon>();
	}

	
	public void draw (Graphics2D g)
	{
		Point myPoint = new Point(Game.mousePos.x - Map.cameraPos.x, Game.mousePos.y - Map.cameraPos.y);
		
		if (getFacingRight())
		{
			if (!isFacingRight())
			{
				flip();
				setFacingRight(true);
			}
		}
		else
		{
			if (isFacingRight())
			{
				flip();
				setFacingRight(false);
			}
		}
		
		pic.playAnimation(anim);
		pic.draw(g);
		
		heldWep.pic.setPosition(pic.getPosition().x, pic.getPosition().y);
		heldWep.draw(pic, g, myPoint, isFacingRight());
		
	}

	public Projectile attack()
	{
		Point myPoint = null;
		
		if (willAttack)
		{
			myPoint = new Point(Game.mousePos.x - Map.cameraPos.x, Game.mousePos.y - Map.cameraPos.y);
			willAttack = false;
		}
		
		heldWep.update();
		
		if (myPoint != null)
		{
			return heldWep.attack(myPoint);
		}
		
		return null;
	}
	
	public void addWeapon(String w) throws IOException
	{
		weapons.add(Game.manager.getWeapon(w));
		heldWep = weapons.get(weapons.size()-1);
	}
	
	public void jump()
	{	
		if (canJump)
		{
			super.jump();
		}
	}
	
	public boolean getFacingRight()
	{
		return (Game.mousePos.x - Map.cameraPos.x > pic.getPosition().x);
	}
	
	public void disableInput()
	{
		waitingForKeyPress = false;
	}
	
	public void enableInput()
	{
		waitingForKeyPress = true;
	}
	
	public void reactToKeys()
	{
		// React appropriately
		if (waitingForKeyPress)
		{
			if (leftPressed)
			{
				if (action == "climb")
				{
					xMomentum = (xMomentum < -3) ? xMomentum : xMomentum - .2;
				}
				else
				{
					if (-xMomentum <= speedCap)
					{
						xMomentum -= acceleration;
						anim = "walk";
					}
				}

			}
			else if (rightPressed)
			{
				if (action == "climb")
				{
					xMomentum = (xMomentum > 3) ? xMomentum : xMomentum + .2;
				}
				else
				{
					if (xMomentum <= speedCap)
					{
						xMomentum += acceleration;
						anim = "walk";
					}
				}
			}
			else
			{
				anim = "default";
			}
			
			if (upPressed)
			{
				if (action == "climb")
				{
					yMomentum = (yMomentum < -3) ? yMomentum : yMomentum - .2;
				}
				else
				{
					jump();
					anim = "jump";
				}
			}
			
			if (downPressed)
			{
				if (action == "climb")
				{
					yMomentum = (yMomentum > 3) ? yMomentum : yMomentum + .2;
				}
			}
			
			if (qPressed)
			{
				qPressed = false;
				
				int q = weapons.indexOf(heldWep);
				
				if (q == 0)
				{
					q = weapons.size() - 1;
				}
				else
				{
					q--;
				}
				
				heldWep = weapons.get(q);
			}
			
			if (ePressed)
			{
				ePressed = false;
				
				int e = weapons.indexOf(heldWep);
				
				if (e == weapons.size() - 1)
				{
					e = 0;
				}
				else
				{
					e++;
				}
				
				heldWep = weapons.get(e);
			}
		}
	}
	
	public void keyPressHandle(KeyEvent e)
	{
		
		// Figure out which keys were pressed
		if (e.getKeyCode() == Game.KEY_LEFT)
		{
			leftPressed = true;
		}
		if (e.getKeyCode() == Game.KEY_RIGHT)
		{
			rightPressed = true;
		}
		if (e.getKeyCode() == Game.KEY_UP)
		{
			upPressed = true;
		}
		if (e.getKeyCode() == Game.KEY_DOWN)
		{
			downPressed = true;
		}
	}
	
	public void keyReleaseHandle(KeyEvent e)
	{
		if (e.getKeyCode() == Game.KEY_LEFT)
		{
			leftPressed = false;
		}
		if (e.getKeyCode() == Game.KEY_RIGHT)
		{
			rightPressed = false;
		}
		if (e.getKeyCode() == Game.KEY_UP)
		{
			upPressed = false;
			canJump = true;
		}
		if (e.getKeyCode() == Game.KEY_DOWN)
		{
			downPressed = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_Q)
		{
			qPressed = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_E)
		{
			ePressed = true;
		}
	}
	
	public void reactToMouse()
	{
		if (mousePressed)
		{
			
		}
		if (mouseReleased)
		{
			willAttack = true;
			mouseReleased = false;
		}
	}
	
	public void mousePressHandle(MouseEvent e)
	{
		mousePressed = true;
	}
	
	public void mouseReleaseHandle(MouseEvent e)
	{
		mousePressed = false;
		mouseReleased = true;
	}


	public void stop()
	{
		mousePressed = false;
		mouseReleased = false;
		upPressed = false;
		downPressed = false;
		leftPressed = false;
		rightPressed = false;
		
		xMomentum = 0;
	}
}
