package ogpc.earth2300.resource;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import ogpc.earth2300.entity.Projectile;
import ogpc.earth2300.game.Game;
import ogpc.earth2300.game.Level;
import ogpc.earth2300.game.Map;
import ogpc.earth2300.game.Overworld;
import ogpc.earth2300.graphics.Animation;
import ogpc.earth2300.graphics.Backdrop;
import ogpc.earth2300.graphics.Sprite;
import ogpc.earth2300.item.RangedWeapon;
import ogpc.earth2300.item.Weapon;
import ogpc.earth2300.visible.Block;
import ogpc.earth2300.visible.Scenery;

public class FileReader 
{
	private FileInputStream fStream;
	private DataInputStream inStream;
	private BufferedReader reader;
	
	private ScriptReader scriptHandle;
	
	public static final String drive = "resource/";
	
	private boolean isSetUp;
	
	public FileReader()
	{
		isSetUp = false;
		scriptHandle = new ScriptReader();
	}
	
	public FileReader (String filename) throws FileNotFoundException
	{
		setUp(filename);
	}
	
	private void setUp(String filename) throws FileNotFoundException
	{
		// Open the file that is the first 
		fStream = new FileInputStream(filename);
		
		// Get the object of DataInputStream
		inStream = new DataInputStream(fStream);
		reader = new BufferedReader(new InputStreamReader(inStream));
	}
	
	private String getLine() throws IOException
	{
		String strLine = new String();
		strLine = reader.readLine();
		if (strLine != null)
		{
			return strLine;
		}
		else
		{
			return null;
		}
  	}
	
	private void parseList(String filename, ArrayList<String> ret, String url) throws IOException
	{
		setUp(filename);
		
		String myDrive = drive + url;
		String tempStr = getLine();
		String tempDir = "";
		String tempFile;
		
		while (tempStr != null)
		{
			if (tempStr.contains(":"))
			// Directory
			{
				tempDir = tempStr;
				tempDir = tempDir.replaceAll(":", "/");
			}
			else if (tempStr.substring(0, 1).equals("-"))
			// Filename
			{
				
				tempStr = tempStr.replaceAll("-", "");
				tempFile = myDrive + tempDir + tempStr;
				
				tempStr = tempStr.split("\\.")[0];
				
				ret.add(tempFile);
			}
			
			tempStr = getLine();
		}
	}
	
	public void parseStringList (String filename, ArrayList<String> ret) throws IOException
	{
		setUp(filename);
		
		String temp = getLine();
		
		while (temp != null)
		{
			ret.add(temp);
			temp = getLine();
		}
	}
	
	public static Color RGBA(int x, int y, BufferedImage image)
	{
		int argb =  image.getRGB(x,y); 
		int b = (argb)&0xFF;
		int g = (argb>>8)&0xFF;
		int r = (argb>>16)&0xFF;
		int a = (argb>>24)&0xFF;
		
		int[] clr = new int[4];
		clr[0] = r;
		clr[1] = g;
		clr[2] = b;
		clr[3] = a;
		
		return new Color(clr[0], clr[1], clr[2], clr[3]);
	}
	
	public HashMap<String, Animation> parseSpriteAnims(BufferedImage sheet, String fileguide) throws IOException
	{
		setUp(fileguide);
		
		String[] temp = getLine().split("x");
		
		int sizeX = Integer.parseInt(temp[0]);
		int sizeY = Integer.parseInt(temp[1]);
		
		int picX = sheet.getWidth()/sizeX;
		int picY = sheet.getHeight()/sizeY;
		
		BufferedImage[] pics = new BufferedImage[sizeX*sizeY];
		
		for (int y = 0; y < sizeY; y++)
		{
			for (int x = 0; x < sizeX; x++)
			{
				pics[x + sizeX*y] = sheet.getSubimage(x * picX, y * picY, picX, picY);
			}
		}
		
		HashMap<String, Animation> ret = new HashMap<String, Animation>();
		
		String myStr = getLine();
		
		while (myStr != null)
		{
			temp = myStr.replace(" ", "").split("=");
			int start = Integer.parseInt(temp[0].split("-")[0]);
			int end = Integer.parseInt(temp[0].split("-")[1]);
			
			String name = temp[1].split(",")[0];
			int frameDelay = Integer.parseInt(temp[1].split(",")[1]);
			
			BufferedImage[] ra = new BufferedImage[(end-start)*frameDelay];
			
			for (int q = 0; q < end-start; q++)
			{
				for (int s = 0; s < frameDelay; s++)
				{
					ra[q*frameDelay + s] = pics[start+q];
				}
			}
			
			ret.put(name, new Animation(ra));
			
			myStr = getLine();
		}
		return ret;
	}
	
