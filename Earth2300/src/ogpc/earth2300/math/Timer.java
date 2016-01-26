package ogpc.earth2300.math;

public class Timer
{
	private long start;
	private long end;
	private long temp;
	private long duration;
	
	private boolean activated;
	
	public Timer(long _duration)
	{
		activated = false;
		start = 0;
		end = 0;
		duration = _duration;
		temp = start;
	}
	
	public void start()
	{
		if (activated && !expired())
		{
			return;
		}
		
		activated = true;
		start = System.currentTimeMillis();
		end = start + duration;
		temp = start;
	}
	
	public long timePassed()
	{
		long ret;
		
		if (!expired())
		{
			ret =  (System.currentTimeMillis() - temp);
			temp = System.currentTimeMillis();
		}
		else
		{
			ret = (end - temp);
		}
		
		return ret;
	}
	
	public boolean expired()
	{
		if (!activated)
		{
			return false;
		}
		
		return (System.currentTimeMillis() >= end);
	}
	
	public long getDuration()
	{
		return duration;
	}
}
