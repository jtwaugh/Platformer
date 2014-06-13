package resource;

import item.RangedWeapon;
import item.Weapon;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;

import entity.Projectile;
import game.Game;
import game.Level;
import game.LevelState;
import game.OverworldState;
import game.TitleScreen;
import graphics.Backdrop;
import graphics.Sprite;
import visible.Block;
import visible.Scenery;

public class ResourceManager
// Load resources
{
	private HashMap<String, Sprite> 	sprites;
	private HashMap<String, Backdrop> 	backdrops;
	private HashMap<String, Level> 		levels;
	private HashMap<String, Projectile> projtypes;
	private HashMap<String, Weapon> 	weapons;
	private HashMap<Color, Scenery> 	scenery;
	private HashMap<Color, Block> 		blocks;
	
	private int numLoaded;
	
	private FileReader reader;
	
	public ResourceManager() throws IOException
	{
		reader = new FileReader();
		
		numLoaded = 0;
		
		sprites 	= 	new HashMap<String, Sprite>();
		backdrops 	= 	new HashMap<String, Backdrop>();
		levels 		= 	new HashMap<String, Level>();
		projtypes 	=	new HashMap<String, Projectile>();
		weapons 	= 	new HashMap<String, Weapon>();
		scenery 	= 	new HashMap <Color, Scenery>();
		blocks 		= 	new HashMap<Color, Block>();
	}
	
	public int loadNext() throws IOException
	// For loading bar purposes
	{
		switch (numLoaded)
		{
		case 0:
		{
			loadSprites();
			break;
		}
		case 1:
		{
			loadBlocks();
			break;
		}
		case 2:
		{
			loadScenery();
			break;
		}
		case 3:
		{
			loadProjectiles();
			break;
		}
		case 4:
		{
			loadWeapons();
			break;
		}
		case 5:
		{
			loadBGs();
			break;
		}
		case 6:
		{
			loadLevels();
			break;
		}
		case 7:
		{
			Game.gameStates.put("level", new LevelState());
			break;
		}
		case 8:
		{
			Game.gameStates.put("world", new OverworldState());
			break;
		}
		case 9:
		{
			Game.gameStates.put("title screen", new TitleScreen());
			break;
		}
		}
		
		numLoaded++;
		
		return numLoaded;
	}
	
	public void loadBlocks() throws IOException
	{
		reader.parseBlocksList(blocks);
	}
	
	public void loadBGs() throws IOException
	{
		reader.parseBackdropList(backdrops);
	}
	
	public void loadScenery() throws IOException
	{
		reader.parseSceneries(scenery);
	}
	
	public void loadWeapons() throws IOException
	{
		reader.getWeapons(FileReader.drive + "weapons/WeaponsList.txt", weapons);
	}
	
	public void loadSprites() throws IOException
	{
		reader.getSprites(sprites);
	}
	
	public void loadLevels() throws IOException
	{
		reader.getLevels(FileReader.drive + "levels/LevelsList.txt", levels);
	}
	
	public void loadProjectiles() throws IOException
	{
		reader.getProjectiles(FileReader.drive + "weapons/projectiles/ProjList.txt", projtypes);
	}
	
	public Block getBlock(Color key)
	{
		try
		{
			if (key.equals(new Color(255, 255, 255)))
			{
				return null;
			}
			return new Block(blocks.get(key));
		}
		catch (NullPointerException e)
		{
			return null;
		}
	}
	
	public Scenery getScenery(Color key)
	{
		try
		{
			if (key.equals(new Color(255, 255, 255, 255)))
			{
				return null;
			}
			Scenery s = new Scenery(scenery.get(key), 0, 0);
			//System.out.println("Added scenery with color value " + s.toString());
			return s;
			
		}
		catch (NullPointerException e)
		{
			return null;
		}
	}
	
	public Scenery getScenery (String name)
	{
		for (Color c : scenery.keySet())
		{
			if (scenery.get(c).name.equals(name))
			{
				return new Scenery(scenery.get(c), 0, 0);
			}
		}
		
		return null;
	}
	
	public Sprite getSprite(String key)
	{
		return new Sprite(sprites.get(key));
		
	}
	
	public Level getLevel(String key) throws IOException
	{
		return levels.get(key);
	}
	
	public Projectile getProj(String key)
	{
		return new Projectile(projtypes.get(key));
	}
	
	public Backdrop getBG(String key)
	{
		if (key.equals("null"))
		{
			return null;
		}
		return backdrops.get(key);
	}
	
	public Weapon getWeapon(String key) throws IOException
	{
		if (key.equals("null"))
		{
			return null;
		}
		
		if (weapons.get(key).getClips() > 0)
		{
			return new RangedWeapon(weapons.get(key));
		}
		return new Weapon(weapons.get(key));
	}
	
	public void put(String key, Sprite pic)
	{
		sprites.put(key, pic);
	}
	
	
	public void loadSpriteFromFile(String filename) throws IOException
	{
		String key = filename.split(".")[0];
		
		sprites.put(key, new Sprite(filename));
	}
}
