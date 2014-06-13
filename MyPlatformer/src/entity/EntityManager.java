package entity;

import java.util.ArrayList;

public class EntityManager 
{
	public ArrayList<Entity> entities;
	private Player steve;
	
	public EntityManager(Player steve)
	{
		entities = new ArrayList<Entity>();
		entities.add(steve);
	}
	
	public Entity get(int index)
	{
		return entities.get(index);
	}
	
	public int size()
	{
		return entities.size();
	}
	
	public Player getPlayer()
	{
		return steve;
	}
	
	
}