	public String[] parseMapFile(String filename) throws IOException
	// Warning: does not close file
	{
		String[] ret = new String[6];
		
		setUp(filename);
		
		// get name
		ret[0] = getLine();
		// get image file
		ret[1] = getLine();
		// get scenery file
		ret[2] = getLine();
		// get background
		ret[3] = getLine();
		// get spawn
		String[] temp = getLine().replace(" ", "").split(",");
		ret[4] = temp[0];
		ret[5] = temp[1];
		
		return ret;
	}
	
	public void parseMapBlocks(BufferedImage img, ArrayList<ArrayList<Block>> blocks) throws IOException
	{
		Color clr;
		
		for (int y = 0; y < img.getHeight(); y++)
		{
			blocks.add(new ArrayList<Block>());
			for (int x = 0; x < img.getWidth(); x++)
			{
				clr = RGBA(x, y, img);
				
				//System.out.println("Checking color: " + clr.getRed() + ", " + clr.getGreen() + ", " + clr.getBlue());
				
				try
				{
					Block myBlock = new Block(Game.manager.getBlock(clr), x*20, y*20);
					blocks.get(y).add(myBlock);
				}
				catch (NullPointerException e)
				{
					blocks.get(y).add(null);
				}
			}
		}
		
		reader.close();
	}
	
	public void parseMapScenery(BufferedImage img, ArrayList<ArrayList<Scenery>> scenery) throws IOException
	{
		Color clr;
		
		for (int y = 0; y < img.getHeight(); y++)
		{
			scenery.add(new ArrayList<Scenery>());
			for (int x = 0; x < img.getWidth(); x++)
			{
				clr = RGBA(x, y, img);
				
				Scenery s = Game.manager.getScenery(clr);
				
				if (s == null)
				{
					scenery.get(y).add(s);
				}
				else
				{
					scenery.get(y).add(new Scenery(s, x*20, y*20));
				}
			}
		}
		
		reader.close();
	}
	
	public void parseBlocksList(String filename, HashMap<Color, Block> ret) throws IOException
	{
		String key;
		String colstr;
		String[] rgb;
		boolean s;
		double f;
		
		ArrayList<String> files = new ArrayList<String>();
		
		parseList(filename, files, "blocks/");
	
		for(String file : files)
		{	
			setUp(file);
			key = getLine();
			key = key.split("/")[key.split("/").length-1];
			
			String[] myStr = key.split("\\.");
			key = key.split("\\.")[0];
			
			String ss = getLine();
			
			if (ss.contains("true") || ss.contains("True"))
			{
				s = true;
			}
			else 
			{
				s = false;
			}
			
			colstr = getLine();
			
			f = Double.parseDouble(getLine());
			
			reader.close();
			
			colstr = colstr.replaceAll(" ", "");
			
			rgb = colstr.split(",");
			
			Color myCol = new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
			
			ret.put(myCol, new Block(Game.manager.getSprite(key), s, myCol, f));
			
			reader.close();
		}
		
		
	}
	
	public void parseSceneries(String filename, HashMap<Color, Scenery> ret) throws IOException
	{	
		ArrayList<String> files = new ArrayList<String>();
		
		parseList(filename, files, "scenery/files/");
		String[] tempVars;
		String[] rgb;
		
		
		for (String q : files)
		{
			tempVars = parseScenery(q);
			
			rgb = tempVars[2].split(",");
			
			Color myCol = new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
			
			ret.put(myCol, new Scenery(tempVars));
		}
	}
	
	public String[] parseEntity(String filename) throws IOException
	{
		String ret[] = new String[4];
		
		setUp(filename);
		
		// get image file
		ret[0] = drive + getLine();
		
		// get script file
		ret[1] = drive + getLine();
		
		// get speed
		ret[2] = getLine();
		
		// get friction
		ret[3] = getLine();
		
		return ret;
	}
	
	public String[] parseProjectile(String filename) throws IOException
	{
		String ret[] = new String[5];
		
		setUp(filename);
		
		// get name
		ret[0] = getLine();
		
		// get image
		ret[1] = getLine();
		
		// get weight (damage)
		ret[2] = getLine();
		
		// get speed
		ret[3] = getLine();
		
		// get if ballistic
		ret[4] = getLine();
		
		return ret;
	}
	
