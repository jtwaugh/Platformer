package ogpc.earth2300.gui;

import java.awt.Graphics2D;
import java.awt.Point;

import ogpc.earth2300.entity.Player;

public class HUD extends MyInterface
{
	private TickBar health;
	private TickBar ammo;
	
	private Counter clips;
	private Counter frags;
	
	private boolean displayAmmo;
	private boolean displayClips;
	
	public HUD(int _width, int _height, Player steve)
	{
		super(_width, _height);
		
		health = new TickBar(new Point(5, 580), "heart", "heart_grey", steve.vitalityMax);
		
		ammo = new TickBar(new Point(50, 560), "bullet", "bullet_grey", steve.getWeapon().getMaxAmmo());
		
		clips = new Counter(new Point(5, 555), "clip", steve.getWeapon().getClips());
		
		frags = new Counter(new Point(745, 570), "skull", steve.kills);
		
	}
	
	public void render(Graphics2D g)
	{	
		health.draw(g);
		
		if (displayAmmo)
		{
			ammo.draw(g);
		}
		
		if (displayClips)
		{
			clips.draw(g);
		}
		
		
		frags.draw(g);
	}

	public void update(Player steve)
	{
		health.setSize(steve.vitality);
		
		if (steve.getWeapon().getAmmo() != -1)
		{
			displayAmmo = true;
			ammo.setSize(steve.getWeapon().getAmmo());
		}
		else
		{
			displayAmmo = false;
		}
		
		if (steve.getWeapon().getClips() != -1)
		{
			displayClips = true;
			clips.setNum(steve.getWeapon().getClips());
		}
		else
		{
			displayClips = false;
		}
		
		frags.setNum(steve.kills);
	}	
}
