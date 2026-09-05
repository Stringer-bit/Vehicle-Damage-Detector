package acsse.vehicle.Classification;

import java.util.ArrayList;
import java.util.List;
import acsse.vehicle.Node.PatchNode;

public class DamageRegion 
{
	//Member Variables
	private List<PatchNode> patches = null;
	private DAMAGE_TYPE type;
	private double severityScore = 0.0;
	private int minX, minY, maxX, maxY = 0;
	
	/**
	 * Constructor to initialize default values
	 */
	public DamageRegion()
	{
		patches = new ArrayList<>();
		this.minX = Integer.MAX_VALUE; 
        this.minY = Integer.MAX_VALUE;  
        this.maxX = Integer.MIN_VALUE; 
        this.maxY = Integer.MIN_VALUE;
	}

	/*
	 * Getters and Setter functions
	 */
	public List<PatchNode> getPatches() 
	{
		return patches;
	}

	public void setPatches(List<PatchNode> patches) 
	{
		this.patches = patches;
	}
	
	public DAMAGE_TYPE getType() 
	{
		return type;
	}

	public void setType(DAMAGE_TYPE type) 
	{
		this.type = type;
	}

	public double getSeverityScore() 
	{
		return severityScore;
	}

	public void setSeverityScore(double severityScore) 
	{
		this.severityScore = severityScore;
	}

	public int getMinX() 
	{
		return minX;
	}

	public void setMinX(int minX) 
	{
		this.minX = minX;
	}

	public int getMinY() 
	{
		return minY;
	}

	public void setMinY(int minY) 
	{
		this.minY = minY;
	}

	public int getMaxX() 
	{
		return maxX;
	}

	public void setMaxX(int maxX) 
	{
		this.maxX = maxX;
	}

	public int getMaxY() 
	{
		return maxY;
	}

	public void setMaxY(int maxY) 
	{
		this.maxY = maxY;
	}
	
	
}