	public String[] parseScenery(String filename) throws IOException
	{
		String[] ret = new String[10];
		
		setUp(filename);
		
		// name get
		ret[0] = getLine().replace(" = ", "=").split("=")[1];
		// sprite get
		ret[1] = getLine().replace(" = ", "=").split("=")[1];
		// color get
		ret[2] = getLine().replace(" = ", "=").split("=")[1];
		// solid get
		ret[3] = getLine().replace(" = ", "=").split("=")[1];
		// topcollide get
		ret[4] = getLine().replace(" = ", "=").split("=")[1];
		// botcollide get
		ret[5] = getLine().replace(" = ", "=").split("=")[1];
		// sidecollide get
		ret[6] = getLine().replace(" = ", "=").split("=")[1];
		// action get
		ret[7] = getLine().replace(" = ", "=").split("=")[1];
		// damage get
		ret[8] = getLine().replace(" = ", "=").split("=")[1];
		// death get
		ret[9] = getLine().replace(" = ", "=").split("=")[1];
	
		return ret;
	}
	
	public String[] parseRangedWeapon(String filename) throws IOException
	{
		String ret[] = new String[9];
		
		setUp(filename);
		
		for (int i = 0; i < 9; i++)
		{
			ret[i] = getLine();
		}
		
		return ret;
	}
	
	public ArrayList<String> parseEntityRoutine(String filename) throws IOException
	{
		setUp(filename);
		
		ArrayList<String> ret = new ArrayList<String>();
		String tempStr = getLine();
		String tempCom;
		int tempNum;
		String[] temp;
		
		
		while (tempStr != null)
		{
			temp = tempStr.split(" ");
			tempCom = temp[0];
			tempNum = Integer.parseInt(temp[1]);
			
			for (int q = 0; q < tempNum; q++)
			{
				ret.add(tempCom);
			}			
			
			tempStr = getLine();
		}
		
		return ret;
	}

	public Level parseLevelFile(String filename) throws IOException
	{
		setUp(filename);
		
		String[] rets = new String[3];
		String[] mapNames;
		String[] tempMapVars;
		BufferedImage img;
		
		// Get name
		rets[0] = getLine();
		// Get title
		rets[1] = getLine();
		// Get description
		rets[2] = getLine();
		
		Level ret = new Level(rets[0], rets[1], rets[2]);
		
		mapNames = getLine().replace(" ", "").split(",");
		
		for (int m = 0; m < mapNames.length; m++)
		{
			tempMapVars = parseMapFile(drive + "levels/maps/files/" + mapNames[m]);
			
			Map retMap = new Map(tempMapVars[0], tempMapVars[3], scriptHandle.parseScript(), ret, new Point(Integer.parseInt(tempMapVars[4]), Integer.parseInt(tempMapVars[5])));
			
			ret.addMap(tempMapVars[0], retMap);
			if (m == 0)
			{
				ret.changeMap(tempMapVars[0]);
			}
			
			String myURL = drive + "levels/maps/" + tempMapVars[1];
			
			String sceneURL = drive + "levels/maps/" + tempMapVars[2];
			
			img = ImageIO.read(new File(myURL));
			
			parseMapBlocks(img, retMap.tiles);
			
			img = ImageIO.read(new File(sceneURL));
			
			parseMapScenery(img, retMap.scenery);
			
			retMap.getSize();
		}
		
		reader.close();
		
		return ret;
	}
	
	public Overworld parseWorld(String filename) throws IOException
	{
		setUp(filename);
		
		Overworld ret = new Overworld(getLine(), getLine(), scriptHandle.parseScript());
		
		return ret;
	}
	
	public void getSprites(String filename, HashMap<String, Sprite> ret) throws IOException
	// IMPORTANT: Key is the filename without extension
	{
		setUp(filename);

		String myDrive = drive + "sprites/";
		String tempStr = getLine();
		String tempDir = "";
		String tempFile;
		
		while (tempStr != null)
		{
			if (tempStr.contains(":"))
			// Directory
			{
				tempDir = tempStr;
				tempDir = tempDir.replaceAll(":", "/");
			}
			else if (tempStr.substring(0, 1).equals("-"))
			// Filename
			{
				
				tempStr = tempStr.replaceAll("-", "");
				tempFile = myDrive + tempDir + tempStr;
				
				tempStr = tempStr.split("\\.")[0];
				
				File imgGuide = new File(myDrive + tempDir + tempStr + ".txt");
				if (imgGuide.exists())
				{
					ret.put(tempStr, new Sprite(tempFile, imgGuide.getPath()));
				}
				else
				{
					ret.put(tempStr, new Sprite(tempFile));
				}
			}
			tempStr = getLine();
		}
	}
	
	public void getLevels(String filename, HashMap<String, Level> ret) throws IOException
	{
		ArrayList<String> filenames = new ArrayList<String>();
		String[] rets;
		BufferedImage img;
		
		parseList(filename, filenames, "levels/");
		
		for (int i = 0; i < filenames.size(); i++)
		{
			Level myLevel = parseLevelFile(filenames.get(i));
			
			myLevel.initOnMap();
			
			ret.put(myLevel.name, myLevel);
		}
	}
	
