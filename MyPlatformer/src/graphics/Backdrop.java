package graphics;

import game.Map;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Backdrop
{
	private BufferedImage pic;
	int xSize;
	int ySize;
	
	public Backdrop(String filename) throws IOException
	{
		File file = new File(filename);
		pic = ImageIO.read(file);
	}
	
	public void draw(Graphics2D g
			//,Map s
			)
	{
//		float dx = Map.cameraPos.x / (s.xSize*20);
//		float dy = Map.cameraPos.y / (s.ySize*20);
//		
//		g.translate((int)(-dx), (int)(-dy));
		
		g.drawImage(pic, 0, 0, null);
		
//		g.translate((int)(dx), (int)(dy));
	}
}
