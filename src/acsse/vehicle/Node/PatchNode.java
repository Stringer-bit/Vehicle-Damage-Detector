package acsse.vehicle.Node;

import java.util.ArrayList;

public class PatchNode
{

	private int X,Y;
	private int[][] patch;
	
	private PatchNode up,down;
	private PatchNode left;
	private PatchNode right;
	

	
	public int         id;
    public ArrayList<int[]> pixels = new ArrayList<>();
    public double avgIntensity;
    public double textureVariance;
    public double avgR, avgG, avgB;
    public double centroidX, centroidY;
    public double area;
    public double zoneX, zoneY;
    public double anomalyScore  =  0;
    public boolean isDamaged = false;
    
    public double getAvgIntensity() 
    { 
    	return avgIntensity; 
    
    }
    public void setAvgIntensity(double val) 
    { 
    	
    	this.avgIntensity = val; 
    }

    public double getAnomalyScore() 
    { 
    	return anomalyScore; 
    }
    
    public void setAnomalyScore(double val) 
    { 
    	this.anomalyScore = val; 
    }

    public boolean isDamaged() 
    { 
    	return isDamaged; 
    
    
    }
    
    
    public void setDamaged(boolean val) 
    { 
    	this.isDamaged = val; 
    }
	
	
	public int[][] getPatch() {
		return patch;
	}

	public void setPatch(int[][] patch) {
		this.patch = patch;
	}
	
	public PatchNode getUp()
	{
		return up;
	}
	
	public void setUp(PatchNode up)
	{
		this.up = up;
	}
	
	public PatchNode getDown()
	{
		return down;
	}
	
	public void setDown(PatchNode down)
	{
		this.down = down;
	}
	
	public PatchNode getLeft() {
		return left;
	}

	public void setLeft(PatchNode left) {
		this.left = left;
	}
	
	public PatchNode getRight() {
		return right;
	}

	public void setRight(PatchNode right) {
		this.right = right;
	}

	public int getX() {
		return X;
	}

	public void setX(int x) {
		X = x;
	}

	public int getY() {
		return Y;
	}

	public void setY(int y) {
		Y = y;
	}

    public PatchNode getElement() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getElement'");
    }

	

}