	public void getProjectiles(String filename, HashMap<String, Projectile> ret) throws IOException
	{
		ArrayList<String> filenames = new ArrayList<String>();
		String[] rets = new String[3];
		
		parseList(filename, filenames, "weapons/projectiles/");
		
		for (int i = 0; i < filenames.size(); i++)
		{
			rets = parseProjectile(filenames.get(i));
			ret.put(rets[0], new Projectile(Game.manager.getSprite(rets[1]), Double.parseDouble(rets[2]), Double.parseDouble(rets[3]), Boolean.parseBoolean(rets[4]), new Point(0, 0), new Point(0, 0)));
		}
	}
	
	public void getScenery(String filename, HashMap<String, Scenery> ret) throws IOException
	{
		ArrayList<String> filenames = new ArrayList<String>();
		String[] rets = new String[8];
		
		parseList(filename, filenames, "scenery/");
		
		for (int i = 0; i < filenames.size(); i++)
		{
			rets = parseProjectile(filenames.get(i));
			
			ret.put(rets[0], new Scenery(rets));
		}
	}

	public void getWeapons(String filename, HashMap<String, Weapon> ret) throws IOException
	{
		ArrayList<String> filenames = new ArrayList<String>();
		String[] rangedRets = new String[9];
		
		parseList(filename, filenames, "weapons/");
		
		for (int i = 0; i < filenames.size(); i++)
		{
			if (filenames.get(i).contains("ranged"))
			{
				rangedRets = parseRangedWeapon(filenames.get(i));
				
				String name = rangedRets[0];
				
				double r = Double.parseDouble(rangedRets[2]);
				double s = Double.parseDouble(rangedRets[3]);
				double reload = Double.parseDouble(rangedRets[4]);
				double recoil = Double.parseDouble(rangedRets[5]);
				
				int ammoCap = Integer.parseInt(rangedRets[6]);
				int clipSize = Integer.parseInt(rangedRets[7]);
				
				ret.put(name, new RangedWeapon(Game.manager.getSprite(rangedRets[1]), r, s, reload, recoil, ammoCap, clipSize, null, Game.manager.getProj(rangedRets[8]), null));
			}
		}
	}

	public void parseBackdropList(String filename, HashMap<String, Backdrop> ret) throws IOException
	{
		Backdrop rets;
		String tempStr;
		
		ArrayList<String> files = new ArrayList<String>();
		
		parseList(filename, files, "background");
	
		for(String file : files)
		{	
			rets = new Backdrop(file);
			
			String[] temp = file.split("/");
			
			
			ret.put(temp[temp.length-1].split("\\.")[0], rets);
		}
	}
	
	class ScriptReader
	{
		private ScriptNode currentNode;
		
		private int index;
		
		public ArrayList<ScriptNode> parseScript() throws IOException
		{
			ArrayList<ScriptNode> nodes = new ArrayList<ScriptNode>();
			
			// Called from reading a level file
			
			String tempStr = getLine();
			
			while (tempStr != null)
			{
				if (!tempStr.contains("#"))
				{
					tempStr = tempStr.replace("\t", "");
					
					if (!tempStr.equals(""))
					{
						if (tempStr.contains("="))
						{
							readAssignment(tempStr, nodes);
						}
						else if(tempStr.equals("}"))		// End of node
						{
							currentNode = currentNode.getParent();
						}
					}
				}
				tempStr = getLine();
			}
			
			return nodes;
		}
		
		private void readAssignment(String source, ArrayList<ScriptNode> nodes)
		{
			String[] args; // 0 = name, 1 = value
			if (source.indexOf("\"") != -1)
			{
				source = source.substring(0, source.indexOf("\"")).replace(" ", "") + source.substring(source.indexOf("\"") + 1);
			}
			else
			{
				source = source.replace(" ", "");
			}
			source = source.replace("\"", "");
			
			args = source.split("=");
			
			try
			{
				if (args[1].equals("{"))			// Beginning of node
				{
					
					if (currentNode == null)
					{
						nodes.add(new ScriptNode(args[0], null));
						currentNode = nodes.get(nodes.size()-1);
					}
					else
					{
						currentNode.addChild(args[0]);
						currentNode = currentNode.getNewestChild();
					}
				}
				else								// Variable assignment				
				{
					currentNode.add(new Assignment(args[0], args[1]));
				}
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				System.out.println("Script error (\"" + source + ",\"): Check delimiters");
				System.exit(1);
			}
			
		}
	}
}