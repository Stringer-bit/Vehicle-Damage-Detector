package acsse.vehicle.Similarity;

import acsse.vehicle.Classification.DAMAGE_TYPE;

/*
 * Class that determines variables to use to compare different types of damage
 */
public class ReferenceDamage 
{
	private String label = null;           
	private DAMAGE_TYPE type = null;       
	private int patchCount = 0;         
	private double aveIntensity = 0.0;    
	private double aveTextureVariance = 0.0; 
	private double aveAnomalyScore = 0.0;
	private double aveEdgeWeight = 0.0; 
    
	/**
	 * Constructor for when the class is initialized we store default values
	 * 
	 * @param label
	 * @param type
	 * @param patchCount
	 * @param aveIntensity
	 * @param aveTextureVariance
	 * @param aveAnomalyScore
	 * @param aveEdgeWeight
	 */
    public ReferenceDamage(String label, DAMAGE_TYPE type, int patchCount, double aveIntensity, double aveTextureVariance, double aveAnomalyScore,double aveEdgeWeight) 
    {
		this.label = label;
		this.type = type;
		this.patchCount = patchCount;
		this.aveIntensity = aveIntensity;
		this.aveTextureVariance = aveTextureVariance;
		this.aveAnomalyScore = aveAnomalyScore;
		this.aveEdgeWeight = aveEdgeWeight;
    }

	public String getLabel() 
	{
		return label;
	}

	public void setLabel(String label) 
	{
		this.label = label;
	}

	public DAMAGE_TYPE getType() 
	{
		return type;
	}

	public void setType(DAMAGE_TYPE type) 
	{
		this.type = type;
	}

	public int getPatchCount() 
	{
		return patchCount;
	}

	public void setPatchCount(int patchCount) 
	{
		this.patchCount = patchCount;
	}

	public double getAveIntensity() 
	{
		return aveIntensity;
	}

	public void setAveIntensity(double aveIntensity) 
	{
		this.aveIntensity = aveIntensity;
	}

	public double getAveTextureVariance() 
	{
		return aveTextureVariance;
	}

	public void setAveTextureVariance(double aveTextureVariance) 
	{
		this.aveTextureVariance = aveTextureVariance;
	}

	public double getAveAnomalyScore() 
	{
		return aveAnomalyScore;
	}

	public void setAveAnomalyScore(double aveAnomalyScore) 
	{
		this.aveAnomalyScore = aveAnomalyScore;
	}

	public double getAveEdgeWeight() 
	{
		return aveEdgeWeight;
	}

	public void setAveEdgeWeight(double aveEdgeWeight) 
	{
		this.aveEdgeWeight = aveEdgeWeight;
	}
}
