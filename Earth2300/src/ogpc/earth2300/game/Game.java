package ogpc.earth2300.game;
//
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.*;

import ogpc.earth2300.entity.Mob;
import ogpc.earth2300.resource.KeywordTable;
import ogpc.earth2300.resource.ResourceManager;


public class Game extends Canvas implements ImageObserver
{
	
	/**
	TODO:
	 * Fix riots not appearing
	 * Add more levels (2 or 3 storyline, one each of resource riots)

	 **/
	private static final long serialVersionUID = 2425228983258178634L;
	private static final int boundX = 800;
	private static final int boundY = 600;
	
	private JFrame container;
	private JPanel panel;
	
	private BufferStrategy strategy;
	
	public static final int KEY_UP = KeyEvent.VK_W;
	public static final int KEY_LEFT = KeyEvent.VK_A;
	public static final int KEY_RIGHT = KeyEvent.VK_D;
	public static final int KEY_DOWN = KeyEvent.VK_S;
	public static final int KEY_ACTION = KeyEvent.VK_SPACE;
	
	private boolean gameRunning = true;
	private int frame;
	private boolean waitingForKeyPress = true;
	public static Point mousePos;
	
	public static ResourceManager manager;
	public static KeywordTable keywords;
	
	public static HashMap<String, State> gameStates;
	private State currentState;
	
	public static String command;
	
	public static Font textFont;
	
	// Hardcode zone

	private Mob testMob;

	public Game() throws IOException
	{	
		mousePos = new Point(0, 0);
		
		setUpWindow();
		
		keywords = new KeywordTable();
		
		manager = new ResourceManager();
		
		gameStates = new HashMap<String, State>();
		gameStates.put("loadingScreen", new LoadScreen());
	
		command = "";
		
		textFont = new Font("SansSerif", Font.PLAIN, 12);
		
		hardcodeZone();
		unitTests();
	}
	
	private void hardcodeZone() throws IOException
	// This should be empty by release
	{	
		currentState = gameStates.get("loadingScreen");
	}
	
	private void unitTests()
	{
	}

	private void setUpWindow()
	{
		container = new JFrame("Earth 2300");
		

		panel = (JPanel) container.getContentPane();
		panel.setPreferredSize(new Dimension(boundX, boundY));
		panel.setLayout(null);
		

		setBounds(0, 0, boundX, boundY);
		
		panel.add(this);
		

		setIgnoreRepaint(true);

		container.pack();
		container.setResizable(false);
		container.setVisible(true);
		

		container.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e) 
			{
				System.exit(0);
			}
		});
		

		addKeyListener(new KeyInputHandler());
		addMouseListener(new MouseInputHandler());
		

		requestFocus();


		createBufferStrategy(2);
		strategy = getBufferStrategy();
	}

	public void enterLevel(String levelName) throws IOException
	{
		if (command.equals("overworld"))
		{
			setState("world");
			OverworldState l = (OverworldState)currentState;
		}
		else if (command.equals("title screen"))
		{
			setState(command);
		}
		else
		{
			setState("level");
			LevelState l = (LevelState)currentState;
			l.setLevel(levelName);
		}
		
	}
	
	public void setState(String stateName)
	{
		currentState = gameStates.get(stateName);
	}
	
	private void startGame() 
	{
		frame = 0;
	}

	private void drawBackground(Graphics2D g)
	{
		g.setColor(Color.blue);
		g.fillRect(getX(), getY(), getWidth(), getHeight());
	}

	public void gameLoop() throws IOException
	{
		
		long lastLoopTime = System.currentTimeMillis();
		long delta;
		
		while (gameRunning)
		{	
			// Get command
			if (!command.equals(""))
			{
				enterLevel(command);
				command = "";
			}
			
			// Get the cursor's coordinates
			getMouse();
			
			// Create rendering object for this frame
			
			Graphics2D backdropRenderer = (Graphics2D) strategy.getDrawGraphics();
			
			Graphics2D gameRenderer = (Graphics2D) strategy.getDrawGraphics();
			
			Graphics2D interfaceRenderer = (Graphics2D) strategy.getDrawGraphics();
			
			// Draw the background
			drawBackground(gameRenderer);
			
			// Let the game state render the frame
			currentState.loop(backdropRenderer, gameRenderer, interfaceRenderer);
			
			// Wipe rendering objects
			gameRenderer.dispose();
			
			interfaceRenderer.dispose();
			
			// Up the frame count
			frame++;
			
			// Display the frame
			strategy.show();
			
			// Get time in frame
			delta = System.currentTimeMillis() - lastLoopTime;
			lastLoopTime = System.currentTimeMillis();
			
			// Schlafen
			try 
			{ 
				Thread.sleep(15);
			}
			catch (Exception e) {}
		}
	}
	
	private class KeyInputHandler extends KeyAdapter 
	{
		
		private int pressCount = 1;
		
		public void keyPressed(KeyEvent e) 
		{
			if (waitingForKeyPress) 
			{
				return;
			}
			else
			{
				currentState.keyPressHandle(e);
			}
		} 
		
		public void keyReleased(KeyEvent e) 
		{
			if (waitingForKeyPress)
			{
				waitingForKeyPress = false;
			}
			else 
			{
				currentState.keyReleaseHandle(e);
			}
		}

		public void keyTyped(KeyEvent e) 
		{
			if (e.getKeyChar() == 27)
			{
				System.exit(0);
			}
			
			
			else if (waitingForKeyPress) 
			{
				if (pressCount == 1) 
				{
					waitingForKeyPress = false;
					startGame();
					pressCount = 0;
				} 
				else 
				{
					pressCount++;
				}
			}
			
		}
	}
	
	private class MouseInputHandler extends MouseAdapter
	{
		public void mousePressed(MouseEvent e)
		{
			currentState.mousePressHandle(e);
		}
		
		public void mouseReleased(MouseEvent e)
		{
			currentState.mouseReleaseHandle(e);
		}
	}

	private void getMouse()
	{
		PointerInfo a = MouseInfo.getPointerInfo();
		Point b = a.getLocation();
		mousePos.x = (int) b.getX() - getLocationOnScreen().x;
		mousePos.y = (int) b.getY() - getLocationOnScreen().y;
	}
	
	public static void main(String argv[]) throws IOException 
	{
		Game g = new Game();

		g.gameLoop();
	}
}

